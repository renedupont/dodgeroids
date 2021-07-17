package de.games.engine.graphics;

import android.opengl.GLU;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Camera {
    public enum Projection {
        PERSPECTIVE,
        ORTHOGRAPHIC
    }

    private float frustumWidth;
    private float frustumHeight;
    private float fovY;
    private float zFar;
    private float zNear;
    private final Vector up;
    private Vector position;
    private Vector direction;

    public Camera(
            final Vector position,
            final Vector direction,
            final Vector up,
            final float width,
            final float height,
            final float fovY,
            final float zNear,
            final float zFar) {
        this.position = position;
        this.direction = direction;
        this.up = up;
        this.fovY = fovY;
        this.zNear = zNear;
        this.zFar = zFar;
        this.frustumWidth = width;
        this.frustumHeight = height;
    }

    public void render(final GL11 gl) {
        GLU.gluLookAt(
                gl,
                position.x,
                position.y,
                position.z,
                direction.x,
                direction.y,
                direction.z,
                up.x,
                up.y,
                up.z);
    }

    public void setMode(final GL11 gl, final Projection mode) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        switch (mode) {
            case ORTHOGRAPHIC:
                gl.glOrthof(0, frustumWidth, 0, frustumHeight, -1f, 1f);
                break;
            case PERSPECTIVE:
                float aspectRatio = frustumWidth / frustumHeight;
                GLU.gluPerspective(gl, fovY, aspectRatio, zNear, zFar);
                break;
        }
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void setPosition(final Vector position) {
        this.position = position;
    }

    public void update(final float delta) {}

    public float getzFar() {
        return zFar;
    }

    public float getzNear() {
        return zNear;
    }
}
