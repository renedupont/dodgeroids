package de.games.engine.datamanagers;

import android.view.KeyEvent;

public interface KeyEventListener {

    void keyPressed(KeyEvent event);

    void keyReleased(KeyEvent event);

    void keyHeld(KeyEvent event);
}
