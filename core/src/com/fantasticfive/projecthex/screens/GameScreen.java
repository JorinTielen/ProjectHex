package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.fantasticfive.projecthex.InputManager;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.projecthex.tables.*;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;

import java.rmi.RemoteException;

public class GameScreen implements Screen{
    final GameMain game;

    //lib gdx
    private InputManager input= new InputManager(this);
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //game
    private LocalGame localGame;
    private IMap map;

    //ui
    private Skin skin;
    private Stage stage;
    private Table table;

    //tables
    private Table unitShopTable;
    private Table playerTable;
    private Table buildingShopTable;
    private Table unitSellTable;
    private Table buildingSellTable;
    private Table optionsTable;
    private Building buildingToBuild;

    public GameScreen(final GameMain game) {
        this.game = game;

        //Setup Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Setup Map

        //Setup Game
        localGame = new LocalGame();
        map = localGame.getMap();

        //Setup window
        batch = new SpriteBatch();

        //Setup UI
        skin = new Skin();
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);

        createSkin();

        //Create UI
        createPlayerUI();
        if (playerTable != null) {
            table.addActor(playerTable);
        }

        createBuildingShopUI();
        if (buildingShopTable != null) {
            table.addActor(buildingShopTable);
        }

        createUnitShopUI();
        if (unitShopTable != null) {
            table.addActor(unitShopTable);
        }

        createUnitSellUI();
        if (unitSellTable != null) {
            table.addActor(unitSellTable);
        }

        createBuildingSellUI();
        if (buildingSellTable != null) {
            table.addActor(buildingSellTable);
        }

        createGameUI();
        createOptionsUI();
        if (optionsTable != null) {
            table.addActor(optionsTable);
        }

        //Input processor
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(input);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Handle input
        input.HandleInput();

        //Move camera
        camera.translate(input.getCamPos());
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw all the sprites
        batch.begin();

        //draw all hexes from the map
        for (Hexagon hex : map.getHexagons()) {
            batch.draw(hex.groundImage, hex.getPos().x, hex.getPos().y);
            if (hex.objectImage != null) {
                batch.draw(hex.objectImage, hex.getPos().x, hex.getPos().y);
            }
        }

        //draw all buildings and units from all players
        for (Player p : localGame.getPlayers()) {
            for (Building b : p.getBuildings()) {
                if(b.getImage() == null) {
                    System.out.println("no image");
                }
                Hexagon h = map.getHexAtLocation(b.getLocation());
                batch.draw(b.getImage(), h.getPos().x, h.getPos().y);
            }
            for (Unit u : p.getUnits()) {
                Hexagon h = map.getHexAtLocation(u.getLocation());
                batch.draw(u.getTexture(), h.getPos().x, h.getPos().y);
            }
        }

        //draw building when trying to place it on a Hexagon
        if (buildingToBuild != null) {
            Vector3 mousePos = new Vector3(Gdx.input.getX() - 80, Gdx.input.getY() + 80, 0); //Image position gets set hard-coded to get it under the cursor.
            camera.unproject(mousePos);
            batch.draw(buildingToBuild.getImage(), mousePos.x, mousePos.y);
        }

        batch.end();

        //update UI information
        updatePlayerUI();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void screenLeftClick(int x, int y) {
        clearUI();

        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);

