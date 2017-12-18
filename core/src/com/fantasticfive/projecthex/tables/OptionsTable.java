package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.fantasticfive.projecthex.LocalGame;

import java.util.logging.Logger;


public class OptionsTable extends Table {
    private static final Logger LOGGER = Logger.getLogger( OptionsTable.class.getName() );

    private Table t;
    private Table tTimer;
    private int colWidth = 130;
    private int colHeight = 40;

    final private LocalGame game;
    private Skin skin;
    private TextButton buttonEndTurn;
    private Label timeLeftLabel;
    private boolean endTurnButtonBig;

    private int secondsLeftInTurn;
    private int frameButtonGrew;

    public OptionsTable(LocalGame game, Skin skin) {
        secondsLeftInTurn = Integer.MAX_VALUE;
        endTurnButtonBig = false;
        this.game = game;
        this.skin = skin;

        t = new Table();
        tTimer = new Table();
        buttonEndTurn = new TextButton("End turn", skin);
        buttonEndTurn.setTransform(true);
        t.add(buttonEndTurn).fill().size(colWidth,colHeight);

       buttonEndTurn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("Ending turn");
                OptionsTable.this.game.endTurn();
                if (game.isMyTurn()){
                    setTimeLabel(7200);
                }
            }
        });


        timeLeftLabel = new Label("-:--", skin);
        timeLeftLabel.setFontScale(2);
        tTimer.add(timeLeftLabel);
        t.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - colHeight);
        tTimer.setPosition(Gdx.graphics.getWidth() / 2 + 140, Gdx.graphics.getHeight() - colHeight);

        addActor(t);
        addActor(tTimer);
    }

    public void animateEndTurnButton(int frame){
        if (frame == 1){
            setTimeLabel(frame);
        }
        boolean growButton = false;
        Cell endTurnCell = t.getCell(buttonEndTurn);
        if (secondsLeftInTurn != 0 && frame != 0){
            if (frame % 60 == 0) {
                secondsLeftInTurn = 120 - (frame / 60);
                setTimeLabel(frame);
            }
            if (frame < 2599 && frame % 300 == 0){
                growButton = true;
            }
            if (frame > 2599 && frame < 5399 && frame % 200 == 0){
                growButton = true;
            }
            if (frame > 5399 && frame < 6299 && frame % 100 == 0){
                growButton = true;
            }
            if (frame > 6299 && frame % 50 == 0){
                growButton = true;
            }
            if (!endTurnButtonBig && growButton){
                System.out.println(endTurnCell.getMinWidth() + " - " + frame);
                endTurnCell.width(endTurnCell.getMinWidth() + 32);
                endTurnCell.height(endTurnCell.getMinHeight() + 10);
                buttonEndTurn.getLabel().setFontScale(1.2F);
                endTurnButtonBig = true;
                frameButtonGrew = frame;
            }
            if (frame >= frameButtonGrew + 15 && endTurnButtonBig){
                System.out.println(endTurnCell.getMinWidth() + " - " + frame);
                endTurnCell.width(endTurnCell.getMinWidth() - 32);
                endTurnCell.height(endTurnCell.getMinHeight() - 10);
                buttonEndTurn.getLabel().setFontScale(1F);
                endTurnButtonBig = false;
                frameButtonGrew = frame;
            }
        }

    }

    private void setTimeLabel(int frame){
        int seconds = secondsLeftInTurn;
        if (frame == 1){
            this.timeLeftLabel.setText("2:00");
        }
        else if (frame == 7200){
            timeLeftLabel.setColor(Color.WHITE);
            this.timeLeftLabel.setText("-:--");
        }
        else {
            if (secondsLeftInTurn == 15){
                timeLeftLabel.setColor(Color.RED);
            }
            StringBuilder stringBuilder = new StringBuilder();
            if (secondsLeftInTurn - 59 > 0){
                stringBuilder.append("1:");
                seconds = seconds - 60;
            }
            else {
                stringBuilder.append("0:");
            }
            if (seconds == 60){
                stringBuilder.append(00);
            }
            else if (seconds < 10){
                stringBuilder.append(0);
                stringBuilder.append(seconds);
            }
            else {
                stringBuilder.append(seconds);
            }
            timeLeftLabel.setText(stringBuilder);
        }
    }
}
