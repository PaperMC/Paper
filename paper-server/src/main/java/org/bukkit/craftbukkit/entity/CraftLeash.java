package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.decoration.EntityLeash;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;

public class CraftLeash extends CraftHanging implements LeashHitch {
    public CraftLeash(CraftServer server, EntityLeash entity) {
        super(server, entity);
    }

    @Override
    public boolean setFacingDirection(BlockFace face, boolean force) {
        Preconditions.checkArgument(face == BlockFace.SELF, "%s is not a valid facing direction", face);

        return force || getHandle().generation || getHandle().survives();
    }

    @Override
    public BlockFace getFacing() {
        // Leash hitch has no facing direction, so we return self
        return BlockFace.SELF;
    }

    @Override
    public EntityLeash getHandle() {
        return (EntityLeash) entity;
    }

    @Override
    public String toString() {
        return "CraftLeash";
    }

    @Override
    public EntityType getType() {
        return EntityType.LEASH_HITCH;
    }
}
