package org.bukkit.util;

/**
 * Utils for casting number types to other number types
 */
public final class NumberConversions {
    private NumberConversions() {}

    public static int toInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        } else {
            int result = 0;

            try {
                result = Integer.parseInt(object.toString());
            } catch (NumberFormatException ex) {}

            return result;
        }
    }

    public static float toFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        } else {
            float result = 0;

            try {
                result = Float.parseFloat(object.toString());
            } catch (NumberFormatException ex) {}

            return result;
        }
    }

    public static double toDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        } else {
            double result = 0;

            try {
                result = Double.parseDouble(object.toString());
            } catch (NumberFormatException ex) {}

            return result;
        }
    }

    public static long toLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        } else {
            long result = 0;

            try {
                result = Long.parseLong(object.toString());
            } catch (NumberFormatException ex) {}

            return result;
        }
    }

    public static short toShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        } else {
            short result = 0;

            try {
                result = Short.parseShort(object.toString());
            } catch (NumberFormatException ex) {}

            return result;
        }
    }

    public static byte toByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        } else {
            byte result = 0;

            try {
                result = Byte.parseByte(object.toString());
            } catch (NumberFormatException ex) {}

            return result;
        }
    }
}
