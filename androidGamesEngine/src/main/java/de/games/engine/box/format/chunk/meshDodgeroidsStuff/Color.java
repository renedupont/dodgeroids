package de.games.engine.box.format.chunk.meshDodgeroidsStuff;

public class Color {

    public float r;

    public float g;
    public float b;
    public float a;

    public Color() {}

    public Color(int rgba8888) {
        rgba8888ToColor(this, rgba8888);
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    public Color(Color color) {
        set(color);
    }

    public float[] getRGBA() {
        return new float[] {this.r, this.g, this.b, this.a};
    }

    public Color set(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
        return this;
    }

    public Color add(Color color) {
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        this.a += color.a;
        return clamp();
    }

    public Color clamp() {
        if (this.r < 0.0F) {
            this.r = 0.0F;
        } else if (this.r > 1.0F) {
            this.r = 1.0F;
        }
        if (this.g < 0.0F) {
            this.g = 0.0F;
        } else if (this.g > 1.0F) {
            this.g = 1.0F;
        }
        if (this.b < 0.0F) {
            this.b = 0.0F;
        } else if (this.b > 1.0F) {
            this.b = 1.0F;
        }
        if (this.a < 0.0F) {
            this.a = 0.0F;
        } else if (this.a > 1.0F) {
            this.a = 1.0F;
        }
        return this;
    }

    public Color set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return clamp();
    }

    public Color set(int rgba) {
        rgba8888ToColor(this, rgba);
        return this;
    }

    public Color add(float r, float g, float b, float a) {
        this.r += r;
        this.g += g;
        this.b += b;
        this.a += a;
        return clamp();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return (toIntBits() == color.toIntBits());
    }

    public int toIntBits() {
        int color =
                (int) (255.0F * this.a) << 24
                        | (int) (255.0F * this.b) << 16
                        | (int) (255.0F * this.g) << 8
                        | (int) (255.0F * this.r);
        return color;
    }

    public String toString() {
        String value =
                Integer.toHexString(
                        (int) (255.0F * this.r) << 24
                                | (int) (255.0F * this.g) << 16
                                | (int) (255.0F * this.b) << 8
                                | (int) (255.0F * this.a));
        while (value.length() < 8) value = "0" + value;
        return value;
    }

    public static void rgba8888ToColor(Color color, int value) {
        color.r = ((value & 0xFF000000) >>> 24) / 255.0F;
        color.g = ((value & 0xFF0000) >>> 16) / 255.0F;
        color.b = ((value & 0xFF00) >>> 8) / 255.0F;
        color.a = (value & 0xFF) / 255.0F;
    }
}
