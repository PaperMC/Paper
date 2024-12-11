/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBubbleColumn extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.BubbleColumn {

    public CraftBubbleColumn() {
        super();
    }

    public CraftBubbleColumn(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftBubbleColumn

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty DRAG = getBoolean(net.minecraft.world.level.block.BubbleColumnBlock.class, "drag");

    @Override
    public boolean isDrag() {
        return this.get(CraftBubbleColumn.DRAG);
    }

    @Override
    public void setDrag(boolean drag) {
        this.set(CraftBubbleColumn.DRAG, drag);
    }
}
