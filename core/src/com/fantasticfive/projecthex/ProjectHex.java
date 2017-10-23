package com.fantasticfive.projecthex;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fantasticfive.game.*;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.ObjectType;
import com.fantasticfive.game.enums.UnitType;

public class ProjectHex extends ApplicationAdapter {
    private InputManager input = new InputManager(this);
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Map map;
    private Skin skin;
    private Stage stage;
    private Table table;
    private Table unitShopTable;
    private Table playerTable;
    private Table unitSellTable;
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
        game.addPlayer("enemy");
        game.startGame();

        //setup window
        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(1280, 720, camera);

        //UI
        skin = new Skin();
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);

        //createBuildingShopUI();

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



        //input mess
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(input);
        Gdx.input.setInputProcessor(inputMultiplexer);
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
                batch.draw(u.texture, h.getPos().x, h.getPos().y);
            }
        }

        //draw buttons


        batch.end();

        //draw ui
        table.clearChildren();

        if (unitShopTable != null) {
            table.addActor(unitShopTable);
        }

        createPlayerUI();
        if (playerTable != null) {
            table.addActor(playerTable);
        }

        if(unitSellTable != null) {
            table.addActor(unitSellTable);
        }

        stage.addActor(table);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void screenLeftClick(int x, int y) {
        unitShopTable = null;
        unitSellTable = null;

        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);
        for (Hexagon hex : map.getHexagons()) {
            Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                    hex.groundImage.getWidth(), hex.groundImage.getHeight());
            if (clickArea.contains(tmp.x, tmp.y)) {
                System.out.println("clicked hex: " + hex.getLocation().x + " " + hex.getLocation().y);
                if(game.getUnitOnHex(hex) != null || game.getSelectedUnit() != null) {
                    unitClick(hex);
                }
            }
        }
    }

    public void screenRightClick(int x, int y) {
        unitShopTable = null;
        unitSellTable = null;

        Vector3 tmp = new Vector3(x, y, 0);
        camera.unproject(tmp);
        for (Hexagon hex : map.getHexagons()) {
            Rectangle clickArea = new Rectangle(hex.getPos().x, hex.getPos().y,
                    hex.groundImage.getWidth(), hex.groundImage.getHeight());
            if (clickArea.contains(tmp.x, tmp.y)) {
                System.out.println("clicked hex: " + hex.getLocation().x + " " + hex.getLocation().y);

                Building b = game.getBuildingAtLocation(hex.getLocation());
                if (b != null) {
                    if (b instanceof Barracks) {
                        System.out.println("You clicked on a Barracks");
                        createUnitShopUI(x, y);
                    } else if (b instanceof TownCentre) {
                        System.out.println("You clicked on a TownCentre");

                    }
                }
                Unit u = game.getUnitOnHex(hex);
                if(u != null) {
                    if(u.getOwner() == game.getCurrentPlayer())
                    createSellUnitUI(x,y,u);
                    System.out.println("You clicked on a unit!");
                }
            }
        }
    }

    private void unitClick(Hexagon hex) {
        //If clicked on unit and no unit is selected
        if(game.getUnitOnHex(hex) != null && game.getSelectedUnit() == null && game.getUnitOnHex(hex).getOwner() == game.getPlayers().get(0)) {
            Unit u = game.getUnitOnHex(hex);
            u.toggleSelected();
        //If not clicked on unit and unit is selected
        } else if(game.getUnitOnHex(hex) == null && game.getSelectedUnit() != null) {
            Unit u = game.getSelectedUnit();
            //TODO Is this supposed to be here?
            if (hex.getObjectType() != ObjectType.MOUNTAIN
                    && hex.getGroundType() != GroundType.WATER
                    && game.getBuildingAtLocation(hex.getLocation()) == null) {
                u.move(new Point(hex.getLocation().x, hex.getLocation().y));
                u.toggleSelected();
            }
        //If clicked on unit and unit is selected
        } else if(game.getUnitOnHex(hex) != null && game.getSelectedUnit() != null) {
            //If clicked on the selected unit
            if(game.getUnitOnHex(hex) == game.getSelectedUnit()) {
                game.getSelectedUnit().toggleSelected();
            //If clicked on a different unit with the same owner
            } else if(game.getUnitOnHex(hex).getOwner() == game.getSelectedUnit().getOwner()) {
                game.getSelectedUnit().toggleSelected();
                game.getUnitOnHex(hex).toggleSelected();
            //If clicked on a unit with a different owner than the selected unit
            } else if(game.getUnitOnHex(hex).getOwner() != game.getSelectedUnit().getOwner()) {
                Unit enemy = game.getUnitOnHex(hex);
                Unit playerUnit = game.getSelectedUnit();
                playerUnit.attack(enemy);
                playerUnit.toggleSelected();
                if(enemy.getHealth() == 0) {
                    enemy.getOwner().removeUnit(enemy);
                }
            }
        }
    }

    public void createUnitShopUI(float x, float y) {
        System.out.println(x + ", " + y);
        Table t = new Table();
        t.add(new Label("Buy Unit", skin)).fill();
        t.row();

        final TextButton buttonBuyArcher = new TextButton("Archer", skin);
        t.add(buttonBuyArcher).fill();
        t.row();

        final TextButton buttonBuySwordsman = new TextButton("Swordsman", skin);
        t.add(buttonBuySwordsman).fill();
        t.row();

        t.setPosition(x, Gdx.graphics.getHeight() - y);

        buttonBuyArcher.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //method for buying archer
                System.out.println("you bought an archer");
                game.createUnit(game.getCurrentPlayer(), UnitType.ARCHER, new Point(3,1));
                unitShopTable = null;
            }
        });

        buttonBuySwordsman.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                //method for buying swordsman
                System.out.println("you bought a swordsman");
                game.createUnit(game.getCurrentPlayer(), UnitType.SWORDSMAN, new Point(3,1));
                unitShopTable = null;
            }
        });

        unitShopTable = t;
    }

    private void createSellUnitUI(float x, float y, final Unit unit) {
        Table t = new Table();

        t.add(new Label(unit.getUnitType().toString(), skin)).fill();
        t.row();

        final TextButton buttonSellUnit = new TextButton("Sell", skin);
        t.add(buttonSellUnit).fill();
        t.row();

        t.setPosition(x, Gdx.graphics.getHeight() - y);

        buttonSellUnit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Selling unit");
                game.getCurrentPlayer().sellUnit(unit);
                unitSellTable = null;
            }
        });

        unitSellTable = t;
    }

    public void createPlayerUI() {
        Table t = new Table();
        Label l = new Label("GOLD: " + game.getCurrentPlayer().getGold(), skin);
        t.add(l).width(90);

        Label gpt = new Label("GPT: " + game.getCurrentPlayer().getGoldPerTurn(), skin);
        if(game.getCurrentPlayer().getGoldPerTurn() < 0) {
            gpt.setColor(Color.RED);
        } else {
            gpt.setColor(Color.WHITE);
        }
        t.add(gpt).width(90);

        t.setPosition(100, Gdx.graphics.getHeight() - 10);

        playerTable = t;
    }
}
