package android.games.datamanagers;

import android.view.KeyEvent;

public interface KeyEventListener {
	public void keyPressed(KeyEvent event);

	public void keyReleased(KeyEvent event);

	public void keyHeld(KeyEvent event);
}
