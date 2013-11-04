package net.minecraft.server;

// CraftBukkit - package-private -> public
public class EntityMinecartCommandBlockListener extends CommandBlockListenerAbstract {

    final EntityMinecartCommandBlock a;

    EntityMinecartCommandBlockListener(EntityMinecartCommandBlock entityminecartcommandblock) {
        this.a = entityminecartcommandblock;
        this.sender = (org.bukkit.craftbukkit.entity.CraftMinecartCommand) entityminecartcommandblock.getBukkitEntity(); // CraftBukkit - Set the sender
    }

    public void e() {
        this.a.getDataWatcher().watch(23, this.i());
        this.a.getDataWatcher().watch(24, ChatSerializer.a(this.h()));
    }

    public ChunkCoordinates getChunkCoordinates() {
        return new ChunkCoordinates(MathHelper.floor(this.a.locX), MathHelper.floor(this.a.locY + 0.5D), MathHelper.floor(this.a.locZ));
    }

    public World getWorld() {
        return this.a.world;
    }
}
