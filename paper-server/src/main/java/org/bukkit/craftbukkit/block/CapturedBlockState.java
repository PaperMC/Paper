package org.bukkit.craftbukkit.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Deprecated(forRemoval = true)
public final class CapturedBlockState extends CraftBlockState {

    private final boolean treeBlock;

    public CapturedBlockState(Block block, int capturedFlags, boolean treeBlock) {
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
    public boolean place(int flags) {
        boolean result = super.place(flags);
        this.addBees();

        return result;
    }

    private void addBees() {
        // SPIGOT-5537: Horrible hack to manually add bees given Level#captureTreeGeneration does not support block entities
        if (this.treeBlock && this.getType() == Material.BEE_NEST) {
            WorldGenLevel worldGenLevel = this.world.getHandle();
            BlockPos pos = this.getPosition();
            RandomSource randomSource = worldGenLevel.getRandom();

            // Begin copied block from BeehiveDecorator
            worldGenLevel.getBlockEntity(pos, BlockEntityType.BEEHIVE).ifPresent(beehiveBlockEntity -> {
                int i1 = 2 + randomSource.nextInt(2);

                for (int i2 = 0; i2 < i1; i2++) {
                    beehiveBlockEntity.storeBee(BeehiveBlockEntity.Occupant.create(randomSource.nextInt(599)));
                }
            });
            // End copied block
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

    public static CapturedBlockState getTreeBlockState(Level world, BlockPos pos, int flag) {
        return new CapturedBlockState(CraftBlock.at(world, pos), flag, true);
    }
}
