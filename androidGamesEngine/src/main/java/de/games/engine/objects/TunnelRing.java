package de.games.engine.objects;

import de.games.engine.datamanagers.Scene;
import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.microedition.khronos.opengles.GL11;

public class TunnelRing extends AbstractGameObject {

    public TunnelRing(
            final HashMap<Mesh, RotationSettings> meshIds,
            final Texture textureId,
            final Vector velocity,
            final Vector startPosition) {
        super(meshIds, textureId, velocity, startPosition);
        this.rotate(0f, 90.0f, 0f);
    }


    @Override
    public void update(final float delta) {
        pos.z += velocity.z * delta;
    }

    @Override
    public void render(final GL11 gl, final Mesh.RenderType type) {
        gl.glColor4f(1, 1, 1, 1f);
        gl.glPushMatrix();
        for (Entry<Mesh, RotationSettings> entry : meshes.entrySet()) {
            entry.getKey().render(type);
        }
        gl.glPopMatrix();
        gl.glColor4f(1, 1, 1, 1);
    }
}
