package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.SideChaining;
import org.bukkit.block.data.Waterlogged;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Shelf extends Directional, SideChaining, Waterlogged, Powerable {
}
