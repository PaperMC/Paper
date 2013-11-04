package net.minecraft.server;

// CraftBukkit - package-private -> public
public class TileEntityCommandListener extends CommandBlockListenerAbstract {

    final TileEntityCommand a;

    TileEntityCommandListener(TileEntityCommand tileentitycommand) {
        this.a = tileentitycommand;
        sender = new org.bukkit.craftbukkit.command.CraftBlockCommandSender(this); // CraftBukkit - add sender
    }

    public ChunkCoordinates getChunkCoordinates() {
        return new ChunkCoordinates(this.a.x, this.a.y, this.a.z);
    }

    public World getWorld() {
        return this.a.getWorld();
    }

    public void a(String s) {
        super.a(s);
        this.a.update();
    }

    public void e() {
        this.a.getWorld().notify(this.a.x, this.a.y, this.a.z);
    }
}
