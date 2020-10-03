package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class BlockIce extends BlockHalfTransparent {

    public BlockIce(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        super.a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        if (EnchantmentManager.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) == 0) {
            if (world.getDimensionManager().isNether()) {
                world.a(blockposition, false);
                return;
            }

            Material material = world.getType(blockposition.down()).getMaterial();

            if (material.isSolid() || material.isLiquid()) {
                world.setTypeUpdate(blockposition, Blocks.WATER.getBlockData());
            }
        }

    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (worldserver.getBrightness(EnumSkyBlock.BLOCK, blockposition) > 11 - iblockdata.b((IBlockAccess) worldserver, blockposition)) {
            this.melt(iblockdata, worldserver, blockposition);
        }

    }

    protected void melt(IBlockData iblockdata, World world, BlockPosition blockposition) {
        if (world.getDimensionManager().isNether()) {
            world.a(blockposition, false);
        } else {
            world.setTypeUpdate(blockposition, Blocks.WATER.getBlockData());
            world.a(blockposition, Blocks.WATER, blockposition);
        }
    }

    @Override
    public EnumPistonReaction getPushReaction(IBlockData iblockdata) {
        return EnumPistonReaction.NORMAL;
    }
}
