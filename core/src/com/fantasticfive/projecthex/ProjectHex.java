package com.fantasticfive.projecthex;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fantasticfive.game.*;

public class ProjectHex extends ApplicationAdapter {
    private InputManager input = new InputManager(this);
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Map map;
	private Game game;
	
	@Override
	public void create () {
	    //setup the camera
		camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //setup map
		map = new Map(20,15);

        //setup test game
        game = new Game();
        game.setMap(map);
        game.addPlayer("test1");

		//setup window
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
	    //handle input
	    input.HandleInput();
	    Gdx.input.setInputProcessor(input);

	    //move camera
		camera.translate(input.getCamPos());
		camera.update();
        batch.setProjectionMatrix(camera.combined);

        //set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw all the sprites
        batch.begin();

        for (Hexagon hex : map.getHexagons()) {

			batch.draw(hex.groundImage, hex.getPos().x, hex.getPos().y);
			if(hex.objectImage != null){
				batch.draw(hex.objectImage, hex.getPos().x, hex.getPos().y);
			}
		}

		for (Player p : game.getPlayers()) {
            for (Building b : p.getBuildings()) {
                Hexagon h = map.getHexAtLocation(b.getLocation());
                batch.draw(b.image, h.getPos().x, h.getPos().y);
            }
        }

        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
