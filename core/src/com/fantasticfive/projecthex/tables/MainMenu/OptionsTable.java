package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.screens.GameMain;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class OptionsTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private Properties prop = new Properties();
    private InputStream input;
    private OutputStream out;

    public OptionsTable(MainMenuScreen menuScreen, GameMain game) {
        t = new Table();

        final SelectBox selectResolution = new SelectBox(menuScreen.skin);
        String[] resolutions = new String[]{"1920x1080", "1600x900", "1280x720"};
        selectResolution.setItems(resolutions);

        final CheckBox checkFullScreen = new CheckBox("Fullscreen", menuScreen.skin);

        final Slider sliderMusic = new Slider(0, 1, 0.01f, false, menuScreen.skin);
        sliderMusic.setValue(menuScreen.menuMusic.getVolume());
        Label musicLabel = new Label(Float.toString(sliderMusic.getValue() * 100), menuScreen.skin);

        final TextButton btnApplyChanges = new TextButton("Apply changes", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to menu", menuScreen.skin);

        try {
            input = new FileInputStream("options.properties");
            prop.load(input);
            String propResolution = String.valueOf((int) screenWidth + "x" + (int) screenHeight);
            for (int i = 0; i < resolutions.length; i++) {
                if (resolutions[i].equals(propResolution)) {
                    selectResolution.setSelectedIndex(i);
                }
            }
            checkFullScreen.setChecked(Boolean.valueOf(prop.getProperty("fullscreen")));
            sliderMusic.setValue(Float.valueOf(prop.getProperty("musicvolume")));
            menuScreen.menuMusic.setVolume(sliderMusic.getValue());
            musicLabel.setText(String.format("%.0f", (sliderMusic.getValue() * 100)));
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        t.add(new Label("Resolution: ", menuScreen.skin));
        t.add(selectResolution);
        t.add(checkFullScreen);
        t.row();
        t.add(new Label("Music: ", menuScreen.skin));
        t.add(sliderMusic).width(collumnWidth).height(collumnHeight).pad(5);
        t.add(musicLabel);
        t.row();
        t.add(btnApplyChanges).height(collumnHeight).width(collumnWidth / 2).pad(5);
        t.add(btnBack).height(collumnHeight).width(collumnWidth / 2).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        sliderMusic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen.menuMusic.setVolume(sliderMusic.getValue());
                musicLabel.setText(String.format("%.0f", (sliderMusic.getValue() * 100)));
            }
        });

        btnApplyChanges.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String resolution = (String) selectResolution.getSelected();
                String[] resolutionParts = resolution.split("x");
                if (checkFullScreen.isChecked()) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    resolution = String.valueOf(Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
                } else {
                    Gdx.graphics.setWindowedMode(Integer.valueOf(resolutionParts[0]), Integer.valueOf(resolutionParts[1]));
                }

                try {
                    out = new FileOutputStream("options.properties");
                    prop.setProperty("resolution", resolution);
                    prop.setProperty("fullscreen", String.valueOf(checkFullScreen.isChecked()));
                    prop.setProperty("musicvolume", String.valueOf(sliderMusic.getValue()));
                    prop.store(out, "");
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menuScreen.menuMusic.dispose();
                game.setScreen(new MainMenuScreen(game));

            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    input = new FileInputStream("options.properties");
                    prop.load(input);
                    String propResolution = String.valueOf((int) screenWidth + "x" + (int) screenHeight);
                    for (int i = 0; i < resolutions.length; i++) {
                        if (resolutions[i].equals(propResolution)) {
                            selectResolution.setSelectedIndex(i);
                        }
                    }
                    checkFullScreen.setChecked(Boolean.valueOf(prop.getProperty("fullscreen")));
                    sliderMusic.setValue(Float.valueOf(prop.getProperty("musicvolume")));
                    menuScreen.menuMusic.setVolume(sliderMusic.getValue());
                    musicLabel.setText(String.format("%.0f", (sliderMusic.getValue() * 100)));
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                menuScreen.optionsTable.setVisible(false);
                menuScreen.mainMenuTable.setVisible(true);
            }
        });

        addActor(t);
    }
}
