package net.minecraft.server;

import java.util.ArrayList; // CraftBukkit
import java.util.Random;

public class BlockDeadBush extends BlockFlower {

    protected BlockDeadBush(int i, int j) {
        super(i, j, Material.REPLACEABLE_PLANT);
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
    }

    protected boolean d(int i) {
        return i == Block.SAND.id;
    }

    public int a(int i, int j) {
        return this.textureId;
    }

    public int getDropType(int i, Random random, int j) {
        return -1;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.U() != null && entityhuman.U().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            /* CraftBukkit start - moved this line into calculateDrops
            this.a(world, i, j, k, new ItemStack(Block.DEAD_BUSH, 1, l));
            */
            this.doActualDrop(world, i, j, k);
            // CraftBukkit end
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }

    // CraftBukkit start - Calculate drops
    public ArrayList<ItemStack> calculateDrops(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.U() != null && entityhuman.U().id == Item.SHEARS.id) {
            this.a(world, i, j, k, new ItemStack(Block.DEAD_BUSH, 1, l));
            return this.dropList;
        } else {
            return super.calculateDrops(world, entityhuman, i, j, k, l);
        }
    }
    // CraftBukkit end
}
