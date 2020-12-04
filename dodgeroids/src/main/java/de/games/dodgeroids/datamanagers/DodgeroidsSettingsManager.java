package de.games.dodgeroids.datamanagers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import de.games.engine.datamanagers.ISettingsManager;

public class DodgeroidsSettingsManager implements ISettingsManager {

	private final Activity activity;

	private boolean isSoundOn;
	private boolean isControlFlipped;

	private static DodgeroidsSettingsManager instance;

	private DodgeroidsSettingsManager(final Activity activity) {
		this.activity = activity;
	}

	public static void init(final Activity activity) {
		instance = new DodgeroidsSettingsManager(activity);
		instance.loadAllFromPreferences();
	}

	public static synchronized DodgeroidsSettingsManager getInstance() {
		if (instance == null) {
			throw new RuntimeException("Use init() first!");
		}
		return instance;
	}

	public synchronized void saveAllToPreferences() {
		SharedPreferences.Editor prefs = activity.getPreferences(
				Context.MODE_PRIVATE).edit();
		prefs.putBoolean("isSoundOn", isSoundOn).commit();
		prefs.putBoolean("isControlFlipped", isControlFlipped).commit();
	}

	public synchronized void saveSound(final boolean isSoundOn) {
		this.isSoundOn = isSoundOn;
		SharedPreferences.Editor prefs = activity.getPreferences(
				Context.MODE_PRIVATE).edit();
		prefs.putBoolean("isSoundOn", isSoundOn).commit();
	}

	public synchronized void saveControlFlipped(final boolean isFlipped) {
		this.isControlFlipped = isFlipped;
		SharedPreferences.Editor prefs = activity.getPreferences(
				Context.MODE_PRIVATE).edit();
		prefs.putBoolean("isControlFlipped", isControlFlipped).commit();
	}

	private void loadAllFromPreferences() {
		isSoundOn = activity.getPreferences(Context.MODE_PRIVATE).getBoolean(
				"isSoundOn", true);
		isControlFlipped = activity.getPreferences(Context.MODE_PRIVATE)
				.getBoolean("isControlFlipped", true);
	}

	public synchronized boolean isControlFlipped() {
		return isControlFlipped;
	}

	public synchronized boolean isSoundOn() {
		return isSoundOn;
	}

}
