package de.games.engine.screens;

import javax.microedition.khronos.opengles.GL11;

import de.games.engine.graphics.GameRenderer;
import android.view.View.OnTouchListener;

public interface IGameScreen extends OnTouchListener {
	public GameRenderer getRenderer();

	public void update(float deltaTime);

	// public void onSurfaceChanged(int width, int height);
	public boolean isDone();

	public void onBackPressed();

	public IGameScreen switchScreen(GL11 gl);

	public void dispose();
}
