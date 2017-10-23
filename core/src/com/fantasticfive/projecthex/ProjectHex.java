package com.fantasticfive.projecthex;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fantasticfive.game.*;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.ObjectType;

public class ProjectHex extends ApplicationAdapter {
    private InputManager input = new InputManager(this);
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Map map;
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

        //setup window
        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(1280, 720, camera);
        Gdx.input.setInputProcessor(input);
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
                if(game.getUnitOnHex(hex) != null || game.getSelectedUnit() != null) {
                    unitClick(hex);
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
                    && hex.getGroundType() != GroundType.WATER) {
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
}
