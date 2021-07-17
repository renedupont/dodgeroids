package de.games.dodgeroids.objects;

import de.games.engine.graphics.Mesh;
import de.games.engine.graphics.RotationSettings;
import de.games.engine.graphics.Texture;
import de.games.engine.graphics.Vector;
import de.games.engine.objects.AbstractGameObject;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.microedition.khronos.opengles.GL11;

public class Asteroid extends AbstractGameObject {

    public Asteroid(
            final HashMap<Mesh, RotationSettings> meshIds,
            final Texture textureId,
            final float scaleFactor,
            final Vector velocity,
            final Vector startPosition) {
        super(meshIds, textureId, velocity, startPosition);
        setScale(scaleFactor, scaleFactor, scaleFactor);
        rot.x = 2.0f * ((float) Math.random() / 1.0f) - 1.0f;
        rot.y = 2.0f * ((float) Math.random() / 1.0f) - 1.0f;
        rot.z = 2.0f * ((float) Math.random() / 1.0f) - 1.0f;
    }

    @Override
    public void update(final float delta) {
        updateAllMeshRotations(delta);
        pos.z += (velocity.z * delta);
    }

    @Override
    public void render(final GL11 gl, final Mesh.RenderType type) {
        Vector rotation;
        for (Entry<Mesh, RotationSettings> entry : meshes.entrySet()) {
            gl.glPushMatrix();
            rotation = entry.getValue().getCurrentRotation();
            gl.glRotatef(rotation.x, 1, 0, 0);
            gl.glRotatef(rotation.y, 0, 1, 0);
            gl.glRotatef(rotation.z, 0, 0, 1);
            entry.getKey().render(type);
            gl.glPopMatrix();
        }
    }
}
