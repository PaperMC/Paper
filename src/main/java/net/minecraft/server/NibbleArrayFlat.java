package net.minecraft.server;

public class NibbleArrayFlat extends NibbleArray {

    public NibbleArrayFlat() {
        super(128);
    }

    public NibbleArrayFlat(NibbleArray nibblearray, int i) {
        super(128);
        System.arraycopy(nibblearray.getIfSet(), i * 128, this.a, 0, 128); // Paper
    }

    @Override
    protected int b(int i, int j, int k) {
        return k << 4 | i;
    }

    @Override
    public byte[] asBytes() {
        byte[] abyte = BYTE_2048.acquire(); // Paper

        for (int i = 0; i < 16; ++i) {
            System.arraycopy(this.a, 0, abyte, i * 128, 128);
        }

        return abyte;
    }
}
