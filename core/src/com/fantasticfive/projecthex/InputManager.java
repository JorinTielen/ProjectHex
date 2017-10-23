package com.fantasticfive.projecthex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.io.InputStream;
import java.net.InetAddress;

public class InputManager implements InputProcessor {
    private Vector2 camPos = Vector2.Zero;
    private ProjectHex projectHex;

    public InputManager(ProjectHex projectHex) {
        this.projectHex = projectHex;
    }

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
        } else {
            camPos = Vector2.Zero;
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            projectHex.screenLeftClick(screenX, screenY);
            return false;
        } else if(button == Input.Buttons.RIGHT) {
            projectHex.screenRightClick(screenX, screenY);
            return false;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
