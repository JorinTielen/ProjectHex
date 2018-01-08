/*package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.Database;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

public class LobbyTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private Database database = new Database();

    private boolean owner;

    public LobbyTable(MainMenuScreen menuScreen, boolean owner) {
        t = new Table();
        this.owner = owner;

        final TextButton btnReady = new TextButton("Ready?", menuScreen.skin);

        final TextButton btnBack = new TextButton("Back to main menu", menuScreen.skin);


        t.add(new Label("Username: ", menuScreen.skin));
        t.add(txtUsername).height(collumnHeight).width(collumnWidth).pad(5);
        t.row();
        t.add(new Label("Password: ", menuScreen.skin));
        t.add(txtPassword).height(collumnHeight).width(collumnWidth).pad(5);
        t.row();
        t.add();
        t.add(lblMessage).width(collumnWidth);
        t.row();
        t.add(checkRemeberMe).height(collumnHeight).width(collumnWidth).pad(5);
        t.add(btnLogin).height(collumnHeight).width(collumnWidth).pad(5);
        t.row();
        t.add(new Label("Don't have an account yet?", menuScreen.skin));
        t.add(btnRegister).height(collumnHeight).width(collumnWidth).pad(5);
        t.row();

        t.setPosition(screenWidth / 2, screenHeight / 2);

        btnLogin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!database.checkUsername(txtUsername.getText())) {
                    lblMessage.setText("Username doesn't exist");
                } else if (!database.checkLogin(txtUsername.getText(), generatePasswordHash(txtPassword.getText()))) {
                    lblMessage.setText("Login failed. Username and/or password incorrect");
                } else {
                    try {
                        out = new FileOutputStream("login.properties");
                        if (checkRemeberMe.isChecked()) {
                            prop.setProperty("username", txtUsername.getText());
                            prop.setProperty("password", txtPassword.getText());
                            prop.setProperty("remember", "true");
                        } else {
                            prop.setProperty("username", "");
                            prop.setProperty("password", "");
                            prop.setProperty("remember", "false");
                        }

                        prop.store(out, "");
                        input.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    menuScreen.username = txtUsername.getText();
                    LoginTable.this.setVisible(false);
                    menuScreen.mainMenuTable.setVisible(true);
                }
            }
        });

        btnRegister.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (database.checkUsername(txtUsername.getText())) {
                    lblMessage.setText("Username already exists");
                } else if (!checkPasswordValid(txtPassword.getText())) {
                    lblMessage.setText("Password must be between 6 and 15 characters long,\nand must contain at least one lower case letter,\none upper case letter and one digit.");
                } else if (!database.registerAccount(txtUsername.getText(), generatePasswordHash(txtPassword.getText()))) {
                    lblMessage.setText("Failed to register. Check if both fields are filled");
                } else {
                    try {
                        out = new FileOutputStream("login.properties");
                        if (checkRemeberMe.isChecked()) {
                            prop.setProperty("username", txtUsername.getText());
                            prop.setProperty("password", txtPassword.getText());
                            prop.setProperty("remember", "true");
                        } else {
                            prop.setProperty("username", "");
                            prop.setProperty("password", "");
                            prop.setProperty("remember", "false");
                        }

                        prop.store(out, "");
                        input.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LoginTable.this.setVisible(false);
                    menuScreen.mainMenuTable.setVisible(true);
                }
            }
        });

        txtUsername.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lblMessage.setText("");
            }
        });

        txtPassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lblMessage.setText("");
            }
        });

        addActor(t);
    }
}*/
