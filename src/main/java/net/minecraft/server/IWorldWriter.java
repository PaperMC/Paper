package net.minecraft.server;

import javax.annotation.Nullable;

public interface IWorldWriter {

    boolean a(BlockPosition blockposition, IBlockData iblockdata, int i, int j);

    default boolean setTypeAndData(BlockPosition blockposition, IBlockData iblockdata, int i) {
        return this.a(blockposition, iblockdata, i, 512);
    }

    boolean a(BlockPosition blockposition, boolean flag);

    default boolean b(BlockPosition blockposition, boolean flag) {
        return this.a(blockposition, flag, (Entity) null);
    }

    default boolean a(BlockPosition blockposition, boolean flag, @Nullable Entity entity) {
        return this.a(blockposition, flag, entity, 512);
    }

    boolean a(BlockPosition blockposition, boolean flag, @Nullable Entity entity, int i);

    default boolean addEntity(Entity entity) {
        return false;
    }

    // CraftBukkit start
    default boolean addEntity(Entity entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason reason) {
        return false;
    }
    // CraftBukkit end
}
