package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityCombustByBlockEvent extends CraftEntityCombustEvent implements EntityCombustByBlockEvent {

    private final @Nullable Block combuster;

    public CraftEntityCombustByBlockEvent(final @Nullable Block combuster, final Entity combustee, final float duration) {
        super(combustee, duration);
        this.combuster = combuster;
    }

    public CraftEntityCombustByBlockEvent(
        final Level level, final @Nullable BlockPos combusterPos, final net.minecraft.world.entity.Entity combustee, final float duration
    ) {
        this(Optionull.map(combusterPos, pos -> CraftBlock.at(level, pos)), combustee.getBukkitEntity(), duration);
    }

    @Override
    public @Nullable Block getCombuster() {
        return this.combuster;
    }
}
