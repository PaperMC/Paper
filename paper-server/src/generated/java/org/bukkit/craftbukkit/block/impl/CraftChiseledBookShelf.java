package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.papermc.paper.annotation.GeneratedClass;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.ChiseledBookshelf;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftChiseledBookShelf extends CraftBlockData implements ChiseledBookshelf {
    private static final EnumProperty<Direction> FACING = ChiseledBookShelfBlock.FACING;

    private static final List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES = ChiseledBookShelfBlock.SLOT_OCCUPIED_PROPERTIES;

    public CraftChiseledBookShelf(BlockState state) {
        super(state);
    }

    @Override
    public BlockFace getFacing() {
        return this.get(FACING, BlockFace.class);
    }

    @Override
    public void setFacing(final BlockFace blockFace) {
        Preconditions.checkArgument(blockFace != null, "blockFace cannot be null!");
        Preconditions.checkArgument(blockFace.isCartesian() && blockFace.getModY() == 0, "Invalid face, only cartesian horizontal face are allowed for this property!");
        this.set(FACING, blockFace);
    }

    @Override
    public Set<BlockFace> getFaces() {
        return this.getValues(FACING, BlockFace.class);
    }

    @Override
    public boolean isSlotOccupied(final int index) {
        return this.get(SLOT_OCCUPIED_PROPERTIES.get(index));
    }

    @Override
    public void setSlotOccupied(final int index, final boolean slotOccupied) {
        this.set(SLOT_OCCUPIED_PROPERTIES.get(index), slotOccupied);
    }

    @Override
    public Set<Integer> getOccupiedSlots() {
        ImmutableSet.Builder<Integer> slotOccupieds = ImmutableSet.builder();
        for (int index = 0, size = SLOT_OCCUPIED_PROPERTIES.size(); index < size; index++) {
            if (this.get(SLOT_OCCUPIED_PROPERTIES.get(index))) {
                slotOccupieds.add(index);
            }
        }
        return slotOccupieds.build();
    }

    @Override
    public int getMaximumOccupiedSlots() {
        return SLOT_OCCUPIED_PROPERTIES.size();
    }
}
