package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenTaiga2 extends WorldGenerator {

    public WorldGenTaiga2() {}

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start
        // sk: The idea is to have (our) WorldServer implement
        // BlockChangeDelegate and then we can implicitly cast World to
        // WorldServer (a safe cast, AFAIK) and no code will be broken. This
        // then allows plugins to catch manually-invoked generation events
        return this.generate((BlockChangeDelegate) world, random, i, j, k);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(4) + 6;
        int i1 = 1 + random.nextInt(2);
        int j1 = l - i1;
        int k1 = 2 + random.nextInt(2);
        boolean flag = true;

        if (j >= 1) {
            int l1 = j + l + 1;

            world.getClass();
            if (l1 <= 128) {
                int i2;
                int j2;
                int k2;
                int l2;

                for (i2 = j; i2 <= j + 1 + l && flag; ++i2) {
                    boolean flag1 = true;

                    if (i2 - j < i1) {
                        l2 = 0;
                    } else {
                        l2 = k1;
                    }

                    for (j2 = i - l2; j2 <= i + l2 && flag; ++j2) {
                        for (int i3 = k - l2; i3 <= k + l2 && flag; ++i3) {
                            if (i2 >= 0) {
                                world.getClass();
                                if (i2 < 128) {
                                    k2 = world.getTypeId(j2, i2, i3);
                                    if (k2 != 0 && k2 != Block.LEAVES.id) {
                                        flag = false;
                                    }
                                    continue;
                                }
                            }

                            flag = false;
                        }
                    }
                }

                if (!flag) {
                    return false;
                }

                i2 = world.getTypeId(i, j - 1, k);
                if (i2 == Block.GRASS.id || i2 == Block.DIRT.id) {
                    world.getClass();
                    if (j < 128 - l - 1) {
                        world.setRawTypeId(i, j - 1, k, Block.DIRT.id);
                        l2 = random.nextInt(2);
                        j2 = 1;
                        byte b0 = 0;

                        int j3;
                        int k3;

                        for (k2 = 0; k2 <= j1; ++k2) {
                            k3 = j + l - k2;

                            for (j3 = i - l2; j3 <= i + l2; ++j3) {
                                int l3 = j3 - i;

                                for (int i4 = k - l2; i4 <= k + l2; ++i4) {
                                    int j4 = i4 - k;

                                    if ((Math.abs(l3) != l2 || Math.abs(j4) != l2 || l2 <= 0) && !Block.o[world.getTypeId(j3, k3, i4)]) {
                                        world.setRawTypeIdAndData(j3, k3, i4, Block.LEAVES.id, 1);
                                    }
                                }
                            }

                            if (l2 >= j2) {
                                l2 = b0;
                                b0 = 1;
                                ++j2;
                                if (j2 > k1) {
                                    j2 = k1;
                                }
                            } else {
                                ++l2;
                            }
                        }

                        k2 = random.nextInt(3);

                        for (k3 = 0; k3 < l - k2; ++k3) {
                            j3 = world.getTypeId(i, j + k3, k);
                            if (j3 == 0 || j3 == Block.LEAVES.id) {
                                world.setRawTypeIdAndData(i, j + k3, k, Block.LOG.id, 1);
                            }
                        }

                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }
}
