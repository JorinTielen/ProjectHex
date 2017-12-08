package com.fantasticfive.projecthex.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fantasticfive.projecthex.screens.GameMain;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("options.properties")) {
            prop.load(input);
            String resolution = prop.getProperty("resolution");
            String[] resolutionParts = resolution.split("x");
            if (Integer.valueOf(resolutionParts[0]) >= 1280) config.width = Integer.valueOf(resolutionParts[0]);
            else config.width = 1280;
            if (Integer.valueOf(resolutionParts[1]) >= 720) config.height = Integer.valueOf(resolutionParts[1]);
            else config.height = 720;
            config.fullscreen = Boolean.valueOf(prop.getProperty("fullscreen"));
        } catch (Exception e) {
            e.printStackTrace();
            config.width = 1280;
            config.height = 720;
            config.fullscreen = false;
        }

        config.resizable = false;
        new LwjglApplication(new GameMain(), config);
    }
}
