package com.destroystokyo.paper.antixray;

public final class DataBitsWriter {

    private byte[] dataBits;
    private int bitsPerObject;
    private long mask;
    private int longInDataBitsIndex;
    private int bitInLongIndex;
    private long current;
    private boolean dirty;

    public void setDataBits(byte[] dataBits) {
        this.dataBits = dataBits;
    }

    public void setBitsPerObject(int bitsPerObject) {
        this.bitsPerObject = bitsPerObject;
        mask = (1 << bitsPerObject) - 1;
    }

    public void setIndex(int index) {
        this.longInDataBitsIndex = index;
        bitInLongIndex = 0;
        init();
    }

    private void init() {
        if (dataBits.length > longInDataBitsIndex + 7) {
            current = ((((long) dataBits[longInDataBitsIndex]) << 56)
                    | (((long) dataBits[longInDataBitsIndex + 1] & 0xff) << 48)
                    | (((long) dataBits[longInDataBitsIndex + 2] & 0xff) << 40)
                    | (((long) dataBits[longInDataBitsIndex + 3] & 0xff) << 32)
                    | (((long) dataBits[longInDataBitsIndex + 4] & 0xff) << 24)
                    | (((long) dataBits[longInDataBitsIndex + 5] & 0xff) << 16)
                    | (((long) dataBits[longInDataBitsIndex + 6] & 0xff) << 8)
                    | (((long) dataBits[longInDataBitsIndex + 7] & 0xff)));
        }

        dirty = false;
    }

    public void finish() {
        if (dirty && dataBits.length > longInDataBitsIndex + 7) {
            dataBits[longInDataBitsIndex] = (byte) (current >> 56 & 0xff);
            dataBits[longInDataBitsIndex + 1] = (byte) (current >> 48 & 0xff);
            dataBits[longInDataBitsIndex + 2] = (byte) (current >> 40 & 0xff);
            dataBits[longInDataBitsIndex + 3] = (byte) (current >> 32 & 0xff);
            dataBits[longInDataBitsIndex + 4] = (byte) (current >> 24 & 0xff);
            dataBits[longInDataBitsIndex + 5] = (byte) (current >> 16 & 0xff);
            dataBits[longInDataBitsIndex + 6] = (byte) (current >> 8 & 0xff);
            dataBits[longInDataBitsIndex + 7] = (byte) (current & 0xff);
        }
    }

    public void write(int value) {
        if (bitInLongIndex + bitsPerObject > 64) {
            finish();
            bitInLongIndex = 0;
            longInDataBitsIndex += 8;
            init();
        }

        current = current & ~(mask << bitInLongIndex) | (value & mask) << bitInLongIndex;
        dirty = true;
        bitInLongIndex += bitsPerObject;
    }

    public void skip() {
        bitInLongIndex += bitsPerObject;

        if (bitInLongIndex > 64) {
            finish();
            bitInLongIndex = bitsPerObject;
            longInDataBitsIndex += 8;
            init();
        }
    }
}
