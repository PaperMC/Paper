package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public abstract class BlockDiodeAbstract extends BlockDirectional {

    protected final boolean a;

    protected BlockDiodeAbstract(boolean flag) {
        super(Material.ORIENTABLE);
        this.a = flag;
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean d() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return !World.a((IBlockAccess) world, i, j - 1, k) ? false : super.canPlace(world, i, j, k);
    }

    public boolean j(World world, int i, int j, int k) {
        return !World.a((IBlockAccess) world, i, j - 1, k) ? false : super.j(world, i, j, k);
    }

    public void a(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        if (!this.g((IBlockAccess) world, i, j, k, l)) { // CraftBukkit - Cast world to IBlockAccess to call the right method.
            boolean flag = this.a(world, i, j, k, l);

            if (this.a && !flag) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end

                world.setTypeAndData(i, j, k, this.i(), l, 2);
            } else if (!this.a) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end

                world.setTypeAndData(i, j, k, this.e(), l, 2);
                if (!flag) {
                    world.a(i, j, k, this.e(), this.k(l), -1);
                }
            }
        }
    }

    public int b() {
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
            int j1 = l(i1);

            return j1 == 0 && l == 3 ? this.f(iblockaccess, i, j, k, i1) : (j1 == 1 && l == 4 ? this.f(iblockaccess, i, j, k, i1) : (j1 == 2 && l == 2 ? this.f(iblockaccess, i, j, k, i1) : (j1 == 3 && l == 5 ? this.f(iblockaccess, i, j, k, i1) : 0)));
        }
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (!this.j(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            world.applyPhysics(i + 1, j, k, this);
            world.applyPhysics(i - 1, j, k, this);
            world.applyPhysics(i, j, k + 1, this);
            world.applyPhysics(i, j, k - 1, this);
            world.applyPhysics(i, j - 1, k, this);
            world.applyPhysics(i, j + 1, k, this);
        } else {
            this.b(world, i, j, k, block);
        }
    }

    protected void b(World world, int i, int j, int k, Block block) {
        int l = world.getData(i, j, k);

        if (!this.g((IBlockAccess) world, i, j, k, l)) {
            boolean flag = this.a(world, i, j, k, l);

            if ((this.a && !flag || !this.a && flag) && !world.a(i, j, k, (Block) this)) {
                byte b0 = -1;

                if (this.i(world, i, j, k, l)) {
                    b0 = -3;
                } else if (this.a) {
                    b0 = -2;
                }

                world.a(i, j, k, this, this.b(l), b0);
            }
        }
    }

    public boolean g(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    protected boolean a(World world, int i, int j, int k, int l) {
        return this.h(world, i, j, k, l) > 0;
    }

    protected int h(World world, int i, int j, int k, int l) {
        int i1 = l(l);
        int j1 = i + Direction.a[i1];
        int k1 = k + Direction.b[i1];
        int l1 = world.getBlockFacePower(j1, j, k1, Direction.d[i1]);

        return l1 >= 15 ? l1 : Math.max(l1, world.getType(j1, j, k1) == Blocks.REDSTONE_WIRE ? world.getData(j1, j, k1) : 0);
    }

    protected int h(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = l(l);

        switch (i1) {
        case 0:
        case 2:
            return Math.max(this.i(iblockaccess, i - 1, j, k, 4), this.i(iblockaccess, i + 1, j, k, 5));

        case 1:
        case 3:
            return Math.max(this.i(iblockaccess, i, j, k + 1, 3), this.i(iblockaccess, i, j, k - 1, 2));

        default:
            return 0;
        }
    }

    protected int i(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        Block block = iblockaccess.getType(i, j, k);

        return this.a(block) ? (block == Blocks.REDSTONE_WIRE ? iblockaccess.getData(i, j, k) : iblockaccess.getBlockPower(i, j, k, l)) : 0;
    }

    public boolean isPowerSource() {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        int l = ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;

        world.setData(i, j, k, l, 3);
        boolean flag = this.a(world, i, j, k, l);

        if (flag) {
            world.a(i, j, k, this, 1);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        this.e(world, i, j, k);
    }

    protected void e(World world, int i, int j, int k) {
        int l = l(world.getData(i, j, k));

        if (l == 1) {
            world.e(i + 1, j, k, this);
            world.b(i + 1, j, k, this, 4);
        }

        if (l == 3) {
            world.e(i - 1, j, k, this);
            world.b(i - 1, j, k, this, 5);
        }

        if (l == 2) {
            world.e(i, j, k + 1, this);
            world.b(i, j, k + 1, this, 2);
        }

        if (l == 0) {
            world.e(i, j, k - 1, this);
            world.b(i, j, k - 1, this, 3);
        }
    }

    public void postBreak(World world, int i, int j, int k, int l) {
        if (this.a) {
            world.applyPhysics(i + 1, j, k, this);
            world.applyPhysics(i - 1, j, k, this);
            world.applyPhysics(i, j, k + 1, this);
            world.applyPhysics(i, j, k - 1, this);
            world.applyPhysics(i, j - 1, k, this);
            world.applyPhysics(i, j + 1, k, this);
        }

        super.postBreak(world, i, j, k, l);
    }

    public boolean c() {
        return false;
    }

    protected boolean a(Block block) {
        return block.isPowerSource();
    }

    protected int f(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return 15;
    }

    public static boolean d(Block block) {
        return Blocks.DIODE_OFF.e(block) || Blocks.REDSTONE_COMPARATOR_OFF.e(block);
    }

    public boolean e(Block block) {
        return block == this.e() || block == this.i();
    }

    public boolean i(World world, int i, int j, int k, int l) {
        int i1 = l(l);

        if (d(world.getType(i - Direction.a[i1], j, k - Direction.b[i1]))) {
            int j1 = world.getData(i - Direction.a[i1], j, k - Direction.b[i1]);
            int k1 = l(j1);

            return k1 != i1;
        } else {
            return false;
        }
    }

    protected int k(int i) {
        return this.b(i);
    }

    protected abstract int b(int i);

    protected abstract BlockDiodeAbstract e();

    protected abstract BlockDiodeAbstract i();

    public boolean c(Block block) {
        return this.e(block);
    }
}
