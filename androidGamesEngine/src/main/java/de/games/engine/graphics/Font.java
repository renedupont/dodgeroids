package de.games.engine.graphics;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import de.games.engine.graphics.Mesh.RenderType;
import java.util.HashMap;
import javax.microedition.khronos.opengles.GL11;

public class Font {
    public class Rectangle {
        public float x;
        public float y;
        public float width;
        public float height;

        public Rectangle() {
            x = 0;
            y = 0;
            width = 0;
            height = 0;
        }
    }

    /** A FontStyle defines the style of a font */
    public enum FontStyle {
        Plain,
        Bold,
        Italic,
        BoldItalic
    }

    public enum SizeType {
        /**
         * Skalierungsschl�ssel zur px Berechnung: Als Referenz dient eine screen width von 800 und
         * 16/32/64px f�r den glyph Small 50 (800/16) Medium 25 (800/32) Large 13 (800/64)
         */
        Small(50),
        Medium(25),
        Large(13);

        private int intVal;

        SizeType(final int intVal) {
            this.intVal = intVal;
        }

        public int getIntVal() {
            return intVal;
        }
    }

    /** glyph hashmap * */
    private final HashMap<Character, Glyph> glyphs = new HashMap<>();
    /** current position in glyph texture to write the next glyph to * */
    private int glyphX = 0;

    private int glyphY = 0;
    private final Typeface font;
    private final Paint paint;
    private final int fontColor;
    private final FontMetrics metrics;
    private final Texture texture;

    public Font(
            final GL11 gl,
            final AssetManager assets,
            final String file,
            final SizeType sizeType,
            final int screenWidth,
            final FontStyle style,
            final int fontColor) {
        this.texture =
                new Texture(
                        gl,
                        512,
                        512,
                        Texture.TextureFilter.Nearest,
                        Texture.TextureFilter.Nearest,
                        Texture.TextureWrap.ClampToEdge,
                        Texture.TextureWrap.ClampToEdge);
        font = Typeface.createFromAsset(assets, file);
        paint = new Paint();
        paint.setTypeface(font);
        paint.setTextSize(getFontSizeInPx(sizeType, screenWidth));
        paint.setAntiAlias(false);
        this.fontColor = fontColor;
        metrics = paint.getFontMetrics();
    }

    private float getFontSizeInPx(final SizeType size, final int screenWidth) {
        return (screenWidth / size.getIntVal());
    }

    public int getGlyphAdvance(final char character) {
        float[] width = new float[1];
        paint.getTextWidths("" + character, width);
        return (int) (Math.ceil(width[0]));
    }

    public Object getGlyphBitmap(final char character) {
        Rect rect = new Rect();
        paint.getTextBounds("" + character, 0, 1, rect);
        Bitmap bitmap =
                Bitmap.createBitmap(
                        rect.width() == 0 ? 1 : rect.width() + 5,
                        getLineHeight(),
                        Bitmap.Config.ARGB_8888);
        Canvas g = new Canvas(bitmap);
        paint.setColor(0x00000000);
        paint.setStyle(Style.FILL);
        g.drawRect(new Rect(0, 0, rect.width() + 5, getLineHeight()), paint);
        paint.setColor(fontColor);
        g.drawText("" + character, 0, -metrics.ascent, paint);
        return bitmap;
    }

    public int getLineGap() {
        return (int) (Math.ceil(metrics.leading));
    }

    public int getLineHeight() {
        return (int) Math.ceil(Math.abs(metrics.ascent) + Math.abs(metrics.descent));
    }

