package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockButton extends Block {

    protected BlockButton(int i, int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(true);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public int d() {
        return 20;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return l == 2 && world.e(i, j, k + 1) ? true : (l == 3 && world.e(i, j, k - 1) ? true : (l == 4 && world.e(i + 1, j, k) ? true : l == 5 && world.e(i - 1, j, k)));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.e(i - 1, j, k) ? true : (world.e(i + 1, j, k) ? true : (world.e(i, j, k - 1) ? true : world.e(i, j, k + 1)));
    }

    public void postPlace(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 8;

        i1 &= 7;
        if (l == 2 && world.e(i, j, k + 1)) {
            i1 = 4;
        } else if (l == 3 && world.e(i, j, k - 1)) {
            i1 = 3;
        } else if (l == 4 && world.e(i + 1, j, k)) {
            i1 = 2;
        } else if (l == 5 && world.e(i - 1, j, k)) {
            i1 = 1;
        } else {
            i1 = this.g(world, i, j, k);
        }

        world.setData(i, j, k, i1 + j1);
    }

    private int g(World world, int i, int j, int k) {
        return world.e(i - 1, j, k) ? 1 : (world.e(i + 1, j, k) ? 2 : (world.e(i, j, k - 1) ? 3 : (world.e(i, j, k + 1) ? 4 : 1)));
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (this.h(world, i, j, k)) {
            int i1 = world.getData(i, j, k) & 7;
            boolean flag = false;

            if (!world.e(i - 1, j, k) && i1 == 1) {
                flag = true;
            }

            if (!world.e(i + 1, j, k) && i1 == 2) {
                flag = true;
            }

            if (!world.e(i, j, k - 1) && i1 == 3) {
                flag = true;
            }

            if (!world.e(i, j, k + 1) && i1 == 4) {
                flag = true;
            }

            if (flag) {
                this.b(world, i, j, k, world.getData(i, j, k), 0);
                world.setTypeId(i, j, k, 0);
            }
        }
    }

    private boolean h(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
            return false;
        } else {
            return true;
        }
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        int i1 = l & 7;
        boolean flag = (l & 8) > 0;
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.1875F;
        float f3 = 0.125F;

        if (flag) {
            f3 = 0.0625F;
        }

        if (i1 == 1) {
            this.a(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
        } else if (i1 == 2) {
            this.a(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
        } else if (i1 == 3) {
            this.a(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
        } else if (i1 == 4) {
            this.a(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
        }
    }

    public void b(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.interact(world, i, j, k, entityhuman);
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        int l = world.getData(i, j, k);
        int i1 = l & 7;
        int j1 = 8 - (l & 8);

        if (j1 == 0) {
            return true;
        } else {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = (j1 != 8) ? 1 : 0;
            int current = (j1 == 8) ? 1 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (j1 == 8)) {
                return true;
            }
            // CraftBukkit end

            world.setData(i, j, k, i1 + j1);
            world.b(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
            world.applyPhysics(i, j, k, this.id);
            if (i1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (i1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (i1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (i1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }

            world.c(i, j, k, this.id, this.d());
            return true;
        }
    }

    public void remove(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        if ((l & 8) > 0) {
            world.applyPhysics(i, j, k, this.id);
            int i1 = l & 7;

            if (i1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (i1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (i1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (i1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }
        }

        super.remove(world, i, j, k);
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0;
    }

    public boolean d(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);

        if ((i1 & 8) == 0) {
            return false;
        } else {
            int j1 = i1 & 7;

            return j1 == 5 && l == 1 ? true : (j1 == 4 && l == 2 ? true : (j1 == 3 && l == 3 ? true : (j1 == 2 && l == 4 ? true : j1 == 1 && l == 5)));
        }
    }

    public boolean isPowerSource() {
        return true;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0) {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 1, 0);
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) return;
                // CraftBukkit end

                world.setData(i, j, k, l & 7);
                world.applyPhysics(i, j, k, this.id);
                int i1 = l & 7;

                if (i1 == 1) {
                    world.applyPhysics(i - 1, j, k, this.id);
                } else if (i1 == 2) {
                    world.applyPhysics(i + 1, j, k, this.id);
                } else if (i1 == 3) {
                    world.applyPhysics(i, j, k - 1, this.id);
                } else if (i1 == 4) {
                    world.applyPhysics(i, j, k + 1, this.id);
                } else {
                    world.applyPhysics(i, j - 1, k, this.id);
                }

                world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
                world.b(i, j, k, i, j, k);
            }
        }
    }

    public void f() {
        float f = 0.1875F;
        float f1 = 0.125F;
        float f2 = 0.125F;

        this.a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }
}
