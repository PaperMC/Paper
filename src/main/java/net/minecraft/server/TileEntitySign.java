package net.minecraft.server;

public class TileEntitySign extends TileEntity {

    public String[] a = new String[] { "", "", "", ""};
    public int b = -1;
    public boolean fresh = true; // Craftbukkit

    public TileEntitySign() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Text1", this.a[0]);
        nbttagcompound.a("Text2", this.a[1]);
        nbttagcompound.a("Text3", this.a[2]);
        nbttagcompound.a("Text4", this.a[3]);
    }

    public void a(NBTTagCompound nbttagcompound) {
        fresh = false; // Craftbukkit
        super.a(nbttagcompound);

        for (int i = 0; i < 4; ++i) {
            this.a[i] = nbttagcompound.i("Text" + (i + 1));
            if (this.a[i].length() > 15) {
                this.a[i] = this.a[i].substring(0, 15);
            }
        }
    }

    public Packet e() {
        String[] astring = new String[4];

        for (int i = 0; i < 4; ++i) {
            astring[i] = this.a[i];
        }

        return new Packet130UpdateSign(this.e, this.f, this.g, astring);
    }
}
