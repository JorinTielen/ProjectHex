package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.shared.IGame;
import com.fantasticfive.shared.IUnit;


public class UnitSellTable extends Table {
    private Table t;
    private Label l;

    final private IGame game;
    private Skin skin;
    private IUnit unit;

    public UnitSellTable(IGame game, Skin skin) {
        setVisible(false);
        this.t = new Table();
        this.game = game;
        this.skin = skin;
        l = new Label("", skin);

        t.add(l).fill();
        t.row();

        final TextButton buttonSellUnit = new TextButton("Sell", skin);
        t.add(buttonSellUnit).fill();
        t.row();

        //Method for selling unit
        buttonSellUnit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Selling unit");
                UnitSellTable.this.game.getCurrentPlayer().sellUnit(unit);
                setVisible(false);
            }
        });

        addActor(t);
    }

    public void setUnit (IUnit u) {
        this.unit = u;
        this.l.setText(this.unit.getUnitType().toString());
    }
}
