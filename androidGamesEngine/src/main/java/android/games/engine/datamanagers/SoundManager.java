package android.games.engine.datamanagers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class SoundManager {
	// Zum benutzen dieser klasse muss in arrays.xml ein array namens 'sounds'
	// erstellt werden
	// mit verweise auf soundresourcen

	private static SoundManager instance;
	private final AudioManager audioManager;
	private SoundPool soundPool;
	private final SparseIntArray soundPoolResources;
	private MediaPlayer mediaPlayer;

	private final Activity activity;

	private boolean soundOn = true;
	private int soundListId;

	private SoundManager(final Activity activity) {
		this.activity = activity;
		this.soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		this.audioManager = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		this.soundPoolResources = new SparseIntArray();
	}

	/*
	 * Requests the instance of the Sound Manager and creates it if it does not
	 * exist.Returns the single instance of the SoundManager
	 */
	public static synchronized SoundManager getInstance() {
		if (instance == null) {
			throw new RuntimeException("Use init() first!");
		}
		return instance;
	}

	public static void init(final Activity activity, final int soundListId) {
		instance = new SoundManager(activity);
		instance.soundListId = soundListId;
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		instance.loadSounds(activity);
	}

	public void addSound(final int soundResource) {
		soundPoolResources.put(soundResource,
				soundPool.load(activity, soundResource, 1));
	}

	private void loadSounds(final Activity activity) {
		// TODO: evtl. hier noch exception abfangen falls resource nicht geladen
		// werden konnte...

		Resources resources = activity.getResources();
		TypedArray soundResources = resources.obtainTypedArray(soundListId);// R.array.sounds);
		int length = soundResources.length();
		int soundID;
		for (int i = 0; i < length; i++) {
			soundID = soundResources.getResourceId(i, -1);
			soundPoolResources.put(soundID,
					soundPool.load(activity, soundID, 1));
		}
		soundResources.recycle();
	}

	public void playSound(final int soundID, final float speed) {
		if (soundOn) {
			float actualVolume = audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = actualVolume / maxVolume;
			soundPool.play(soundPoolResources.get(soundID), volume, volume, 1,
					0, speed);
		}
	}

	public void stopSound(final int streamID) {
		soundPool.stop(soundPoolResources.get(streamID));
	}

	public void prepareMusic(final int musicID) {
		if (soundOn) {
			mediaPlayer = MediaPlayer.create(activity, musicID);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setLooping(true);
		} else {
			mediaPlayer = null;
		}
	}

	public void pauseMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}

	public void startOrResumeMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
		}
	}

	private void stopMusic() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();

			// mediaPlayer = null;
		}
	}

	// public void releaseMusic() {
	// if (mediaPlayer != null)
	// mediaPlayer.release();
	// }

	public void resetMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		}
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(final boolean soundOn) {
		this.soundOn = soundOn;
	}

	public void dispose() {
		soundPool.release();
		soundPool = null;
		soundPoolResources.clear();
		audioManager.unloadSoundEffects();
		// if (mediaPlayer != null) {
		// mediaPlayer.stop();
		// mediaPlayer.release();
		// mediaPlayer = null;
		// }
		stopMusic();
		mediaPlayer = null;
		instance = null;
	}

}
