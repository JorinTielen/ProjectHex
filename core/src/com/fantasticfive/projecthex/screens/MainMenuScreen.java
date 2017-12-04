package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.server.RMIServer;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.*;

import java.util.*;
import java.util.List;

public class MainMenuScreen implements Screen {

    private final GameMain game;

    private OrthographicCamera camera;
    private float camZoom = 2f;

    private Map map;
    private int mapWidth = 50;
    private int mapHeight = 50;
    private List<Player> players = new ArrayList<>();

    //ui
    private Skin skin;
    private Stage stage;
    private Table table;
    private SpriteBatch menuBatch = new SpriteBatch();

    private Texture title;
    private Texture titleStart;
    private Music menuMusic;

    //Tables
    private Table loginTable = new Table();

    private Random random = new Random();
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private boolean startScreen = true;

    public MainMenuScreen(final GameMain game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth * camZoom, screenHeight * camZoom);

        //Setup UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);
        Gdx.input.setInputProcessor(stage);

        //Create map
        map = new Map(mapWidth, mapHeight);
        map.setTextures();

        createPlayers();

        createTimer();

        createUIElements();

        startMusic();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        //set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Move camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        camera.translate(new Vector2(0.75f, 1));

        //draw all the sprites
        game.batch.begin();

        //draw all hexes from the map
        for (Hexagon hex : map.getHexagons()) {
            game.batch.draw(hex.groundImage, hex.getPos().x, hex.getPos().y);
            if (hex.objectImage != null) {
                game.batch.draw(hex.objectImage, hex.getPos().x, hex.getPos().y);
            }
        }

        //draw units
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                Hexagon h = map.getHexAtLocation(u.getLocation());
                game.batch.draw(u.getTexture(), h.getPos().x, h.getPos().y);
            }
        }
        game.batch.end();

        menuBatch.begin();
        title = new Texture("title.png");
        titleStart = new Texture("titleStart.png");
        menuBatch.draw(title, (screenWidth / 2) - (title.getWidth() / 2), screenHeight / 100 * 80);
        if (startScreen) {
            menuBatch.draw(titleStart, (screenWidth / 2) - (titleStart.getWidth() / 2), screenHeight / 100 * 40);
        }
        menuBatch.end();

        if (Gdx.input.isTouched() && startScreen) {
            startScreen = false;
            loginTable.setVisible(true);
        }
        stage.act(delta);
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
        menuMusic.dispose();
    }

    public void startMusic() {
        menuMusic = Gdx.audio.newMusic(new FileHandle("menuMusic.ogg"));
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    private void createUIElements() {
        loginTable = new LoginTable();

        if (loginTable != null) {
            table.addActor(loginTable);
            loginTable.setVisible(false);
        }
    }

    private void createMenuBatch() {

    }

    private void createTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Player p : players) {
                    for (Unit u : p.getUnits()) {
                        int low = -1;
                        int high = 2;
                        int randomx = random.nextInt(high - low) + low;
                        int randomy = random.nextInt(high - low) + low;
                        Point randomLocation = u.getLocation().value();
                        int randomXY = random.nextInt(2);
                        if (randomXY == 0) {
                            randomLocation.x += randomx;
                        } else {
                            randomLocation.y += randomy;
                        }
                        if (randomLocation.x >= 0 && randomLocation.x < mapWidth && randomLocation.y >= 0 && randomLocation.y < mapHeight)
                            u.setLocation(randomLocation);
                    }
                }
            }
        }, 0, 1500);
    }

    private void createPlayers() {
        Player p1 = new Player("bot1", Color.RED, 1);
        p1.addGold(1000);
        p1.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p1.purchaseUnit(new Unit(UnitType.SCOUT, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p1.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p1);

        Player p2 = new Player("bot1", Color.BLUE, 2);
        p2.addGold(1000);
        p2.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p2.purchaseUnit(new Unit(UnitType.SCOUT, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p2.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p2);

        Player p3 = new Player("bot1", Color.YELLOW, 3);
        p3.addGold(1000);
        p3.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p3.purchaseUnit(new Unit(UnitType.SCOUT, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p3.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p3);

        Player p4 = new Player("bot1", Color.GREEN, 4);
        p4.addGold(1000);
        p4.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p4.purchaseUnit(new Unit(UnitType.SCOUT, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p4.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p3);


        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                u.setOwner(p);
                u.setTexture();
                u.setLocation(new Point(random.nextInt(mapWidth), random.nextInt(mapHeight)));
            }
        }
    }

    private void connectToServer(String ipAdress, String username) {
        LocalGame localGame = new LocalGame(ipAdress, username);
        game.setScreen(new GameScreen(game, localGame));
        dispose();
    }

    private class LoginTable extends Table {
        private Table t;
        private float collumnWidth = screenWidth / 100 * 25;
        private float collumnHeight = screenHeight / 100 * 6;

        public LoginTable() {
            t = new Table();

            final TextField txtIP = new TextField("", skin);

            final TextField txtUsername = new TextField("", skin);

            final TextButton btnConnect = new TextButton("Connect to server!", skin);

            t.add(new Label("Server IP: ", skin));
            t.add(txtIP).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(new Label("Username: ", skin));
            t.add(txtUsername).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(btnConnect).fill().height(collumnHeight).colspan(2).pad(5);
            t.row();

            t.setPosition(screenWidth / 2, screenHeight / 2);

            btnConnect.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    connectToServer(txtIP.getText(), txtUsername.getText());
                    // RMIServer server = new RMIServer();
                    //txtUsername.setText(server.getIpAddress());
                }
            });

            addActor(t);
        }
    }
}
