package net.minecraft.server;

import java.util.Random;

public class BlockReed extends Block {

    protected BlockReed(int i, int j) {
        super(i, Material.PLANT);
        this.textureId = j;
        float f = 0.375F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        this.b(true);
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (world.isEmpty(i, j + 1, k)) {
            int l;

            for (l = 1; world.getTypeId(i, j - l, k) == this.id; ++l) {
                ;
            }

            if (l < 3) {
                int i1 = world.getData(i, j, k);

                if (i1 == 15) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j + 1, k, this.id, 0); // CraftBukkit
                    world.setData(i, j, k, 0);
                } else {
                    world.setData(i, j, k, i1 + 1);
                }
            }
        }
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j - 1, k);

        return l == this.id ? true : (l != Block.GRASS.id && l != Block.DIRT.id && l != Block.SAND.id ? false : (world.getMaterial(i - 1, j - 1, k) == Material.WATER ? true : (world.getMaterial(i + 1, j - 1, k) == Material.WATER ? true : (world.getMaterial(i, j - 1, k - 1) == Material.WATER ? true : world.getMaterial(i, j - 1, k + 1) == Material.WATER))));
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        this.k_(world, i, j, k);
    }

    protected final void k_(World world, int i, int j, int k) {
        if (!this.d(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
        }
    }

    public boolean d(World world, int i, int j, int k) {
        return this.canPlace(world, i, j, k);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public int getDropType(int i, Random random, int j) {
        return Item.SUGAR_CANE.id;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int d() {
        return 1;
    }
}
