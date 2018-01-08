package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMain extends Game {

    public void create() {

       // this.setScreen(new MainMenuScreen(this)); //Start game without logo intro
        this.setScreen(new BeginCreditsScreen(this)); //Start game with logo intro
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
    }
}
