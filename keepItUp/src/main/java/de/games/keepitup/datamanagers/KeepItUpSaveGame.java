package de.games.keepitup.datamanagers;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.engine.graphics.Vector;
import android.engine.objects.Player;
import de.games.keepitup.R;

// Hier wird irgendwann mal alle Variablen reinkommen die f�r den momentanten Spielstand relevant sind
public class KeepItUpSaveGame implements Serializable {

	/**
	 * der shit wird momentan noch in den preferences gespeichert. vll. wenn man
	 * mal mehr daten speichern will den ganzen shit in die DB speichern... die
	 * klasse ist eigentlich im mom nur ne singleton fassade um values aus den
	 * prefs zu l33chen
	 */
	private static final long serialVersionUID = -2184423110712993233L;

	private static KeepItUpSaveGame instance;
	private final Activity activity;

	/*
	 * score, lives, steine position, player position, restzeit, maybe tunnel,
	 * welches level, GameStatus, GameMode
	 */

	private KeepItUpSaveGame(final Activity activity/* Player player/*,
													* AbstractGameObjectChain
													* obstacles, Game.Mode mode
													*/) {
		this.activity = activity;
	}

	public static void init(final Activity activity) {
		instance = new KeepItUpSaveGame(activity);
	}

	public synchronized static KeepItUpSaveGame getInstance() {
		if (instance == null) {
			throw new RuntimeException("Use init() first!");
		}
		return instance;
	}

	public void storeSaveGame(final Player player) {
		SharedPreferences.Editor prefs = activity.getPreferences(
				Context.MODE_PRIVATE).edit();
		prefs.putFloat("xPlayer", player.getPos().x).commit();
		prefs.putFloat("yPlayer", player.getPos().y).commit();
		prefs.putFloat("zPlayer", player.getPos().z).commit();
		prefs.putInt("playerLives", player.lives).commit();
		prefs.putFloat("playerScore", player.score).commit();
	}

	public Vector getPlayerPosition() {
		return new Vector(activity.getPreferences(Context.MODE_PRIVATE)
				.getFloat("xPlayer", 0.0f), activity.getPreferences(
				Context.MODE_PRIVATE).getFloat("yPlayer", 0.0f), activity
				.getPreferences(Context.MODE_PRIVATE).getFloat("zPlayer", 0.0f));
	}

	public int getPlayerLives() {
		return activity.getPreferences(Context.MODE_PRIVATE).getInt(
				"playerLives", 0);
	}

	public float getScore() {
		return activity.getPreferences(Context.MODE_PRIVATE).getFloat(
				"playerScore", 0.0f);
	}

	public boolean isResumable() {
		return activity.getPreferences(Context.MODE_PRIVATE).getBoolean(
				"saveGameResumable", false);
	}

	public synchronized void setResumable(final boolean loadable) {
		SharedPreferences.Editor prefs = activity.getPreferences(
				Context.MODE_PRIVATE).edit();
		prefs.putBoolean("saveGameResumable", loadable).commit();
	}

	public boolean saveIfNewHighScore(final float score) {
		float highScore = getHighScore();
		if (score > highScore) {
			SharedPreferences.Editor gameScore = activity.getPreferences(
					Context.MODE_PRIVATE).edit();
			gameScore.putFloat(activity.getString(R.string.pref_highscore),
					score).commit();
			return true;
		}
		return false;
	}

	public float getHighScore() {
		return activity.getPreferences(Context.MODE_PRIVATE).getFloat(
				activity.getString(R.string.pref_highscore), 0.0f);
	}

}
