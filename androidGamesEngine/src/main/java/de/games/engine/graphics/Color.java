package de.games.engine.graphics;

//
public class Color {

    public static final Color CLEAR = new Color(0, 0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1, 1);

    /** the red, green, blue and alpha components * */
    public float r, g, b, a;

    /** Constructs a new Color with all components set to 0. */
    public Color() {}

    /**
     * @see #rgba8888ToColor(Color, int)
     */
    public Color(final int rgba8888) {
        rgba8888ToColor(this, rgba8888);
    }

    /**
     * Constructor, sets the components of the color
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     */
    public Color(final float r, final float g, final float b, final float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    /**
     * Constructs a new color using the given color
     *
     * @param color the color
     */
    public Color(final Color color) {
        set(color);
    }

    //
    public float[] getRGBA() {
        return new float[] {r, g, b, a};
    }

    /**
     * Sets this color to the given color.
     *
     * @param color the Color
     */
    public Color set(final Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        return this;
    }

    /**
     * Adds the given color to this color.
     *
     * @param color the color
     * @return this color
     */
    public Color add(final Color color) {
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        this.a += color.a;
        return clamp();
    }

    /**
     * @return this Color for chaining
     */
    public Color clamp() {
        if (r < 0) {
            r = 0;
        } else if (r > 1) {
            r = 1;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 1) {
            g = 1;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 1) {
            b = 1;
        }

        if (a < 0) {
            a = 0;
        } else if (a > 1) {
            a = 1;
        }
        return this;
    }

    /**
     * @return this Color for chaining
     */
    public Color add(final float r, final float g, final float b, final float a) {
        this.r += r;
        this.g += g;
        this.b += b;
        this.a += a;
        return clamp();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Color color = (Color) o;
        return toIntBitsABGR() == color.toIntBitsABGR();
    }

    /**
     * Packs the color components into a 32-bit integer with the format ABGR.
     *
     * @return the packed color as a 32-bit int.
     */
    public int toIntBitsABGR() {
        return ((int) (255 * a) << 24)
                | ((int) (255 * b) << 16)
                | ((int) (255 * g) << 8)
                | ((int) (255 * r));
    }

    /** Returns the color encoded as hex string with the format RRGGBBAA. */
    @Override
    public String toString() {
        String value =
                Integer.toHexString(
                        ((int) (255 * r) << 24)
                                | ((int) (255 * g) << 16)
                                | ((int) (255 * b) << 8)
                                | ((int) (255 * a)));
        while (value.length() < 8) {
            value = "0" + value;
        }
        return value;
    }

    public static int toIntBitsARGB(final int r, final int g, final int b, final int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Sets the Color components using the specified integer value in the format RGBA8888. This is
     * inverse to the rgba8888(r, g, b, a) method.
     *
     * @param color The Color to be modified.
     * @param value An integer color value in RGBA8888 format.
     */
    public static void rgba8888ToColor(final Color color, final int value) {
        color.r = ((value & 0xff000000) >>> 24) / 255f;
        color.g = ((value & 0x00ff0000) >>> 16) / 255f;
        color.b = ((value & 0x0000ff00) >>> 8) / 255f;
        color.a = ((value & 0x000000ff)) / 255f;
    }
}
