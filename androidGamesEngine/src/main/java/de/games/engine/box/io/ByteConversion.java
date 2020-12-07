package de.games.engine.box.io;

public class ByteConversion {
    public enum ByteOrder {
        BigEndian,
        LittleEndian;
    }

    public static final ByteOrder DEFAULT_BYTEORDER = ByteOrder.BigEndian;
    public static final int BYTES_PER_SHORT = 2;
    public static final int BYTES_PER_INT = 4;
    public static final int BYTES_PER_LONG = 8;
    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_DOUBLE = 8;
    public static final int BYTES_PER_CHAR = 2;

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

    public static final int[] intArrayFromBytes(byte[] value) {
        return intArrayFromBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final int[] intArrayFromBytes(byte[] value, int bytesPerInt) {
        return intArrayFromBytes(value, bytesPerInt, DEFAULT_BYTEORDER);
    }

    public static final int[] intArrayFromBytes(byte[] value, ByteOrder byteOrder) {
        return intArrayFromBytes(value, 4, byteOrder);
    }

    public static final int[] intArrayFromBytes(
            byte[] value, int bytesPerInt, ByteOrder byteOrder) {
        if (bytesPerInt == 0 || bytesPerInt < 4) {
            return null;
        }
        int[] data = new int[value.length / bytesPerInt];
        for (int i = 0; i < data.length; i++) {
            byte[] s = new byte[bytesPerInt];
            for (int d = 0; d < s.length; d++) {
                s[d] = value[i * bytesPerInt + d];
            }
            data[i] = intFromBytes(s, byteOrder);
        }
        return data;
    }

    public static final long longFromBytes(byte[] value) {
        return longFromBytes(value, DEFAULT_BYTEORDER);
    }

    public static final long longFromBytes(byte[] value, ByteOrder byteOrder) {
        int i, bytes = value.length;
        long s = 0L;
        if (bytes == 0) return s;
        switch (byteOrder) {
            case BigEndian:
                for (i = 0; i < value.length; i++) {
                    s = (s << 8L) + (value[i] & 0xFF);
                }
                break;
            case LittleEndian:
                for (i = 0; i < value.length; i++) s += (value[i] & 0xFFL) << 8 * i;
                break;
        }
        return s;
    }

    public static final long[] longArrayFromBytes(byte[] value) {
        return longArrayFromBytes(value, 8, DEFAULT_BYTEORDER);
    }

    public static final long[] longArrayFromBytes(byte[] value, int bytesPerLong) {
        return longArrayFromBytes(value, bytesPerLong, DEFAULT_BYTEORDER);
    }

    public static final long[] longArrayFromBytes(byte[] value, ByteOrder byteOrder) {
        return longArrayFromBytes(value, 8, byteOrder);
    }

    public static final long[] longArrayFromBytes(
            byte[] value, int bytesPerLong, ByteOrder byteOrder) {
        if (bytesPerLong == 0 || bytesPerLong < 8) {
            return null;
        }
        long[] data = new long[value.length / bytesPerLong];
        for (int i = 0; i < data.length; i++) {
            byte[] s = new byte[bytesPerLong];
            for (int d = 0; d < s.length; d++) {
                s[d] = value[i * bytesPerLong + d];
            }
            data[i] = longFromBytes(s, byteOrder);
        }
        return data;
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

    public static final float[] floatArrayFromBytes(byte[] value, int bytesPerFloat) {
        return floatArrayFromBytes(value, bytesPerFloat, DEFAULT_BYTEORDER);
    }

    public static final float[] floatArrayFromBytes(byte[] value, ByteOrder byteOrder) {
        return floatArrayFromBytes(value, 4, byteOrder);
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

    public static final double doubleFromBytes(byte[] value) {
        return Double.longBitsToDouble(longFromBytes(value, DEFAULT_BYTEORDER));
    }

    public static final double doubleFromBytes(byte[] value, ByteOrder byteOrder) {
        return Double.longBitsToDouble(longFromBytes(value, byteOrder));
    }

    public static final double[] doubleArrayFromBytes(byte[] value) {
        return doubleArrayFromBytes(value, 8, DEFAULT_BYTEORDER);
    }

    public static final double[] doubleArrayFromBytes(byte[] value, int bytesPerValue) {
        return doubleArrayFromBytes(value, bytesPerValue, DEFAULT_BYTEORDER);
    }

    public static final double[] doubleArrayFromBytes(byte[] value, ByteOrder byteOrder) {
        return doubleArrayFromBytes(value, 8, byteOrder);
    }

    public static final double[] doubleArrayFromBytes(
            byte[] value, int bytesPerValue, ByteOrder byteOrder) {
        if (bytesPerValue == 0 || bytesPerValue < 8) {
            return null;
        }
        double[] data = new double[value.length / bytesPerValue];
        for (int i = 0; i < data.length; i++) {
            byte[] s = new byte[bytesPerValue];
            for (int d = 0; d < s.length; d++) {
                s[d] = value[i * bytesPerValue + d];
            }
            data[i] = doubleFromBytes(s, byteOrder);
        }
        return data;
    }

    public static final char charFromBytes(byte[] value) {
        return charFromBytes(value, DEFAULT_BYTEORDER);
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

    public static final char[] charArrayFromBytes(byte[] value, int bytesPerValue) {
        return charArrayFromBytes(value, bytesPerValue, DEFAULT_BYTEORDER);
    }

    public static final char[] charArrayFromBytes(byte[] value, ByteOrder byteOrder) {
        return charArrayFromBytes(value, 2, byteOrder);
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

    public static final byte[] shortToBytes(short value, int bytes) {
        return shortToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] shortToBytes(short value, ByteOrder byteOrder) {
        return shortToBytes(value, 2, byteOrder);
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

    public static final byte[] shortArrayToBytes(short[] value) {
        return shortArrayToBytes(value, 2, DEFAULT_BYTEORDER);
    }

    public static final byte[] shortArrayToBytes(short[] value, int bytesPerShort) {
        return shortArrayToBytes(value, bytesPerShort, DEFAULT_BYTEORDER);
    }

    public static final byte[] shortArrayToBytes(short[] value, ByteOrder byteOrder) {
        return shortArrayToBytes(value, 2, byteOrder);
    }

    public static final byte[] shortArrayToBytes(short[] value, int bytes, ByteOrder byteOrder) {
        if (bytes < 2) {
            return null;
        }
        byte[] r = new byte[value.length * bytes];
        for (int i = 0; i < value.length; i++) {
            byte[] x = shortToBytes(value[i], bytes, byteOrder);
            for (int s = 0; s < bytes; s++) {
                r[i * bytes + s] = x[s];
            }
        }
        return r;
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

    public static final byte[] intArrayToBytes(int[] value) {
        return intArrayToBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final byte[] intArrayToBytes(int[] value, int bytesPerInt) {
        return intArrayToBytes(value, bytesPerInt, DEFAULT_BYTEORDER);
    }

    public static final byte[] intArrayToBytes(int[] value, ByteOrder byteOrder) {
        return intArrayToBytes(value, 4, byteOrder);
    }

    public static final byte[] intArrayToBytes(int[] value, int bytes, ByteOrder byteOrder) {
        if (bytes < 4) {
            return null;
        }
        byte[] r = new byte[value.length * bytes];
        for (int i = 0; i < value.length; i++) {
            byte[] x = intToBytes(value[i], bytes, byteOrder);
            for (int s = 0; s < bytes; s++) {
                r[i * bytes + s] = x[s];
            }
        }
        return r;
    }

    public static final byte[] longToBytes(long value) {
        return longToBytes(value, 8, DEFAULT_BYTEORDER);
    }

    public static final byte[] longToBytes(long value, int bytes) {
        return longToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] longToBytes(long value, ByteOrder byteOrder) {
        return longToBytes(value, 8, byteOrder);
    }

    public static final byte[] longToBytes(long value, int bytes, ByteOrder byteOrder) {
        int i;
        if (bytes < 8) return null;
        byte[] s = new byte[bytes];
        switch (byteOrder) {
            case BigEndian:
                s[0] = (byte) (int) (value >>> (bytes - 1) * 8);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (int) (value >> (bytes - i + 2) * 8 & 0xFFL);
                }
                s[bytes - 1] = (byte) (int) (value & 0xFFL);
                break;
            case LittleEndian:
                s[0] = (byte) (int) (value & 0xFFL);
                for (i = 0; i < bytes - 2; i++) {
                    s[i + 1] = (byte) (int) (value >> (i + 2) * 8 & 0xFFL);
                }
                s[bytes - 1] = (byte) (int) (value >>> bytes * 8);
                break;
        }
        return s;
    }

    public static final byte[] longArrayToBytes(long[] value) {
        return longArrayToBytes(value, 8, DEFAULT_BYTEORDER);
    }

    public static final byte[] longArrayToBytes(long[] value, int bytes) {
        return longArrayToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] longArrayToBytes(long[] value, ByteOrder byteOrder) {
        return longArrayToBytes(value, 8, byteOrder);
    }

    public static final byte[] longArrayToBytes(long[] value, int bytes, ByteOrder byteOrder) {
        if (bytes < 8) {
            return null;
        }
        byte[] r = new byte[value.length * bytes];
        for (int i = 0; i < value.length; i++) {
            byte[] x = longToBytes(value[i], bytes, byteOrder);
            for (int s = 0; s < bytes; s++) {
                r[i * bytes + s] = x[s];
            }
        }
        return r;
    }

    public static final byte[] floatToBytes(float value) {
        return floatToBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final byte[] floatToBytes(float value, int bytes) {
        return floatToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] floatToBytes(float value, ByteOrder byteOrder) {
        return floatToBytes(value, 4, byteOrder);
    }

    public static final byte[] floatToBytes(float value, int bytes, ByteOrder byteOrder) {
        return intToBytes(Float.floatToIntBits(value), bytes, byteOrder);
    }

    public static final byte[] floatArrayToBytes(float[] value) {
        return floatArrayToBytes(value, 4, DEFAULT_BYTEORDER);
    }

    public static final byte[] floatArrayToBytes(float[] value, int bytes) {
        return floatArrayToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] floatArrayToBytes(float[] value, ByteOrder byteOrder) {
        return floatArrayToBytes(value, 4, byteOrder);
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

    public static final byte[] doubleToBytes(double value) {
        return doubleToBytes(value, 8, DEFAULT_BYTEORDER);
    }

    public static final byte[] doubleToBytes(double value, int bytes) {
        return doubleToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] doubleToBytes(double value, ByteOrder byteOrder) {
        return doubleToBytes(value, 8, byteOrder);
    }

    public static final byte[] doubleToBytes(double value, int bytes, ByteOrder byteOrder) {
        return longToBytes(Double.doubleToRawLongBits(value), bytes, byteOrder);
    }

    public static final byte[] doubleArrayToBytes(double[] value) {
        return doubleArrayToBytes(value, 8, DEFAULT_BYTEORDER);
    }

    public static final byte[] doubleArrayToBytes(double[] value, int bytes) {
        return doubleArrayToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] doubleArrayToBytes(double[] value, ByteOrder byteOrder) {
        return doubleArrayToBytes(value, 8, byteOrder);
    }

    public static final byte[] doubleArrayToBytes(double[] value, int bytes, ByteOrder byteOrder) {
        if (bytes < 8) {
            return null;
        }
        byte[] r = new byte[value.length * bytes];
        for (int i = 0; i < value.length; i++) {
            byte[] x = longToBytes(Double.doubleToRawLongBits(value[i]), bytes, byteOrder);
            for (int s = 0; s < bytes; s++) {
                r[i * bytes + s] = x[s];
            }
        }
        return r;
    }

    public static final byte[] charToBytes(char value) {
        return charToBytes(value, 2, DEFAULT_BYTEORDER);
    }

    public static final byte[] charToBytes(char value, int bytes) {
        return charToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] charToBytes(char value, ByteOrder byteOrder) {
        return charToBytes(value, 2, byteOrder);
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

    public static final byte[] charArrayToBytes(char[] value, int bytes) {
        return charArrayToBytes(value, bytes, DEFAULT_BYTEORDER);
    }

    public static final byte[] charArrayToBytes(char[] value, ByteOrder byteOrder) {
        return charArrayToBytes(value, 2, byteOrder);
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
