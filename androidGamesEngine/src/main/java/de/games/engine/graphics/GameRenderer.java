package de.games.engine.graphics;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import de.games.engine.AbstractGameActivity;
import de.games.engine.datamanagers.Scene;
import de.games.engine.objects.AbstractGameObject;

public class GameRenderer {

	public enum DisplayMode {
		DEFAULT, WIREFRAME // coordinate system
	}

	public static boolean RENDER_BOUNDS = false;

	protected AbstractGameActivity activity;
	protected GL11 gl;
	protected Scene scene;
	protected Camera camera;

	public GameRenderer(final GL11 gl, final AbstractGameActivity activity,
			final Scene scene) {
		this.activity = activity;
		this.gl = gl;
		this.scene = scene;
		this.camera = scene.getCamera();
		gl.glShadeModel(GL10.GL_SMOOTH);
	}

	public void setAmbientColor(final Color color) {
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, color.getRGBA(), 0);
	}

	protected void drawObject(final GL11 gl, final AbstractGameObject o,
			final DisplayMode mode) {
		Mesh.RenderType renderType;
		if (o.isHidden()) {
			return;
		}
		renderType = o.renderType;
		if (o.texture != null) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			o.texture.bind();
		} else {
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}

		gl.glPushMatrix();
		gl.glTranslatef(o.getPos().x, o.getPos().y, o.getPos().z);
		gl.glRotatef(o.getRot().x, 1, 0, 0);
		gl.glRotatef(o.getRot().y, 0, 1, 0);
		gl.glRotatef(o.getRot().z, 0, 0, 1);
		gl.glScalef(o.getScale().x, o.getScale().y, o.getScale().z);
		o.render(gl, renderType);
		gl.glPopMatrix();
	}

	protected void renderBounds(final GL11 gl) {
		for (AbstractGameObject o : scene.getGameObjects()) {
			// if (o instanceof TunnelPart)
			// continue;
			for (AbstractBound b : o.getBounds()) {
				gl.glDisable(GL10.GL_LIGHTING);
				gl.glDisable(GL10.GL_CULL_FACE);
				gl.glDisable(GL10.GL_TEXTURE_2D);
				gl.glPushMatrix();
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				gl.glColor4f(1, 0, 1, 0.8f);
				gl.glEnable(GL10.GL_BLEND);
				Vector pos = Vector.add(o.getPos(), b.getOffset());
				gl.glTranslatef(pos.x, pos.y, pos.z);
				Mesh m = b.getMesh();
				m.render(Mesh.RenderType.TRIANGLES);
				// m.render(RenderType.LINES);
				gl.glColor4f(1, 1, 1, 1f);
				gl.glDisable(GL10.GL_BLEND);
				gl.glPopMatrix();
				gl.glEnable(GL10.GL_TEXTURE_2D);
				gl.glEnable(GL10.GL_CULL_FACE);
				gl.glEnable(GL10.GL_LIGHTING);
			}
		}
	}

	public void render(final GL11 gl, final int width, final int height) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, width, height);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		if (scene.getBackground() != null) {
			scene.getBackground().render();
		}
		renderBackgroundPass(gl);

		camera.setMode(gl, Camera.Projection.PERSPECTIVE);
		camera.render(gl);

		gl.glDisable(GL10.GL_DITHER);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		if (scene.getLights().size() > 0) {
			gl.glEnable(GL10.GL_LIGHTING);
		}

		for (Light light : scene.getLights()) {
			light.render(gl);
		}

		gl.glEnable(GL10.GL_CULL_FACE);

		renderMainPass(gl);

		for (AbstractGameObject o : scene.getGameObjects()) {
			drawObject(gl, o, DisplayMode.DEFAULT);// engine.displayMode);
		}
		if (RENDER_BOUNDS) {
			renderBounds(gl);
		}

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_CULL_FACE);

		for (Sprite sprite : scene.getSprites()) {
			gl.glPushMatrix();
			gl.glTranslatef(sprite.getPos().x, sprite.getPos().y,
					sprite.getPos().z);
			sprite.render(gl);
			gl.glPopMatrix();

		}

		camera.setMode(gl, Camera.Projection.ORTHOGRAPHIC);

		if (scene.getLights().size() > 0) {
			gl.glDisable(GL10.GL_LIGHTING);
		}

		gl.glDisable(GL10.GL_DEPTH_TEST);

		for (Font.Text text : scene.getTexts()) {
			gl.glPushMatrix();
			gl.glTranslatef(text.getPosX(), text.getPosY(), 0f);
			text.render();
			gl.glPopMatrix();
		}

		renderUIPass(gl);
	}

	public void renderBackgroundPass(final GL11 gl) {
	};

	public void renderMainPass(final GL11 gl) {
	};

	public void renderUIPass(final GL11 gl) {
	};

	public void dispose() {
	};

}