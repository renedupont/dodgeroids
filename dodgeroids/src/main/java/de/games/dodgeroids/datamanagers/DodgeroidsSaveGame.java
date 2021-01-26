package de.games.dodgeroids.datamanagers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import de.games.dodgeroids.R;
import de.games.dodgeroids.objects.Player;
import de.games.engine.graphics.Vector;
import java.io.Serializable;

public class DodgeroidsSaveGame implements Serializable {

    /**
     * This class is basically a singleton facade that saves game data to the android preferences
     * store and pull them for there. If at any time more data need to be saved, please consider
     * using the android internal database.
     */
    private static final long serialVersionUID = -2184423110712993233L;

    private static DodgeroidsSaveGame instance;
    private final Activity activity;

    private DodgeroidsSaveGame(final Activity activity) {
        this.activity = activity;
    }

    public static void init(final Activity activity) {
        instance = new DodgeroidsSaveGame(activity);
    }

    public static synchronized DodgeroidsSaveGame getInstance() {
        if (instance == null) {
            throw new RuntimeException("Use init() first!");
        }
        return instance;
    }

    public void storeSaveGame(final Player player) {
        SharedPreferences.Editor prefs = activity.getPreferences(Context.MODE_PRIVATE).edit();
        prefs.putFloat("xPlayer", player.getPos().x).commit();
        prefs.putFloat("yPlayer", player.getPos().y).commit();
        prefs.putFloat("zPlayer", player.getPos().z).commit();
        prefs.putInt("playerLives", player.lives).commit();
        prefs.putFloat("playerScore", player.score).commit();
    }

    public Vector getPlayerPosition() {
        return new Vector(
                activity.getPreferences(Context.MODE_PRIVATE).getFloat("xPlayer", 0.0f),
                activity.getPreferences(Context.MODE_PRIVATE).getFloat("yPlayer", 0.0f),
                activity.getPreferences(Context.MODE_PRIVATE).getFloat("zPlayer", 0.0f));
    }

    public int getPlayerLives() {
        return activity.getPreferences(Context.MODE_PRIVATE).getInt("playerLives", 0);
    }

    public float getScore() {
        return activity.getPreferences(Context.MODE_PRIVATE).getFloat("playerScore", 0.0f);
    }

    public boolean isResumable() {
        return activity.getPreferences(Context.MODE_PRIVATE).getBoolean("saveGameResumable", false);
    }

    public synchronized void setResumable(final boolean loadable) {
        SharedPreferences.Editor prefs = activity.getPreferences(Context.MODE_PRIVATE).edit();
        prefs.putBoolean("saveGameResumable", loadable).commit();
    }

    public boolean saveIfNewHighScore(final float score) {
        float highScore = getHighScore();
        if (score > highScore) {
            SharedPreferences.Editor gameScore =
                    activity.getPreferences(Context.MODE_PRIVATE).edit();
            gameScore.putFloat(activity.getString(R.string.pref_highscore), score).commit();
            return true;
        }
        return false;
    }

    public float getHighScore() {
        return activity.getPreferences(Context.MODE_PRIVATE)
                .getFloat(activity.getString(R.string.pref_highscore), 0.0f);
    }
}
