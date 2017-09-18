package com.fantasticfive.projecthex;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ProjectHex extends ApplicationAdapter {
    private InputManager input = new InputManager();

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture hexTex;
	private HexMap map;
	
	@Override
	public void create () {
	    //setup the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		//hardcoded texture
        hexTex = new Texture("grassClear.png");

        //setup map
		map = new HexMap(40, 25);

		//setup window
		batch = new SpriteBatch();
		ExtendViewport viewport = new ExtendViewport(1280, 720, camera);

	}

	@Override
	public void render () {
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
		for (Hex hex : map.getHexes()) {
			batch.draw(hexTex, hex.getPos().x, hex.getPos().y);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		batch.dispose();
	}
}
