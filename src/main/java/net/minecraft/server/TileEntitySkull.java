package net.minecraft.server;

public class TileEntitySkull extends TileEntity {

    private int a;
    private int b;
    private String c = "";

    public TileEntitySkull() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setByte("SkullType", (byte) (this.a & 255));
        nbttagcompound.setByte("Rot", (byte) (this.b & 255));
        nbttagcompound.setString("ExtraType", this.c);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.getByte("SkullType");
        this.b = nbttagcompound.getByte("Rot");
        if (nbttagcompound.hasKey("ExtraType")) {
            this.c = nbttagcompound.getString("ExtraType");
        }
    }

    public Packet getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new Packet132TileEntityData(this.x, this.y, this.z, 4, nbttagcompound);
    }

    public void setSkullType(int i, String s) {
        this.a = i;
        this.c = s;
    }

    public int getSkullType() {
        return this.a;
    }

    public void setRotation(int i) {
        this.b = i;
    }

    // CraftBukkit start
    public int getRotation() {
        return this.b;
    }
    // CraftBukkit end

    public String getExtraType() {
        return this.c;
    }
}
