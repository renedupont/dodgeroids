package android.games.graphics;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL11;

import android.games.graphics.Mesh.RenderType;
import android.games.graphics.Texture.TextureFilter;
import android.games.graphics.Texture.TextureWrap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Sprite {
	private boolean firstIteration = true; // TODO: TEMP
	private final Mesh[] meshes;
	private final Texture texture;

	private Vector position = new Vector();

	private int birthTime = 0;
	private int animationLength;
	public boolean isPlaying;
	private boolean loopAnimation = false;

	private int segmentLength;

	public Sprite(final GL11 gl, final InputStream bitmapStream,
			final float width, final float height, final int gridSizeX,
			final int gridSizeY) {
		this(gl, BitmapFactory.decodeStream(bitmapStream), width, height,
				gridSizeX, gridSizeY);
	}

	public Sprite(final GL11 gl, final Bitmap source, final float width,
			final float height, final int gridSizeX, final int gridSizeY) {
		this(gl, new Texture(gl, source, TextureFilter.MipMap,
				TextureFilter.Linear, TextureWrap.ClampToEdge,
				TextureWrap.ClampToEdge), width, height, gridSizeX, gridSizeY);
	}

	public Sprite(final GL11 gl, final Texture texture, final float width,
			final float height, final int gridSizeX, final int gridSizeY) {
		this.texture = texture;

		float halfWidth = width / 2;
		float halfHeight = height / 2;

		meshes = new Mesh[gridSizeX * gridSizeY];

		int c = 0;
		for (int i = 0; i < gridSizeX; i++) {
			for (int s = 0; s < gridSizeY; s++) {
				float texX = ((float) 1 / gridSizeX);
				float texY = ((float) 1 / gridSizeY);
				Mesh mesh = new Mesh(gl, 4, false, true, false);
				mesh.texCoord(texX * s, texY * i);
				mesh.vertex(-halfWidth, halfHeight, 0);
				mesh.texCoord(texX * (s + 1), texY * i);
				mesh.vertex(halfWidth, halfHeight, 0);
				mesh.texCoord(texX * (s + 1), texY * (i + 1));
				mesh.vertex(halfWidth, -halfHeight, 0);
				mesh.texCoord(texX * s, texY * (i + 1));
				mesh.vertex(-halfWidth, -halfHeight, 0);
				meshes[c] = mesh;
				c++;
			}
		}
	}

	public Vector getPos() {
		return position;
	}

	private void draw(final int length) {
		isPlaying = true;
		birthTime = 0;
		animationLength = length;
		segmentLength = length / meshes.length;
	}

	public void drawSprite(final int length) {
		draw(length);
		loopAnimation = false;
	}

	public void loopSprite(final int length) {
		draw(length);
		loopAnimation = true;
	}

	public void stop() {
		isPlaying = false;
		loopAnimation = false;
	}

	public void render(final GL11 gl) {
		/********
		 * TEMP ***************************************** FIXME: GAAAAAAANZ
		 * DRECKIG HIER * TODO: es wird die gesamte sequenz einmal offscreen
		 * gerendert * XXX: damit es sp�ter nichtmehr flackert *
		 ****************************************************************/
		if (firstIteration) {
			Vector oldPos = position.clone();
			position.x = Float.POSITIVE_INFINITY;
			position.y = Float.POSITIVE_INFINITY;
			position.z = Float.POSITIVE_INFINITY;
			for (Mesh mesh : meshes) {
				texture.bind();
				mesh.render(RenderType.TRIANGLE_FAN);
			}
			position = oldPos.clone();
			firstIteration = false;
		}
		/******** TEMP ENDE ************/

		if (isPlaying) {
			texture.bind();
			meshes[birthTime / segmentLength].render(RenderType.TRIANGLE_FAN);
		}
	}

	public void setPosition(final Vector position) {
		this.position = position;
	}

	public Vector getPosition() {
		return position;
	}

	public void update(final float delta) {
		if (isPlaying) {
			birthTime += delta * 1000;
		}

		if (isPlaying && birthTime >= animationLength) {
			if (loopAnimation) {
				birthTime = 0;
			} else {
				isPlaying = false;
			}
		}
	}

	public void dispose() {
		for (Mesh mesh : meshes) {
			mesh.dispose();
		}
		texture.dispose();
	}
}
