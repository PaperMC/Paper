package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.LeavesDecayEvent; // CraftBukkit

public abstract class BlockLeaves extends BlockTransparent {

    int[] a;
    protected IIcon[][] M = new IIcon[2][];

    public BlockLeaves() {
        super(Material.LEAVES, false);
        this.a(true);
        this.a(CreativeModeTab.c);
        this.c(0.2F);
        this.g(1);
        this.a(h);
    }

    public void remove(World world, int i, int j, int k, Block block, int l) {
        byte b0 = 1;
        int i1 = b0 + 1;

        if (world.b(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
            for (int j1 = -b0; j1 <= b0; ++j1) {
                for (int k1 = -b0; k1 <= b0; ++k1) {
                    for (int l1 = -b0; l1 <= b0; ++l1) {
                        if (world.getType(i + j1, j + k1, k + l1).getMaterial() == Material.LEAVES) {
                            int i2 = world.getData(i + j1, j + k1, k + l1);

                            world.setData(i + j1, j + k1, k + l1, i2 | 8, 4);
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

                if (world.b(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
                    int i2;
                    int j2;

                    for (l1 = -b0; l1 <= b0; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                Block block = world.getType(i + l1, j + i2, k + j2);

                                if (block != Blocks.LOG && block != Blocks.LOG2) {
                                    if (block.getMaterial() == Material.LEAVES) {
                                        this.a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    } else {
                                        this.a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                } else {
                                    this.a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                for (int k2 = -b0; k2 <= b0; ++k2) {
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
                    world.setData(i, j, k, l & -9, 4);
                } else {
                    this.e(world, i, j, k);
                }
            }
        }
    }

    private void e(World world, int i, int j, int k) {
        // CraftBukkit start
        LeavesDecayEvent event = new LeavesDecayEvent(world.getWorld().getBlockAt(i, j, k));
        world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end

        this.b(world, i, j, k, world.getData(i, j, k), 0);
        world.setAir(i, j, k);
    }

    public int a(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getDropType(int i, Random random, int j) {
        return Item.getItemOf(Blocks.SAPLING);
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            int j1 = this.b(l);

            if (i1 > 0) {
                j1 -= 2 << i1;
                if (j1 < 10) {
                    j1 = 10;
                }
            }

            if (world.random.nextInt(j1) == 0) {
                Item item = this.getDropType(l, world.random, i1);

                this.a(world, i, j, k, new ItemStack(item, 1, this.getDropData(l)));
            }

            j1 = 200;
            if (i1 > 0) {
                j1 -= 10 << i1;
                if (j1 < 40) {
                    j1 = 40;
                }
            }

            this.c(world, i, j, k, l, j1);
        }
    }

    protected void c(World world, int i, int j, int k, int l, int i1) {}

    protected int b(int i) {
        return 20;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.bD() != null && entityhuman.bD().getItem() == Items.SHEARS) {
            entityhuman.a(StatisticList.C[Block.b((Block) this)], 1);
            this.a(world, i, j, k, new ItemStack(Item.getItemOf(this), 1, l & 3));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }

    public int getDropData(int i) {
        return i & 3;
    }

    public boolean c() {
        return !this.P;
    }

    protected ItemStack j(int i) {
        return new ItemStack(Item.getItemOf(this), 1, i & 3);
    }

    public abstract String[] e();
}
