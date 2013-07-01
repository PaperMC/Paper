package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public abstract class BlockDiodeAbstract extends BlockDirectional {

    protected final boolean a;

    protected BlockDiodeAbstract(int i, boolean flag) {
        super(i, Material.ORIENTABLE);
        this.a = flag;
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return !world.w(i, j - 1, k) ? false : super.canPlace(world, i, j, k);
    }

    public boolean f(World world, int i, int j, int k) {
        return !world.w(i, j - 1, k) ? false : super.f(world, i, j, k);
    }

    public void a(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        if (!this.e((IBlockAccess) world, i, j, k, l)) {
            boolean flag = this.d(world, i, j, k, l);

            if (this.a && !flag) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end

                world.setTypeIdAndData(i, j, k, this.j().id, l, 2);
            } else if (!this.a) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end

                world.setTypeIdAndData(i, j, k, this.i().id, l, 2);
                if (!flag) {
                    world.a(i, j, k, this.i().id, this.h(l), -1);
                }
            }
        }
    }

    public int d() {
        return 36;
    }

    protected boolean c(int i) {
        return this.a;
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return this.b(iblockaccess, i, j, k, l);
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getData(i, j, k);

        if (!this.c(i1)) {
            return 0;
        } else {
            int j1 = j(i1);

            return j1 == 0 && l == 3 ? this.d(iblockaccess, i, j, k, i1) : (j1 == 1 && l == 4 ? this.d(iblockaccess, i, j, k, i1) : (j1 == 2 && l == 2 ? this.d(iblockaccess, i, j, k, i1) : (j1 == 3 && l == 5 ? this.d(iblockaccess, i, j, k, i1) : 0)));
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.f(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
        } else {
            this.f(world, i, j, k, l);
        }
    }

    protected void f(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);

        if (!this.e((IBlockAccess) world, i, j, k, i1)) {
            boolean flag = this.d(world, i, j, k, i1);

            if ((this.a && !flag || !this.a && flag) && !world.a(i, j, k, this.id)) {
                byte b0 = -1;

                if (this.h(world, i, j, k, i1)) {
                    b0 = -3;
                } else if (this.a) {
                    b0 = -2;
                }

                world.a(i, j, k, this.id, this.k_(i1), b0);
            }
        }
    }

    public boolean e(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    protected boolean d(World world, int i, int j, int k, int l) {
        return this.e(world, i, j, k, l) > 0;
    }

    protected int e(World world, int i, int j, int k, int l) {
        int i1 = j(l);
        int j1 = i + Direction.a[i1];
        int k1 = k + Direction.b[i1];
        int l1 = world.getBlockFacePower(j1, j, k1, Direction.d[i1]);

        return l1 >= 15 ? l1 : Math.max(l1, world.getTypeId(j1, j, k1) == Block.REDSTONE_WIRE.id ? world.getData(j1, j, k1) : 0);
    }

    protected int f(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = j(l);

        switch (i1) {
        case 0:
        case 2:
            return Math.max(this.g(iblockaccess, i - 1, j, k, 4), this.g(iblockaccess, i + 1, j, k, 5));

        case 1:
        case 3:
            return Math.max(this.g(iblockaccess, i, j, k + 1, 3), this.g(iblockaccess, i, j, k - 1, 2));

        default:
            return 0;
        }
    }

    protected int g(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getTypeId(i, j, k);

        return this.e(i1) ? (i1 == Block.REDSTONE_WIRE.id ? iblockaccess.getData(i, j, k) : iblockaccess.getBlockPower(i, j, k, l)) : 0;
    }

    public boolean isPowerSource() {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        int l = ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;

        world.setData(i, j, k, l, 3);
        boolean flag = this.d(world, i, j, k, l);

        if (flag) {
            world.a(i, j, k, this.id, 1);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        this.h_(world, i, j, k);
    }

    protected void h_(World world, int i, int j, int k) {
        int l = j(world.getData(i, j, k));

        if (l == 1) {
            world.g(i + 1, j, k, this.id);
            world.c(i + 1, j, k, this.id, 4);
        }

        if (l == 3) {
            world.g(i - 1, j, k, this.id);
            world.c(i - 1, j, k, this.id, 5);
        }

        if (l == 2) {
            world.g(i, j, k + 1, this.id);
            world.c(i, j, k + 1, this.id, 2);
        }

        if (l == 0) {
            world.g(i, j, k - 1, this.id);
            world.c(i, j, k - 1, this.id, 3);
        }
    }

    public void postBreak(World world, int i, int j, int k, int l) {
        if (this.a) {
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
        }

        super.postBreak(world, i, j, k, l);
    }

    public boolean c() {
        return false;
    }

    protected boolean e(int i) {
        Block block = Block.byId[i];

        return block != null && block.isPowerSource();
    }

    protected int d(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return 15;
    }

    public static boolean f(int i) {
        return Block.DIODE_OFF.g(i) || Block.REDSTONE_COMPARATOR_OFF.g(i);
    }

    public boolean g(int i) {
        return i == this.i().id || i == this.j().id;
    }

    public boolean h(World world, int i, int j, int k, int l) {
        int i1 = j(l);

        if (f(world.getTypeId(i - Direction.a[i1], j, k - Direction.b[i1]))) {
            int j1 = world.getData(i - Direction.a[i1], j, k - Direction.b[i1]);
            int k1 = j(j1);

            return k1 != i1;
        } else {
            return false;
        }
    }

    protected int h(int i) {
        return this.k_(i);
    }

    protected abstract int k_(int i);

    protected abstract BlockDiodeAbstract i();

    protected abstract BlockDiodeAbstract j();

    public boolean i(int i) {
        return this.g(i);
    }
}
