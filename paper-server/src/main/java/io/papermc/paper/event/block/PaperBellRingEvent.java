package io.papermc.paper.event.block;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBellRingEvent;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.Nullable;

public class PaperBellRingEvent extends CraftBellRingEvent implements BellRingEvent {

    public PaperBellRingEvent(final Block block, final BlockFace face, final @Nullable Entity entity) {
        super(block, face, entity);
    }

    public PaperBellRingEvent(final Level level, final BlockPos pos, final Direction face, final net.minecraft.world.entity.@Nullable Entity entity) {
        this(
            CraftBlock.at(level, pos),
            CraftBlock.notchToBlockFace(face),
            Optionull.map(entity, net.minecraft.world.entity.Entity::getBukkitEntity)
        );
    }
}
