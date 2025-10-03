package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SculkCatalystBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.SculkCatalyst;

public class CraftSculkCatalyst extends CraftBlockEntityState<SculkCatalystBlockEntity> implements SculkCatalyst {

    public CraftSculkCatalyst(World world, SculkCatalystBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftSculkCatalyst(CraftSculkCatalyst state, Location location) {
        super(state, location);
    }

    @Override
    public void bloom(Block block, int charge) {
        Preconditions.checkArgument(block != null, "block cannot be null");
        Preconditions.checkArgument(charge > 0, "charge must be positive");
        this.requirePlaced();

        // bloom() is for visual blooming effect, cursors are what changes the blocks.
        this.getBlockEntity().getListener().bloom(
            this.world.getHandle(),
            this.getPosition(),
            this.getBlockEntity().getBlockState(),
            this.world.getHandle().getRandom()
        );
        this.getBlockEntity().getListener().getSculkSpreader().addCursors(new BlockPos(block.getX(), block.getY(), block.getZ()), charge);
    }

    @Override
    public CraftSculkCatalyst copy() {
        return new CraftSculkCatalyst(this, null);
    }

    @Override
    public CraftSculkCatalyst copy(Location location) {
        return new CraftSculkCatalyst(this, location);
    }

    // Paper start - SculkCatalyst bloom API
    @Override
    public void bloom(io.papermc.paper.math.Position position, int charge) { // kinda a duplicate of above method
        Preconditions.checkArgument(position != null, "position cannot be null");
        this.requirePlaced();

        // bloom() is for visual blooming effect, cursors are what changes the blocks.
        this.getBlockEntity().getListener().bloom(
            this.world.getHandle(),
            this.getPosition(),
            this.getBlockEntity().getBlockState(),
            this.world.getHandle().getRandom()
        );
        this.getBlockEntity().getListener().getSculkSpreader().addCursors(io.papermc.paper.util.MCUtil.toBlockPos(position), charge);
    }
    // Paper end
}
