package org.bukkit.craftbukkit.block;

import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityBee;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class CapturedBlockState extends CraftBlockState {

    private final boolean treeBlock;

    public CapturedBlockState(Block block, int flag, boolean treeBlock) {
        super(block, flag);

        this.treeBlock = treeBlock;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        // SPIGOT-5537: Horrible hack to manually add bees given World.captureTreeGeneration does not support tiles
        if (this.treeBlock && getType() == Material.BEE_NEST) {
            GeneratorAccessSeed generatoraccessseed = this.world.getHandle();
            BlockPosition blockposition1 = this.getPosition();
            Random random = generatoraccessseed.getRandom();

            // Begin copied block from WorldGenFeatureTreeBeehive
            TileEntity tileentity = generatoraccessseed.getBlockEntity(blockposition1);

            if (tileentity instanceof TileEntityBeehive) {
                TileEntityBeehive tileentitybeehive = (TileEntityBeehive) tileentity;
                int j = 2 + random.nextInt(2);

                for (int k = 0; k < j; ++k) {
                    EntityBee entitybee = new EntityBee(EntityTypes.BEE, generatoraccessseed.getMinecraftWorld());

                    tileentitybeehive.addOccupantWithPresetTicks(entitybee, false, random.nextInt(599));
                }
            }
            // End copied block
        }

        return result;
    }

    public static CapturedBlockState getBlockState(World world, BlockPosition pos, int flag) {
        return new CapturedBlockState(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag, false);
    }

    public static CapturedBlockState getTreeBlockState(World world, BlockPosition pos, int flag) {
        return new CapturedBlockState(world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), flag, true);
    }
}
