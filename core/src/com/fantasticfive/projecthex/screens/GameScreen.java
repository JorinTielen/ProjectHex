package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.projecthex.InputManager;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.shared.*;

public class GameScreen implements Screen{
    final GameMain game;

    //lib gdx
    private InputManager input;
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

    private IBuilding buildingToBuild;

    public GameScreen(final GameMain game) {
        this.game = game;

        input = new InputManager(game);

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

        /*
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

        */

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
            for (IBuilding b : p.getBuildings()) {
                if(b.getImage() == null) {
                }
                Hexagon h = map.getHexAtLocation(b.getLocation());
                batch.draw(b.getImage(), h.getPos().x, h.getPos().y);
            }
            for (IUnit u : p.getUnits()) {
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
        //updatePlayerUI();

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
}
