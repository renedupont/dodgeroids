package de.games.keepitup.screens;

import android.games.engine.AbstractGameActivity;
import android.games.engine.datamanagers.Scene;
import android.games.engine.datamanagers.SoundManager;
import android.games.engine.graphics.Color;
import android.games.engine.graphics.Font.Text;
import android.games.engine.graphics.GameRenderer;
import android.games.engine.levels.AbstractLevelFactory;
import android.games.engine.logic.AbstractGameLogic;
import android.games.engine.objects.Player;
import android.games.engine.screens.IGameScreen;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import javax.microedition.khronos.opengles.GL11;

import de.games.keepitup.R;
import de.games.keepitup.datamanagers.KeepItUpSaveGame;
import de.games.keepitup.levels.SpaceLevelFactory;
import de.games.keepitup.logic.GameLoopLogic;

public final class GameLoopScreen implements IGameScreen {

    /* class elements */
    private final AbstractGameActivity activity;
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

    public GameLoopScreen(final AbstractGameActivity activity, final GL11 gl,
                          final String levelName) {
        this.activity = activity;
        this.levelFactory = new SpaceLevelFactory();
        this.scene = new Scene(activity, gl, new GameLoopFactory(),
                levelFactory);
        this.renderer = new GameRenderer(gl, activity, scene);
        renderer.setAmbientColor(new Color(0.5f, 0.5f, 0.5f, 1.0f));
        this.logic = new GameLoopLogic(activity, scene, levelFactory);

        SoundManager.getInstance().prepareMusic(R.raw.loop1);
        activity.getGLSurfaceView().setOnTouchListener(this);

        this.text_stats = scene.getText(activity
                .getString(R.string.label_stats));
        this.immortalCounter = scene.getText(activity
                .getString(R.string.label_immortalCounter));
        this.text_pause = scene.getText(activity
                .getString(R.string.label_pause));
        this.player = scene.getPlayer();

        // Load savegame
        if (KeepItUpSaveGame.getInstance().isResumable()) {
            player.lives = KeepItUpSaveGame.getInstance().getPlayerLives();
            player.score = KeepItUpSaveGame.getInstance().getScore();
            player.setPosition(KeepItUpSaveGame.getInstance()
                    .getPlayerPosition());
        } else {
            KeepItUpSaveGame.getInstance().setResumable(true);
        }

        isDoneLoading = true;
    }

    @Override
    public void update(final float deltaTime) {
        logic.update(deltaTime);

        text_stats.setText("lives: " + player.lives + "  score: "
                + (int) player.score);
        immortalCounter
                .setText((int) player.getRemainingImmortalityTime() + "");

        if (logic.isPaused()) {
            text_pause.setText(activity.getString(R.string.text_pausehint));
        } else if (!text_pause.getText().equals("")) {
            text_pause.setText("");
        }

        if (player.isImmortal()) {
            immortalCounter.setText((int) player.getRemainingImmortalityTime()
                    + "");
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
            KeepItUpSaveGame.getInstance().setResumable(false);
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
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });

        if (isBackPressed) {
            return new StartScreen(activity, gl);
        }
        KeepItUpSaveGame.getInstance().saveIfNewHighScore(score);
        /*return new GameLoopScreen(activity, gl, "space");*/
        return new GameOverScreen(activity, gl);
    }

    // @Override
    // public void onSurfaceChanged(int width, int height) { // TODO wird evtl.
    // nochmal gebraucht wenn in den Activity events noch gebastelt wird
    // scene.getCamera().setFrustum(width, height);
    // }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        // TODO evtl hier noch von pause modus die m�glichkeit bieten das
        // spiel zu beenden und zum punktestand zu gehen

        if (isDoneLoading && event.getAction() == MotionEvent.ACTION_DOWN) {
            ((GameLoopLogic) logic).setDownPressed(true); // TODO: setdownpressed in das interface der engine packen
            //			SoundManager.getInstance().pauseMusic();
        } else if (isDoneLoading && event.getAction() == MotionEvent.ACTION_UP) {
            ((GameLoopLogic) logic).setDownPressed(false);
            if (logic.isPaused()) {
                SoundManager.getInstance().startOrResumeMusic();
                activity.runOnUiThread(new Runnable() { // TODO: muss hier evtl. nit in runonuithread laufen? und vll. auch in ACTIVE->Pause case obendr�ber setzen?
                    @Override
                    public void run() {
                        activity.getWindow().addFlags(
                                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                });
                logic.setPaused(false);
            }

        }

        //		} else if (isDoneLoading && event.getAction() == MotionEvent.ACTION_UP) {
        //			// int touchX = (int)event.getX();
        //			// int touchY = (int)event.getY();
        //			isDownPressed = false;
        //			if (!logic.isPaused()) {
        //				activity.runOnUiThread(new Runnable() {
        //					@Override
        //					public void run() {
        //						activity.getWindow().clearFlags(
        //								WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //					}
        //				});
        //				SoundManager.getInstance().pauseMusic();
        //				GameSensorManager.getInstance().setCalibrated(false);
        //			} else {
        //				// if (touchX <= activity.getViewportWidth() / 2) {
        //				// KeepItUpSaveGame.getInstance().storeSaveGame(player);
        //				// isDone = true;
        //				// }
        //				// else {
        //				GameSensorManager.getInstance().calibrate();
        //				SoundManager.getInstance().startOrResumeMusic();
        //				activity.runOnUiThread(new Runnable() { // TODO: muss hier evtl.
        //														// nit in runonuithread
        //														// laufen? und vll. auch
        //														// in ACTIVE->Pause case
        //														// obendr�ber setzen?
        //					@Override
        //					public void run() {
        //						activity.getWindow().addFlags(
        //								WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //					}
        //				});
        //				// }
        //			}
        //			logic.setPaused(!logic.isPaused());
        //		}
        try {
            Thread.sleep(30); // TODO: WTF? nur damit onTouch auch bei den
            // update methoden mal als true ankommt? OMFG
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
