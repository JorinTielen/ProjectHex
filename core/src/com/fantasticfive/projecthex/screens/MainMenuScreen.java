package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.server.RMIServer;
import com.fantasticfive.server.ServerManager;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.Color;
import com.fantasticfive.shared.enums.UnitType;
import com.fantasticfive.shared.Point;
import com.fantasticfive.shared.enums.*;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private Skin skin;
    private Stage stage;
    private Table table;
    private SpriteBatch menuBatch = new SpriteBatch();

    Texture title = new Texture("title.png");
    Texture titleStart = new Texture("titleStart.png");
    Texture titleCopyright = new Texture("titleCopyright.png");

    //stuff for creating server
    private boolean serverStarted;
    private Future<Boolean> future;
    TextButton btnStartGame;
    private int frame;

    private Music menuMusic;

    //Tables
    private GameSelectTable gameSelectTable;
    private CreateServerTable createServerTable;
    private JoinServerTable joinServerTable;
    private OptionsTable optionsTable;

    private Random random = new Random();
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private boolean startScreen = true;
    private boolean cameraUp = true;

    Thread serverThread;


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
        game.getBatch().setProjectionMatrix(camera.combined);


        //draw all the sprites
        game.getBatch().begin();

        //draw all hexes from the map
        for (Hexagon hex : map.getHexagons()) {
            game.getBatch().draw(hex.getGroundImage(), hex.getPos().x, hex.getPos().y);
            if (hex.getObjectImage() != null) {
                game.getBatch().draw(hex.getObjectImage(), hex.getPos().x, hex.getPos().y);
            }
        }

        //draw units
        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                Hexagon h = map.getHexAtLocation(u.getLocation());
                game.getBatch().draw(u.getTexture(), h.getPos().x, h.getPos().y);
            }
        }
        game.getBatch().end();

        drawMenuBatch();
        if (serverStarted){
            if (future.isDone() && btnStartGame.getTouchable() == Touchable.disabled){
                btnStartGame.setText("Start game");
                btnStartGame.setTouchable(Touchable.enabled);
            }
            else if (btnStartGame.getTouchable() == Touchable.disabled && frame % 60 == 0) {
                btnStartGame.setText(btnStartGame.getText() + ".");
                if (btnStartGame.getText().length() > 10){
                    btnStartGame.setText("Loading.");
                }
            }
        }

        if (Gdx.input.isTouched() && startScreen) {
            startScreen = false;
            gameSelectTable.setVisible(true);
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
        game.batch.dispose();
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
        gameSelectTable = new GameSelectTable();
        createServerTable = new CreateServerTable();
        joinServerTable = new JoinServerTable();
        optionsTable = new OptionsTable();

        if (gameSelectTable != null) {
            table.addActor(gameSelectTable);
            gameSelectTable.setVisible(false);
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

    private void connectToServer(String ipAdress, String username) {
        localGame = new LocalGame(ipAdress, username);
        game.setScreen(new GameScreen(game, localGame));
        dispose();
    }

    private float resizeImage(float originalSize) {
        return originalSize / 1080 * screenHeight;
    }

    private class GameSelectTable extends Table {
        private Table t;
        private float collumnWidth = screenWidth / 100 * 25;
        private float collumnHeight = screenHeight / 100 * 6;

        public GameSelectTable() {
            t = new Table();

            final TextButton btnCreateServer = new TextButton("Create Server", skin);

            final TextButton btnJoinServer = new TextButton("Join Server", skin);

            final TextButton btnOptions = new TextButton("Options", skin);

            final TextButton btnExitGame = new TextButton("Exit", skin);

            t.add(btnCreateServer).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(btnJoinServer).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(btnOptions).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(btnExitGame).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();

            t.setPosition(screenWidth / 2, screenHeight / 2);

            btnCreateServer.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameSelectTable.setVisible(false);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    future = executor.submit(new ServerManager());
                    serverStarted = true;
                    /*serverThread = new Thread(() -> {
                        RMIServer server = new RMIServer();

                    });
                    serverThread.start();*/
                    InetAddress localhost = null;
                    try {
                        localhost = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    createServerTable.setIpAddress(localhost.getHostAddress()); //TODO: can be better because can't see when loading is done now
                    createServerTable.setVisible(true);
                }
            });

            btnJoinServer.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameSelectTable.setVisible(false);
                    joinServerTable.setVisible(true);
                }
            });

            btnOptions.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameSelectTable.setVisible(false);
                    optionsTable.setVisible(true);
                }
            });

            btnExitGame.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Gdx.app.exit();
                }
            });

            addActor(t);
        }
    }

    private class CreateServerTable extends Table {
        private Table t;
        private float collumnWidth = screenWidth / 100 * 25;
        private float collumnHeight = screenHeight / 100 * 6;

        private String ipAddress;
        private Label ipLabel;

        public CreateServerTable() {
            t = new Table();

            final TextButton btnCopyToClipboard = new TextButton("Copy to clipboard", skin);

            final TextButton btnBack = new TextButton("Back to menu", skin);

            btnStartGame = new TextButton("Start Game", skin);

            final TextField txtUsername = new TextField("", skin);

            t.add(ipLabel = new Label("Server ip address: ", skin));
            t.add(btnCopyToClipboard).width(collumnWidth / 2).height(collumnHeight / 2).pad(5);
            t.row();
            t.add(new Label("Username: ", skin));
            t.add(txtUsername).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(btnStartGame).fill().height(collumnHeight).colspan(2).pad(5);
            t.row();
            t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).colspan(2).pad(5);
            t.row();

            t.setPosition(screenWidth / 2, screenHeight / 2);

            btnStartGame.setText("Loading");
            btnStartGame.setTouchable(Touchable.disabled);

            btnCopyToClipboard.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
                    StringSelection stringSelection = new StringSelection(ipAddress);
                    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clpbrd.setContents(stringSelection, null);
                }
            });

            btnStartGame.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    connectToServer(ipAddress, txtUsername.getText());
                }
            });

            btnBack.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    btnStartGame.setText("Loading");
                    btnStartGame.setTouchable(Touchable.disabled);
                    serverStarted = false;
                    createServerTable.setVisible(false);
                    gameSelectTable.setVisible(true);
                }
            });


            addActor(t);
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            ipLabel.setText("Server ip address: " + ipAddress);
        }
    }

    private class JoinServerTable extends Table {
        private Table t;
        private float collumnWidth = screenWidth / 100 * 25;
        private float collumnHeight = screenHeight / 100 * 6;

        public JoinServerTable() {
            t = new Table();

            final TextField txtIP = new TextField("", skin);

            final TextField txtUsername = new TextField("", skin);

            final TextButton btnConnect = new TextButton("Connect to server", skin);

            final TextButton btnBack = new TextButton("Back to menu", skin);

            t.add(new Label("Server IP: ", skin));
            t.add(txtIP).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(new Label("Username: ", skin));
            t.add(txtUsername).width(collumnWidth).height(collumnHeight).pad(5);
            t.row();
            t.add(btnConnect).fill().height(collumnHeight).colspan(2).pad(5);
            t.row();
            t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).colspan(2).pad(5);
            t.row();

            t.setPosition(screenWidth / 2, screenHeight / 2);

            btnConnect.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    connectToServer(txtIP.getText(), txtUsername.getText());
                }
            });

            btnBack.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    joinServerTable.setVisible(false);
                    gameSelectTable.setVisible(true);
                }
            });

            addActor(t);
        }
    }

    private class OptionsTable extends Table {
        private Table t;
        private float collumnWidth = screenWidth / 100 * 25;
        private float collumnHeight = screenHeight / 100 * 6;

        private Properties prop = new Properties();
        private InputStream input;
        private OutputStream out;

        public OptionsTable() {
            t = new Table();

            final SelectBox selectResolution = new SelectBox(skin);
            String[] resolutions = new String[]{"1920x1080", "1600x900", "1280x720"};
            selectResolution.setItems(resolutions);

            final CheckBox checkFullScreen = new CheckBox("Fullscreen", skin);

            final Slider sliderMusic = new Slider(0, 1, 0.01f, false, skin);
            sliderMusic.setValue(menuMusic.getVolume());
            Label musicLabel = new Label(Float.toString(sliderMusic.getValue() * 100), skin);

            final TextButton btnApplyChanges = new TextButton("Apply changes", skin);

            final TextButton btnBack = new TextButton("Back to menu", skin);

            try {
                input = new FileInputStream("options.properties");
                prop.load(input);
                String propResolution = String.valueOf((int)screenWidth + "x" + (int)screenHeight);
                for (int i = 0; i < resolutions.length; i++) {
                    if (resolutions[i].equals(propResolution)) {
                         selectResolution.setSelectedIndex(i);
                    }
                }
                checkFullScreen.setChecked(Boolean.valueOf(prop.getProperty("fullscreen")));
                sliderMusic.setValue(Float.valueOf(prop.getProperty("musicvolume")));
                menuMusic.setVolume(sliderMusic.getValue());
                musicLabel.setText(String.format("%.0f", (sliderMusic.getValue() * 100)));
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            t.add(new Label("Resolution: ", skin));
            t.add(selectResolution);
            t.add(checkFullScreen);
            t.row();
            t.add(new Label("Music: ", skin));
            t.add(sliderMusic).width(collumnWidth).height(collumnHeight).pad(5);
            t.add(musicLabel);
            t.row();
            t.add(btnApplyChanges).height(collumnHeight).width(collumnWidth / 2).pad(5);
            t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).pad(5);
            t.row();

            t.setPosition(screenWidth / 2, screenHeight / 2);

            sliderMusic.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    menuMusic.setVolume(sliderMusic.getValue());
                    musicLabel.setText(String.format("%.0f", (sliderMusic.getValue() * 100)));
                }
            });

            btnApplyChanges.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    String resolution = (String) selectResolution.getSelected();
                    String[] resolutionParts = resolution.split("x");
                    if (checkFullScreen.isChecked()) {
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                        resolution = String.valueOf(Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
                    }
                    else{
                        Gdx.graphics.setWindowedMode(Integer.valueOf(resolutionParts[0]), Integer.valueOf(resolutionParts[1]));
                    }

                    try{
                        out = new FileOutputStream("options.properties");
                        prop.setProperty("resolution", resolution);
                        prop.setProperty("fullscreen", String.valueOf(checkFullScreen.isChecked()));
                        prop.setProperty("musicvolume", String.valueOf(sliderMusic.getValue()));
                        prop.store(out, "");
                        input.close();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    menuMusic.dispose();
                    game.setScreen(new MainMenuScreen(game));

                }
            });

            btnBack.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    try {
                        input = new FileInputStream("options.properties");
                        prop.load(input);
                        String propResolution = String.valueOf((int)screenWidth + "x" + (int)screenHeight);
                        for (int i = 0; i < resolutions.length; i++) {
                            if (resolutions[i].equals(propResolution)) {
                                selectResolution.setSelectedIndex(i);
                            }
                        }
                        checkFullScreen.setChecked(Boolean.valueOf(prop.getProperty("fullscreen")));
                        sliderMusic.setValue(Float.valueOf(prop.getProperty("musicvolume")));
                        menuMusic.setVolume(sliderMusic.getValue());
                        musicLabel.setText(String.format("%.0f", (sliderMusic.getValue() * 100)));
                        input.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    optionsTable.setVisible(false);
                    gameSelectTable.setVisible(true);
                }
            });

            addActor(t);
        }
    }

}
