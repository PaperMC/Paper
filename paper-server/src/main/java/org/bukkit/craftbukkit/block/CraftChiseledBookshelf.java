package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.SelectableSlotContainer;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.phys.Vec2;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.inventory.CraftInventoryChiseledBookshelf;
import org.bukkit.inventory.ChiseledBookshelfInventory;
import org.bukkit.util.Vector;

public class CraftChiseledBookshelf extends CraftBlockEntityState<ChiseledBookShelfBlockEntity> implements ChiseledBookshelf {

    public CraftChiseledBookshelf(World world, ChiseledBookShelfBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftChiseledBookshelf(CraftChiseledBookshelf state, Location location) {
        super(state, location);
    }

    @Override
    public int getLastInteractedSlot() {
        return this.getSnapshot().getLastInteractedSlot();
    }

    @Override
    public void setLastInteractedSlot(int lastInteractedSlot) {
        this.getSnapshot().lastInteractedSlot = lastInteractedSlot;
    }

    @Override
    public ChiseledBookshelfInventory getSnapshotInventory() {
        return new CraftInventoryChiseledBookshelf(this.getSnapshot());
    }

    @Override
    public ChiseledBookshelfInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryChiseledBookshelf(this.getBlockEntity());
    }

    @Override
    public int getSlot(Vector clickVector) {
        BlockFace facing = ((Directional) this.getBlockData()).getFacing();

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

        return CraftChiseledBookshelf.getHitSlot(faceVector);
    }

    private static int getHitSlot(Vec2 vec2) {
        final int rows = 2;
        final int columns = 3;
        final int row = SelectableSlotContainer.getSection(1.0F - vec2.y, rows);
        final int column = SelectableSlotContainer.getSection(vec2.x, columns);
        return column + row * columns;
    }

    @Override
    public CraftChiseledBookshelf copy() {
        return new CraftChiseledBookshelf(this, null);
    }

    @Override
    public CraftChiseledBookshelf copy(Location location) {
        return new CraftChiseledBookshelf(this, location);
    }
}
