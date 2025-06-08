package org.bukkit.craftbukkit.util;

import net.minecraft.world.level.block.state.BlockState;

public record CapturedBlock(BlockState state, int flags) {
}
