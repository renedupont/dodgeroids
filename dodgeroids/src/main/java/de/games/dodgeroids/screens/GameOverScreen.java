package de.games.dodgeroids.screens;

import android.view.MotionEvent;
import android.view.View;
import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.Scene;
import de.games.engine.graphics.GameRenderer;
import de.games.engine.screens.IGameScreen;
import javax.microedition.khronos.opengles.GL11;

public final class GameOverScreen implements IGameScreen {

    /** class elements * */
    private final AbstractGameActivity activity;

    private final Scene scene;
    private final GameRenderer renderer;

    /** control flags * */
    private boolean isDone = false;

    public GameOverScreen(final AbstractGameActivity activity, final GL11 gl) {
        this.activity = activity;
        this.scene = new Scene(activity, gl, new GameOverFactory());
        this.renderer = new GameRenderer(gl, activity, scene);
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
    public boolean onTouch(final View v, final MotionEvent event) {
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
    public IGameScreen switchScreen(final GL11 gl) {
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
