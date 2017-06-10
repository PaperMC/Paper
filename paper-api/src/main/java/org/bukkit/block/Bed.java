package org.bukkit.block;

import org.bukkit.material.Colorable;

/**
 * Represents a captured state of a bed.
 */
// Paper start
// @Deprecated(since = "1.13")
public interface Bed extends TileState, Colorable {

    @Override
    @org.jetbrains.annotations.NotNull org.bukkit.DyeColor getColor();

    /**
     * <b>Unsupported</b>
     *
     * @throws UnsupportedOperationException not supported, set the block type
     */
    @Override
    @org.jetbrains.annotations.Contract("_ -> fail")
    @Deprecated(forRemoval = true)
    void setColor(@org.bukkit.UndefinedNullability("not supported") org.bukkit.DyeColor color);
// Paper end
}
