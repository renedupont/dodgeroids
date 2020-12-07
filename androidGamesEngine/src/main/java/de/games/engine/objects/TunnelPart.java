package de.games.engine.objects;

import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class TunnelPart extends AbstractGameObject {

    public TunnelPart(
            final HashMap<Mesh, RotationSettings> meshIds,
            final Texture textureId,
            final Vector velocity,
            final Vector startPosition) {
        super(meshIds, textureId, velocity, startPosition);
        this.transparent = true;
    }

    @Override
    public void render(final GL11 gl, final Mesh.RenderType type) {
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(1, 1, 1, 0.35f);
        gl.glEnable(GL10.GL_BLEND);
        for (Entry<Mesh, RotationSettings> entry : meshes.entrySet()) {
            entry.getKey().render(type);
        }
        gl.glColor4f(1, 1, 1, 1);
        gl.glDisable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_LIGHTING);
    }

    @Override
    public void update(final float delta) {
        pos.z += velocity.z * delta;
    }
}
