package com.redsponge.spongimation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;


public class SAnimation {

    private Array<SFrame<TextureRegion>> frames;
    private PlayMode playMode;
    private Animation<TextureRegion> builtAnimation;

    private float[] timeStamps;

    public SAnimation(Array<SFrame<TextureRegion>> frames, PlayMode playMode) {
        this.frames = new Array<>(frames);
        this.playMode = playMode;
        this.timeStamps = new float[frames.size];
        buildAnimation();
    }

    private void buildAnimation() {
        int[] frameLens = new int[frames.size];
        float timeCounter = 0;
        for (int i = 0; i < frames.size; i++) {
            frameLens[i] = frames.get(i).getTime();
            timeStamps[i] = timeCounter;
            timeCounter += frames.get(i).getTime() / 1000f;
        }

        int divisor = findGCD(frameLens);
        Array<TextureRegion> spacedFrames = new Array<>();
        for (int i = 0; i < frames.size; i++) {
            for(int j = 0; j < frameLens[i] / divisor; j++) {
                spacedFrames.add(frames.get(i).getRegion());
            }
        }

        builtAnimation = new Animation<>(divisor / 1000f, spacedFrames);
        builtAnimation.setPlayMode(playMode);
    }

    public static int gcd(int p, int q) {
        if (q == 0) {
            return p;
        }
        return gcd(q, p % q);
    }

    public static int findGCD(int[] arr)
    {
        int result = arr[0];
        for (int i = 1; i < arr.length; i++)
        {
            result = gcd(arr[i], result);

            if(result == 1)
            {
                return 1;
            }
        }
        return result;
    }

    public int getIndexForTime(float time) {
        time = getByPlaymode(time);
        int begin = 0;
        int end = timeStamps.length;
        System.out.println(Arrays.toString(timeStamps));
        while(begin != end && begin != end - 1) {
            int idx = (begin + end) / 2;
            if(timeStamps[idx] > time) {
                end = idx;
            } else if(timeStamps[idx] == time) {
                return idx;
            } else {
                begin = idx;
            }
        }
        return begin;
    }

    private float getByPlaymode(float time) {
        switch (playMode) {
            case NORMAL:
                return time;
            case LOOP:
                return time % builtAnimation.getAnimationDuration();
            case REVERSED:
                return builtAnimation.getAnimationDuration() - time;
            case LOOP_REVERSED:
                return builtAnimation.getAnimationDuration() - (time % builtAnimation.getAnimationDuration());
            case LOOP_PINGPONG:
                int evenFlag = (int) (time / builtAnimation.getAnimationDuration()) % 2;
                float evenOption = time % builtAnimation.getAnimationDuration();
                float oddOption = builtAnimation.getAnimationDuration() - evenOption;
                return evenFlag * evenOption + (1 - evenFlag) * oddOption;
        }
        return 0;
    }

    public Animation<TextureRegion> getBuiltAnimation() {
        return builtAnimation;
    }

    public Array<SFrame<TextureRegion>> getFrames() {
        return frames;
    }

    public void rebuild() {
        buildAnimation();
    }
}
