package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.BubbleColumn;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftBubbleColumn extends CraftBlockData implements BubbleColumn {
    private static final BooleanProperty DRAG_DOWN = BubbleColumnBlock.DRAG_DOWN;

    public CraftBubbleColumn(BlockState state) {
        super(state);
    }

    @Override
    public boolean isDrag() {
        return this.get(DRAG_DOWN);
    }

    @Override
    public void setDrag(final boolean drag) {
        this.set(DRAG_DOWN, drag);
    }
}
