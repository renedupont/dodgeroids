package de.games.engine.screens;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import de.games.engine.AbstractGameActivity;
import de.games.engine.graphics.Background;
import de.games.engine.graphics.Camera;
import de.games.engine.graphics.Font;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.Sprite;
import de.games.engine.graphics.Texture;
import java.util.HashMap;
import javax.microedition.khronos.opengles.GL11;

public abstract class AbstractScreenFactory {

    protected Font font;

    public abstract Camera createCamera(int frustumWidth, int frustumHeight);

    public HashMap<String, Font.Text> createTexts(
            final AbstractGameActivity activity, final GL11 gl) {
        return new HashMap<String, Font.Text>();
    }

    public HashMap<String, Sprite> createSprites(
            final AbstractGameActivity activity, final GL11 gl) {
        return new HashMap<String, Sprite>();
    }

    protected abstract String getDefaultBackgroundTextureId();

    public Background createDefaultBackground(final Activity activity, final GL11 gl) {
        return createBackground(activity, gl, getDefaultBackgroundTextureId());
    }

    public Background createBackground(
            final Activity activity, final GL11 gl, final String textureId) {
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
                Bitmap bitmap = BitmapFactory.decodeStream(activity.getAssets().open(textureId));
                texture =
                        new Texture(
                                gl,
                                bitmap,
                                Texture.TextureFilter.MipMap,
                                Texture.TextureFilter.Nearest,
                                Texture.TextureWrap.ClampToEdge,
                                Texture.TextureWrap.ClampToEdge);
                bitmap.recycle();
            } catch (Exception ex) {
                // Log.d("Dodge It!", "couldn't load textures");
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
