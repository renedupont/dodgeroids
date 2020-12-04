package de.games.engine;

import android.app.Activity;
import android.opengl.GLSurfaceView;

public abstract class AbstractGameActivity extends Activity implements
		GLSurfaceView.Renderer {
	public abstract int getViewportWidth();

	public abstract int getViewportHeight();

	public abstract GLSurfaceView getGLSurfaceView();
}
