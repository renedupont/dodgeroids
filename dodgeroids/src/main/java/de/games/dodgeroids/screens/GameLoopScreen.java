package de.games.dodgeroids.screens;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import de.games.dodgeroids.DodgeroidsActivity;
import de.games.dodgeroids.R;
import de.games.dodgeroids.datamanagers.DodgeroidsSaveGame;
import de.games.dodgeroids.levels.SpaceLevelFactory;
import de.games.dodgeroids.logic.GameLoopLogic;
import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.Scene;
import de.games.engine.datamanagers.SoundManager;
import de.games.engine.graphics.Color;
import de.games.engine.graphics.Font.Text;
import de.games.engine.graphics.GameRenderer;
import de.games.engine.levels.AbstractLevelFactory;
import de.games.engine.logic.AbstractGameLogic;
import de.games.engine.objects.Player;
import de.games.engine.screens.IGameScreen;

import javax.microedition.khronos.opengles.GL11;

public final class GameLoopScreen implements IGameScreen {

    /* class elements */
    private final DodgeroidsActivity activity;
    private final AbstractLevelFactory levelFactory;
    private final Scene scene;
    private final GameRenderer renderer;
    private final AbstractGameLogic logic;

    /* quick references */
    private final Player player;
    private final Text text_stats;
    private final Text immortalCounter;
    private final Text text_pause;

    /* control flags */
    private boolean isBackPressed = false;
    private boolean isDoneLoading = false;

    public GameLoopScreen(
            final DodgeroidsActivity activity, final GL11 gl, final String levelName) {
        this.activity = activity;
        this.levelFactory = new SpaceLevelFactory();
        this.scene = new Scene(activity,gl, new GameLoopFactory(), levelFactory, activity.getResources(), activity.getCacheDir(), activity.getAssets(), activity.getViewportWidth(), activity.getViewportHeight());
        this.renderer = new GameRenderer(gl,  scene);
        renderer.setAmbientColor(new Color(0.5f, 0.5f, 0.5f, 1.0f));
        this.logic = new GameLoopLogic(scene, levelFactory, activity.getString(R.string.label_explosion));

        SoundManager.getInstance().prepareMusic(R.raw.loop1);
        activity.getGLSurfaceView().setOnTouchListener(this);

        this.text_stats = scene.getText(activity.getString(R.string.label_stats));
        this.immortalCounter = scene.getText(activity.getString(R.string.label_immortalCounter));
        this.text_pause = scene.getText(activity.getString(R.string.label_pause));
        this.player = scene.getPlayer();

        // load savegame
        if (DodgeroidsSaveGame.getInstance().isResumable()) {
            player.lives = DodgeroidsSaveGame.getInstance().getPlayerLives();
            player.score = DodgeroidsSaveGame.getInstance().getScore();
            player.setPosition(DodgeroidsSaveGame.getInstance().getPlayerPosition());
        } else {
            DodgeroidsSaveGame.getInstance().setResumable(true);
        }

        isDoneLoading = true;
    }

    @Override
    public void update(final float deltaTime) {
        logic.update(deltaTime);

        text_stats.setText("lives: " + player.lives + "  score: " + (int) player.score);
        immortalCounter.setText((int) player.getRemainingImmortalityTime() + "");

        if (logic.isPaused()) {
            text_pause.setText(activity.getString(R.string.text_pausehint));
        } else if (!text_pause.getText().equals("")) {
            text_pause.setText("");
        }

        if (player.isImmortal()) {
            immortalCounter.setText((int) player.getRemainingImmortalityTime() + "");
        } else if (!immortalCounter.getText().equals("")) {
            immortalCounter.setText("");
        }
    }

    @Override
    public GameRenderer getRenderer() {
        return renderer;
    }

    @Override
    public boolean isDone() {
        if (logic.isGameDone()) {
            DodgeroidsSaveGame.getInstance().setResumable(false);
            logic.storeSaveGame();
            return true;
        } else if (isBackPressed) {
            logic.storeSaveGame();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        this.isBackPressed = true;
    }

    @Override
    public IGameScreen switchScreen(final GL11 gl) {
        float score = player.score;
        activity.runOnUiThread(
                () ->
                        activity.getWindow()
                                .clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));

        if (isBackPressed) {
            return new StartScreen(activity, gl);
        }
        DodgeroidsSaveGame.getInstance().saveIfNewHighScore(score);
        return new GameOverScreen(activity, gl);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        // TODO consider the functionality to switch from pause- into game over mode to see final
        // score, basically ending the game without the need to loose all lives

        if (isDoneLoading && event.getAction() == MotionEvent.ACTION_DOWN) {
            ((GameLoopLogic) logic)
                    .setDownPressed(true); // TODO: move setDownPressed into the engines interface?
            //			SoundManager.getInstance().pauseMusic();
        } else if (isDoneLoading && event.getAction() == MotionEvent.ACTION_UP) {
            ((GameLoopLogic) logic).setDownPressed(false);
            if (logic.isPaused()) {
                SoundManager.getInstance().startOrResumeMusic();
                activity.runOnUiThread(
                        () ->
                                activity.getWindow()
                                        .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));
                logic.setPaused(false);
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
    public void dispose() {
        scene.dispose();
        renderer.dispose();
        logic.dispose();
        SoundManager.getInstance().resetMusic();
    }

    public AbstractGameLogic getLogic() {
        return logic;
    }
}
