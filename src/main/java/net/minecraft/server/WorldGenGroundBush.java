package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenGroundBush extends WorldGenTrees {

    private int a;
    private int b;

    public WorldGenGroundBush(int i, int j) {
        super(false);
        this.b = i;
        this.a = j;
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    @Override
    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        Block block;

        while (((block = world.getType(i, j, k)).getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) && j > 0) {
            --j;
        }

        Block block1 = world.getType(i, j, k);

        if (block1 == Blocks.DIRT || block1 == Blocks.GRASS) {
            ++j;
            this.setTypeAndData(world, i, j, k, Blocks.LOG, this.b);

            for (int l = j; l <= j + 2; ++l) {
                int i1 = l - j;
                int j1 = 2 - i1;

                for (int k1 = i - j1; k1 <= i + j1; ++k1) {
                    int l1 = k1 - i;

                    for (int i2 = k - j1; i2 <= k + j1; ++i2) {
                        int j2 = i2 - k;

                        if ((Math.abs(l1) != j1 || Math.abs(j2) != j1 || random.nextInt(2) != 0) && !world.getType(k1, l, i2).j()) {
                            this.setTypeAndData(world, k1, l, i2, Blocks.LEAVES, this.a);
                        }
                    }
                }
            }
        // CraftBukkit start - Return false if gen was unsuccessful
        } else {
            return false;
        }
        // CraftBukkit end

        return true;
    }
}
