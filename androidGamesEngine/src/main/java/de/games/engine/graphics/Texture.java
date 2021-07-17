package de.games.engine.graphics;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Texture {
    /** Texture filter enum featuring the 3 most used filters */
    public enum TextureFilter {
        Nearest,
        Linear,
        MipMap
    }

    /** Texture wrap enum */
    public enum TextureWrap {
        ClampToEdge,
        Wrap
    }

    /** the texture handle * */
    private int textureHandle;
    /** handle to gl wrapper * */
    private final GL11 gl;

    /** Creates a new texture based on the given image */
    public Texture(
            final GL11 gl,
            final Bitmap image,
            final TextureFilter minFilter,
            final TextureFilter maxFilter,
            final TextureWrap sWrap,
            final TextureWrap tWrap) {
        this.gl = gl;

        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        textureHandle = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle);
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, getTextureFilter(minFilter));
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, getTextureFilter(maxFilter));
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, getTextureWrap(sWrap));
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, getTextureWrap(tWrap));
        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        buildMipmap(image);
        image.recycle();
    }

    public Texture(
            final GL11 gl,
            final int width,
            final int height,
            final TextureFilter minFilter,
            final TextureFilter maxFilter,
            final TextureWrap sWrap,
            final TextureWrap tWrap) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap image = Bitmap.createBitmap(width, height, config);

        this.gl = gl;

        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        textureHandle = textures[0];

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle);
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, getTextureFilter(minFilter));
        gl.glTexParameterf(
                GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, getTextureFilter(maxFilter));
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, getTextureWrap(sWrap));
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, getTextureWrap(tWrap));
        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        buildMipmap(image);
        image.recycle();
    }

    private int getTextureFilter(final TextureFilter filter) {
        if (filter == TextureFilter.Linear) {
            return GL10.GL_LINEAR;
        } else if (filter == TextureFilter.Nearest) {
            return GL10.GL_NEAREST;
        } else {
            return GL10.GL_LINEAR_MIPMAP_NEAREST;
        }
    }

    private int getTextureWrap(final TextureWrap wrap) {
        if (wrap == TextureWrap.ClampToEdge) {
            return GL10.GL_CLAMP_TO_EDGE;
        } else {
            return GL10.GL_REPEAT;
        }
    }

    private void buildMipmap(Bitmap bitmap) {
        int level = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        while (height >= 1 || width >= 1 && level < 4) {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
            if (height == 1 || width == 1) {
                break;
            }

            level++;
            if (height > 1) {
                height /= 2;
            }
            if (width > 1) {
                width /= 2;
            }

            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
            bitmap.recycle();
            bitmap = bitmap2;
        }
    }

    /** Draws the given image to the texture */
    public void draw(final Object bmp, final int x, final int y) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle);
        Bitmap bitmap = (Bitmap) bmp;
        int level = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        while (height >= 1 || width >= 1 && level < 4) {
            GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, level, x, y, bitmap);
            if (height == 1 || width == 1) {
                break;
            }

            level++;
            if (height > 1) {
                height /= 2;
            }
            if (width > 1) {
                width /= 2;
            }

            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
            bitmap.recycle();
            bitmap = bitmap2;
        }
    }

    /** Binds the texture */
    public void bind() {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureHandle);
    }

    /** Disposes the texture and frees the associated resourcess */
    public void dispose() {
        int[] textures = {textureHandle};
        gl.glDeleteTextures(1, textures, 0);
        textureHandle = 0;
    }
}
