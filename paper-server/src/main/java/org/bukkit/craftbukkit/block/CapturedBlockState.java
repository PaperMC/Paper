package org.bukkit.craftbukkit.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Deprecated(forRemoval = true)
public final class CapturedBlockState extends CraftBlockState {

    private final boolean treeBlock;

    public CapturedBlockState(Block block, @net.minecraft.world.level.block.Block.UpdateFlags int capturedFlags, boolean treeBlock) {
        super(block, capturedFlags);

        this.treeBlock = treeBlock;
    }

    private CapturedBlockState(CapturedBlockState state, Location location) {
        super(state, location);
        this.treeBlock = state.treeBlock;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.addBees();
        }

        return result;
    }

    @Override
    public boolean place(@net.minecraft.world.level.block.Block.UpdateFlags int flags) {
        boolean result = super.place(flags);
        this.addBees();

        return result;
    }

    private void addBees() {
        // SPIGOT-5537: Horrible hack to manually add bees given Level#captureTreeGeneration does not support block entities
        if (this.treeBlock && this.getType() == Material.BEE_NEST) {
            WorldGenLevel level = this.world.getHandle();
            BlockPos pos = this.getPosition();
            RandomSource random = level.getRandom();

            // copied from BeehiveDecorator
            level.getBlockEntity(pos, BlockEntityTypes.BEEHIVE).ifPresent(beehive -> {
                int numBees = 2 + random.nextInt(2);

                for (int count = 0; count < numBees; count++) {
                    beehive.storeBee(BeehiveBlockEntity.Occupant.create(random.nextInt(599)));
                }
            });
        }
    }

    @Override
    public CapturedBlockState copy() {
        return new CapturedBlockState(this, null);
    }

    @Override
    public CapturedBlockState copy(Location location) {
        return new CapturedBlockState(this, location);
    }

    public static CapturedBlockState getTreeBlockState(Level world, BlockPos pos, @net.minecraft.world.level.block.Block.UpdateFlags int flags) {
        return new CapturedBlockState(CraftBlock.at(world, pos), flags, true);
    }
}
