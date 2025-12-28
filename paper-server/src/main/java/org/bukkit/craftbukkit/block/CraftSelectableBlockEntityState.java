package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.SelectableSlotContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.util.Vector;

public abstract class CraftSelectableBlockEntityState<T extends BlockEntity> extends CraftBlockEntityState<T> {

    public CraftSelectableBlockEntityState(World world, T blockEntity) {
        super(world, blockEntity);
    }

    protected CraftSelectableBlockEntityState(CraftBlockEntityState<T> state, Location location) {
        super(state, location);
    }

    public int getSlot(final int rows, final int columns, final Vector clickVector) {
        if (this.getBlockData() instanceof Directional directional) {
            final BlockFace facing = directional.getFacing();

            Vec2 faceVector;
            switch (facing) {
                case NORTH:
                    faceVector = new Vec2((float) (1.0f - clickVector.getX()), (float) clickVector.getY());
                    break;
                case SOUTH:
                    faceVector = new Vec2((float) clickVector.getX(), (float) clickVector.getY());
                    break;
                case WEST:
                    faceVector = new Vec2((float) clickVector.getZ(), (float) clickVector.getY());
                    break;
                case EAST:
                    faceVector = new Vec2((float) (1f - clickVector.getZ()), (float) clickVector.getY());
                    break;
                case DOWN:
                case UP:
                default:
                    return -1;
            }

            return CraftSelectableBlockEntityState.getHitSlot(rows, columns, faceVector);
        }

        return -1;
    }

    static int getHitSlot(final int rows, final int columns, final Vec2 vec2) {
        final int row = SelectableSlotContainer.getSection(1.0F - vec2.y, rows);
        final int column = SelectableSlotContainer.getSection(vec2.x, columns);
        return column + row * columns;
    }

}
