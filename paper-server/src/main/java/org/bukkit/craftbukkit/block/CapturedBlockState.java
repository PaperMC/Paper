package org.bukkit.craftbukkit.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.CraftLocation;

public final class CapturedBlockState extends CraftBlockState {

    private final boolean treeBlock;

    public CapturedBlockState(Block block, int flag, boolean treeBlock) {
        super(block, flag);

        this.treeBlock = treeBlock;
    }

    protected CapturedBlockState(CapturedBlockState state, Location location) {
        super(state, location);
        this.treeBlock = state.treeBlock;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        // Probably no longer needed with the extra #updatedTree method,
        // but leave if here for now in case a plugin for whatever reason relies on this.
        this.addBees();

        return result;
    }

    private void updatedTree() {
        // SPIGOT-7248 - Manual update to avoid physics where appropriate
        // SPIGOT-7572 - Move SPIGOT-7248 fix from nms ItemStack to here, to allow bee generation in nests
        this.world.getHandle().setBlock(CraftLocation.toBlockPosition(this.getLocation()), this.getHandle(), this.getFlag());

        this.addBees();
    }

    private void addBees() {
        // SPIGOT-5537: Horrible hack to manually add bees given World.captureTreeGeneration does not support tiles
        if (this.treeBlock && this.getType() == Material.BEE_NEST) {
            WorldGenLevel generatoraccessseed = this.world.getHandle();
            BlockPos blockposition1 = this.getPosition();
            RandomSource random = generatoraccessseed.getRandom();

            // Begin copied block from WorldGenFeatureTreeBeehive
            BlockEntity tileentity = generatoraccessseed.getBlockEntity(blockposition1);

            if (tileentity instanceof BeehiveBlockEntity) {
                BeehiveBlockEntity tileentitybeehive = (BeehiveBlockEntity) tileentity;
                int j = 2 + random.nextInt(2);

                for (int k = 0; k < j; ++k) {
                    tileentitybeehive.storeBee(BeehiveBlockEntity.Occupant.create(random.nextInt(599)));
                }
            }
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

    public static CapturedBlockState getBlockState(Level world, BlockPos pos, int flag) {
        return new CapturedBlockState(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag, false);
    }

    public static CapturedBlockState getTreeBlockState(Level world, BlockPos pos, int flag) {
        return new CapturedBlockState(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag, true);
    }

    public static void setBlockState(BlockState blockState) {
        if (blockState instanceof CapturedBlockState capturedBlockState) {
            capturedBlockState.updatedTree();
        } else {
            blockState.update(true);
        }
    }
}
