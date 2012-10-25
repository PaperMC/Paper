package net.minecraft.server;

import java.util.List;
import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockButton extends Block {

    private final boolean a;

    protected BlockButton(int i, int j, boolean flag) {
        super(i, j, Material.ORIENTABLE);
        this.b(true);
        this.a(CreativeModeTab.d);
        this.a = flag;
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public int r_() {
        return this.a ? 30 : 20;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return l == 2 && world.s(i, j, k + 1) ? true : (l == 3 && world.s(i, j, k - 1) ? true : (l == 4 && world.s(i + 1, j, k) ? true : l == 5 && world.s(i - 1, j, k)));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.s(i - 1, j, k) ? true : (world.s(i + 1, j, k) ? true : (world.s(i, j, k - 1) ? true : world.s(i, j, k + 1)));
    }

    public void postPlace(World world, int i, int j, int k, int l, float f, float f1, float f2) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 8;

        i1 &= 7;
        if (l == 2 && world.s(i, j, k + 1)) {
            i1 = 4;
        } else if (l == 3 && world.s(i, j, k - 1)) {
            i1 = 3;
        } else if (l == 4 && world.s(i + 1, j, k)) {
            i1 = 2;
        } else if (l == 5 && world.s(i - 1, j, k)) {
            i1 = 1;
        } else {
            i1 = this.l(world, i, j, k);
        }

        world.setData(i, j, k, i1 + j1);
    }

    private int l(World world, int i, int j, int k) {
        return world.s(i - 1, j, k) ? 1 : (world.s(i + 1, j, k) ? 2 : (world.s(i, j, k - 1) ? 3 : (world.s(i, j, k + 1) ? 4 : 1)));
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (this.n(world, i, j, k)) {
            int i1 = world.getData(i, j, k) & 7;
            boolean flag = false;

            if (!world.s(i - 1, j, k) && i1 == 1) {
                flag = true;
            }

            if (!world.s(i + 1, j, k) && i1 == 2) {
                flag = true;
            }

            if (!world.s(i, j, k - 1) && i1 == 3) {
                flag = true;
            }

            if (!world.s(i, j, k + 1) && i1 == 4) {
                flag = true;
            }

            if (flag) {
                this.c(world, i, j, k, world.getData(i, j, k), 0);
                world.setTypeId(i, j, k, 0);
            }
        }
    }

    private boolean n(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
            return false;
        } else {
            return true;
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);

        this.e(l);
    }

    private void e(int i) {
        int j = i & 7;
        boolean flag = (i & 8) > 0;
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.1875F;
        float f3 = 0.125F;

        if (flag) {
            f3 = 0.0625F;
        }

        if (j == 1) {
            this.a(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
        } else if (j == 2) {
            this.a(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
        } else if (j == 3) {
            this.a(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
        } else if (j == 4) {
            this.a(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
        }
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {}

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 7;
        int k1 = 8 - (i1 & 8);

        if (k1 == 0) {
            return true;
        } else {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = (k1 != 8) ? 1 : 0;
            int current = (k1 == 8) ? 1 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (k1 == 8)) {
                return true;
            }
            // CraftBukkit end

            world.setData(i, j, k, j1 + k1);
            world.e(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
            this.d(world, i, j, k, j1);
            world.a(i, j, k, this.id, this.r_());
            return true;
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        if ((i1 & 8) > 0) {
            int j1 = i1 & 7;

            this.d(world, i, j, k, j1);
        }

        super.remove(world, i, j, k, l, i1);
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0;
    }

    public boolean c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getData(i, j, k);

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

    public void b(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0) {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 1, 0);
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                // CraftBukkit end

                if (this.a) {
                    this.o(world, i, j, k);
                } else {
                    world.setData(i, j, k, l & 7);
                    int i1 = l & 7;

                    this.d(world, i, j, k, i1);
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
                    world.e(i, j, k, i, j, k);
                }
            }
        }
    }

    public void f() {
        float f = 0.1875F;
        float f1 = 0.125F;
        float f2 = 0.125F;

        this.a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (!world.isStatic) {
            if (this.a) {
                if ((world.getData(i, j, k) & 8) == 0) {
                    this.o(world, i, j, k);
                }
            }
        }
    }

    private void o(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = l & 7;
        boolean flag = (l & 8) != 0;

        this.e(l);
        List list = world.a(EntityArrow.class, AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ));
        boolean flag1 = !list.isEmpty();

        if (flag1 && !flag) {
            world.setData(i, j, k, i1 | 8);
            this.d(world, i, j, k, i1);
            world.e(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!flag1 && flag) {
            world.setData(i, j, k, i1);
            this.d(world, i, j, k, i1);
            world.e(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (flag1) {
            world.a(i, j, k, this.id, this.r_());
        }
    }

    private void d(World world, int i, int j, int k, int l) {
        world.applyPhysics(i, j, k, this.id);
        if (l == 1) {
            world.applyPhysics(i - 1, j, k, this.id);
        } else if (l == 2) {
            world.applyPhysics(i + 1, j, k, this.id);
        } else if (l == 3) {
            world.applyPhysics(i, j, k - 1, this.id);
        } else if (l == 4) {
            world.applyPhysics(i, j, k + 1, this.id);
        } else {
            world.applyPhysics(i, j - 1, k, this.id);
        }
    }
}
