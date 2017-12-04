package com.fantasticfive.projecthex.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fantasticfive.projecthex.screens.GameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 800;
		config.fullscreen = false;
		//new LwjglApplication(new ProjectHex(), config);
		new LwjglApplication(new GameMain(), config);
	}
}
