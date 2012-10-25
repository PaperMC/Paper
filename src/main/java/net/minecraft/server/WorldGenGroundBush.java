package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenGroundBush extends WorldGenerator implements BlockSapling.TreeGenerator { // CraftBukkit add interface

    private int a;
    private int b;

    public WorldGenGroundBush(int i, int j) {
        this.b = i;
        this.a = j;
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - moved to generate
        return this.generate((BlockChangeDelegate) world, random, i, j, k);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l;

        for (boolean flag = false; ((l = world.getTypeId(i, j, k)) == 0 || l == Block.LEAVES.id) && j > 0; --j) {
            ;
        }

        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.DIRT.id || i1 == Block.GRASS.id) {
            ++j;
            this.setTypeAndData(world, i, j, k, Block.LOG.id, this.b);

            for (int j1 = j; j1 <= j + 2; ++j1) {
                int k1 = j1 - j;
                int l1 = 2 - k1;

                for (int i2 = i - l1; i2 <= i + l1; ++i2) {
                    int j2 = i2 - i;

                    for (int k2 = k - l1; k2 <= k + l1; ++k2) {
                        int l2 = k2 - k;

                        if ((Math.abs(j2) != l1 || Math.abs(l2) != l1 || random.nextInt(2) != 0) && !Block.q[world.getTypeId(i2, j1, k2)]) {
                            this.setTypeAndData(world, i2, j1, k2, Block.LEAVES.id, this.a);
                        }
                    }
                }
            }
        // CraftBukkit start - return false if gen was unsuccessful
        } else {
            return false;
        }
        // CraftBukkit end

        return true;
    }
}
