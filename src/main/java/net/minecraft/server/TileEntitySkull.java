package net.minecraft.server;

public class TileEntitySkull extends TileEntity {

    private int a;
    private int i;
    private String j = "";

    public TileEntitySkull() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setByte("SkullType", (byte) (this.a & 255));
        nbttagcompound.setByte("Rot", (byte) (this.i & 255));
        nbttagcompound.setString("ExtraType", this.j);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.getByte("SkullType");
        this.i = nbttagcompound.getByte("Rot");
        if (nbttagcompound.hasKeyOfType("ExtraType", 8)) {
            this.j = nbttagcompound.getString("ExtraType");
        }
    }

    public Packet getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new PacketPlayOutTileEntityData(this.x, this.y, this.z, 4, nbttagcompound);
    }

    public void setSkullType(int i, String s) {
        this.a = i;
        this.j = s;
    }

    public int getSkullType() {
        return this.a;
    }

    public void setRotation(int i) {
        this.i = i;
    }

    // CraftBukkit start
    public int getRotation() {
        return this.i;
    }
    // CraftBukkit end

    public String getExtraType() {
        return this.j;
    }
}
