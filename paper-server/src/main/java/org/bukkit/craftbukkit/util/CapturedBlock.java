package org.bukkit.craftbukkit.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record CapturedBlock(BlockState state, int flags, @Nullable BlockEntity blockEntity) {
}
