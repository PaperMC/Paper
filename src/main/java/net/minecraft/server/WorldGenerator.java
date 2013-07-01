package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

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

    // CraftBukkit - change signature
    protected void setType(BlockChangeDelegate world, int i, int j, int k, int l) {
        this.setTypeAndData(world, i, j, k, l, 0);
    }

    // CraftBukkit - change signature
    protected void setTypeAndData(BlockChangeDelegate world, int i, int j, int k, int l, int i1) {
        if (this.a) {
            // CraftBukkit - BlockChangeDelegate doesn't have the 6th parameter
            world.setTypeIdAndData(i, j, k, l, i1);
        } else {
            // CraftBukkit start - Layering violation :(
            if (world instanceof World) {
                ((World) world).setTypeIdAndData(i, j, k, l, i1, 2);
            } else {
                world.setRawTypeIdAndData(i, j, k, l, i1);
            }
            // CraftBukkit end
        }
    }
}
