package de.games.engine.datamanagers;

import android.view.KeyEvent;

public interface KeyEventListener {
	public void keyPressed(KeyEvent event);

	public void keyReleased(KeyEvent event);

	public void keyHeld(KeyEvent event);
}
