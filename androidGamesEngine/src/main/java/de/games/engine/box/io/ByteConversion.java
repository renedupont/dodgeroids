package de.games.engine.box.io;

public class ByteConversion {
    public enum ByteOrder {
        BigEndian,
        LittleEndian
    }

    public static final ByteOrder DEFAULT_BYTEORDER = ByteOrder.BigEndian;

    public static final short shortFromBytes(byte[] value) {
        return shortFromBytes(value, DEFAULT_BYTEORDER);
    }

    public static final short shortFromBytes(byte[] value, ByteOrder byteOrder) {
        int i, bytes = value.length;
        if (bytes < 2) {
            return 0;
        }
        int s = 0;
        if (bytes == 0) return (short) s;
        switch (byteOrder) {
            case BigEndian:
                for (i = 0; i < value.length; i++) {
                    s = (s << 8) + (value[i] & 0xFF);
                }
                break;
            case LittleEndian:
                for (i = 0; i < value.length; i++) s = (int) (s + ((value[i] & 0xFFL) << 8 * i));
                break;
        }
        return (short) s;
    }

    public static final short[] shortArrayFromBytes(byte[] value) {
        return shortArrayFromBytes(value, 2, DEFAULT_BYTEORDER);
    }

    public static final short[] shortArrayFromBytes(byte[] value, int bytesPerShort) {
        return shortArrayFromBytes(value, bytesPerShort, DEFAULT_BYTEORDER);
    }

    public static final short[] shortArrayFromBytes(byte[] value, ByteOrder byteOrder) {
        return shortArrayFromBytes(value, 2, byteOrder);
    }

    public static final short[] shortArrayFromBytes(
            byte[] value, int bytesPerShort, ByteOrder byteOrder) {
        if (bytesPerShort == 0 || bytesPerShort < 2) {
            return null;
        }
        short[] data = new short[value.length / bytesPerShort];
        for (int i = 0; i < data.length; i++) {
            byte[] s = new byte[bytesPerShort];
            for (int d = 0; d < s.length; d++) {
                s[d] = value[i * bytesPerShort + d];
            }
            data[i] = shortFromBytes(s, byteOrder);
        }
        return data;
    }

    public static final int intFromBytes(byte[] value) {
        return intFromBytes(value, DEFAULT_BYTEORDER);
    }

    public static final int intFromBytes(byte[] value, ByteOrder byteOrder) {
        int i, bytes = value.length;
        int s = 0;
        if (bytes == 0) return s;
        switch (byteOrder) {
            case BigEndian:
                for (i = 0; i < value.length; i++) {
                    s = (s << 8) + (value[i] & 0xFF);
                }
                break;
            case LittleEndian:
                for (i = 0; i < value.length; i++) s = (int) (s + ((value[i] & 0xFFL) << 8 * i));
                break;
        }
        return s;
    }

    public static final float floatFromBytes(byte[] value) {
        return Float.intBitsToFloat(intFromBytes(value, DEFAULT_BYTEORDER));
    }

    public static final float floatFromBytes(byte[] value, ByteOrder byteOrder) {
        return Float.intBitsToFloat(intFromBytes(value, byteOrder));
    }

