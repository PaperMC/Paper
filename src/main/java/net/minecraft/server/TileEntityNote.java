package net.minecraft.server;

public class TileEntityNote extends TileEntity {

    public byte note;
    public boolean b;

    public TileEntityNote() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setByte("note", this.note);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.note = nbttagcompound.getByte("note");
        if (this.note < 0) {
            this.note = 0;
        }

        if (this.note > 24) {
            this.note = 24;
        }
    }

    public void a() {
        this.note = (byte) ((this.note + 1) % 25);
        this.update();
    }

    public void play(World world, int i, int j, int k) {
        if (world.getMaterial(i, j + 1, k) == Material.AIR) {
            Material material = world.getMaterial(i, j - 1, k);
            byte b0 = 0;

            if (material == Material.STONE) {
                b0 = 1;
            }

            if (material == Material.SAND) {
                b0 = 2;
            }

            if (material == Material.SHATTERABLE) {
                b0 = 3;
            }

            if (material == Material.WOOD) {
                b0 = 4;
            }

            // CraftBukkit start
            org.bukkit.event.block.NotePlayEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callNotePlayEvent(this.world, i, j, k, b0, this.note);
            if (!event.isCancelled()) {
                this.world.playNote(i, j, k, Block.NOTE_BLOCK.id, event.getInstrument().getType(), event.getNote().getId());
            }
            // CraftBukkit end
        }
    }
}
