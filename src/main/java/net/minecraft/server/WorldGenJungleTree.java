package net.minecraft.server;

import java.util.Random;
import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenJungleTree extends WorldGenMegaTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    public WorldGenJungleTree(boolean flag, int i, int j, int k, int l) {
        super(flag, i, j, k, l);
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = this.a(random);

        if (!this.a(world, random, i, j, k, l)) {
            return false;
        } else {
            this.c(world, i, k, j + l, 2, random);

            for (int i1 = j + l - 2 - random.nextInt(4); i1 > j + l / 2; i1 -= 2 + random.nextInt(4)) {
                float f = random.nextFloat() * 3.1415927F * 2.0F;
                int j1 = i + (int) (0.5F + MathHelper.cos(f) * 4.0F);
                int k1 = k + (int) (0.5F + MathHelper.sin(f) * 4.0F);

                int l1;

                for (l1 = 0; l1 < 5; ++l1) {
                    j1 = i + (int) (1.5F + MathHelper.cos(f) * (float) l1);
                    k1 = k + (int) (1.5F + MathHelper.sin(f) * (float) l1);
                    this.setTypeAndData(world, j1, i1 - 3 + l1 / 2, k1, Blocks.LOG, this.b);
                }

                l1 = 1 + random.nextInt(2);
                int i2 = i1;

                for (int j2 = i1 - l1; j2 <= i2; ++j2) {
                    int k2 = j2 - i2;

                    this.b(world, j1, j2, k1, 1 - k2, random);
                }
            }

            for (int l2 = 0; l2 < l; ++l2) {
                Block block = world.getType(i, j + l2, k);

                if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                    this.setTypeAndData(world, i, j + l2, k, Blocks.LOG, this.b);
                    if (l2 > 0) {
                        if (random.nextInt(3) > 0 && world.isEmpty(i - 1, j + l2, k)) {
                            this.setTypeAndData(world, i - 1, j + l2, k, Blocks.VINE, 8);
                        }

                        if (random.nextInt(3) > 0 && world.isEmpty(i, j + l2, k - 1)) {
                            this.setTypeAndData(world, i, j + l2, k - 1, Blocks.VINE, 1);
                        }
                    }
                }

                if (l2 < l - 1) {
                    block = world.getType(i + 1, j + l2, k);
                    if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                        this.setTypeAndData(world, i + 1, j + l2, k, Blocks.LOG, this.b);
                        if (l2 > 0) {
                            if (random.nextInt(3) > 0 && world.isEmpty(i + 2, j + l2, k)) {
                                this.setTypeAndData(world, i + 2, j + l2, k, Blocks.VINE, 2);
                            }

                            if (random.nextInt(3) > 0 && world.isEmpty(i + 1, j + l2, k - 1)) {
                                this.setTypeAndData(world, i + 1, j + l2, k - 1, Blocks.VINE, 1);
                            }
                        }
                    }

                    block = world.getType(i + 1, j + l2, k + 1);
                    if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                        this.setTypeAndData(world, i + 1, j + l2, k + 1, Blocks.LOG, this.b);
                        if (l2 > 0) {
                            if (random.nextInt(3) > 0 && world.isEmpty(i + 2, j + l2, k + 1)) {
                                this.setTypeAndData(world, i + 2, j + l2, k + 1, Blocks.VINE, 2);
                            }

                            if (random.nextInt(3) > 0 && world.isEmpty(i + 1, j + l2, k + 2)) {
                                this.setTypeAndData(world, i + 1, j + l2, k + 2, Blocks.VINE, 4);
                            }
                        }
                    }

                    block = world.getType(i, j + l2, k + 1);
                    if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                        this.setTypeAndData(world, i, j + l2, k + 1, Blocks.LOG, this.b);
                        if (l2 > 0) {
                            if (random.nextInt(3) > 0 && world.isEmpty(i - 1, j + l2, k + 1)) {
                                this.setTypeAndData(world, i - 1, j + l2, k + 1, Blocks.VINE, 8);
                            }

                            if (random.nextInt(3) > 0 && world.isEmpty(i, j + l2, k + 2)) {
                                this.setTypeAndData(world, i, j + l2, k + 2, Blocks.VINE, 4);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }

    // CraftBukkit - Changed signature
    private void c(CraftBlockChangeDelegate world, int i, int j, int k, int l, Random random) {
        byte b0 = 2;

        for (int i1 = k - b0; i1 <= k; ++i1) {
            int j1 = i1 - k;

            this.a(world, i, i1, j, l + 1 - j1, random);
        }
    }
}
