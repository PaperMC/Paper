package net.minecraft.server;

public class TileEntitySign extends TileEntity {

    public String[] lines = new String[] { "", "", "", ""};
    public int b = -1;
    public boolean isEditable = true; // CraftBukkit - priv to public

    public TileEntitySign() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("Text1", this.lines[0]);
        nbttagcompound.setString("Text2", this.lines[1]);
        nbttagcompound.setString("Text3", this.lines[2]);
        nbttagcompound.setString("Text4", this.lines[3]);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.isEditable = false;
        super.a(nbttagcompound);

        for (int i = 0; i < 4; ++i) {
            this.lines[i] = nbttagcompound.getString("Text" + (i + 1));
            if (this.lines[i].length() > 15) {
                this.lines[i] = this.lines[i].substring(0, 15);
            }
        }
    }

    public Packet k() {
        String[] astring = new String[4];

        for (int i = 0; i < 4; ++i) {
            astring[i] = this.lines[i];

            // CraftBukkit start - limit sign text to 15 chars per line
            if (this.lines[i].length() > 15) {
                astring[i] = this.lines[i].substring(0, 15);
            }
            // CraftBukkit end
        }

        return new Packet130UpdateSign(this.x, this.y, this.z, astring);
    }

    public boolean c() {
        return this.isEditable;
    }
}
