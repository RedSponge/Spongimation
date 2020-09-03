package com.redsponge.spongimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class GUIScreen extends ScreenAdapter {

    private Stage stage;
    private FitViewport viewport;
    private Image animationDisplay;

    private SAnimations animations;
    private Animation<TextureRegion> anim;
    private float time;

    private int selectedFrame;
    private boolean isPlaying;

    private Array<ImageButton> frameButtons;

    private String animationName;

    private TextField durationInput;

    @Override
    public void show() {
        super.show();
        animationName = "attack_up";
        viewport = new FitViewport(720, 720);
        stage = new Stage(viewport, Spongimation.i().getBatch());
        Array<SFrame<TextureRegion>> frames = new Array<>();
        frames.add(new SFrame<>(new TextureRegion(new Texture("anim_1.png")), 200));
        frames.add(new SFrame<>(new TextureRegion(new Texture("anim_2.png")), 100));
        frames.add(new SFrame<>(new TextureRegion(new Texture("anim_3.png")), 200));
        animations = new SAnimations(Gdx.files.internal("player.sanim"));
        anim = animations.get(animationName).getBuiltAnimation();
        buildStage();

        Gdx.input.setInputProcessor(stage);
    }

    private void buildStage() {
        VisTable table = new VisTable();
        VisTable top = new VisTable();
        VisTable left = new VisTable();
        VisTable mid = new VisTable();
        VisTable right = new VisTable();
        VisTable bottom = new VisTable();

        durationInput = new TextField("", VisUI.getSkin());
        durationInput.setMaxLength(4);
        durationInput.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    int newDur = Integer.parseInt(durationInput.getText());
                    animations.get(animationName).getFrames().get(selectedFrame).setTime(newDur);
                    animations.get(animationName).rebuild();
                    System.out.println("HI");
                } catch (NumberFormatException e) {
                    System.out.println("Empty Field!");
                }
            }
        });
        durationInput.setTextFieldFilter((textField, c) -> '0' <= c && c <= '9');
        left.add(durationInput);

//        left.add(new Label("Very cool test", VisUI.getSkin()));
//        mid.add(new Label("Incredibly Fancy", VisUI.getSkin()));
        right.add(new Label("Uwu", VisUI.getSkin()));

        animationDisplay = new Image(new TextureRegionDrawable());
        animationDisplay.setScaling(Scaling.fit);
        mid.add(animationDisplay).grow();

        frameButtons = new Array<>();
        Table frames = new Table();
        frames.setTransform(true);
        int idx = 0;
        for (SFrame<TextureRegion> frame : animations.get(animationName).getFrames()) {
            ImageButton button = new ImageButton(new TextureRegionDrawable(frame.getRegion()));
            button.setTransform(true);
            button.getImageCell().grow();
            frames.add(button).growY().width(64).height(96);
            int finalIdx = idx;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    setSelectedFrame(finalIdx);
                }
            });
            frameButtons.add(button);
            idx++;
        }
        ScrollPane pane = new ScrollPane(frames, VisUI.getSkin());
        pane.setOverscroll(false, false);
        bottom.add(pane).grow().width(viewport.getWorldWidth());
        pane.setDebug(true);
        bottom.setDebug(true);

        TextButton playButton = new TextButton("Play Animation", VisUI.getSkin());
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePlay();
            }
        });
        top.add(playButton);

//        stage.setDebugAll(true);

        table.add(top).colspan(5).growX();
        table.row().grow();
        table.add(left);
        table.add(mid).colspan(3);
        table.add(right);
        table.row();
        table.add(bottom).colspan(5).height(100).growX();
        table.setPosition(0, 0);
        table.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(table);
    }

    private void togglePlay() {
        isPlaying = !isPlaying;
        if(isPlaying) {
            beginPlaying();
        } else {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        durationInput.setDisabled(false);
    }

    private void beginPlaying() {
        time = 0;
        durationInput.setDisabled(true);
    }

    private void updatePlaying() {
        if(isPlaying) {
            System.out.println(time + " " + animations.get(animationName).getIndexForTime(time));
            setSelectedFrame(animations.get(animationName).getIndexForTime(time));
        }
    }

    private void updateGUI() {
        durationInput.setText(String.valueOf(animations.get(animationName).getFrames().get(selectedFrame).getTime()));
    }

    private void setSelectedFrame(int selectedFrame) {
        frameButtons.get(this.selectedFrame).getImage().setColor(Color.WHITE);
        this.selectedFrame = selectedFrame;
        updateGUI();
        frameButtons.get(this.selectedFrame).getImage().setColor(Color.YELLOW);
        System.out.println(frameButtons.get(this.selectedFrame));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += delta;
        updatePlaying();
        StringBuilder b = new StringBuilder();
        ((TextureRegionDrawable)animationDisplay.getDrawable()).setRegion(animations.get(animationName).getFrames().get(selectedFrame).getRegion());
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
