package de.games.dodgeroids;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import de.games.dodgeroids.datamanagers.DodgeroidsSaveGame;
import de.games.dodgeroids.datamanagers.DodgeroidsSettingsManager;
import de.games.dodgeroids.screens.GameLoopScreen;
import de.games.dodgeroids.screens.StartScreen;
import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.KeyInputManager;
import de.games.engine.datamanagers.SoundManager;
import de.games.engine.graphics.Mesh;
import de.games.engine.screens.IGameScreen;
import java.util.Locale;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class DodgeroidsActivity extends AbstractGameActivity {

    private GLSurfaceView glSurface;
    private IGameScreen screen;
    private int width, height; // width and height of the viewport
    private long lastFrameStart; // start time of last frame in nano seconds
    private float deltaTime; // delta time in seconds

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Dodgeroids", "onCreate");

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set GLSurfaceView and GLSurfaceView.Renderer
        glSurface = new GLSurfaceView(this);
        glSurface.setEGLConfigChooser(
                8, 8, 8, 8, 16,
                0); // http://stackoverflow.com/questions/14167319/android-opengl-demo-no-config-chosen
        glSurface.setRenderer(this);
        this.setContentView(glSurface);

        // initialize singletons
        SoundManager.init(this, R.array.sounds);
        DodgeroidsSettingsManager.init(this);
        DodgeroidsSaveGame.init(this);

        Log.d("Dodgeroids", "onCreate end");
    }

    @Override
    public void onBackPressed() {
        if (screen != null) {
            screen.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Dodgeroids", "onSaveInstanceState");
        if (screen instanceof GameLoopScreen) {
            ((GameLoopScreen) screen).getLogic().storeSaveGame();
        }
        Log.d("Dodgeroids", "onSaveInstanceState end");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Dodgeroids", "onPause");
        glSurface.onPause();
        if (screen != null) {
            screen.dispose();
        }
        Log.d("Dodgeroids", "onPause end");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Dodgeroids", "onResume");
        glSurface.onResume();
        SoundManager.getInstance().prepareMusic(R.raw.loop1);
        Log.d("Dodgeroids", "onResume end");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Dodgeroids", "onDestroy");
        SoundManager.getInstance().dispose();
        KeyInputManager.getInstance().dispose();
        // if( screen != null )
        // screen.dispose();
        Log.d("Dodgeroids", "onDestroy end");
    }

    /** Called when the GLSurfaceView has finished initialization */
    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
        Log.d("Dodgeroids", "onSurfaceCreated");
        lastFrameStart = System.nanoTime();
        String renderer = gl.glGetString(GL10.GL_RENDERER);
        if (renderer.toLowerCase(Locale.US).contains("pixelflinger")) {
            Mesh.globalVBO = false;
        }
        Log.d("Dodgeroids", "onSurfaceCreated end");
    }

    /** Called when the surface size changed, e.g. due to tilting */
    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        // TODO was ist wenn diese Methode mitten im spiel aufgerufen wird weil	sich
        // tats�chlich der surface ver�ndert hat und nicht weil es in standby ging und
        // neu geladen hat? Ist dann nicht das savegame futsch oder generell ist es ja
        // dann komisch das das game einfach in das startmen� geht.
        Log.d("Dodgeroids", "onSurfaceChanged");
        this.width = width;
        this.height = height;
        screen = new StartScreen(this, (GL11) gl);
        // screen.onSurfaceChanged(width, height); // TODO wird im Moment nicht
        // gebraucht weil zur Zeit sowieso neuer Screen erstellt wird
        Log.d("Dodgeroids", "onSurfaceChanged end");
    }

    /** Called each Frame */
    @Override
    public void onDrawFrame(final GL10 gl) {
        long currentFrameStart = System.nanoTime();
        deltaTime = (currentFrameStart - lastFrameStart) / 1000000000.0f;
        lastFrameStart = currentFrameStart;
        mainLoopIteration((GL11) gl);
    }

    public void mainLoopIteration(final GL11 gl) {
        // setup
        // if (firstSetup) {
        // firstSetup = false;
        // screen = new StartScreen(gl, this);
        // }
        screen.update(deltaTime);
        if (screen.isDone()) {
            screen.dispose();
            screen = screen.switchScreen(gl);
            // isBackPressed = false;
        }
        screen.getRenderer().render(gl, width, height);
    }

    /** @return the viewport width in pixels */
    @Override
    public int getViewportWidth() {
        return width;
    }

    /** @return the viewport height in pixels */
    @Override
    public int getViewportHeight() {
        return height;
    }

    @Override
    public GLSurfaceView getGLSurfaceView() {
        return glSurface;
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent event) {
        boolean status = super.dispatchKeyEvent(event);
        KeyInputManager.getInstance().registerKeyEvent(event, status);
        return status;
    }
}
