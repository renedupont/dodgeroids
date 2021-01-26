package de.games.dodgeroids.screens;

import android.view.MotionEvent;
import android.view.View;
import de.games.dodgeroids.DodgeroidsActivity;
import de.games.engine.graphics.GameRenderer;
import de.games.engine.scenes.Scene;
import javax.microedition.khronos.opengles.GL11;

public final class GameOverScreen implements IGameScreen {

    private DodgeroidsActivity activity;
    private Scene scene;
    private GameRenderer renderer;

    private boolean isDone = false;

    public GameOverScreen(DodgeroidsActivity activity, GL11 gl) {
        this.activity = activity;
        GameOverFactory screen = new GameOverFactory();
        this.scene =
                new Scene(
                        screen.createTexts(activity, gl),
                        screen.createSprites(activity, gl),
                        screen.createBackground(
                                activity.getAssets(), gl, screen.getDefaultBackgroundTextureId()),
                        screen.createCamera(
                                activity.getViewportWidth(), activity.getViewportHeight()));
        this.renderer = new GameRenderer(gl, scene);
        activity.getGLSurfaceView().setOnTouchListener(this);
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void onBackPressed() {
        isDone = true;
    }

    @Override
    public void update(final float deltaTime) {}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isDone = true;
        }
        try {
            Thread.sleep(30); // TODO: this exists only so that onTouch will be true in the update
            // methods, please check if there is a better way
        } catch (Exception ex) {
        }
        return true;
    }

    @Override
    public IGameScreen switchScreen(GL11 gl) {
        return (new StartScreen(activity, gl));
    }

    @Override
    public GameRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void dispose() {
        renderer.dispose();
        scene.dispose();
    }
}
