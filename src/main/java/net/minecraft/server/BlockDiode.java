package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockDiode extends BlockDirectional {

    public static final double[] a = new double[] { -0.0625D, 0.0625D, 0.1875D, 0.3125D};
    private static final int[] b = new int[] { 1, 2, 3, 4};
    private final boolean c;

    protected BlockDiode(int i, boolean flag) {
        super(i, 6, Material.ORIENTABLE);
        this.c = flag;
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return !world.v(i, j - 1, k) ? false : super.canPlace(world, i, j, k);
    }

    public boolean d(World world, int i, int j, int k) {
        return !world.v(i, j - 1, k) ? false : super.d(world, i, j, k);
    }

    public void b(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);
        boolean flag = this.e(world, i, j, k, l);

        if (!flag) {
            boolean flag1 = this.i(world, i, j, k, l);

            if (this.c && !flag1) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 15, 0).getNewCurrent() != 0) {
                    return;
                }
                // CraftBukkit end

                world.setTypeIdAndData(i, j, k, Block.DIODE_OFF.id, l);
            } else if (!this.c) {
                // CraftBukkit start
                if (CraftEventFactory.callRedstoneChange(world, i, j, k, 0, 15).getNewCurrent() != 15) {
                    return;
                }
                // CraftBukkit end

                world.setTypeIdAndData(i, j, k, Block.DIODE_ON.id, l);
                if (!flag1) {
                    int i1 = (l & 12) >> 2;

                    world.a(i, j, k, Block.DIODE_ON.id, b[i1] * 2);
                }
            }
        }
    }

    public int a(int i, int j) {
        return i == 0 ? (this.c ? 99 : 115) : (i == 1 ? (this.c ? 147 : 131) : 5);
    }

    public int d() {
        return 15;
    }

    public int a(int i) {
        return this.a(i, 0);
    }

    public boolean c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return this.b(iblockaccess, i, j, k, l);
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.c) {
            return false;
        } else {
            int i1 = e(iblockaccess.getData(i, j, k));

            return i1 == 0 && l == 3 ? true : (i1 == 1 && l == 4 ? true : (i1 == 2 && l == 2 ? true : i1 == 3 && l == 5));
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.d(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
        } else {
            int i1 = world.getData(i, j, k);
            boolean flag = this.e(world, i, j, k, i1);

            if (!flag) {
                boolean flag1 = this.i(world, i, j, k, i1);
                int j1 = (i1 & 12) >> 2;

                if (this.c && !flag1 || !this.c && flag1) {
                    byte b0 = 0;

                    if (this.d(world, i, j, k, i1)) {
                        b0 = -1;
                    }

                    world.a(i, j, k, this.id, b[j1] * 2, b0);
                }
            }
        }
    }

    private boolean i(World world, int i, int j, int k, int l) {
        int i1 = e(l);

        switch (i1) {
        case 0:
            return world.isBlockFaceIndirectlyPowered(i, j, k + 1, 3) || world.getTypeId(i, j, k + 1) == Block.REDSTONE_WIRE.id && world.getData(i, j, k + 1) > 0;

        case 1:
            return world.isBlockFaceIndirectlyPowered(i - 1, j, k, 4) || world.getTypeId(i - 1, j, k) == Block.REDSTONE_WIRE.id && world.getData(i - 1, j, k) > 0;

        case 2:
            return world.isBlockFaceIndirectlyPowered(i, j, k - 1, 2) || world.getTypeId(i, j, k - 1) == Block.REDSTONE_WIRE.id && world.getData(i, j, k - 1) > 0;

        case 3:
            return world.isBlockFaceIndirectlyPowered(i + 1, j, k, 5) || world.getTypeId(i + 1, j, k) == Block.REDSTONE_WIRE.id && world.getData(i + 1, j, k) > 0;

        default:
            return false;
        }
    }

    public boolean e(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = e(l);

        switch (i1) {
        case 0:
        case 2:
            return iblockaccess.isBlockFacePowered(i - 1, j, k, 4) && c(iblockaccess.getTypeId(i - 1, j, k)) || iblockaccess.isBlockFacePowered(i + 1, j, k, 5) && c(iblockaccess.getTypeId(i + 1, j, k));

        case 1:
        case 3:
            return iblockaccess.isBlockFacePowered(i, j, k + 1, 3) && c(iblockaccess.getTypeId(i, j, k + 1)) || iblockaccess.isBlockFacePowered(i, j, k - 1, 2) && c(iblockaccess.getTypeId(i, j, k - 1));

        default:
            return false;
        }
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        int i1 = world.getData(i, j, k);
        int j1 = (i1 & 12) >> 2;

        j1 = j1 + 1 << 2 & 12;
        world.setData(i, j, k, j1 | i1 & 3);
        return true;
    }

    public boolean isPowerSource() {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;

        world.setData(i, j, k, l);
        boolean flag = this.i(world, i, j, k, l);

        if (flag) {
            world.a(i, j, k, this.id, 1);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        world.applyPhysics(i + 1, j, k, this.id);
        world.applyPhysics(i - 1, j, k, this.id);
        world.applyPhysics(i, j, k + 1, this.id);
        world.applyPhysics(i, j, k - 1, this.id);
        world.applyPhysics(i, j - 1, k, this.id);
        world.applyPhysics(i, j + 1, k, this.id);
    }

    public void postBreak(World world, int i, int j, int k, int l) {
        if (this.c) {
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

    public int getDropType(int i, Random random, int j) {
        return Item.DIODE.id;
    }

    public static boolean c(int i) {
        return i == Block.DIODE_ON.id || i == Block.DIODE_OFF.id;
    }

    public boolean d(World world, int i, int j, int k, int l) {
        int i1 = e(l);

        if (c(world.getTypeId(i - Direction.a[i1], j, k - Direction.b[i1]))) {
            int j1 = world.getData(i - Direction.a[i1], j, k - Direction.b[i1]);
            int k1 = e(j1);

            return k1 != i1;
        } else {
            return false;
        }
    }
}
