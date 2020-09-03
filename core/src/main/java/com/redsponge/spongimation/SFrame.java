package com.redsponge.spongimation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SFrame<T> {

    private T region;
    private int time;

    public SFrame(T region, int time) {
        this.region = region;
        this.time = time;
    }

    public T getRegion() {
        return region;
    }

    public int getTime() {
        return time;
    }

    public SFrame<T> setRegion(T region) {
        this.region = region;
        return this;
    }

    public SFrame<T> setTime(int time) {
        this.time = time;
        return this;
    }
}
