package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.LeavesDecayEvent; // CraftBukkit

public class BlockLeaves extends BlockLeavesBase {

    private int c;
    int[] a;

    protected BlockLeaves(int i, int j) {
        super(i, j, Material.LEAVES, false);
        this.c = j;
        this.a(true);
    }

    public void remove(World world, int i, int j, int k) {
        byte b0 = 1;
        int l = b0 + 1;

        if (world.a(i - l, j - l, k - l, i + l, j + l, k + l)) {
            for (int i1 = -b0; i1 <= b0; ++i1) {
                for (int j1 = -b0; j1 <= b0; ++j1) {
                    for (int k1 = -b0; k1 <= b0; ++k1) {
                        int l1 = world.getTypeId(i + i1, j + j1, k + k1);

                        if (l1 == Block.LEAVES.id) {
                            int i2 = world.getData(i + i1, j + j1, k + k1);

                            world.setRawData(i + i1, j + j1, k + k1, i2 | 8);
                        }
                    }
                }
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0 && (l & 4) == 0) {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.a == null) {
                    this.a = new int[b1 * b1 * b1];
                }

                int l1;

                if (world.a(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
                    int i2;
                    int j2;
                    int k2;

                    for (l1 = -b0; l1 <= b0; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                k2 = world.getTypeId(i + l1, j + i2, k + j2);
                                if (k2 == Block.LOG.id) {
                                    this.a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                } else if (k2 == Block.LEAVES.id) {
                                    this.a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                } else {
                                    this.a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                for (k2 = -b0; k2 <= b0; ++k2) {
                                    if (this.a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1) {
                                        if (this.a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2) {
                                            this.a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2) {
                                            this.a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2) {
                                            this.a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2) {
                                            this.a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.a[k1 * j1 + k1 * b1 + k1];
                if (l1 >= 0) {
                    world.setRawData(i, j, k, l & -9);
                } else {
                    this.g(world, i, j, k);
                }
            }
        }
    }

    private void g(World world, int i, int j, int k) {
        // CraftBukkit start
        LeavesDecayEvent event = new LeavesDecayEvent(world.getWorld().getBlockAt(i, j, k));
        world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // CraftBukkit end

        this.b(world, i, j, k, world.getData(i, j, k), 0);
        world.setTypeId(i, j, k, 0);
    }

    public int a(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public int a(int i, Random random, int j) {
        return Block.SAPLING.id;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.P() != null && entityhuman.P().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            this.a(world, i, j, k, new ItemStack(Block.LEAVES.id, 1, l & 3));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }

    protected int c(int i) {
        return i & 3;
    }

    public boolean a() {
        return !this.b;
    }

    public int a(int i, int j) {
        return (j & 3) == 1 ? this.textureId + 80 : this.textureId;
    }

    public void b(World world, int i, int j, int k, Entity entity) {
        super.b(world, i, j, k, entity);
    }
}
