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
        mask = BitManipulationUtil.createBitMask(bits);
    }

    public void setIndex(int index) {
        longInBufferIndex = index;
        bitInLongIndex = 0;
        init();
    }

    private void init() {
        current = BitManipulationUtil.readLongFromBuffer(buffer, longInBufferIndex);
        dirty = false;
    }

    public void flush() {
        if (dirty) {
            BitManipulationUtil.writeLongToBuffer(buffer, longInBufferIndex, current);
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
