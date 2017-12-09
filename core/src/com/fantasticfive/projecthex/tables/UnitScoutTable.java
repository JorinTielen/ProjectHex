package com.fantasticfive.projecthex.tables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fantasticfive.projecthex.LocalGame;
import com.fantasticfive.shared.Unit;

import java.util.logging.Logger;

public class UnitScoutTable extends Table {
    private static final Logger LOGGER = Logger.getLogger( UnitScoutTable.class.getName() );

    private Table t;
    private Label l;

    final private LocalGame game;
    private Skin skin;
    private Unit unit;

    public UnitScoutTable(LocalGame game, Skin skin) {
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
                LOGGER.info("Selling unit");
                UnitScoutTable.this.game.sellUnit(unit);
                setVisible(false);
            }
        });

        final TextButton buttonClaimLand = new TextButton("Claim Land", skin);
        t.add(buttonClaimLand).fill();
        t.row();

        //Method for selling unit
        buttonClaimLand.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("Claiming land");
                UnitScoutTable.this.game.claimLand(unit);
                setVisible(false);
            }
        });

        addActor(t);
    }

    public void setUnit(Unit u) {
        this.unit = u;
        this.l.setText(this.unit.getUnitType().toString());
    }
}
