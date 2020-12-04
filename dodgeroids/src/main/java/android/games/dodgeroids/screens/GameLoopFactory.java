package android.games.dodgeroids.screens;

import java.io.IOException;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import android.games.engine.AbstractGameActivity;
import android.games.engine.graphics.Camera;
import android.games.engine.graphics.Font;
import android.games.engine.graphics.Font.FontStyle;
import android.games.engine.graphics.Font.HorizontalAlign;
import android.games.engine.graphics.Font.SizeType;
import android.games.engine.graphics.Font.Text;
import android.games.engine.graphics.Font.VerticalAlign;
import android.games.engine.graphics.Sprite;
import android.games.engine.graphics.Vector;
import android.games.engine.screens.AbstractScreenFactory;
import android.games.dodgeroids.R;
import android.games.dodgeroids.datamanagers.DodgeroidsSaveGame;

public class GameLoopFactory extends AbstractScreenFactory {

	@Override
	public Camera createCamera(final int frustumWidth, final int frustumHeight) {
		return new Camera(new Vector(13.0f, 0.0f, -15.0f), new Vector(0.0f,
				0.0f, -15.0f), new Vector(0.0f, 1.0f, 0.0f), frustumWidth,
				frustumHeight, 80.0f, 2.0f, 84.0f);
	}

	@Override
	protected String getDefaultBackgroundTextureId() {
        if (Math.random() > 0.5d) {
            return "background.jpg";
        } else {
            return "nebulae_4.jpg";
        }
	}

	@Override
	public HashMap<String, Text> createTexts(
			final AbstractGameActivity activity, final GL11 gl) {
		HashMap<String, Text> texts = new HashMap<String, Text>();

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Small, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text text_stats = font.newText(gl);
		text_stats.setPosition(0, activity.getViewportHeight());
		texts.put(activity.getString(R.string.label_stats), text_stats);

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Small, activity.getViewportWidth(), FontStyle.Plain,
                0xff808080);
		Text text_highscore = font.newText(gl);
		text_highscore.setPosition(0, SizeType.Small.getIntVal() / 2);
		text_highscore.setText(activity.getString(R.string.text_highscore)+ " " +
				+ (int) DodgeroidsSaveGame.getInstance().getHighScore());
		texts.put(activity.getString(R.string.text_highscore), text_highscore);

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Medium, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text immortalCounter = font.newText(gl);
		immortalCounter.setHorizontalAlign(HorizontalAlign.Center);
		immortalCounter.setVerticalAlign(VerticalAlign.Center);
		immortalCounter.setPosition(activity.getViewportWidth() / 2,
				activity.getViewportHeight() / 2.f);
		texts.put(activity.getString(R.string.label_immortalCounter),
				immortalCounter);

		font = new Font(gl, activity.getAssets(), "gunplay.ttf",
				SizeType.Medium, activity.getViewportWidth(), FontStyle.Plain,
				0xffffffff);
		Text text_pause = font.newText(gl);
		text_pause.setHorizontalAlign(HorizontalAlign.Center);
		text_pause.setVerticalAlign(VerticalAlign.Center);
		text_pause.setPosition(activity.getViewportWidth() / 2,
				activity.getViewportHeight() / 2.f);
		texts.put(activity.getString(R.string.label_pause), text_pause);

		return texts;
	}

	@Override
	public HashMap<String, Sprite> createSprites(
			final AbstractGameActivity activity, final GL11 gl) {
		HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

		Sprite explosion;
		try {
			explosion = new Sprite(gl, activity.getAssets().open(
					"explosion.png"), 2.4f, 2.4f, 5, 5);
			sprites.put(activity.getString(R.string.label_explosion), explosion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sprites;
	}

}
