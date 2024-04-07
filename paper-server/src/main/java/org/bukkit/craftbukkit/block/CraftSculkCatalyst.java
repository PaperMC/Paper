package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.SculkCatalyst;

public class CraftSculkCatalyst extends CraftBlockEntityState<SculkCatalystBlockEntity> implements SculkCatalyst {

    public CraftSculkCatalyst(World world, SculkCatalystBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftSculkCatalyst(CraftSculkCatalyst state, Location location) {
        super(state, location);
    }

    @Override
    public void bloom(Block block, int charge) {
        Preconditions.checkArgument(block != null, "block cannot be null");
        Preconditions.checkArgument(charge > 0, "charge must be positive");
        requirePlaced();

        // bloom() is for visual blooming effect, cursors are what changes the blocks.
        getTileEntity().getListener().bloom(world.getHandle(), getPosition(), getHandle(), world.getHandle().getRandom());
        getTileEntity().getListener().getSculkSpreader().addCursors(new BlockPosition(block.getX(), block.getY(), block.getZ()), charge);
    }

    @Override
    public CraftSculkCatalyst copy() {
        return new CraftSculkCatalyst(this, null);
    }

    @Override
    public CraftSculkCatalyst copy(Location location) {
        return new CraftSculkCatalyst(this, location);
    }
}
