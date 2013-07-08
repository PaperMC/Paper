package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class BlockPistonExtension extends Block {

    public BlockPistonExtension(int i) {
        super(i, Material.PISTON);
        this.a(k);
        this.c(0.5F);
    }

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {
        if (entityhuman.abilities.canInstantlyBuild) {
            int i1 = d(l);
            int j1 = world.getTypeId(i - Facing.b[i1], j - Facing.c[i1], k - Facing.d[i1]);

            if (j1 == Block.PISTON.id || j1 == Block.PISTON_STICKY.id) {
                world.setAir(i - Facing.b[i1], j - Facing.c[i1], k - Facing.d[i1]);
            }
        }

        super.a(world, i, j, k, l, entityhuman);
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        super.remove(world, i, j, k, l, i1);
        if ((i1 & 7) >= Facing.OPPOSITE_FACING.length) return; // CraftBukkit - fix a piston AIOOBE issue
        int j1 = Facing.OPPOSITE_FACING[d(i1)];

        i += Facing.b[j1];
        j += Facing.c[j1];
        k += Facing.d[j1];
        int k1 = world.getTypeId(i, j, k);

        if (k1 == Block.PISTON.id || k1 == Block.PISTON_STICKY.id) {
            i1 = world.getData(i, j, k);
            if (BlockPiston.e(i1)) {
                Block.byId[k1].c(world, i, j, k, i1, 0);
                world.setAir(i, j, k);
            }
        }
    }

    public int d() {
        return 17;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return false;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
        int l = world.getData(i, j, k);
        float f = 0.25F;
        float f1 = 0.375F;
        float f2 = 0.625F;
        float f3 = 0.25F;
        float f4 = 0.75F;

        switch (d(l)) {
        case 0:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            this.a(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            break;

        case 1:
            this.a(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            this.a(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            break;

        case 2:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            this.a(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            break;

        case 3:
            this.a(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            this.a(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            break;

        case 4:
            this.a(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            this.a(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            break;

        case 5:
            this.a(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
            this.a(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
            super.a(world, i, j, k, axisalignedbb, list, entity);
        }

        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        float f = 0.25F;

        switch (d(l)) {
        case 0:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            break;

        case 1:
            this.a(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;

        case 2:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
            break;

        case 3:
            this.a(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
            break;

        case 4:
            this.a(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
            break;

        case 5:
            this.a(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        int i1 = d(world.getData(i, j, k));
        if ((i1 & 7) >= Facing.OPPOSITE_FACING.length) return; // CraftBukkit - fix a piston AIOOBE issue
        int j1 = world.getTypeId(i - Facing.b[i1], j - Facing.c[i1], k - Facing.d[i1]);

        if (j1 != Block.PISTON.id && j1 != Block.PISTON_STICKY.id) {
            world.setAir(i, j, k);
        } else {
            Block.byId[j1].doPhysics(world, i - Facing.b[i1], j - Facing.c[i1], k - Facing.d[i1], l);
        }
    }

    public static int d(int i) {
        return i & 7;
    }
}
