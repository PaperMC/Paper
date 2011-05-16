package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class BlockSapling extends BlockFlower {

    protected BlockSapling(int i, int j) {
        super(i, j);
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
        if (world.getLightLevel(i, j + 1, k) >= 9 && random.nextInt(30) == 0) {
            int l = world.getData(i, j, k);

            if ((l & 8) == 0) {
                world.setData(i, j, k, l | 8);
            } else {
                this.b(world, i, j, k, random);
            }
        }
    }

    public int a(int i, int j) {
        j &= 3;
        return j == 1 ? 63 : (j == 2 ? 79 : super.a(i, j));
    }

    public void b(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k) & 3;

        world.setRawTypeId(i, j, k, 0);

        // CraftBukkit start - fixes client updates on recently grown trees
        boolean grownTree;
        BlockChangeWithNotify delegate = new BlockChangeWithNotify(world);

        if (l == 1) {
            grownTree = new WorldGenTaiga2().generate(delegate, random, i, j, k);
        } else if (l == 2) {
            grownTree = new WorldGenForest().generate(delegate, random, i, j, k);
        } else {
            if (random.nextInt(10) == 0) {
                grownTree = new WorldGenBigTree().generate(delegate, random, i, j, k);
            } else {
                grownTree = new WorldGenTrees().generate(delegate, random, i, j, k);
            }
        }

        if (!grownTree) {
            world.setRawTypeIdAndData(i, j, k, this.id, l);
        }
        // CraftBukkit end
    }

    protected int b(int i) {
        return i & 3;
    }

    // CraftBukkit start
    private class BlockChangeWithNotify implements BlockChangeDelegate {
        World world;

        BlockChangeWithNotify(World world) { this.world = world; }

        public boolean setRawTypeId(int x, int y, int z, int type) {
            return world.setTypeId(x, y, z, type);
        }

        public boolean setRawTypeIdAndData(int x, int y, int z, int type, int data) {
            return world.setTypeIdAndData(x, y, z, type, data);
        }

        public int getTypeId(int x, int y, int z) {
            return world.getTypeId(x, y, z);
        }
    }
    // CraftBukkit end
}
