package net.minecraft.server;

import java.util.Random;

public class BlockDiode extends Block {

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
        return !world.e(i, j - 1, k) ? false : super.canPlace(world, i, j, k);
    }

    public boolean f(World world, int i, int j, int k) {
        return !world.e(i, j - 1, k) ? false : super.f(world, i, j, k);
    }

    public void a(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);
        boolean flag = this.f(world, i, j, k, l);

        if (this.c && !flag) {
            world.setTypeIdAndData(i, j, k, Block.DIODE_OFF.id, l);
            this.postPlace(world, i, j, k, this.id); // CraftBukkit - update indirect neighbors
        } else if (!this.c) {
            world.setTypeIdAndData(i, j, k, Block.DIODE_ON.id, l);
            this.postPlace(world, i, j, k, this.id); // CraftBukkit - update indirect neighbors
            if (!flag) {
                int i1 = (l & 12) >> 2;

                world.c(i, j, k, Block.DIODE_ON.id, b[i1] * 2);
            }
        }
    }

    public int a(int i, int j) {
        return i == 0 ? (this.c ? 99 : 115) : (i == 1 ? (this.c ? 147 : 131) : 5);
    }

    public int c() {
        return 15;
    }

    public int a(int i) {
        return this.a(i, 0);
    }

    public boolean d(World world, int i, int j, int k, int l) {
        return this.a(world, i, j, k, l);
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!this.c) {
            return false;
        } else {
            int i1 = iblockaccess.getData(i, j, k) & 3;

            return i1 == 0 && l == 3 ? true : (i1 == 1 && l == 4 ? true : (i1 == 2 && l == 2 ? true : i1 == 3 && l == 5));
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.f(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
        } else {
            int i1 = world.getData(i, j, k);
            boolean flag = this.f(world, i, j, k, i1);
            int j1 = (i1 & 12) >> 2;

            if (this.c && !flag) {
                world.c(i, j, k, this.id, b[j1] * 2);
            } else if (!this.c && flag) {
                world.c(i, j, k, this.id, b[j1] * 2);
            }
        }
    }

    private boolean f(World world, int i, int j, int k, int l) {
        int i1 = l & 3;

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

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        int l = world.getData(i, j, k);
        int i1 = (l & 12) >> 2;

        i1 = i1 + 1 << 2 & 12;
        world.setData(i, j, k, i1 | l & 3);
        return true;
    }

    public boolean isPowerSource() {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;

        world.setData(i, j, k, l);
        boolean flag = this.f(world, i, j, k, l);

        if (flag) {
            world.c(i, j, k, this.id, 1);
        }
    }

    public void postPlace(World world, int i, int j, int k, int l) { // CraftBukkit - onPlace(World, int, int, int) -> postPlace(World, int, int, int, int)
        world.applyPhysics(i + 1, j, k, this.id);
        world.applyPhysics(i - 1, j, k, this.id);
        world.applyPhysics(i, j, k + 1, this.id);
        world.applyPhysics(i, j, k - 1, this.id);
        world.applyPhysics(i, j - 1, k, this.id);
        world.applyPhysics(i, j + 1, k, this.id);
    }

    public void postBreak(World world, int i, int j, int k, int l) {
        if (this.c) {
            world.applyPhysics(i, j + 1, k, this.id);
        }

        super.postBreak(world, i, j, k, l);
    }

    public boolean a() {
        return false;
    }

    public int getDropType(int i, Random random, int j) {
        return Item.DIODE.id;
    }
}