    public static final float[] floatArrayFromBytes(byte[] value) {
        return floatArrayFromBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final float[] floatArrayFromBytes(
            byte[] value, int bytesPerFloat, ByteOrder byteOrder) {
        if (bytesPerFloat == 0 || bytesPerFloat < 4) {
            return null;
        }
        float[] data = new float[value.length / bytesPerFloat];
        for (int i = 0; i < data.length; i++) {
            byte[] s = new byte[bytesPerFloat];
            for (int d = 0; d < s.length; d++) {
                s[d] = value[i * bytesPerFloat + d];
            }
            data[i] = floatFromBytes(s, byteOrder);
        }
        return data;
    }

    public static final char charFromBytes(byte[] value, ByteOrder byteOrder) {
        int i, bytes = value.length;
        if (bytes < 2) {
            return Character.MIN_VALUE;
        }
        int s = 0;
        if (bytes == 0) return (char) s;
        switch (byteOrder) {
            case BigEndian:
                for (i = 0; i < value.length; i++) {
                    s = (s << 8) + (value[i] & 0xFF);
                }
                break;
            case LittleEndian:
                for (i = 0; i < value.length; i++) s = (int) (s + ((value[i] & 0xFFL) << 8 * i));
                break;
        }
        return (char) s;
    }

    public static final char[] charArrayFromBytes(byte[] value) {
        return charArrayFromBytes(value, 2, DEFAULT_BYTEORDER);
    }

    public static final char[] charArrayFromBytes(
            byte[] value, int bytesPerValue, ByteOrder byteOrder) {
        if (bytesPerValue == 0 || bytesPerValue < 2) {
            return null;
        }
        char[] data = new char[value.length / bytesPerValue];
        for (int i = 0; i < data.length; i++) {
            byte[] s = new byte[bytesPerValue];
            for (int d = 0; d < s.length; d++) {
                s[d] = value[i * bytesPerValue + d];
            }
            data[i] = charFromBytes(s, byteOrder);
        }
        return data;
    }

    public static final byte[] shortToBytes(short value) {
        return shortToBytes(value, 2, DEFAULT_BYTEORDER);
    }

    public static final byte[] shortToBytes(short value, int bytes, ByteOrder byteOrder) {
        int i;
        if (bytes < 2) return null;
        byte[] s = new byte[bytes];
        switch (byteOrder) {
            case BigEndian:
                if (bytes == 2) {
                    s[0] = (byte) (value >> 8 & 0xFF);
                    s[1] = (byte) (value & 0xFF);
                    return s;
                }
                s[0] = (byte) (value >>> (bytes - 1) * 8);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (value >> (bytes - i + 2) * 8 & 0xFF);
                }
                s[bytes - 1] = (byte) (value & 0xFF);
                break;
            case LittleEndian:
                if (bytes == 2) {
                    s[0] = (byte) (value & 0xFF);
                    s[1] = (byte) (value >> 8 & 0xFF);
                    return s;
                }
                s[0] = (byte) (value & 0xFF);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (value >> (i + 2) * 8 & 0xFF);
                }
                s[bytes - 1] = (byte) (value >>> bytes * 8);
                break;
        }
        return s;
    }

    public static final byte[] intToBytes(int value) {
        return intToBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final byte[] intToBytes(int value, int bytesPerValue) {
        return intToBytes(value, bytesPerValue, DEFAULT_BYTEORDER);
    }

    public static final byte[] intToBytes(int value, ByteOrder byteOrder) {
        return intToBytes(value, 4, byteOrder);
    }

    public static final byte[] intToBytes(int value, int bytes, ByteOrder byteOrder) {
        int i;
        if (bytes < 4) return null;
        byte[] s = new byte[bytes];
        switch (byteOrder) {
            case BigEndian:
                s[0] = (byte) (value >>> (bytes - 1) * 8 & 0xFF);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (value >> (bytes - i + 2) * 8 & 0xFF);
                }
                s[bytes - 1] = (byte) (value & 0xFF);
                break;
            case LittleEndian:
                s[0] = (byte) (value & 0xFF);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (value >> (i + 2) * 8 & 0xFF);
                }
                s[bytes - 1] = (byte) (value >>> bytes * 8);
                break;
        }
        return s;
    }

    public static final byte[] floatToBytes(float value) {
        return floatToBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final byte[] floatToBytes(float value, int bytes, ByteOrder byteOrder) {
        return intToBytes(Float.floatToIntBits(value), bytes, byteOrder);
    }

    public static final byte[] floatArrayToBytes(float[] value) {
        return floatArrayToBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final byte[] floatArrayToBytes(float[] value, int bytes, ByteOrder byteOrder) {
        if (bytes < 4) {
            return null;
        }
        byte[] r = new byte[value.length * bytes];
        for (int i = 0; i < value.length; i++) {
            byte[] x = intToBytes(Float.floatToIntBits(value[i]), bytes, byteOrder);
            for (int s = 0; s < bytes; s++) {
                r[i * bytes + s] = x[s];
            }
        }
        return r;
    }

    public static final byte[] charToBytes(char value, int bytes, ByteOrder byteOrder) {
        int i;
        if (bytes < 2) return null;
        byte[] s = new byte[bytes];
        switch (byteOrder) {
            case BigEndian:
                if (bytes == 2) {
                    s[0] = (byte) (value >> 8 & 0xFF);
                    s[1] = (byte) (value & 0xFF);
                    return s;
                }
                s[0] = (byte) (value >>> (bytes - 1) * 8);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (value >> (bytes - i + 2) * 8 & 0xFF);
                }
                s[bytes - 1] = (byte) (value & 0xFF);
                break;
            case LittleEndian:
                if (bytes == 2) {
                    s[0] = (byte) (value & 0xFF);
                    s[1] = (byte) (value >> 8 & 0xFF);
                    return s;
                }
                s[0] = (byte) (value & 0xFF);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (value >> (i + 2) * 8 & 0xFF);
                }
                s[bytes - 1] = (byte) (value >>> bytes * 8);
                break;
        }
        return s;
    }

    public static final byte[] charArrayToBytes(char[] value) {
        return charArrayToBytes(value, 2, DEFAULT_BYTEORDER);
    }

    public static final byte[] charArrayToBytes(char[] value, int bytes, ByteOrder byteOrder) {
        if (bytes < 2) {
            return null;
        }
        byte[] r = new byte[value.length * bytes];
        for (int i = 0; i < value.length; i++) {
            byte[] x = charToBytes(value[i], bytes, byteOrder);
            for (int s = 0; s < bytes; s++) {
                r[i * bytes + s] = x[s];
            }
        }
        return r;
    }
}
