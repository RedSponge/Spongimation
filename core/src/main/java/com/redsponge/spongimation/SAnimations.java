package com.redsponge.spongimation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class SAnimations implements Disposable {

    private Map<String, SAnimation> containedAnimations;
    private TextureAtlas atlas;

    public SAnimations(FileHandle animFile) {
        String animText = animFile.readString();
        parseAnimations(animText);
    }

    private void parseAnimations(String animText) {
        JsonReader reader = new JsonReader();
        JsonValue value = reader.parse(animText);

        String atlasFile = value.getString("atlas");
        atlas = new TextureAtlas(atlasFile);

        containedAnimations = new HashMap<>();
        JsonValue animations = value.get("animations");
        for (JsonValue animation : animations) {
            PlayMode playMode = PlayMode.valueOf(animation.getString("play_mode"));

            Array<SFrame<TextureRegion>> frames = new Array<>();
            for(JsonValue frame : animation.get("frames")) {
                String name = frame.getString("frame");
                int index = frame.getInt("idx");
                int duration = frame.getInt("length");
                frames.add(new SFrame<>(atlas.findRegion(name, index), duration));
            }
            containedAnimations.put(animation.name, new SAnimation(frames, playMode));
        }
    }

    public SAnimation get(String idle) {
        return containedAnimations.get(idle);
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
