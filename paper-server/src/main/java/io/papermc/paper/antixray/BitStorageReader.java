package io.papermc.paper.antixray;

public final class BitStorageReader {

    private byte[] buffer;
    private int bits;
    private long mask; // Changed from int to long to match the utility's return type
    private int longInBufferIndex;
    private int bitInLongIndex;
    private long current;

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
    }

    public int read() {
        if (bitInLongIndex + bits > 64) {
            bitInLongIndex = 0;
            longInBufferIndex += 8;
            init();
        }

        int value = (int) (current >>> bitInLongIndex) & (int)mask;
        bitInLongIndex += bits;
        return value;
    }
}
