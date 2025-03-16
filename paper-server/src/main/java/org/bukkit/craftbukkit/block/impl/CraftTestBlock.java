/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTestBlock extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TestBlock {

    public CraftTestBlock() {
        super();
    }

    public CraftTestBlock(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTestBlock

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> MODE = getEnum(net.minecraft.world.level.block.TestBlock.class, "mode");

    @Override
    public Mode getMode() {
        return this.get(MODE, org.bukkit.block.data.type.TestBlock.Mode.class);
    }

    @Override
    public void setMode(final Mode mode) {
        this.set(MODE, mode);
    }
}
