package io.papermc.paper.antixray;

public final class BitStorageWriter {

    private byte[] buffer;
    private int bits;
    private long mask;
    private int longInBufferIndex;
    private int bitInLongIndex;
    private long current;
    private boolean dirty;

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void setBits(int bits) {
        this.bits = bits;
        mask = (1L << bits) - 1;
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

        dirty = false;
    }

    public void flush() {
        if (dirty && buffer.length > longInBufferIndex + 7) {
            buffer[longInBufferIndex] = (byte) (current >> 56 & 0xff);
            buffer[longInBufferIndex + 1] = (byte) (current >> 48 & 0xff);
            buffer[longInBufferIndex + 2] = (byte) (current >> 40 & 0xff);
            buffer[longInBufferIndex + 3] = (byte) (current >> 32 & 0xff);
            buffer[longInBufferIndex + 4] = (byte) (current >> 24 & 0xff);
            buffer[longInBufferIndex + 5] = (byte) (current >> 16 & 0xff);
            buffer[longInBufferIndex + 6] = (byte) (current >> 8 & 0xff);
            buffer[longInBufferIndex + 7] = (byte) (current & 0xff);
        }
    }

    public void write(int value) {
        if (bitInLongIndex + bits > 64) {
            flush();
            bitInLongIndex = 0;
            longInBufferIndex += 8;
            init();
        }

        current = current & ~(mask << bitInLongIndex) | (value & mask) << bitInLongIndex;
        dirty = true;
        bitInLongIndex += bits;
    }

    public void skip() {
        bitInLongIndex += bits;

        if (bitInLongIndex > 64) {
            flush();
            bitInLongIndex = bits;
            longInBufferIndex += 8;
            init();
        }
    }
}
