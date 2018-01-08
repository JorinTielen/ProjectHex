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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.projecthex.tables.MainMenu.*;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.enums.*;

import java.util.*;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MainMenuScreen implements Screen {

    private static final Logger LOGGER = Logger.getLogger(MainMenuScreen.class.getName());

    private final GameMain game;
    private LocalGame localGame;

    private OrthographicCamera camera;
    private float camZoom = 2f;

    private Map map;
    private int mapWidth = 50;
    private int mapHeight = 50;
    private List<Player> players = new ArrayList<>();

    //ui
    public Skin skin;
    private Stage stage;
    private Table table;
    private SpriteBatch menuBatch = new SpriteBatch();
    private SpriteBatch backgroundBatch = new SpriteBatch();

    Texture title = new Texture("title.png");
    Texture titleStart = new Texture("titleStart.png");
    Texture titleCopyright = new Texture("titleCopyright.png");

    //stuff for creating server
    public boolean serverStarted;
    public Future<Boolean> future;
    private int frame;

    public Music menuMusic;

    //Tables
    public LoginTable loginTable;
    public MainMenuTable mainMenuTable;
    public CreateServerTable createServerTable;
    public JoinServerTable joinServerTable;
    public OptionsTable optionsTable;

    private Random random = new Random();
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private boolean startScreen = true;
    private boolean cameraUp = true;

    public String username;

    public MainMenuScreen(final GameMain game) {
        this.serverStarted = false;
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth * camZoom, screenHeight * camZoom);
        camera.translate(new Vector2(screenWidth, 100));

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

        startMusic();

        createPlayers();

        createTimer();

        createUIElements();
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

        Vector2 camVec = new Vector2(camera.position.x - screenWidth, camera.position.y - screenHeight);
        if (camVec.y >= map.getHexAtLocation(new Point(mapHeight - 1, 0)).getPos().y - (screenHeight * camZoom))
            cameraUp = false;
        if (camVec.y <= map.getHexAtLocation(new Point(0, 0)).getPos().y) cameraUp = true;

        float xMovement = 0.75f;
        float yMovement = 1f;

        if (cameraUp) {
            camera.translate(new Vector2(xMovement, yMovement));
        } else {
            camera.translate(new Vector2(-xMovement, -yMovement));
        }
        camera.update();
        backgroundBatch.setProjectionMatrix(camera.combined);


        //draw all the sprites
        backgroundBatch.begin();

        //draw all hexes from the map
        for (Hexagon hex : map.getHexagons()) {
            backgroundBatch.draw(hex.getGroundImage(), hex.getPos().x, hex.getPos().y);
            if (hex.getObjectImage() != null) {
                backgroundBatch.draw(hex.getObjectImage(), hex.getPos().x, hex.getPos().y);
            }
        }

        //draw units
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                Hexagon h = map.getHexAtLocation(u.getLocation());
                backgroundBatch.draw(u.getTexture(), h.getPos().x, h.getPos().y);
            }
        }
        backgroundBatch.end();

        drawMenuBatch();
        if (serverStarted) {
            if (future.isDone() && createServerTable.btnStartGame.getTouchable() == Touchable.disabled) {
                createServerTable.btnStartGame.setText("Start game");
                createServerTable.btnStartGame.setTouchable(Touchable.enabled);
            } else if (createServerTable.btnStartGame.getTouchable() == Touchable.disabled && frame % 60 == 0) {
                createServerTable.btnStartGame.setText(createServerTable.btnStartGame.getText() + ".");
                if (createServerTable.btnStartGame.getText().length() > 10) {
                    createServerTable.btnStartGame.setText("Loading.");
                }
            }
        }

        if (Gdx.input.isTouched() && startScreen) {
            startScreen = false;
            loginTable.setVisible(true);
            //mainMenuTable.setVisible(true); //TODO: Change this to login screen
        }

        stage.act(delta);
        stage.draw();
        frame++;
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
        menuBatch.dispose();
        backgroundBatch.dispose();
        if (menuMusic != null) menuMusic.dispose();
    }

    public void startMusic() {
        menuMusic = Gdx.audio.newMusic(new FileHandle("menuMusic.ogg"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0);
        menuMusic.play();
        // menuMusic.setVolume(0); //Comment if you want to turn off music
    }

    private void drawMenuBatch() {
        menuBatch.begin();
        menuBatch.draw(title, (screenWidth / 2) - (resizeImage(title.getWidth() / 2f)), screenHeight / 100 * 80, resizeImage(title.getWidth()), resizeImage(title.getHeight()));
        menuBatch.draw(titleCopyright, 10, 10, resizeImage(titleCopyright.getWidth()), resizeImage(titleCopyright.getHeight()));
        if (startScreen) {
            menuBatch.draw(titleStart, (screenWidth / 2) - (resizeImage(titleStart.getWidth()) / 2f), screenHeight / 100 * 40, resizeImage(titleStart.getWidth()), resizeImage(titleStart.getHeight()));
        }
        menuBatch.end();

    }

    private void createUIElements() {
        loginTable = new LoginTable(this);
        mainMenuTable = new MainMenuTable(this);
        createServerTable = new CreateServerTable(this);
        joinServerTable = new JoinServerTable(this);
        optionsTable = new OptionsTable(this, game);

        if (loginTable != null) {
            table.addActor(loginTable);
            loginTable.setVisible(false);
        }

        if (mainMenuTable != null) {
            table.addActor(mainMenuTable);
            mainMenuTable.setVisible(false);
        }

        if (createServerTable != null) {
            table.addActor(createServerTable);
            createServerTable.setVisible(false);
        }

        if (joinServerTable != null) {
            table.addActor(joinServerTable);
            joinServerTable.setVisible(false);
        }

        if (optionsTable != null) {
            table.addActor(optionsTable);
            optionsTable.setVisible(false);
        }
    }

    private void createTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Point randomLocation = new Point(0, 0);
                for (Player p : players) {
                    for (Unit u : p.getUnits()) {
                        while (!u.getLocation().equals(randomLocation)) {
                            int low = -1;
                            int high = 2;
                            int randomx = random.nextInt(high - low) + low;
                            int randomy = random.nextInt(high - low) + low;
                            randomLocation = u.getLocation().value();
                            int randomXY = random.nextInt(2);
                            if (randomXY == 0) {
                                randomLocation.x += randomx;
                            } else {
                                randomLocation.y += randomy;
                            }
                            if (map.getHexAtLocation(randomLocation) != null) {
                                if (!map.getHexAtLocation(randomLocation).getIsMountain() && map.getHexAtLocation(randomLocation).getGroundType() != GroundType.WATER) {
                                    if (randomLocation.x >= 0 && randomLocation.x < mapWidth && randomLocation.y >= 0 && randomLocation.y < mapHeight) {
                                        u.setLocation(randomLocation);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 1000);
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
        players.add(p4);

        Player p5 = new Player("bot1", Color.PURPLE, 5);
        p5.addGold(1000);
        p5.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p5.purchaseUnit(new Unit(UnitType.SCOUT, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p5.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p5);

        Player p6 = new Player("bot1", Color.ORANGE, 6);
        p6.addGold(1000);
        p6.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p6.purchaseUnit(new Unit(UnitType.SCOUT, 100, 100, 10, 10, 1, 10, 0, false, 1));
        p6.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p6);

        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                u.setOwner(p);
                u.setTexture();
                Point randomLocation = new Point(0, 0);
                boolean validHex = false;
                while (!validHex) {
                    randomLocation = new Point(random.nextInt(mapWidth), random.nextInt(mapHeight));
                    if (!map.getHexAtLocation(randomLocation).getIsMountain() && map.getHexAtLocation(randomLocation).getGroundType() != GroundType.WATER) {
                        validHex = true;
                    }
                }
                u.setLocation(randomLocation);
            }
        }
    }

    public void connectToServer(String ipAdress, String username) {
        localGame = new LocalGame(ipAdress, username);
        game.setScreen(new GameScreen(game, localGame));
        dispose();
    }

    private float resizeImage(float originalSize) {
        return originalSize / 1080 * screenHeight;
    }
}
