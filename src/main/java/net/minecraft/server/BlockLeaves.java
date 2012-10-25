package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.LeavesDecayEvent; // CraftBukkit

public class BlockLeaves extends BlockTransparant {

    private int cD;
    public static final String[] a = new String[] { "oak", "spruce", "birch", "jungle"};
    int[] b;

    protected BlockLeaves(int i, int j) {
        super(i, j, Material.LEAVES, false);
        this.cD = j;
        this.b(true);
        this.a(CreativeModeTab.c);
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        byte b0 = 1;
        int j1 = b0 + 1;

        if (world.d(i - j1, j - j1, k - j1, i + j1, j + j1, k + j1)) {
            for (int k1 = -b0; k1 <= b0; ++k1) {
                for (int l1 = -b0; l1 <= b0; ++l1) {
                    for (int i2 = -b0; i2 <= b0; ++i2) {
                        int j2 = world.getTypeId(i + k1, j + l1, k + i2);

                        if (j2 == Block.LEAVES.id) {
                            int k2 = world.getData(i + k1, j + l1, k + i2);

                            world.setRawData(i + k1, j + l1, k + i2, k2 | 8);
                        }
                    }
                }
            }
        }
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0 && (l & 4) == 0) {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.b == null) {
                    this.b = new int[b1 * b1 * b1];
                }

                int l1;

                if (world.d(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
                    int i2;
                    int j2;
                    int k2;

                    for (l1 = -b0; l1 <= b0; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                k2 = world.getTypeId(i + l1, j + i2, k + j2);
                                if (k2 == Block.LOG.id) {
                                    this.b[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                } else if (k2 == Block.LEAVES.id) {
                                    this.b[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                } else {
                                    this.b[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                for (k2 = -b0; k2 <= b0; ++k2) {
                                    if (this.b[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1) {
                                        if (this.b[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.b[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.b[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.b[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.b[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2) {
                                            this.b[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.b[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2) {
                                            this.b[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.b[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2) {
                                            this.b[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.b[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2) {
                                            this.b[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.b[k1 * j1 + k1 * b1 + k1];
                if (l1 >= 0) {
                    world.setRawData(i, j, k, l & -9);
                } else {
                    this.l(world, i, j, k);
                }
            }
        }
    }

    private void l(World world, int i, int j, int k) {
        // CraftBukkit start
        LeavesDecayEvent event = new LeavesDecayEvent(world.getWorld().getBlockAt(i, j, k));
        world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end

        this.c(world, i, j, k, world.getData(i, j, k), 0);
        world.setTypeId(i, j, k, 0);
    }

    public int a(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public int getDropType(int i, Random random, int j) {
        return Block.SAPLING.id;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            byte b0 = 20;

            if ((l & 3) == 3) {
                b0 = 40;
            }

            if (world.random.nextInt(b0) == 0) {
                int j1 = this.getDropType(l, world.random, i1);

                this.a(world, i, j, k, new ItemStack(j1, 1, this.getDropData(l)));
            }

            if ((l & 3) == 0 && world.random.nextInt(200) == 0) {
                this.a(world, i, j, k, new ItemStack(Item.APPLE, 1, 0));
            }
        }
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.bP() != null && entityhuman.bP().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            this.a(world, i, j, k, new ItemStack(Block.LEAVES.id, 1, l & 3));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }

    public int getDropData(int i) {
        return i & 3;
    }

    public boolean c() {
        return !this.c;
    }

    public int a(int i, int j) {
        return (j & 3) == 1 ? this.textureId + 80 : ((j & 3) == 3 ? this.textureId + 144 : this.textureId);
    }
}
