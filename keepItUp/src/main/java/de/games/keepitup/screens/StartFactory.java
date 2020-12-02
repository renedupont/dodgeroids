package de.games.keepitup.screens;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import android.games.engine.AbstractGameActivity;
import android.games.engine.graphics.Camera;
import android.games.engine.graphics.Color;
import android.games.engine.graphics.Font;
import android.games.engine.graphics.Font.FontStyle;
import android.games.engine.graphics.Font.HorizontalAlign;
import android.games.engine.graphics.Font.SizeType;
import android.games.engine.graphics.Font.Text;
import android.games.engine.graphics.Font.VerticalAlign;
import android.games.engine.graphics.Vector;
import android.games.engine.screens.AbstractScreenFactory;
import de.games.keepitup.R;
import de.games.keepitup.datamanagers.KeepItUpSaveGame;
import de.games.keepitup.datamanagers.KeepItUpSettingsManager;

public class StartFactory extends AbstractScreenFactory {

	@Override
	public Camera createCamera(final int frustumWidth, final int frustumHeight) {
		return new Camera(new Vector(13f, 0f, -15f), new Vector(0f, 0f, -15f),
				new Vector(0f, 1f, 0f), frustumWidth, frustumHeight, 90.0f,
				2.0f, 84.0f);
	}

	@Override
	protected String getDefaultBackgroundTextureId() {
		return "menu_blank.jpg";
	}

	@Override
	public HashMap<String, Text> createTexts(
			final AbstractGameActivity activity, final GL11 gl) {
		HashMap<String, Text> texts = new HashMap<String, Text>();
		// altes Rot: 0xffff0000, wei�: 0xffffffff
		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Large, activity.getViewportWidth(), FontStyle.Plain,
				Color.toIntBitsARGB(214, 58, 46, 255));
		Text game_title = font.newText(gl);
		game_title.setHorizontalAlign(HorizontalAlign.Center);
		game_title.setVerticalAlign(VerticalAlign.Center);
		game_title.setText(activity.getString(R.string.text_gametitle));
		game_title.setPosition(activity.getViewportWidth() / 2f,
				activity.getViewportHeight() / (4.f / 3.f));
		texts.put(activity.getString(R.string.text_gametitle), game_title);

		// start
		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Large, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text start = font.newText(gl);
		start.setHorizontalAlign(HorizontalAlign.Center);
		start.setVerticalAlign(VerticalAlign.Center);
		start.setText(activity.getString(R.string.text_start));
		texts.put(activity.getString(R.string.text_start), start);
		// resume
		if (KeepItUpSaveGame.getInstance().isResumable()) {

			start.setPosition(activity.getViewportWidth() / (8.f / 2),
					activity.getViewportHeight() / 2.f);

			font = new Font(gl, activity.getAssets(), "gunplay.ttf",
					SizeType.Large, activity.getViewportWidth(),
					FontStyle.Plain, 0xffffffff);
			Text resume = font.newText(gl);
			resume.setHorizontalAlign(HorizontalAlign.Center);
			resume.setVerticalAlign(VerticalAlign.Center);
			resume.setText(activity.getString(R.string.text_resume));
			resume.setPosition(activity.getViewportWidth() / (8.f / 6),
					activity.getViewportHeight() / 2.f);
			texts.put(activity.getString(R.string.text_resume), resume);
		} else {
			start.setPosition(activity.getViewportWidth() / 2.f,
					activity.getViewportHeight() / 2.f);
		}
		// sound label
		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Medium, activity.getViewportWidth(), FontStyle.Plain,
				Color.toIntBitsARGB(214, 58, 46, 255));
		Text sound = font.newText(gl);
		sound.setHorizontalAlign(HorizontalAlign.Center);
		sound.setVerticalAlign(VerticalAlign.Center);
		sound.setText(activity.getString(R.string.text_sound));
        sound.setPosition(activity.getViewportWidth() / (6.f / 3),
                activity.getViewportHeight() / 4.0f);/*
		sound.setPosition(activity.getViewportWidth() / (6.f / 5),
				activity.getViewportHeight() / 4.0f);*/
		texts.put(activity.getString(R.string.text_sound), sound);
		// sound setting value
		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Medium, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text soundValue = font.newText(gl);
		soundValue.setHorizontalAlign(HorizontalAlign.Center);
		soundValue.setVerticalAlign(VerticalAlign.Center);
		soundValue
				.setText(KeepItUpSettingsManager.getInstance().isSoundOn() ? activity
						.getString(R.string.text_on) : activity
						.getString(R.string.text_off));
        soundValue.setPosition(activity.getViewportWidth() / (6.f / 3),
                activity.getViewportHeight() / 6.0f);
        /*soundValue.setPosition(activity.getViewportWidth() / (6.f / 5),
				activity.getViewportHeight() / 6.0f);*/
		texts.put(activity.getString(R.string.label_sound), soundValue);

		return texts;
	}

}
