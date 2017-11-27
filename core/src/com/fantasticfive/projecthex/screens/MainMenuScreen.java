package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.fantasticfive.shared.*;
import com.fantasticfive.shared.Map;
import com.fantasticfive.shared.enums.*;

import java.rmi.RemoteException;
import java.util.*;

public class MainMenuScreen implements Screen {

    final GameMain game;

    OrthographicCamera camera;

    Map map;
    List<Player> players = new ArrayList<>();

    public MainMenuScreen(final GameMain game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Tryout for map background
        map =  new Map(10,10);
        map.setTextures();

        Player p1 = new Player("bot1", Color.RED,1);
        p1.addGold(1000);
        p1.purchaseUnit(new Unit(UnitType.ARCHER, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p1);

        Player p2 = new Player("bot1", Color.RED,1);
        p2.addGold(1000);
        p2.purchaseUnit(new Unit(UnitType.SWORDSMAN, 100, 100, 10, 10, 1, 10, 0, false, 1));
        players.add(p2);


        Random random = new Random();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Player p: players) {
                    for (IUnit u: p.getUnits()) {
                        int low = -1;
                        int high = 1;
                        int randomx = random.nextInt(high - low) + low;
                        int randomy = random.nextInt(high - low) + low;
                        Point randomLocation = u.getLocation();
                        randomLocation.x += randomx;
                        randomLocation.y += randomy;
                    }
                }
            }
        }, 0, 2000);

    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        //set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Move camera
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //draw all the sprites
        game.batch.begin();

        //draw all hexes from the map
        for (Hexagon hex : map.getHexagons()) {
            game.batch.draw(hex.groundImage, hex.getPos().x, hex.getPos().y);
            if (hex.objectImage != null) {
                game.batch.draw(hex.objectImage, hex.getPos().x, hex.getPos().y);
            }
        }

        game.font.draw(game.batch, "Welcome to Project Hex!!! ", 100, 150);
        game.font.draw(game.batch, "Press any button to start", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

}
