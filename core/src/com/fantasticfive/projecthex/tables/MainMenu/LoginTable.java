package com.fantasticfive.projecthex.tables.MainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.Database;
import com.fantasticfive.projecthex.screens.MainMenuScreen;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class LoginTable extends Table {
    private Table t;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();
    private float collumnWidth = screenWidth / 100 * 25;
    private float collumnHeight = screenHeight / 100 * 6;

    private Properties prop = new Properties();
    private InputStream input;
    private OutputStream out;

    private Database database = new Database();

    private static final String salt = "$^*!#%123456789ProjectHex987654321%#!*^$";

    public LoginTable(MainMenuScreen menuScreen) {
        t = new Table();

        final TextField txtUsername = new TextField("", menuScreen.skin);

        final TextField txtPassword = new TextField("", menuScreen.skin);
        txtPassword.setPasswordMode(true);
        txtPassword.setPasswordCharacter('*');

        final CheckBox checkRememberMe = new CheckBox("Remember me", menuScreen.skin);

        final TextButton btnLogin = new TextButton("Login", menuScreen.skin);

        final TextButton btnRegister = new TextButton("Register", menuScreen.skin);

        final Label lblMessage = new Label("", menuScreen.skin);

        try {
            input = new FileInputStream("login.properties");
            prop.load(input);
            if (Boolean.valueOf(prop.getProperty("remember"))) {
                txtUsername.setText(prop.getProperty("username"));
                txtPassword.setText(prop.getProperty("password"));
                checkRememberMe.setChecked(true);
            }
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        t.add(new Label("Username: ", menuScreen.skin));
        t.add(txtUsername).height(collumnHeight).width(collumnWidth).pad(5);
        t.row();
        t.add(new Label("Password: ", menuScreen.skin));
        t.add(txtPassword).height(collumnHeight).width(collumnWidth).pad(5);
        t.row();
        t.add();
        t.add(lblMessage).width(collumnWidth);
        t.row();
        t.add(checkRememberMe).height(collumnHeight).width(collumnWidth).pad(5);
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
                        if (checkRememberMe.isChecked()) {
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
                        if (checkRememberMe.isChecked()) {
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

    private String generatePasswordHash(String password) {
        String md5 = null;
        String input = password + salt;

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    private boolean checkPasswordValid(String password) {
        String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,15}$";
        return password.matches(regexp);
    }
}
