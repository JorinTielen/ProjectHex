package com.fantasticfive.projecthex;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fantasticfive.game.*;
import com.fantasticfive.game.enums.UnitType;

public class ProjectHex extends ApplicationAdapter {
    private InputManager input = new InputManager(this);
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Map map;
//    private Skin skin;
//    private Stage stage;
    private Game game;

    @Override
    public void create() {
        //setup the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //setup map
        map = new Map(20, 15);

        //setup test game
        game = new Game();
        game.setMap(map);
        game.addPlayer("maxim");

        //setup window
        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(1280, 720, camera);
        Gdx.input.setInputProcessor(input);

        //Just trying something
//        skin = new Skin();
//        stage = new Stage();
//        Gdx.input.setInputProcessor(stage);
//        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.WHITE);
//        pixmap.fill();
//        skin.add("white", new Texture(pixmap));
//        skin.add("default", new BitmapFont());
//
//        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
//        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
//        textButtonStyle.font = skin.getFont("default");
//        skin.add("default", textButtonStyle);
//
//        Table table = new Table();
//        table.setFillParent(true);
//        stage.addActor(table);
//
//        final TextButton buttonCreateUnit = new TextButton("Create unit", skin);
//        final TextButton buttonMoveUnit = new TextButton("Move unit", skin);
//        table.add(buttonCreateUnit);
//        table.add(buttonMoveUnit);
//
//        buttonCreateUnit.addListener(new ChangeListener() {
//            public void changed(ChangeEvent event, Actor actor) {
//                game.createUnit(game.getTestPlayer(), UnitType.SWORDSMAN, new Point(2, 2, -4));
//                game.createUnit(game.getTestPlayer(), UnitType.ARCHER, new Point(1, 1, -2));
//                //buttonCreateUnit.setDisabled(true);
//            }
//        });
//
//        buttonMoveUnit.addListener(new ChangeListener() {
//            public void changed(ChangeEvent event, Actor actor) {
//                if (game.getTestUnit() != null) {
//                    game.MoveUnit(game.getTestUnit(), new Point(4, 2, -6));
//                }
//            }
//        });
    }

    @Override
    public void render() {
        //handle input
        input.HandleInput();

        //move camera
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
        for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                Hexagon h = map.getHexAtLocation(b.getLocation());
                batch.draw(b.image, h.getPos().x, h.getPos().y);
            }
            for (Unit u : p.getUnits()) {
                Hexagon h = map.getHexAtLocation(u.getLocation());
                batch.draw(u.getTexture(), h.getPos().x, h.getPos().y);
            }
        }

        batch.end();
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void screenClick(int x, int y) {
        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);
        for (Hexagon hex : map.getHexagons()) {
            Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                    hex.groundImage.getWidth(), hex.groundImage.getHeight());
            if (clickArea.contains(tmp.x,tmp.y)) {
                System.out.println("clicked hex: " + hex.getLocation().x + " " + hex.getLocation().y);
            }
        }
    }
}
