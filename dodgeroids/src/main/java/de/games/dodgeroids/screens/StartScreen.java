package de.games.dodgeroids.screens;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import de.games.dodgeroids.DodgeroidsActivity;
import de.games.dodgeroids.R;
import de.games.dodgeroids.datamanagers.DodgeroidsSaveGame;
import de.games.dodgeroids.datamanagers.DodgeroidsSettingsManager;
import de.games.dodgeroids.datamanagers.SoundManager;
import de.games.engine.graphics.GameRenderer;
import de.games.engine.scenes.Scene;
import javax.microedition.khronos.opengles.GL11;

public final class StartScreen implements IGameScreen {

    private DodgeroidsActivity activity;
    private Scene scene;
    private GameRenderer renderer;

    private boolean isDone = false;
    private boolean soundOptionChanged = false;

    public StartScreen(DodgeroidsActivity activity, GL11 gl) {
        this.activity = activity;
        StartFactory screen = new StartFactory();
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
        if (isDone) {
            SoundManager.getInstance()
                    .setSoundOn(DodgeroidsSettingsManager.getInstance().isSoundOn());
        }
        return isDone;
    }

    @Override
    public void onBackPressed() {
        this.activity.finish();
    }

    @Override
    public void update(float deltaTime) {
        if (soundOptionChanged) {
            scene.getText(activity.getString(R.string.label_sound))
                    .setText(
                            DodgeroidsSettingsManager.getInstance().isSoundOn()
                                    ? activity.getString(R.string.text_on)
                                    : activity.getString(R.string.text_off));
            soundOptionChanged = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int touchX = (int) event.getX();
            int touchY = (int) event.getY();
            activity.runOnUiThread(
                    () ->
                            activity.getWindow()
                                    .clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));
            touchY = activity.getViewportHeight() - touchY;

            if (scene.getText(activity.getString(R.string.text_start))
                    .isTouched(touchX, touchY, 30, 30)) {
                DodgeroidsSaveGame.getInstance().setResumable(false);
                isDone = true;
            } else if (DodgeroidsSaveGame.getInstance().isResumable()
                    && scene.getText(activity.getString(R.string.text_resume))
                            .isTouched(touchX, touchY, 30, 30)) {
                isDone = true;
            } else if (scene.getText(activity.getString(R.string.label_sound))
                    .isTouched(touchX, touchY, 30, 30)) {
                boolean isSoundOn = !DodgeroidsSettingsManager.getInstance().isSoundOn();
                DodgeroidsSettingsManager.getInstance().saveSound(isSoundOn);
                soundOptionChanged = true;
            }
        }
        try {
            Thread.sleep(30); // TODO: this exists only so that onTouch will be true in the update
            // methods, please check if there is a better way
        } catch (Exception ex) {
        }
        return true;
    }

    @Override
    public IGameScreen switchScreen(GL11 gl) { // TODO: space as param?
        return new GameLoopScreen(activity, gl, "space");
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
