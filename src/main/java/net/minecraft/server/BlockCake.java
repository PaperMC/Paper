package net.minecraft.server;

import java.util.Random;

public class BlockCake extends Block {

    protected BlockCake(int i) {
        super(i, Material.CAKE);
        this.b(true);
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        float f = 0.0625F;
        float f1 = (float) (1 + l * 2) / 16.0F;
        float f2 = 0.5F;

        this.a(f1, 0.0F, f, 1.0F - f, f2, 1.0F - f);
    }

    public void g() {
        float f = 0.0625F;
        float f1 = 0.5F;

        this.a(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        float f = 0.0625F;
        float f1 = (float) (1 + l * 2) / 16.0F;
        float f2 = 0.5F;

        return AxisAlignedBB.a().a((double) ((float) i + f1), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) ((float) j + f2 - f), (double) ((float) (k + 1) - f));
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        this.b(world, i, j, k, entityhuman);
        return true;
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.b(world, i, j, k, entityhuman);
    }

    private void b(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (entityhuman.g(false)) {
            // CraftBukkit start
            int oldFoodLevel = entityhuman.getFoodData().foodLevel;
            
            org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, 2 + oldFoodLevel);
            
            if (!event.isCancelled()) {
                entityhuman.getFoodData().eat(event.getFoodLevel() - oldFoodLevel, 0.1F);
            }
            
            ((EntityPlayer) entityhuman).playerConnection.sendPacket(new Packet8UpdateHealth(((EntityPlayer) entityhuman).getBukkitEntity().getScaledHealth(), entityhuman.getFoodData().foodLevel, entityhuman.getFoodData().saturationLevel));
            // CraftBukkit end
            int l = world.getData(i, j, k) + 1;

            if (l >= 6) {
                world.setAir(i, j, k);
            } else {
                world.setData(i, j, k, l, 2);
            }
        }
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return !super.canPlace(world, i, j, k) ? false : this.f(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.f(world, i, j, k)) {
            world.setAir(i, j, k);
        }
    }

    public boolean f(World world, int i, int j, int k) {
        return world.getMaterial(i, j - 1, k).isBuildable();
    }

    public int a(Random random) {
        return 0;
    }

    public int getDropType(int i, Random random, int j) {
        return 0;
    }
}
