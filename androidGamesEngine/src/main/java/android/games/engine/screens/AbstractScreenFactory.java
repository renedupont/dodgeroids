package android.games.engine.screens;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.games.engine.AbstractGameActivity;
import android.games.engine.graphics.Background;
import android.games.engine.graphics.Camera;
import android.games.engine.graphics.Font;
import android.games.engine.graphics.Font.Text;
import android.games.engine.graphics.Mesh;
import android.games.engine.graphics.Sprite;
import android.games.engine.graphics.Texture;
import android.games.engine.graphics.Texture.TextureFilter;
import android.games.engine.graphics.Texture.TextureWrap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class AbstractScreenFactory {

	protected Font font;

	public abstract Camera createCamera(int frustumWidth, int frustumHeight);

	public HashMap<String, Text> createTexts(
			final AbstractGameActivity activity, final GL11 gl) {
		return new HashMap<String, Text>();
	}

	public HashMap<String, Sprite> createSprites(
			final AbstractGameActivity activity, final GL11 gl) {
		return new HashMap<String, Sprite>();
	}

	protected abstract String getDefaultBackgroundTextureId();

	public Background createDefaultBackground(final Activity activity,
			final GL11 gl) {
		return createBackground(activity, gl, getDefaultBackgroundTextureId());
	}

	public Background createBackground(final Activity activity, final GL11 gl,
			final String textureId) {
		if (textureId != null) {
			Mesh mesh = new Mesh(gl, 4, false, true, false);
			mesh.texCoord(0, 0);
			mesh.vertex(-1, 1, 0);
			mesh.texCoord(1, 0);
			mesh.vertex(1, 1, 0);
			mesh.texCoord(1, 1);
			mesh.vertex(1, -1, 0);
			mesh.texCoord(0, 1);
			mesh.vertex(-1, -1, 0);

			Texture texture;
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets()
						.open(textureId));
				texture = new Texture(gl, bitmap, TextureFilter.MipMap,
						TextureFilter.Nearest, TextureWrap.ClampToEdge,
						TextureWrap.ClampToEdge);
				bitmap.recycle();
			} catch (Exception ex) {
				//Log.d("Dodge It!", "couldn't load textures");
				throw new RuntimeException(ex);
			}

			return new Background(mesh, texture);
		} else {
			return null;
		}
	}

	public void dispose() {
		if (font != null) {
			font.dispose();
		}
	}

}
