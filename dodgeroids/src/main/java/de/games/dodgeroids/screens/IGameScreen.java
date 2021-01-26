package de.games.dodgeroids.screens;

import android.view.View.OnTouchListener;
import de.games.engine.graphics.GameRenderer;
import javax.microedition.khronos.opengles.GL11;

public interface IGameScreen extends OnTouchListener {

    GameRenderer getRenderer();

    void update(float deltaTime);

    boolean isDone();

    void onBackPressed();

    IGameScreen switchScreen(GL11 gl);

    void dispose();
}
