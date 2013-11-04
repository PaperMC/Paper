package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockTripwireHook extends Block {

    public BlockTripwireHook() {
        super(Material.ORIENTABLE);
        this.a(CreativeModeTab.d);
        this.a(true);
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public int b() {
        return 29;
    }

    public int a(World world) {
        return 10;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return l == 2 && world.getType(i, j, k + 1).r() ? true : (l == 3 && world.getType(i, j, k - 1).r() ? true : (l == 4 && world.getType(i + 1, j, k).r() ? true : l == 5 && world.getType(i - 1, j, k).r()));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.getType(i - 1, j, k).r() ? true : (world.getType(i + 1, j, k).r() ? true : (world.getType(i, j, k - 1).r() ? true : world.getType(i, j, k + 1).r()));
    }

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
        byte b0 = 0;

        if (l == 2 && world.c(i, j, k + 1, true)) {
            b0 = 2;
        }

        if (l == 3 && world.c(i, j, k - 1, true)) {
            b0 = 0;
        }

        if (l == 4 && world.c(i + 1, j, k, true)) {
            b0 = 1;
        }

        if (l == 5 && world.c(i - 1, j, k, true)) {
            b0 = 3;
        }

        return b0;
    }

    public void postPlace(World world, int i, int j, int k, int l) {
        this.a(world, i, j, k, false, l, false, -1, 0);
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (block != this) {
            if (this.e(world, i, j, k)) {
                int l = world.getData(i, j, k);
                int i1 = l & 3;
                boolean flag = false;

                if (!world.getType(i - 1, j, k).r() && i1 == 3) {
                    flag = true;
                }

                if (!world.getType(i + 1, j, k).r() && i1 == 1) {
                    flag = true;
                }

                if (!world.getType(i, j, k - 1).r() && i1 == 0) {
                    flag = true;
                }

                if (!world.getType(i, j, k + 1).r() && i1 == 2) {
                    flag = true;
                }

                if (flag) {
                    this.b(world, i, j, k, l, 0);
                    world.setAir(i, j, k);
                }
            }
        }
    }

    public void a(World world, int i, int j, int k, boolean flag, int l, boolean flag1, int i1, int j1) {
        int k1 = l & 3;
        boolean flag2 = (l & 4) == 4;
        boolean flag3 = (l & 8) == 8;
        boolean flag4 = !flag;
        boolean flag5 = false;
        boolean flag6 = !World.a((IBlockAccess) world, i, j - 1, k);
        int l1 = Direction.a[k1];
        int i2 = Direction.b[k1];
        int j2 = 0;
        int[] aint = new int[42];

        int k2;
        int l2;
        int i3;
        int j3;

        for (l2 = 1; l2 < 42; ++l2) {
            k2 = i + l1 * l2;
            i3 = k + i2 * l2;
            Block block = world.getType(k2, j, i3);

            if (block == Blocks.TRIPWIRE_SOURCE) {
                j3 = world.getData(k2, j, i3);
                if ((j3 & 3) == Direction.f[k1]) {
                    j2 = l2;
                }
                break;
            }

            if (block != Blocks.TRIPWIRE && l2 != i1) {
                aint[l2] = -1;
                flag4 = false;
            } else {
                j3 = l2 == i1 ? j1 : world.getData(k2, j, i3);
                boolean flag7 = (j3 & 8) != 8;
                boolean flag8 = (j3 & 1) == 1;
                boolean flag9 = (j3 & 2) == 2;

                flag4 &= flag9 == flag6;
                flag5 |= flag7 && flag8;
                aint[l2] = j3;
                if (l2 == i1) {
                    world.a(i, j, k, this, this.a(world));
                    flag4 &= flag7;
                }
            }
        }

        flag4 &= j2 > 1;
        flag5 &= flag4;
        l2 = (flag4 ? 4 : 0) | (flag5 ? 8 : 0);
        l = k1 | l2;
        int k3;

        if (j2 > 0) {
            k2 = i + l1 * j2;
            i3 = k + i2 * j2;
            k3 = Direction.f[k1];
            world.setData(k2, j, i3, k3 | l2, 3);
            this.a(world, k2, j, i3, k3);
            this.a(world, k2, j, i3, flag4, flag5, flag2, flag3);
        }

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);

        BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
        world.getServer().getPluginManager().callEvent(eventRedstone);

        if (eventRedstone.getNewCurrent() > 0) {
            return;
        }
        // CraftBukkit end

        this.a(world, i, j, k, flag4, flag5, flag2, flag3);
        if (!flag) {
            world.setData(i, j, k, l, 3);
            if (flag1) {
                this.a(world, i, j, k, k1);
            }
        }

        if (flag2 != flag4) {
            for (k2 = 1; k2 < j2; ++k2) {
                i3 = i + l1 * k2;
                k3 = k + i2 * k2;
                j3 = aint[k2];
                if (j3 >= 0) {
                    if (flag4) {
                        j3 |= 4;
                    } else {
                        j3 &= -5;
                    }

                    world.setData(i3, j, k3, j3, 3);
                }
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        this.a(world, i, j, k, false, world.getData(i, j, k), true, -1, 0);
    }

    private void a(World world, int i, int j, int k, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        if (flag1 && !flag3) {
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.4F, 0.6F);
        } else if (!flag1 && flag3) {
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.4F, 0.5F);
        } else if (flag && !flag2) {
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.4F, 0.7F);
        } else if (!flag && flag2) {
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.bowhit", 0.4F, 1.2F / (world.random.nextFloat() * 0.2F + 0.9F));
        }
    }

    private void a(World world, int i, int j, int k, int l) {
        world.applyPhysics(i, j, k, this);
        if (l == 3) {
            world.applyPhysics(i - 1, j, k, this);
        } else if (l == 1) {
            world.applyPhysics(i + 1, j, k, this);
        } else if (l == 0) {
            world.applyPhysics(i, j, k - 1, this);
        } else if (l == 2) {
            world.applyPhysics(i, j, k + 1, this);
        }
    }

    private boolean e(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            return false;
        } else {
            return true;
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k) & 3;
        float f = 0.1875F;

        if (l == 3) {
            this.a(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        } else if (l == 1) {
            this.a(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        } else if (l == 0) {
            this.a(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        } else if (l == 2) {
            this.a(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
    }

    public void remove(World world, int i, int j, int k, Block block, int l) {
        boolean flag = (l & 4) == 4;
        boolean flag1 = (l & 8) == 8;

        if (flag || flag1) {
            this.a(world, i, j, k, true, l, false, -1, 0);
        }

        if (flag1) {
            world.applyPhysics(i, j, k, this);
            int i1 = l & 3;

            if (i1 == 3) {
                world.applyPhysics(i - 1, j, k, this);
            } else if (i1 == 1) {
                world.applyPhysics(i + 1, j, k, this);
            } else if (i1 == 0) {
                world.applyPhysics(i, j, k - 1, this);
            } else if (i1 == 2) {
                world.applyPhysics(i, j, k + 1, this);
            }
        }

        super.remove(world, i, j, k, block, l);
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) == 8 ? 15 : 0;
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getData(i, j, k);

        if ((i1 & 8) != 8) {
            return 0;
        } else {
            int j1 = i1 & 3;

            return j1 == 2 && l == 2 ? 15 : (j1 == 0 && l == 3 ? 15 : (j1 == 1 && l == 4 ? 15 : (j1 == 3 && l == 5 ? 15 : 0)));
        }
    }

    public boolean isPowerSource() {
        return true;
    }
}
