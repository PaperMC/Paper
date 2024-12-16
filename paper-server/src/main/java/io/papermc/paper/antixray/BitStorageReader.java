package io.papermc.paper.antixray;

public final class BitStorageReader {

    private byte[] buffer;
    private int bits;
    private int mask;
    private int longInBufferIndex;
    private int bitInLongIndex;
    private long current;

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void setBits(int bits) {
        this.bits = bits;
        mask = (1 << bits) - 1;
    }

    public void setIndex(int index) {
        longInBufferIndex = index;
        bitInLongIndex = 0;
        init();
    }

    private void init() {
        if (buffer.length > longInBufferIndex + 7) {
            current = ((((long) buffer[longInBufferIndex]) << 56)
                | (((long) buffer[longInBufferIndex + 1] & 0xff) << 48)
                | (((long) buffer[longInBufferIndex + 2] & 0xff) << 40)
                | (((long) buffer[longInBufferIndex + 3] & 0xff) << 32)
                | (((long) buffer[longInBufferIndex + 4] & 0xff) << 24)
                | (((long) buffer[longInBufferIndex + 5] & 0xff) << 16)
                | (((long) buffer[longInBufferIndex + 6] & 0xff) << 8)
                | (((long) buffer[longInBufferIndex + 7] & 0xff)));
        }
    }

    public int read() {
        if (bitInLongIndex + bits > 64) {
            bitInLongIndex = 0;
            longInBufferIndex += 8;
            init();
        }

        int value = (int) (current >>> bitInLongIndex) & mask;
        bitInLongIndex += bits;
        return value;
    }
}
