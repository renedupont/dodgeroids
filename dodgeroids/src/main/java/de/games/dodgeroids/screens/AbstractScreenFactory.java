package de.games.dodgeroids.screens;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public HashMap<String, Font.Text> createTexts(Activity activity, GL11 gl) {
        return new HashMap<>();
    }

    public HashMap<String, Sprite> createSprites(Activity activity, GL11 gl) {
        return new HashMap<>();
    }

    protected abstract String getDefaultBackgroundTextureId();

    public Background createDefaultBackground(AssetManager assetManager, GL11 gl) {
        return createBackground(assetManager, gl, getDefaultBackgroundTextureId());
    }

    public Background createBackground(AssetManager assetManager, GL11 gl, String textureId) {
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
                Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(textureId));
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
                // TODO proper exception handling, logging...
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
