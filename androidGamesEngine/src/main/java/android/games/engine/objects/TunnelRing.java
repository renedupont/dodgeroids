package android.games.engine.objects;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.microedition.khronos.opengles.GL11;

import android.games.engine.datamanagers.Scene;
import android.games.engine.graphics.Mesh;
import android.games.engine.graphics.Mesh.RenderType;
import android.games.engine.graphics.RotationSettings;
import android.games.engine.graphics.Texture;
import android.games.engine.graphics.Vector;

public class TunnelRing extends AbstractGameObject {

	public TunnelRing(final HashMap<Mesh, RotationSettings> meshIds,
			final Texture textureId, final Vector velocity,
			final Vector startPosition) {
		this(null, meshIds, textureId, velocity, startPosition);
		this.rotate(0f, 90.0f, 0f);
	}

	public TunnelRing(final Scene scene,
			final HashMap<Mesh, RotationSettings> meshIds,
			final Texture textureId, final Vector velocity,
			final Vector startPosition) {
		super(scene, meshIds, textureId, velocity, startPosition);
		this.rotate(0f, 90.0f, 0f);
	}

	@Override
	public void update(final float delta) {
		pos.z += velocity.z * delta;
	}

	@Override
	public void render(final GL11 gl, final RenderType type) {
		// gl.glDisable(GL11.GL_LIGHTING);
		// gl.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );

		gl.glColor4f(1, 1, 1, 1f);
		// gl.glEnable( GL11.GL_BLEND );
		gl.glPushMatrix();
		// scene.getMesh("tunnelRing").render(type);
		for (Entry<Mesh, RotationSettings> entry : meshes.entrySet()) {
			entry.getKey().render(type);
		}
		gl.glPopMatrix();
		gl.glColor4f(1, 1, 1, 1);

		// gl.glDisable( GL11.GL_BLEND );
		// gl.glEnable(GL11.GL_LIGHTING);
	}
}
