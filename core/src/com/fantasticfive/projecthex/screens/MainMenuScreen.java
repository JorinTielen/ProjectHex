package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.*;

import java.util.*;

public class MainMenuScreen implements Screen {

    private final GameMain game;


    private OrthographicCamera camera;

    private Map map;
    private int mapWidth = 25;
    private int mapHeight = 25;
    private List<Player> players = new ArrayList<>();

    //ui
    private Skin skin;
    private Stage stage;
    private Table table;

    public MainMenuScreen(final GameMain game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        skin = new Skin();
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);

        createSkin();

        //Tryout for map background
        map = new Map(mapWidth, mapHeight);
        map.setTextures();
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

        Random random = new Random();

        for (Player p : players) {
            for (Unit u : p.getUnits()) {
                u.setTexture();
                u.setLocation(new Point(random.nextInt(mapWidth), random.nextInt(mapHeight)));
            }
        }

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
        camera.translate(new Vector2(0, 1));

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
        game.font.draw(game.batch, "Welcome to ProjectHex!!! ", 100 , 150 );
        game.font.draw(game.batch, "Press any button to start", 100 , 100 );
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
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
    }


    private void createSkin() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", com.badlogic.gdx.graphics.Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", com.badlogic.gdx.graphics.Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", com.badlogic.gdx.graphics.Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", com.badlogic.gdx.graphics.Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.background = skin.newDrawable("white", com.badlogic.gdx.graphics.Color.LIGHT_GRAY);
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);
    }
}
