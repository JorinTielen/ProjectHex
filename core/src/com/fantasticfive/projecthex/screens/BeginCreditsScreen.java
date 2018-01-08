package com.fantasticfive.projecthex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class BeginCreditsScreen implements Screen {

    private final GameMain game;

    //ui
    private Skin skin;
    private Stage stage;
    private Table table;

    private Image logo = new Image(new Texture("FantasticFiveLogo.png"));

    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();

    private long startTime;
    private long currentTime;
    private long waitingTime = 750;


    public BeginCreditsScreen(final GameMain game) {
        this.game = game;

        //Setup UI
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        // table.setDebug(true);
        table.setVisible(true);
        table.add(logo);
        startTime = TimeUtils.millis();
    }

    @Override
    public void show() {
        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(fadeIn(0.5f));
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
        currentTime = TimeUtils.millis();
        if ((currentTime - startTime) > waitingTime) {
            switchScreen();
            currentTime = startTime;
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

    public void switchScreen() {
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(0.5f));
        sequenceAction.addAction(run(() -> game.setScreen(new MainMenuScreen(game))));
        stage.getRoot().addAction(sequenceAction);
    }

    private float resizeImage(float originalSize) {
        return originalSize / 1080 * screenHeight;
    }
}
