package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.fantasticfive.shared.Unit;

public class UnitHPLossTable extends Table {

    private Table t;
    private Label l;
    private Skin skin;

    public UnitHPLossTable(Skin skin) {
        this.skin = skin;

        setVisible(false);
        t = new Table();
        l = new Label("", skin);
        l.setFontScale(2);

        t.add(l);

        addActor(t);
    }

    public void setHP(int hp) {
        l.setText("-" + hp + " hp");
        l.setColor(Color.RED);
    }
}
