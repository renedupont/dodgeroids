package de.games.engine.graphics;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Light { //
    public enum Id {
        ZERO(GL11.GL_LIGHT0),
        ONE(GL11.GL_LIGHT1),
        TWO(GL11.GL_LIGHT2),
        THREE(GL11.GL_LIGHT3),
        FOUR(GL11.GL_LIGHT4),
        FIVE(GL11.GL_LIGHT5),
        SIX(GL11.GL_LIGHT6),
        SEVEN(GL11.GL_LIGHT7);

        private final int flag;

        private Id(final int f) {
            this.flag = f;
        }

        public int getFlag() {
            return flag;
        }
    }

    public enum Type {
        POINT,
        DIRECTIONAL,
        SPOT
    }

    private final boolean isEnabled = true;
    private Color ambient = new Color();
    private Color diffuse = new Color();
    private Color specular = new Color();
    private Vector position = new Vector();

    /** spotlight parameter * */
    private Vector direction = new Vector();

    private float cutoff = 30.0f;
    /** cutoff = winkel des lichtkegels * 2, also maximal 180 sonst hat man ein pointlight */
    private float exponent = 0.0f;

    private final Id id;
    private final Type type;

    public Light(final Type type, final Id id) {
        this.type = type;
        this.id = id;
    }

    public void setPosition(final Vector position) {
        this.position = position;
    }

    public void setDirection(final Vector direction) {
        this.direction = direction;
    }

    public void setAmbient(final Color ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(final Color diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(final Color specular) {
        this.specular = specular;
    }

    public void setCutoff(final float cutoff) {
        if (cutoff > 180 || cutoff < 0) {
            throw new IllegalArgumentException("cutoff has to be between 0 and 180");
        }
        this.cutoff = cutoff;
    }

    public void setExponent(final float exponent) {
        this.exponent = exponent;
    }

    public void render(final GL11 gl) {
        if (isEnabled) {
            gl.glEnable(id.getFlag());
        } else {
            gl.glDisable(id.getFlag());
        }

        gl.glLightfv(id.getFlag(), GL10.GL_AMBIENT, ambient.getRGBA(), 0);
        gl.glLightfv(id.getFlag(), GL10.GL_DIFFUSE, diffuse.getRGBA(), 0);
        gl.glLightfv(id.getFlag(), GL10.GL_SPECULAR, specular.getRGBA(), 0);

        float[] pos = {position.x, position.y, position.z, 1.0f};
        switch (type) {
            case DIRECTIONAL:
                gl.glLightfv(id.getFlag(), GL10.GL_POSITION, pos, 0);
                break;
            case POINT:
                pos[3] = 0.0f;
                gl.glLightfv(id.getFlag(), GL10.GL_POSITION, pos, 0);
                break;
            case SPOT:
                float[] co = {cutoff};
                float[] ex = {exponent};
                float[] dir = {direction.x, direction.y, direction.z};
                gl.glLightfv(id.getFlag(), GL10.GL_POSITION, pos, 0);
                gl.glLightfv(id.getFlag(), GL10.GL_SPOT_CUTOFF, co, 0);
                gl.glLightfv(id.getFlag(), GL10.GL_SPOT_EXPONENT, ex, 0);
                gl.glLightfv(id.getFlag(), GL10.GL_SPOT_DIRECTION, dir, 0);
                break;
            default:
                gl.glLightfv(id.getFlag(), GL10.GL_POSITION, pos, 0);
        }
    }
}
