package de.games.engine.graphics;

import android.opengl.GLU;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Camera {
    public enum Projection {
        PERSPECTIVE,
        ORTHOGRAPHIC
    }

    // public enum Type {
    // TWO_D, THREE_D, DEFAULT
    // }

    private float frustumWidth;
    private float frustumHeight;
    private float fovY;
    private float zFar;
    private float zNear;
    private final Vector up;
    private Vector position;
    private Vector direction;

    // public Camera(Vector position, Vector direction, Vector up, float fovY,
    // float zNear, float zFar) {
    // this.position = position;
    // this.direction = direction;
    // this.up = up;
    // this.fovY = fovY;
    // this.zNear = zNear;
    // this.zFar = zFar;
    // }

    public Camera(
            final Vector position,
            final Vector direction,
            final Vector up,
            final float width,
            final float height,
            final float fovY,
            final float zNear,
            final float zFar) {
        // this(position, direction, up, fovY, zNear, zFar);
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
                // GLU.gluOrtho2D( gl, -frustumWidth, frustumWidth, -frustumHeight,
                // frustumHeight );
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

    public Vector getPosition() {
        return position;
    }

    public void setPosition(final Vector position) {
        this.position = position;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(final Vector direction) {
        this.direction = direction;
    }

    public float getFov() {
        return fovY;
    }

    public void setFov(final float fov) {
        this.fovY = fov;
    }

    public void setFrustum(final float width, final float height) { // TODO:
        // Bildschirmaufloesung
        // fuer pixelspace,
        // andere werte fuer
        // ortho effeckt
        frustumWidth = width;
        frustumHeight = height;
    }

    public void update(final float delta) {}

    public float getzFar() {
        return zFar;
    }

    public void setzFar(final float zFar) {
        this.zFar = zFar;
    }

    public float getzNear() {
        return zNear;
    }

    public void setzNear(final float zNear) {
        this.zNear = zNear;
    }
}
