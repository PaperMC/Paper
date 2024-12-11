/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftJigsaw extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Jigsaw {

    public CraftJigsaw() {
        super();
    }

    public CraftJigsaw(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftJigsaw

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ORIENTATION = getEnum(net.minecraft.world.level.block.JigsawBlock.class, "orientation");

    @Override
    public org.bukkit.block.data.type.Jigsaw.Orientation getOrientation() {
        return this.get(CraftJigsaw.ORIENTATION, org.bukkit.block.data.type.Jigsaw.Orientation.class);
    }

    @Override
    public void setOrientation(org.bukkit.block.data.type.Jigsaw.Orientation orientation) {
        this.set(CraftJigsaw.ORIENTATION, orientation);
    }
}
