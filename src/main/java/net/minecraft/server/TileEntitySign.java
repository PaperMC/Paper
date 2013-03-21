package net.minecraft.server;

public class TileEntitySign extends TileEntity {

    public String[] lines = new String[] { "", "", "", ""};
    public int i = -1;
    public boolean isEditable = true; // CraftBukkit - private -> public
    private EntityHuman k;

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

    public Packet getUpdatePacket() {
        String[] astring = sanitizeLines(this.lines); // CraftBukkit - call sign line sanitizer to limit line length

        return new PacketPlayOutUpdateSign(this.x, this.y, this.z, astring);
    }

    public boolean a() {
        return this.isEditable;
    }

    public void a(EntityHuman entityhuman) {
        this.k = entityhuman;
    }

    public EntityHuman b() {
        return this.k;
    }

    // CraftBukkit start - central method to limit sign text to 15 chars per line
    public static String[] sanitizeLines(String[] lines) {
        String[] astring = new String[4];
        for (int i = 0; i < 4; ++i) {
            astring[i] = lines[i];

            if (lines[i].length() > 15) {
                astring[i] = lines[i].substring(0, 15);
            }
        }
        return astring;
    }
    // CraftBukkit end
}