    public int getStringWidth(final String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    Rect tmpRect = new Rect();

    public void getGlyphBounds(final char character, final Rectangle rect) {
        paint.getTextBounds("" + character, 0, 1, tmpRect);
        rect.width = tmpRect.width() + 5;
        rect.height = getLineHeight();
    }

    /**
     * Creates a new text run
     *
     * @return The new text run
     */
    public Text newText(final GL11 gl) {
        return new Text(gl);
    }

    /**
     * Returns the glyph for the given character
     *
     * @param character The character
     * @return The glyph of the character
     */
    protected Glyph getGlyph(final char character) {
        Glyph glyph = glyphs.get(character);
        if (glyph == null) {
            glyph = createGlyph(character);
            glyphs.put(character, glyph);
        }
        return glyph;
    }

    private Glyph createGlyph(final char character) {
        Object bitmap = getGlyphBitmap(character);
        Rectangle rect = new Rectangle();
        getGlyphBounds(character, rect);

        if (glyphX + rect.width >= 512) {
            glyphX = 0;
            glyphY += getLineGap() + getLineHeight();
        }

        texture.draw(bitmap, glyphX, glyphY);

        Glyph glyph =
                new Glyph(
                        getGlyphAdvance(character),
                        (int) rect.width,
                        glyphX / 512.0f,
                        glyphY / 512.0f,
                        rect.width / 512.0f,
                        rect.height / 512.0f);
        glyphX += rect.width;
        return glyph;
    }

    private class Glyph {
        public int advance;
        public int width;
        public float u;
        public float v;
        public float uWidth;
        public float vHeight;

        public Glyph(
                final int advance,
                final int width,
                final float u,
                final float v,
                final float uWidth,
                final float vHeight) {
            this.advance = advance;
            this.width = width;
            this.u = u;
            this.v = v;
            this.uWidth = uWidth;
            this.vHeight = vHeight;
        }
    }

    /**
     * A textrun is a mesh that holds the glyphs of the given string formated to fit the rectangle
     * and alignment.
     */
    public class Text {
        private final GL11 gl;
        private Mesh mesh;
        private String text = "";
        private int width;
        private int height;
        private HorizontalAlign hAlign;
        private VerticalAlign vAlign;
        private String[] lines;
        private int[] widths;
        private float posX, posY;

        /**** temporary *****/
        private float positionX;

        private float positionY;
        private float w;
        private float h;

        protected Text(final GL11 gl) {
            this.gl = gl;
        }

        public void setHorizontalAlign(final HorizontalAlign hAlign) {
            this.hAlign = hAlign;
        }

        public void setVerticalAlign(final VerticalAlign vAlign) {
            this.vAlign = vAlign;
        }

        public String getText() {
            return this.text;
        }

        public void setText(String text) {
            if (this.text.equals(text)) {
                return;
            }

            if (text == null) {
                text = "";
            }

            this.text = text;
            this.lines = text.split("\n");
            this.widths = new int[lines.length];
            for (int i = 0; i < lines.length; i++) {
                widths[i] = getStringWidth(lines[i]);
            }
            rebuild();
        }

        /** workaround * */
        public void setPosition(final float x, final float y) {
            positionX = x;
            positionY = y;
        }

        public float getPosX() {
            return positionX;
        }

        public float getPosY() {
            return positionY;
        }

        private void rebuild() {
            if (mesh == null) {
                mesh = new Mesh(gl, 6 * text.length(), false, true, false);
            }

            if (mesh.getMaximumVertices() / 6 < text.length()) {
                mesh = new Mesh(gl, 6 * text.length(), false, true, false);
            }

            mesh.reset();
            int lineHeight = getLineHeight();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                int x = 0;
                int y = height;

                if (hAlign == HorizontalAlign.Left) {
                    x = 0;
                }
                if (hAlign == HorizontalAlign.Center) {
                    x = width / 2 - widths[i] / 2;
                }
                if (hAlign == HorizontalAlign.Right) {
                    x = width - widths[i];
                }

                if (vAlign == VerticalAlign.Top) {
                    y = height;
                }
                if (vAlign == VerticalAlign.Center) {
                    y = height / 2 + lines.length * (getLineHeight() + getLineGap()) / 2;
                }
                if (vAlign == VerticalAlign.Bottom) {
                    y = lines.length * (getLineHeight() + getLineGap());
                }

                y -= i * (getLineHeight() + getLineGap());

                for (int j = 0; j < line.length(); j++) {
                    Glyph glyph = getGlyph(line.charAt(j));
                    mesh.texCoord(glyph.u, glyph.v);
                    mesh.vertex(posX + x, posY + y, 0);
                    mesh.texCoord(glyph.u + glyph.uWidth, glyph.v);
                    mesh.vertex(posX + x + glyph.width, posY + y, 0);
                    mesh.texCoord(glyph.u + glyph.uWidth, glyph.v + glyph.vHeight);
                    mesh.vertex(posX + x + glyph.width, posY + y - lineHeight, 0);
                    mesh.texCoord(glyph.u + glyph.uWidth, glyph.v + glyph.vHeight);
                    mesh.vertex(posX + x + glyph.width, posY + y - lineHeight, 0);
                    mesh.texCoord(glyph.u, glyph.v + glyph.vHeight);
                    mesh.vertex(posX + x, posY + y - lineHeight, 0);
                    mesh.texCoord(glyph.u, glyph.v);
                    mesh.vertex(posX + x, y, 0);
                    x += glyph.advance;
                }
                // temp: ist nur fuer one liners korrekt
                w = x;
                h = y;
            }
        }

        public boolean isTouched(final int x, final int y, final int padX, final int padY) {
            return (x > (positionX - w) - padX
                    && x < (positionX + w) + padX
                    && y > (positionY - y) - padY
                    && y < (positionY + h) + padY);
        }

        public void render() {
            if (mesh == null) {
                return;
            }
            texture.bind();
            mesh.render(RenderType.TRIANGLES);
        }

        public void dispose() {
            if (mesh != null) {
                mesh.dispose();
            }
        }
    }

    /** Horizontal text alignement */
    public enum HorizontalAlign {
        Left,
        Center,
        Right
    }

    /** Vertical text alignement */
    public enum VerticalAlign {
        Top,
        Center,
        Bottom
    }

    public void dispose() {
        texture.dispose();
    }
}
