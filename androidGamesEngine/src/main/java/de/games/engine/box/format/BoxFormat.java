package de.games.engine.box.format;

import de.games.engine.box.primitive.BoxVersion;

public class BoxFormat {
    public enum Identifier {
        MESH(0),
        MESHDODGEIT(1);

        private final int flag;

        Identifier(int s) {
            this.flag = s;
        }

        public int getFlag() {
            return this.flag;
        }

        public static Identifier getFromFlag(int flag) {
            byte b;
            int i;
            Identifier[] arrayOfIdentifier;
            for (i = (arrayOfIdentifier = values()).length, b = 0; b < i; ) {
                Identifier a = arrayOfIdentifier[b];
                if (a.flag == flag) return a;
                b++;
            }
            throw new IllegalArgumentException("invalid flag");
        }
    }

    public static final byte[] MAGIC_BYTES = new byte[] {61, 66, 79, /* 31 */ 88};
    public static final BoxVersion CURRENT_VERSION = new BoxVersion((short) 0, (short) /* 33 */ 1);
    public static String ENCODING = "UTF-8";
}
