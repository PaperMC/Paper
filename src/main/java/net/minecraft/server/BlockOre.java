package net.minecraft.server;

import java.util.Random;

public class BlockOre extends Block {

    public BlockOre(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    protected int a(Random random) {
        return this == Blocks.COAL_ORE ? MathHelper.nextInt(random, 0, 2) : (this == Blocks.DIAMOND_ORE ? MathHelper.nextInt(random, 3, 7) : (this == Blocks.EMERALD_ORE ? MathHelper.nextInt(random, 3, 7) : (this == Blocks.LAPIS_ORE ? MathHelper.nextInt(random, 2, 5) : (this == Blocks.NETHER_QUARTZ_ORE ? MathHelper.nextInt(random, 2, 5) : (this == Blocks.NETHER_GOLD_ORE ? MathHelper.nextInt(random, 0, 1) : 0)))));
    }

    @Override
    public void dropNaturally(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, ItemStack itemstack) {
        super.dropNaturally(iblockdata, worldserver, blockposition, itemstack);
        if (EnchantmentManager.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) == 0) {
            int i = this.a(worldserver.random);

            if (i > 0) {
                this.dropExperience(worldserver, blockposition, i);
            }
        }

    }
}
