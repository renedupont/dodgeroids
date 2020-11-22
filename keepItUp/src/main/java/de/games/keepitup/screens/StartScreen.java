package de.games.keepitup.screens;

import javax.microedition.khronos.opengles.GL11;

import android.games.AbstractGameActivity;
import android.games.datamanagers.Scene;
import android.games.datamanagers.SoundManager;
import android.games.graphics.GameRenderer;
import android.games.screens.IGameScreen;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import de.games.keepitup.R;
import de.games.keepitup.datamanagers.KeepItUpSaveGame;
import de.games.keepitup.datamanagers.KeepItUpSettingsManager;

public final class StartScreen implements IGameScreen {
	/** class elements **/
	private final AbstractGameActivity activity;
	private final Scene scene;
	private final GameRenderer renderer;

	/** control flags **/
	private boolean isDone = false;
	private boolean soundOptionChanged = false;

	public StartScreen(final AbstractGameActivity activity, final GL11 gl) {
		this.activity = activity;
		this.scene = new Scene(activity, gl, new StartFactory());
		this.renderer = new GameRenderer(gl, activity, scene);
		activity.getGLSurfaceView().setOnTouchListener(this);
	}

	@Override
	public boolean isDone() {
		if (isDone) {
			SoundManager.getInstance().setSoundOn(
					KeepItUpSettingsManager.getInstance().isSoundOn());
		}
		return isDone;
	}

	@Override
	public void onBackPressed() {
		this.activity.finish();
	}

	@Override
	public void update(final float deltaTime) {
		if (soundOptionChanged) {
			scene.getText(activity.getString(R.string.label_sound)).setText(
					KeepItUpSettingsManager.getInstance().isSoundOn() ? activity
							.getString(R.string.text_on) : activity
							.getString(R.string.text_off));
			soundOptionChanged = false;
		}
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int touchX = (int) event.getX();
			int touchY = (int) event.getY();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					activity.getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				}
			});
			touchY = activity.getViewportHeight() - touchY; // LOL

			if (scene.getText(activity.getString(R.string.text_start))
					.isTouched(touchX, touchY, 30, 30)) {
				KeepItUpSaveGame.getInstance().setResumable(false);
				isDone = true;
			} else if (KeepItUpSaveGame.getInstance().isResumable()
					&& scene.getText(activity.getString(R.string.text_resume))
							.isTouched(touchX, touchY, 30, 30)) {
				isDone = true;
			} else if (scene.getText(activity.getString(R.string.label_sound))
					.isTouched(touchX, touchY, 30, 30)) {
				boolean isSoundOn = !KeepItUpSettingsManager.getInstance()
						.isSoundOn();
				KeepItUpSettingsManager.getInstance().saveSound(isSoundOn);
				soundOptionChanged = true;
			}
		}
		try {
			Thread.sleep(30); // TODO: WTF? nur damit onTouch auch bei den
								// update methoden mal als true ankommt? OMFG
		} catch (Exception ex) {
		}
		return true;
	}

	@Override
	public IGameScreen switchScreen(final GL11 gl) { // TODO space �bergabe?
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
