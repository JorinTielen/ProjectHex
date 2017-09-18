package com.fantasticfive.projecthex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class InputManager {
    private Vector2 camPos = Vector2.Zero;

    public Vector2 getCamPos() {
        return camPos;
    }

    public void HandleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camPos = new Vector2(0, 5);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camPos = new Vector2(-5, 0);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camPos = new Vector2(0, -5);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camPos = new Vector2(5, 0);
        }
        else {
            camPos = Vector2.Zero;
        }

    }
}
