package de.games.engine.box.format.chunk.meshDodgeItStuff;

public class Color {
    public static final Color CLEAR = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    public static final Color WHITE = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    public static final Color BLACK = new Color(0.0F, 0.0F, 0.0F, 1.0F);
    public static final Color RED = new Color(1.0F, 0.0F, 0.0F, 1.0F);
    public static final Color GREEN = new Color(0.0F, 1.0F, 0.0F, 1.0F);
    public static final Color BLUE = new Color(0.0F, 0.0F, 1.0F, 1.0F);
    public static final Color LIGHT_GRAY = new Color(0.75F, 0.75F, 0.75F, 1.0F);
    public static final Color GRAY = new Color(0.5F, 0.5F, 0.5F, 1.0F);
    public static final Color DARK_GRAY = new Color(0.25F, 0.25F, 0.25F, 1.0F);
    public static final Color PINK = new Color(1.0F, 0.68F, 0.68F, 1.0F);
    public static final Color ORANGE = new Color(1.0F, 0.78F, 0.0F, 1.0F);
    public static final Color YELLOW = new Color(1.0F, 1.0F, 0.0F, 1.0F);
    public static final Color MAGENTA = new Color(1.0F, 0.0F, 1.0F, 1.0F);
    public static final Color CYAN = new Color(0.0F, 1.0F, 1.0F, 1.0F);
    @Deprecated public static Color tmp = new Color();

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

    public Color mul(Color color) {
        this.r *= color.r;
        this.g *= color.g;
        this.b *= color.b;
        this.a *= color.a;
        return clamp();
    }

    public Color mul(float value) {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
        return clamp();
    }

    public Color add(Color color) {
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        this.a += color.a;
        return clamp();
    }

    public Color sub(Color color) {
        this.r -= color.r;
        this.g -= color.g;
        this.b -= color.b;
        this.a -= color.a;
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

    public Color sub(float r, float g, float b, float a) {
        this.r -= r;
        this.g -= g;
        this.b -= b;
        this.a -= a;
        return clamp();
    }

    public Color mul(float r, float g, float b, float a) {
        this.r *= r;
        this.g *= g;
        this.b *= b;
        this.a *= a;
        return clamp();
    }

    public Color lerp(Color target, float t) {
        this.r += t * (target.r - this.r);
        this.g += t * (target.g - this.g);
        this.b += t * (target.b - this.b);
        this.a += t * (target.a - this.a);
        return clamp();
    }

    public Color lerp(float r, float g, float b, float a, float t) {
        this.r += t * (r - this.r);
        this.g += t * (g - this.g);
        this.b += t * (b - this.b);
        this.a += t * (a - this.a);
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

    public static Color valueOf(String hex) {
        int r = Integer.valueOf(hex.substring(0, 2), 16).intValue();
        int g = Integer.valueOf(hex.substring(2, 4), 16).intValue();
        int b = Integer.valueOf(hex.substring(4, 6), 16).intValue();
        int a = (hex.length() != 8) ? 255 : Integer.valueOf(hex.substring(6, 8), 16).intValue();
        return new Color(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
    }

    public static int toIntBits(int r, int g, int b, int a) {
        return a << 24 | b << 16 | g << 8 | r;
    }

    public static int alpha(float alpha) {
        return (int) (alpha * 255.0F);
    }

    public static int luminanceAlpha(float luminance, float alpha) {
        return (int) (luminance * 255.0F) << 8 | (int) (alpha * 255.0F);
    }

    public static int rgb565(float r, float g, float b) {
        return (int) (r * 31.0F) << 11 | (int) (g * 63.0F) << 5 | (int) (b * 31.0F);
    }

    public static int rgba4444(float r, float g, float b, float a) {
        return (int) (r * 15.0F) << 12
                | (int) (g * 15.0F) << 8
                | (int) (b * 15.0F) << 4
                | (int) (a * 15.0F);
    }

    public static int rgb888(float r, float g, float b) {
        return (int) (r * 255.0F) << 16 | (int) (g * 255.0F) << 8 | (int) (b * 255.0F);
    }

    public static int rgba8888(float r, float g, float b, float a) {
        return (int) (r * 255.0F) << 24
                | (int) (g * 255.0F) << 16
                | (int) (b * 255.0F) << 8
                | (int) (a * 255.0F);
    }

    public static int rgb565(Color color) {
        return (int) (color.r * 31.0F) << 11
                | (int) (color.g * 63.0F) << 5
                | (int) (color.b * 31.0F);
    }

    public static int rgba4444(Color color) {
        return (int) (color.r * 15.0F) << 12
                | (int) (color.g * 15.0F) << 8
                | (int) (color.b * 15.0F) << 4
                | (int) (color.a * 15.0F);
    }

    public static int rgb888(Color color) {
        return (int) (color.r * 255.0F) << 16
                | (int) (color.g * 255.0F) << 8
                | (int) (color.b * 255.0F);
    }

    public static int rgba8888(Color color) {
        return (int) (color.r * 255.0F) << 24
                | (int) (color.g * 255.0F) << 16
                | (int) (color.b * 255.0F) << 8
                | (int) (color.a * 255.0F);
    }

    public static void rgb565ToColor(Color color, int value) {
        color.r = ((value & 0xF800) >>> 11) / 31.0F;
        color.g = ((value & 0x7E0) >>> 5) / 63.0F;
        color.b = ((value & 0x1F) >>> 0) / 31.0F;
    }

    public static void rgba4444ToColor(Color color, int value) {
        color.r = ((value & 0xF000) >>> 12) / 15.0F;
        color.g = ((value & 0xF00) >>> 8) / 15.0F;
        color.b = ((value & 0xF0) >>> 4) / 15.0F;
        color.a = (value & 0xF) / 15.0F;
    }

    public static void rgb888ToColor(Color color, int value) {
        color.r = ((value & 0xFF0000) >>> 16) / 255.0F;
        color.g = ((value & 0xFF00) >>> 8) / 255.0F;
        color.b = (value & 0xFF) / 255.0F;
    }

    public static void rgba8888ToColor(Color color, int value) {
        color.r = ((value & 0xFF000000) >>> 24) / 255.0F;
        color.g = ((value & 0xFF0000) >>> 16) / 255.0F;
        color.b = ((value & 0xFF00) >>> 8) / 255.0F;
        color.a = (value & 0xFF) / 255.0F;
    }

    public Color tmp() {
        return tmp.set(this);
    }

    public Color cpy() {
        return new Color(this);
    }
}
