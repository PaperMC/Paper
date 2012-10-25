package net.minecraft.server;

import java.util.Random;

public class BlockIce extends BlockHalfTransparant {

    public BlockIce(int i, int j) {
        super(i, j, Material.ICE, false);
        this.frictionFactor = 0.98F;
        this.b(true);
        this.a(CreativeModeTab.b);
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        entityhuman.a(StatisticList.C[this.id], 1);
        entityhuman.j(0.025F);
        if (this.s_() && EnchantmentManager.hasSilkTouchEnchantment(entityhuman)) {
            ItemStack itemstack = this.f_(l);

            if (itemstack != null) {
                this.a(world, i, j, k, itemstack);
            }
        } else {
            if (world.worldProvider.e) {
                world.setTypeId(i, j, k, 0);
                return;
            }

            int i1 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(entityhuman);

            this.c(world, i, j, k, l, i1);
            Material material = world.getMaterial(i, j - 1, k);

            if (material.isSolid() || material.isLiquid()) {
                world.setTypeId(i, j, k, Block.WATER.id);
            }
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (world.b(EnumSkyBlock.BLOCK, i, j, k) > 11 - Block.lightBlock[this.id]) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(i, j, k), Block.STATIONARY_WATER.id).isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (world.worldProvider.e) {
                world.setTypeId(i, j, k, 0);
                return;
            }

            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, Block.STATIONARY_WATER.id);
        }
    }

    public int q_() {
        return 0;
    }
}
