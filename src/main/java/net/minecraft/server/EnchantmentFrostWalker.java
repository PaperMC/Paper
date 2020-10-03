package net.minecraft.server;

import java.util.Iterator;

public class EnchantmentFrostWalker extends Enchantment {

    public EnchantmentFrostWalker(Enchantment.Rarity enchantment_rarity, EnumItemSlot... aenumitemslot) {
        super(enchantment_rarity, EnchantmentSlotType.ARMOR_FEET, aenumitemslot);
    }

    @Override
    public int a(int i) {
        return i * 10;
    }

    @Override
    public int b(int i) {
        return this.a(i) + 15;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    public static void a(EntityLiving entityliving, World world, BlockPosition blockposition, int i) {
        if (entityliving.isOnGround()) {
            IBlockData iblockdata = Blocks.FROSTED_ICE.getBlockData();
            float f = (float) Math.min(16, 2 + i);
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
            Iterator iterator = BlockPosition.a(blockposition.a((double) (-f), -1.0D, (double) (-f)), blockposition.a((double) f, -1.0D, (double) f)).iterator();

            while (iterator.hasNext()) {
                BlockPosition blockposition1 = (BlockPosition) iterator.next();

                if (blockposition1.a((IPosition) entityliving.getPositionVector(), (double) f)) {
                    blockposition_mutableblockposition.d(blockposition1.getX(), blockposition1.getY() + 1, blockposition1.getZ());
                    IBlockData iblockdata1 = world.getType(blockposition_mutableblockposition);

                    if (iblockdata1.isAir()) {
                        IBlockData iblockdata2 = world.getType(blockposition1);

                        if (iblockdata2.getMaterial() == Material.WATER && (Integer) iblockdata2.get(BlockFluids.LEVEL) == 0 && iblockdata.canPlace(world, blockposition1) && world.a(iblockdata, blockposition1, VoxelShapeCollision.a())) {
                            world.setTypeUpdate(blockposition1, iblockdata);
                            world.getBlockTickList().a(blockposition1, Blocks.FROSTED_ICE, MathHelper.nextInt(entityliving.getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean a(Enchantment enchantment) {
        return super.a(enchantment) && enchantment != Enchantments.DEPTH_STRIDER;
    }
}
