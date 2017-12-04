/*

package com.fantasticfive.projecthex;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.projecthex.screens.GameMain;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.fantasticfive.projecthex.tables.*;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;

import java.rmi.RemoteException;

public class ProjectHex extends ApplicationAdapter {
    //lib gdx
    private InputManager input = new InputManager(new GameMain());
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //game
    private LocalGame game;
    private IMap map;

    //ui
    private Skin skin;
    private Stage stage;
    private Table table;
    private Texture blankTexture; //blank texture for health bars
    private Texture walkableHexTexture; //Texture overlay for hexes that unit can walk to
    public SpriteAnimation explosionAnimation;

    //tables
    private Table unitShopTable;
    private Table playerTable;
    private Table buildingShopTable;
    private Table unitSellTable;
    private Table buildingSellTable;
    private Table optionsTable;
    private Building buildingToBuild;

    @Override
    public void create() {
        //Setup Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Setup Map

        //Setup Game
        game = new LocalGame();
        map = game.getMap();

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

        //set blank texture
        blankTexture = new Texture("whitePixel.png");
        walkableHexTexture = new Texture("movableHex.png");
    }

    @Override
    public void render() {
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
            if (hex.colorCoding != null) {
                batch.draw(hex.colorCoding, hex.getPos().x, hex.getPos().y);
            }
        }

        //draw all buildings and units from all players
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                if (b.getImage() == null) {
                    System.out.println("no image");
                }
                Hexagon h = map.getHexAtLocation(b.getLocation());
                batch.draw(b.getImage(), h.getPos().x, h.getPos().y);
                batch.setColor(Color.RED);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, 50, 5);
                batch.setColor(Color.GREEN);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, (int) ((double) 50 * ((double) b.getHealth() / (double) b.getMaxHealth())), 5);
                batch.setColor(Color.WHITE);
                if (b.getDestroyed()) {
                    explosionAnimation = new SpriteAnimation("explosion", b.getLocation());
                    b.getOwner().removeBuilding(b);
                }
            }
            for (Unit u : p.getUnits()) {
                Hexagon h = map.getHexAtLocation(u.getLocation());
                batch.draw(u.getTexture(), h.getPos().x, h.getPos().y);
                batch.setColor(Color.RED);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, 50, 5);
                batch.setColor(Color.GREEN);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, (int) ((double) 50 * ((double) u.getHealth() / (double) u.getMaxHealth())), 5);
                batch.setColor(Color.WHITE);
            }
        }

        //draw building when trying to place it on a Hexagon
        if (buildingToBuild != null) {
            Vector3 mousePos = new Vector3(Gdx.input.getX() - 80, Gdx.input.getY() + 80, 0); //Image position gets set hard-coded to get it under the cursor.
            camera.unproject(mousePos);
            buildingToBuild.setImage();
            batch.draw(buildingToBuild.getImage(), mousePos.x, mousePos.y);
        }

        //draw explosion animation
        if (explosionAnimation != null) {
            if (explosionAnimation.getActive()) {
                explosionAnimation.animate();
                batch.draw(explosionAnimation.getTexture(), explosionAnimation.getLocation().x, explosionAnimation.getLocation().y);
            }
        }

        //draw area where unit can walk
        if (game.getSelectedUnit() != null) {
            for (Hexagon h : game.getSelectedUnit().getWalkableHexes()) {
                //Maybe batch.enableBlending();
                batch.draw(walkableHexTexture, h.getPos().x, h.getPos().y);
            }
        }

        batch.end();

        //update UI information
        updatePlayerUI();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    void screenLeftClick(int x, int y) {
        clearUI();

        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);

        for (Hexagon hex : map.getHexagons()) {
            Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                    hex.groundImage.getWidth(), hex.groundImage.getHeight());
            if (clickArea.contains(tmp.x, tmp.y)) {
                System.out.println("clicked hex: " + hex.getLocation().x + " " + hex.getLocation().y);

                //When clicked on unit or unit is selected
                if (game.getUnitOnHex(hex) != null || game.getSelectedUnit() != null) {
                    buildingToBuild = null;
                    unitClick(hex);
                }
                //Create building on hex
                else if (buildingToBuild != null) {
                    if (buildingToBuild instanceof Resource) {
                        game.buyBuilding(BuildingType.RESOURCE, hex.getLocation());
                    } else if (buildingToBuild instanceof Fortification) {
                        game.buyBuilding(BuildingType.FORTIFICATION, hex.getLocation());
                    } else if (buildingToBuild instanceof Barracks) {
                        game.buyBuilding(BuildingType.BARRACKS, hex.getLocation());
                    }
                    buildingToBuild = null;
                }
            }
        }
    }

    void screenRightClick(int x, int y) {
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
                b = game.getBuildingAtLocation(hex.getLocation());
                if (b != null && b.getOwner().getId() == game.getThisPlayer().getId()) {
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
                u = game.getUnitOnHex(hex);
                if (u != null) {
                    if (u.getOwner() == game.getThisPlayer())
                        showUnitSellUI(x, y, u);
                    System.out.println("You clicked on a unit!");
                }
            }
        }
    }

    private void unitClick(Hexagon hex) {
        //If clicked on unit and no unit is selected
        if (game.getUnitOnHex(hex) != null && game.getSelectedUnit() == null && game.getUnitOnHex(hex).getOwner() == game.getThisPlayer()) {
            Unit u = game.getUnitOnHex(hex);
            u.toggleSelected();
            game.setWalkableHexesForUnit(u);
        }
        //If not clicked on unit and unit is selected
        else if (game.getUnitOnHex(hex) == null && game.getSelectedUnit() != null) {
            Unit u = game.getSelectedUnit();
            //Move unit to free hex
            if (game.hexEmpty(hex.getLocation())) {
                u.move(new Point(hex.getLocation().x, hex.getLocation().y));
                u.toggleSelected();
                game.updateFromLocal();
            }
            //Unit attacks enemy building
            else if (game.getBuildingAtLocation(hex.getLocation()) != null
                    && game.getBuildingAtLocation(hex.getLocation()).getOwner() != game.getThisPlayer()) {
                game.attackBuilding(game.getSelectedUnit(), hex.getLocation());
                u.toggleSelected();
            }
            //If clicked on unit and unit is selected
        } else if (game.getUnitOnHex(hex) != null && game.getSelectedUnit() != null) {
            //If clicked on the selected unit
            if (game.getUnitOnHex(hex) == game.getSelectedUnit()) {
                game.getSelectedUnit().toggleSelected();
                //If clicked on a different unit with the same owner
            } else if (game.getUnitOnHex(hex).getOwner() == game.getSelectedUnit().getOwner()) {
                game.getSelectedUnit().toggleSelected();
                game.getUnitOnHex(hex).toggleSelected();
                //If clicked on a unit with a different owner than the selected unit
            } else if (game.getUnitOnHex(hex).getOwner() != game.getSelectedUnit().getOwner()) {
                Unit enemy = game.getUnitOnHex(hex);
                Unit playerUnit = game.getSelectedUnit();
                if (playerUnit.attack(enemy)) {
                    if (enemy.getHealth() == 0) {
                        enemy.getOwner().removeUnit(enemy);
                    }
                }
                playerUnit.toggleSelected();
                if (enemy.getHealth() == 0) {
                    enemy.getOwner().removeUnit(enemy);
                }
                game.updateFromLocal();
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
        unitShopTable = new UnitShopTable(game, skin);
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
        buildingShopTable = new BuildingShopTable(this, game, skin);
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
        unitSellTable = new UnitSellTable(game, skin);
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
        buildingSellTable = new BuildingSellTable(game, skin);
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
            playerTable = new PlayerTable(game, skin);
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
        optionsTable = new OptionsTable(game, skin);
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
*/
