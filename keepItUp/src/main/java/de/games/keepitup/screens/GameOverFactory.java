package de.games.keepitup.screens;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import android.engine.AbstractGameActivity;
import android.engine.graphics.Camera;
import android.engine.graphics.Color;
import android.engine.graphics.Font;
import android.engine.graphics.Font.FontStyle;
import android.engine.graphics.Font.HorizontalAlign;
import android.engine.graphics.Font.SizeType;
import android.engine.graphics.Font.Text;
import android.engine.graphics.Font.VerticalAlign;
import android.engine.graphics.Vector;
import android.engine.screens.AbstractScreenFactory;
import de.games.keepitup.R;
import de.games.keepitup.datamanagers.KeepItUpSaveGame;

public class GameOverFactory extends AbstractScreenFactory {

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

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Large, activity.getViewportWidth(), FontStyle.Plain,
				Color.toIntBitsARGB(214, 58, 46, 255));
		Text text_gameOver = font.newText(gl);
		text_gameOver.setHorizontalAlign(HorizontalAlign.Center);
		text_gameOver.setVerticalAlign(VerticalAlign.Center);
		text_gameOver.setText(activity.getString(R.string.text_gameover));
		text_gameOver.setPosition(activity.getViewportWidth() / 2,
				activity.getViewportHeight() * 0.75f);
		texts.put(activity.getString(R.string.label_gameover), text_gameOver);

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Medium, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text text_score = font.newText(gl);
		text_score.setHorizontalAlign(HorizontalAlign.Center);
		text_score.setVerticalAlign(VerticalAlign.Center);
		text_score.setText(activity.getString(R.string.text_endscore) + "  "
				+ (int) KeepItUpSaveGame.getInstance().getScore());
		text_score.setPosition(activity.getViewportWidth() / 2,
				activity.getViewportHeight() * 0.4f);
		texts.put(activity.getString(R.string.label_endscore), text_score);

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Medium, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text text_highscore = font.newText(gl);
		text_highscore.setHorizontalAlign(HorizontalAlign.Center);
		text_highscore.setVerticalAlign(VerticalAlign.Center);
		text_highscore.setText(activity.getString(R.string.text_highscore)
				+ "  " + (int) KeepItUpSaveGame.getInstance().getHighScore());
		text_highscore.setPosition(activity.getViewportWidth() / 2,
				activity.getViewportHeight() * 0.25f);
		texts.put(activity.getString(R.string.label_highscore), text_highscore);

		return texts;
	}

}
