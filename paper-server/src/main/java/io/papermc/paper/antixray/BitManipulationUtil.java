package io.papermc.paper.antixray;

/**
 * Utility class for common bit manipulation operations used by BitStorageReader and BitStorageWriter.
 * Extracts shared logic to improve maintainability and reduce code duplication.
 */
public final class BitManipulationUtil {

    private BitManipulationUtil() {
        // Utility class, prevent instantiation
    }

    /**
     * Reads a long value from a byte array at the specified index.
     *
     * @param buffer The byte array to read from
     * @param longInBufferIndex The index to start reading from
     * @return The long value read from the buffer, or 0 if the buffer is too short
     */
    public static long readLongFromBuffer(byte[] buffer, int longInBufferIndex) {
        if (buffer.length <= longInBufferIndex + 7) {
            return 0L;
        }

        return ((((long) buffer[longInBufferIndex]) << 56)
            | (((long) buffer[longInBufferIndex + 1] & 0xff) << 48)
            | (((long) buffer[longInBufferIndex + 2] & 0xff) << 40)
            | (((long) buffer[longInBufferIndex + 3] & 0xff) << 32)
            | (((long) buffer[longInBufferIndex + 4] & 0xff) << 24)
            | (((long) buffer[longInBufferIndex + 5] & 0xff) << 16)
            | (((long) buffer[longInBufferIndex + 6] & 0xff) << 8)
            | (((long) buffer[longInBufferIndex + 7] & 0xff)));
    }

    /**
     * Writes a long value to a byte array at the specified index.
     *
     * @param buffer The byte array to write to
     * @param longInBufferIndex The index to start writing at
     * @param value The long value to write
     * @return True if the write was successful, false if the buffer is too short
     */
    public static boolean writeLongToBuffer(byte[] buffer, int longInBufferIndex, long value) {
        if (buffer.length <= longInBufferIndex + 7) {
            return false;
        }

        buffer[longInBufferIndex] = (byte) (value >> 56 & 0xff);
        buffer[longInBufferIndex + 1] = (byte) (value >> 48 & 0xff);
        buffer[longInBufferIndex + 2] = (byte) (value >> 40 & 0xff);
        buffer[longInBufferIndex + 3] = (byte) (value >> 32 & 0xff);
        buffer[longInBufferIndex + 4] = (byte) (value >> 24 & 0xff);
        buffer[longInBufferIndex + 5] = (byte) (value >> 16 & 0xff);
        buffer[longInBufferIndex + 6] = (byte) (value >> 8 & 0xff);
        buffer[longInBufferIndex + 7] = (byte) (value & 0xff);

        return true;
    }

    /**
     * Creates a bit mask for the specified number of bits.
     *
     * @param bits The number of bits in the mask
     * @return A mask with the specified number of least significant bits set to 1
     */
    public static long createBitMask(int bits) {
        return (1L << bits) - 1L;
    }
}
