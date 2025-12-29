package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SelectableSlotContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec2;
import org.bukkit.util.Vector;

public interface CraftSelectable<T extends BaseEntityBlock & SelectableSlotContainer> {

    BlockEntity getBlockEntity();

    BlockState getHandle();

    @SuppressWarnings("unchecked")
    default int getSlot(final Vector clickVector) {
        Preconditions.checkArgument(clickVector != null, "clickVector cannot be null");
        clickVector.checkFinite();
        Preconditions.checkState(this.getBlockEntity().isValidBlockState(this.getHandle()), "Block data is no longer valid to call this method!");

        final Direction facing = this.getHandle().getValue(BlockStateProperties.HORIZONTAL_FACING);
        final Vec2 hitCoords = switch (facing) {
            case NORTH -> new Vec2((float) (1.0F - clickVector.getX()), (float) clickVector.getY());
            case SOUTH -> new Vec2((float) clickVector.getX(), (float) clickVector.getY());
            case WEST -> new Vec2((float) clickVector.getZ(), (float) clickVector.getY());
            case EAST -> new Vec2((float) (1.0F - clickVector.getZ()), (float) clickVector.getY());
            default -> throw new UnsupportedOperationException(); // should never happen
        };

        return this.getHitSlot((T) this.getHandle().getBlock(), hitCoords);
    }

    private int getHitSlot(final T block, final Vec2 hitCoords) {
        final int row = SelectableSlotContainer.getSection(1.0F - hitCoords.y, block.getRows());
        final int column = SelectableSlotContainer.getSection(hitCoords.x, block.getColumns());
        return column + row * block.getColumns();
    }
}
