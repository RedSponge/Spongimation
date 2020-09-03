package com.redsponge.spongimation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Spongimation extends Game {
    private SpriteBatch batch;

    private static Spongimation instance;

    public static Spongimation i() {
        return instance;
    }

    @Override
    public void create() {
        instance = this;
        VisUI.load();
        batch = new SpriteBatch();
        setScreen(new GUIScreen());
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
        VisUI.dispose();
    }

}