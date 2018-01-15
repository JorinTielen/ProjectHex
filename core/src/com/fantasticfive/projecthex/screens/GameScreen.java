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
import com.fantasticfive.projecthex.SpriteAnimation;
import com.fantasticfive.projecthex.tables.*;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.enums.BuildingType;
import com.fantasticfive.shared.enums.UnitType;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameScreen implements Screen {
    private static final Logger LOGGER = Logger.getLogger(GameScreen.class.getName());

    final GameMain game;

    //lib gdx
    private InputManager input = new InputManager(this);
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //game
    private LocalGame localGame;
    private Map map;

    //ui
    private Skin skin;
    private Stage stage;
    private Table table;
    private Texture blankTexture; //blank texture for health bars
    private Texture walkableHexTexture; //Texture overlay for hexes that unit can walk to
    private Texture fogTexture; //Texture for fog of war
    private Texture fogNeighbourTexture; //Texture for fog of war neighbouring visisted land
    private Texture menuTexture;
    private SpriteAnimation explosionAnimation;
    private boolean inMenu;
    private boolean uiClearedAfterEndTurn;

    //endTurnAnimation
    private int frameCounter;

    //tables
    private Table unitShopTable;
    private Table playerTable;
    private Table buildingShopTable;
    private Table unitSellTable;
    private Table buildingSellTable;
    private Table optionsTable;
    private Table playerWinTable;
    private Table unitScoutTable;
    private Table unitHPLossTable;
    private Table gameMenuTable;
    private Building buildingToBuild;
    private int unitHPLossTableUp = 0;

    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();

    public GameScreen(final GameMain game, LocalGame localGame) {
        this.game = game;

        //Setup Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        //Setup Map

        //Setup Game
        this.localGame = localGame;
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

        uiClearedAfterEndTurn = false;

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

        createUnitScoutUI();
        if (unitScoutTable != null) {
            table.addActor(unitScoutTable);
        }

        createUnitHPLossUI();
        if (unitHPLossTable != null) {
            table.addActor(unitHPLossTable);
        }

        createBuildingSellUI();
        if (buildingSellTable != null) {
            table.addActor(buildingSellTable);
        }

        createOptionsUI();
        if (optionsTable != null) {
            table.addActor(optionsTable);
        }

        createPlayerWinUI();
        if (playerWinTable != null) {
            table.add(playerWinTable);
        }

        createGameMenuUI();
        if(gameMenuTable != null){
            table.add(gameMenuTable);
            gameMenuTable.setVisible(false);
        }

        //Input processor
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(input);
        Gdx.input.setInputProcessor(inputMultiplexer);

        //set blank texture
        blankTexture = new Texture("whitePixel.png");
        walkableHexTexture = new Texture("movableHex.png");
        fogTexture = new Texture("fog.png");
        fogNeighbourTexture = new Texture("fogNeighbour.png");
        menuTexture = new Texture("gameMenuBackground.png");

        Hexagon startHex = map.getHexAtLocation(localGame.getThisPlayer().getBuildings().get(0).getLocation());
        camera.position.set(startHex.getPos().x, startHex.getPos().y, 0);

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
            batch.draw(hex.getGroundImage(), hex.getPos().x, hex.getPos().y);
            if (hex.getObjectImage() != null) {
                batch.draw(hex.getObjectImage(), hex.getPos().x, hex.getPos().y);
            }
            if (hex.getColorCoding() != null) {
                batch.draw(hex.getColorCoding(), hex.getPos().x, hex.getPos().y);
            }
        }

        //draw all buildings and units from all players
        for (Player p : localGame.getPlayers()) {
            for (Building b : p.getBuildings()) {
                if (b.getImage() == null) {
                    b.setImage();
                    LOGGER.info("no image");
                }

                Hexagon h = map.getHexAtLocation(b.getLocation());
                batch.draw(b.getImage(), h.getPos().x, h.getPos().y);
                batch.setColor(Color.RED);

                //draw health bar
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, 50, 5);
                batch.setColor(Color.GREEN);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, (int) ((double) 50 * ((double) b.getHealth() / (double) b.getMaxHealth())), 5);
                batch.setColor(Color.WHITE);
                if (b.getDestroyed() && explosionAnimation == null) {
                    explosionAnimation = new SpriteAnimation("explosion", b.getLocation());
                }
                if (b.getDestroyed() && explosionAnimation != null){
                    if (!explosionAnimation.getActive() && b.getLocation() != explosionAnimation.getLocation()){
                        explosionAnimation = new SpriteAnimation("explosion", b.getLocation());
                    }
                }
            }
            for (Unit u : p.getUnits()) {
                if (u.getTexture() == null) {
                    u.setTexture();
                }

                Hexagon h = map.getHexAtLocation(u.getLocation());
                batch.draw(u.getTexture(), h.getPos().x, h.getPos().y);
                batch.setColor(Color.RED);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, 50, 5);
                batch.setColor(Color.GREEN);
                batch.draw(blankTexture, h.getPos().x + 35, h.getPos().y + 100, (int) ((double) 50 * ((double) u.getHealth() / (double) u.getMaxHealth())), 5);
                batch.setColor(Color.WHITE);
            }
        }

        //draw how many HP a unit loses when it is attacked
        if(unitHPLossTable.isVisible()) {
            unitHPLossTable.setY(unitHPLossTable.getY() + 0.25f);
            unitHPLossTableUp++;
            if(unitHPLossTableUp == 25) {
                unitHPLossTableUp = 0;
                unitHPLossTable.setVisible(false);
            }
        }

        //draw building when trying to place it on a Hexagon
        if (buildingToBuild != null) {
            Vector3 mousePos = new Vector3(Gdx.input.getX() - 80, Gdx.input.getY() + 80, 0); //Image position gets set hard-coded to get it under the cursor.
            camera.unproject(mousePos);
            if (buildingToBuild.image == null) {
                buildingToBuild.setImage();
            }
            batch.draw(buildingToBuild.getImage(), mousePos.x, mousePos.y);
        }

        //draw explosion animation
        if (explosionAnimation != null) {
            if (explosionAnimation.getActive()) {
                batch.draw(explosionAnimation.getTexture(), explosionAnimation.getPos().x, explosionAnimation.getPos().y);
                if (!explosionAnimation.animate()) {
                    Building buildingToDestroy = localGame.getBuildingAtLocation(explosionAnimation.getLocation());
                    if (buildingToDestroy != null){
                        localGame.destroyBuilding(buildingToDestroy);
                    }
                }
            }
        }


        //draw fog of war
        if (localGame.getFog() != null) {
            for (Hexagon h : map.getHexagons()) {
                if (!localGame.getFog().isVisisted(h) && !localGame.getFog().isNeighbour(h)) {
                    batch.draw(fogTexture, h.getPos().x, h.getPos().y);
                } else if (localGame.getFog().isNeighbour(h)) {
                    batch.draw(fogNeighbourTexture, h.getPos().x, h.getPos().y);
                }
            }
        }

        //draw area where unit can walk
        if (localGame.getSelectedUnit() != null) {
            if (localGame.getSelectedUnit().getMovementLeft() > 0){
                for (Hexagon h : localGame.getSelectedUnit().getWalkableHexes()) {
                    batch.draw(walkableHexTexture, h.getPos().x, h.getPos().y);
                }
            }
        }

        if (localGame.lastPlayer()) {
            showPlayerWinUI();
        }

        if(inMenu){
            batch.draw(menuTexture, camera.position.x - (screenWidth / 2f),camera.position.y - (screenHeight / 2f),screenWidth, screenHeight);
        }

        if(localGame.isMyTurn()) {
            if(uiClearedAfterEndTurn) {
                uiClearedAfterEndTurn = false;
            }
        } else {
            clearUI();
            uiClearedAfterEndTurn = true;
        }

        batch.end();

        //Animate endTurn button if it is your turn
        if (localGame.isMyTurn()){
            frameCounter++;
            if (frameCounter >= 7200){
                ((OptionsTable)optionsTable).animateEndTurnButton(frameCounter);
                localGame.endTurn();
                frameCounter = 0;
            }
            if (frameCounter != 0){
                ((OptionsTable)optionsTable).animateEndTurnButton(frameCounter);
            }
        }
        else {
            frameCounter = 0;
        }


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

        if (localGame.isMyTurn()) {
            Vector3 tmp = new Vector3(x, y, 0);
            camera.unproject(tmp);

            for (Hexagon hex : map.getHexagons()) {
                Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                        hex.getGroundImage().getWidth(), hex.getGroundImage().getHeight());
                if (clickArea.contains(tmp.x, tmp.y)) {
                    LOGGER.info("clicked hex: " + hex.getLocation().getX() + " " + hex.getLocation().getY());

                    //When clicked on unit or unit is selected
                    if (localGame.getUnitOnHex(hex) != null || localGame.getSelectedUnit() != null) {
                        buildingToBuild = null;
                        unitClick(hex, x, y);
                    }
                    //Create building on hex
                    else if (buildingToBuild != null) {
                        if (buildingToBuild instanceof Resource) {
                            localGame.buyBuilding(BuildingType.RESOURCE, hex.getLocation());
                        } else if (buildingToBuild instanceof Fortification) {
                            localGame.buyBuilding(BuildingType.FORTIFICATION, hex.getLocation());
                        } else if (buildingToBuild instanceof Barracks) {
                            localGame.buyBuilding(BuildingType.BARRACKS, hex.getLocation());
                        }
                        buildingToBuild = null;
                    }
                    return;
                }
            }
        }
    }

    public void screenRightClick(int x, int y) {
        clearUI();

        if (localGame.isMyTurn()) {
            Vector3 tmp = new Vector3(x, y, 0);
            camera.unproject(tmp);

            for (Hexagon hex : map.getHexagons()) {
                Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                        hex.getGroundImage().getWidth(), hex.getGroundImage().getHeight());
                if (clickArea.contains(tmp.x, tmp.y)) {
                    LOGGER.info("clicked hex: " + hex.getLocation().getX() + " " + hex.getLocation().getY());

                    //Right click on own building
                    Building b;
                    b = localGame.getBuildingAtLocation(hex.getLocation());
                    if (b != null && b.getOwner().getId() == localGame.getThisPlayer().getId()) {
                        if (b instanceof Barracks) {
                            LOGGER.info("You clicked on a Barracks");
                            showUnitShopUI(x, y, b);
                        } else if (b instanceof TownCentre) {
                            LOGGER.info("You clicked on a TownCentre");
                            showBuildingShopUI(x, y);
                        } else if (b instanceof Resource) {
                            LOGGER.info("You clicked on a Resource");
                            showBuildingSellUI(x, y, b);
                        } else if (b instanceof Fortification) {
                            LOGGER.info("You clicked on a Fortification");
                            showBuildingSellUI(x, y, b);
                        }
                    }
                    //Right click on own unit
                    Unit u;
                    u = localGame.getUnitOnHex(hex);
                    if (u != null) {
                        if (u.getOwner() == localGame.getThisPlayer()) {
                            if (u.getUnitType() == UnitType.SCOUT) {
                                showUnitScoutUI(x, y, u);
                            } else {
                                showUnitSellUI(x, y, u);
                            }
                            LOGGER.info("You clicked on a unit!");
                        }
                    }
                }
            }
        }
    }

    private void unitClick(Hexagon hex, float x, float y) {
        //If clicked on unit and no unit is selected
        if (localGame.getUnitOnHex(hex) != null && localGame.getSelectedUnit() == null && localGame.getUnitOnHex(hex).getOwner() == localGame.getThisPlayer()) {
            Unit u = localGame.getUnitOnHex(hex);
            u.toggleSelected();
            if (u.getMovementLeft() > 0){
                localGame.setWalkableHexesForUnit(u);
            }
        }
        //If not clicked on unit and unit is selected
        else if (localGame.getUnitOnHex(hex) == null && localGame.getSelectedUnit() != null) {
            Unit u = localGame.getSelectedUnit();
            //Is there a building? if so, attack it.
            Building b = localGame.getBuildingAtLocation(hex.getLocation());
            if (b != null) {
                localGame.attackBuilding(u, b);
                //Move unit to hex if free
            } else {
                localGame.moveUnit(u, hex.getLocation());
                localGame.setWalkableHexesForUnit(localGame.getSelectedUnit());
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
                localGame.setWalkableHexesForUnit(localGame.getSelectedUnit());

                //If clicked on a unit with a different owner than the selected unit
            } else if (localGame.getUnitOnHex(hex).getOwner() != localGame.getSelectedUnit().getOwner()) {
                //Attack the other unit
                Unit u = localGame.getSelectedUnit();
                Unit enemy = localGame.getUnitOnHex(hex);
                int hp = localGame.attackUnit(u, enemy);
                if (hp > 0){
                    showUnitHPLossUI(hp, x, y);
                }
            }
        }
    }

    private void clearUI() {
        buildingSellTable.setVisible(false);
        buildingShopTable.setVisible(false);
        unitSellTable.setVisible(false);
        unitShopTable.setVisible(false);
        unitScoutTable.setVisible(false);
    }

    // ====================
    //  Unit Shop (Barracks)
    // ====================
    private void createUnitShopUI() {
        unitShopTable = new UnitShopTable(localGame, skin);
    }

    private void showUnitShopUI(float x, float y, Building building) {
        ((UnitShopTable) unitShopTable).setBuilding(building);
        unitShopTable.setPosition(x, screenHeight - y);
        unitShopTable.setVisible(true);
    }

    // ====================
    //  Building Shop (Town Centre)
    // ====================
    private void createBuildingShopUI() {
        buildingShopTable = new BuildingShopTable(this, localGame, skin);
    }

    private void showBuildingShopUI(float x, float y) {
        buildingShopTable.setPosition(x, screenHeight - y);
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
        unitSellTable.setPosition(x, screenHeight - y);
        unitSellTable.setVisible(true);
    }

    private void createUnitScoutUI() {
        unitScoutTable = new UnitScoutTable(localGame, skin);
    }

    private void showUnitScoutUI(float x, float y, Unit unit) {
        ((UnitScoutTable) unitScoutTable).setUnit(unit);
        unitScoutTable.setPosition(x, screenHeight - y);
        unitScoutTable.setVisible(true);
    }

    private void createUnitHPLossUI() {
        unitHPLossTable = new UnitHPLossTable(skin);
    }

    private void showUnitHPLossUI(int hp, float x, float y) {
        camera.update();
        unitHPLossTable.setPosition(x, screenHeight - y + 100);
        ((UnitHPLossTable) unitHPLossTable).setHP(hp);
        unitHPLossTable.setVisible(true);
    }


    // ====================
    //  Building UI
    // ====================
    private void createBuildingSellUI() {
        buildingSellTable = new BuildingSellTable(localGame, skin);
    }

    private void showBuildingSellUI(float x, float y, final Building building) {
        ((BuildingSellTable) buildingSellTable).setBuilding(building);
        buildingSellTable.setPosition(x, screenHeight - y);
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
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    private void updatePlayerUI() {
        ((PlayerTable) playerTable).update();
    }

    // ====================
    //  Game UI
    //  (Shows the next turn button)
    // ====================
    private void createOptionsUI() {
        optionsTable = new OptionsTable(localGame, skin);
    }

    // ====================
    //  Player won UI
    //  (Shows which player has won the game)
    // ====================
    private void createPlayerWinUI() {
        playerWinTable = new PlayerWinTable(skin);
    }

    private void showPlayerWinUI() {
        playerWinTable.setPosition(screenWidth / 2 - playerWinTable.getWidth() / 2,
                screenHeight / 2 - playerWinTable.getHeight() / 2);
        playerWinTable.setVisible(true);
        ((PlayerWinTable)playerWinTable).setEndGameLabel(localGame);
    }

    private void createGameMenuUI(){
        gameMenuTable = new GameMenuTable(game, localGame, this, skin);
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
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);
    }

    public void toggleGameMenu(){
        if(gameMenuTable.isVisible()){
            gameMenuTable.setVisible(false);
            inMenu = false;

            optionsTable.setVisible(true);
        }
        else{
            gameMenuTable.setVisible(true);
            inMenu = true;

            optionsTable.setVisible(false);
        }
    }

    public boolean isInMenu() {
        return this.inMenu;
    }

    public void setInMenu(boolean inMenu) {
        this.inMenu = inMenu;
    }
}