        for (Hexagon hex : map.getHexagons()) {
            Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                    hex.groundImage.getWidth(), hex.groundImage.getHeight());
            if (clickArea.contains(tmp.x, tmp.y)) {
                System.out.println("clicked hex: " + hex.getLocation().x + " " + hex.getLocation().y);

                //When clicked on unit or unit is selected
                if (localGame.getUnitOnHex(hex) != null || localGame.getSelectedUnit() != null) {
                    buildingToBuild = null;
                    unitClick(hex);
                }
                //Create building on hex
                else if (buildingToBuild != null) {
                    if (buildingToBuild instanceof Resource) {
                        localGame.buyBuilding(BuildingType.RESOURCE, hex.getLocation());
                        System.out.println("com.fantasticfive.shared.Resource built");
                    } else if (buildingToBuild instanceof Fortification) {
                        localGame.buyBuilding(BuildingType.FORTIFICATION, hex.getLocation());
                        System.out.println("com.fantasticfive.shared.Fortification built");
                    } else if (buildingToBuild instanceof Barracks) {
                        localGame.buyBuilding(BuildingType.BARRACKS, hex.getLocation());
                        System.out.println("com.fantasticfive.shared.Barracks built");
                    }
                    buildingToBuild = null;
                }
            }
        }
    }

    public void screenRightClick(int x, int y) {
        clearUI();

        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);

        for (Hexagon hex : map.getHexagons()) {
            Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                    hex.groundImage.getWidth(), hex.groundImage.getHeight());
            if (clickArea.contains(tmp.x, tmp.y)) {
                System.out.println("clicked hex: " + hex.getLocation().x + " " + hex.getLocation().y);

                //Right click on own building
                Building b = null;
                b = localGame.getBuildingAtLocation(hex.getLocation());
                if (b != null && b.getOwner().getId() == localGame.getThisPlayer().getId()) {
                    if (b instanceof Barracks) {
                        System.out.println("You clicked on a Barracks");
                        showUnitShopUI(x, y, b);
                    } else if (b instanceof TownCentre) {
                        System.out.println("You clicked on a TownCentre");
                        showBuildingShopUI(x, y);
                    } else if (b instanceof Resource) {
                        System.out.println("You clicked on a Resource");
                        showBuildingSellUI(x, y, b);
                    } else if (b instanceof Fortification) {
                        System.out.println("You clicked on a Fortification");
                        showBuildingSellUI(x, y, b);
                    }
                }
                //Right click on own unit
                Unit u = null;
                u = localGame.getUnitOnHex(hex);
                if (u != null) {
                    if (u.getOwner() == localGame.getThisPlayer())
                        showUnitSellUI(x, y, u);
                    System.out.println("You clicked on a unit!");
                }
            }
        }
    }

    private void unitClick(Hexagon hex) {
        //If clicked on unit and no unit is selected
        if (localGame.getUnitOnHex(hex) != null && localGame.getSelectedUnit() == null && localGame.getUnitOnHex(hex).getOwner() == localGame.getThisPlayer()) {
            Unit u = localGame.getUnitOnHex(hex);
            u.toggleSelected();
            //If not clicked on unit and unit is selected
        } else if (localGame.getUnitOnHex(hex) == null && localGame.getSelectedUnit() != null) {
            Unit u = localGame.getSelectedUnit();
            //Move unit to free hex
            if (localGame.hexEmpty(hex.getLocation())) {
                u.move(new Point(hex.getLocation().x, hex.getLocation().y));
                u.toggleSelected();
            }
            //com.fantasticfive.shared.Unit attacks enemy building
            else if (localGame.getBuildingAtLocation(hex.getLocation()) != null
                    && localGame.getBuildingAtLocation(hex.getLocation()).getOwner() != localGame.getThisPlayer()) {
                localGame.attackBuilding(localGame.getSelectedUnit(), hex.getLocation());
                u.toggleSelected();
            }
            //If clicked on unit and unit is selected
        } else if (localGame.getUnitOnHex(hex) != null && localGame.getSelectedUnit() != null) {
            //If clicked on the selected unit
            if (localGame.getUnitOnHex(hex) == localGame.getSelectedUnit()) {
                localGame.getSelectedUnit().toggleSelected();
                //If clicked on a different unit with the same owner
            } else if (localGame.getUnitOnHex(hex).getOwner() == localGame.getSelectedUnit().getOwner()) {
                localGame.getSelectedUnit().toggleSelected();
                localGame.getUnitOnHex(hex).toggleSelected();
                //If clicked on a unit with a different owner than the selected unit
            } else if (localGame.getUnitOnHex(hex).getOwner() != localGame.getSelectedUnit().getOwner()) {
                Unit enemy = localGame.getUnitOnHex(hex);
                Unit playerUnit = localGame.getSelectedUnit();
                playerUnit.attack(enemy);
                playerUnit.toggleSelected();
                if (enemy.getHealth() == 0) {
                    enemy.getOwner().removeUnit(enemy);
                }
            }
        }
    }

    private void clearUI() {
        optionsTable.setVisible(false);
        buildingSellTable.setVisible(false);
        buildingShopTable.setVisible(false);
        unitSellTable.setVisible(false);
        unitShopTable.setVisible(false);
    }

    // ====================
    //  Unit Shop (Barracks)
    // ====================
    private void createUnitShopUI() {
        unitShopTable = new UnitShopTable(localGame, skin);
    }

    private void showUnitShopUI(float x, float y, Building building) {
        ((UnitShopTable) unitShopTable).setBuilding(building);
        unitShopTable.setPosition(x, Gdx.graphics.getHeight() - y);
        unitShopTable.setVisible(true);
    }

    // ====================
    //  Building Shop (Town Centre)
    // ====================
    private void createBuildingShopUI() {
        buildingShopTable = new BuildingShopTable(this, localGame, skin);
    }

    private void showBuildingShopUI(float x, float y) {
        buildingShopTable.setPosition(x, Gdx.graphics.getHeight() - y);
        buildingShopTable.setVisible(true);
    }

    public void updateBuildingToBuild(Building b) {
        buildingToBuild = b;
    }

    // ====================
    //  Unit UI
    // ====================
    private void createUnitSellUI() {
        unitSellTable = new UnitSellTable(localGame, skin);
    }

    private void showUnitSellUI(float x, float y, Unit unit) {
        ((UnitSellTable) unitSellTable).setUnit(unit);
        unitSellTable.setPosition(x, Gdx.graphics.getHeight() - y);
        unitSellTable.setVisible(true);
    }

    // ====================
    //  Building UI
    // ====================
    private void createBuildingSellUI() {
        buildingSellTable = new BuildingSellTable(localGame, skin);
    }

    private void showBuildingSellUI(float x, float y, final Building building) {
        ((BuildingSellTable) buildingSellTable).setBuilding(building);
        buildingSellTable.setPosition(x, Gdx.graphics.getHeight() - y);
        buildingSellTable.setVisible(true);
    }

    // ====================
    //  Player UI
    //  (Shows the amount of gold and GPT cost)
    // ====================
    private void createPlayerUI() {
        try {
            playerTable = new PlayerTable(localGame, skin);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayerUI() {
        ((PlayerTable) playerTable).update();
    }

    // ====================
    //  Game UI
    //  (Shows the next turn button and options)
    // ====================
    private void createGameUI() {
        table.addActor(new GameTable(this, skin));
    }

    private void createOptionsUI() {
        optionsTable = new OptionsTable(localGame, skin);
    }

    public void showOptionsUI() {
        optionsTable.setVisible(true);
    }

    private void createSkin() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);
    }
}
