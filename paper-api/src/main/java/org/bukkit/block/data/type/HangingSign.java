package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.block.data.Attachable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.ApiStatus;

@MinecraftExperimental
@ApiStatus.Experimental
public interface HangingSign extends Attachable, Rotatable, Waterlogged {
}
