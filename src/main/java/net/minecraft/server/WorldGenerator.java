package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public abstract class WorldGenerator {

    private final boolean a;

    public WorldGenerator() {
        this.a = false;
    }

    public WorldGenerator(boolean flag) {
        this.a = flag;
    }

    public abstract boolean a(World world, Random random, int i, int j, int k);

    public void a(double d0, double d1, double d2) {}

    protected void setType(World world, int i, int j, int k, Block block) {
        this.setTypeAndData(world, i, j, k, block, 0);
    }

    // CraftBukkit start - Duplicate method to add support for CraftBlockChangeDelegate
    protected void setType(CraftBlockChangeDelegate world, int i, int j, int k, Block block) {
        this.setTypeAndData(world, i, j, k, block, 0);
    }
    // CraftBukkit end

    protected void setTypeAndData(World world, int i, int j, int k, Block block, int l) {
        if (this.a) {
            world.setTypeAndData(i, j, k, block, l, 3);
        } else {
            world.setTypeAndData(i, j, k, block, l, 2);
        }
    }

    // CraftBukkit start - Duplicate method to add support for CraftBlockChangeDelegate
    protected void setTypeAndData(CraftBlockChangeDelegate world, int i, int j, int k, Block block, int l) {
        if (this.a) {
            world.setTypeAndData(i, j, k, block, l, 3);
        } else {
            world.setTypeAndData(i, j, k, block, l, 2);
        }
    }
    // CraftBukkit end
}
