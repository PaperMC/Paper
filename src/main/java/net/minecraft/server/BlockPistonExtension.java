package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class BlockPistonExtension extends Block {

    public BlockPistonExtension() {
        super(Material.PISTON);
        this.a(i);
        this.c(0.5F);
    }

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {
        if (entityhuman.abilities.canInstantlyBuild) {
            int i1 = b(l);
            Block block = world.getType(i - Facing.b[i1], j - Facing.c[i1], k - Facing.d[i1]);

            if (block == Blocks.PISTON || block == Blocks.PISTON_STICKY) {
                world.setAir(i - Facing.b[i1], j - Facing.c[i1], k - Facing.d[i1]);
            }
        }

        super.a(world, i, j, k, l, entityhuman);
    }

    public void remove(World world, int i, int j, int k, Block block, int l) {
        super.remove(world, i, j, k, block, l);
        if ((l & 7) >= Facing.OPPOSITE_FACING.length) return; // CraftBukkit - fix a piston AIOOBE issue
        int i1 = Facing.OPPOSITE_FACING[b(l)];

        i += Facing.b[i1];
        j += Facing.c[i1];
        k += Facing.d[i1];
        Block block1 = world.getType(i, j, k);

        if (block1 == Blocks.PISTON || block1 == Blocks.PISTON_STICKY) {
            l = world.getData(i, j, k);
            if (BlockPiston.c(l)) {
                block1.b(world, i, j, k, l, 0);
                world.setAir(i, j, k);
            }
        }
    }

    public int b() {
        return 17;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
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

        switch (b(l)) {
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

        switch (b(l)) {
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

    public void doPhysics(World world, int i, int j, int k, Block block) {
        int l = b(world.getData(i, j, k));
        if ((l & 7) >= Facing.OPPOSITE_FACING.length) return; // CraftBukkit - fix a piston AIOOBE issue
        Block block1 = world.getType(i - Facing.b[l], j - Facing.c[l], k - Facing.d[l]);

        if (block1 != Blocks.PISTON && block1 != Blocks.PISTON_STICKY) {
            world.setAir(i, j, k);
        } else {
            block1.doPhysics(world, i - Facing.b[l], j - Facing.c[l], k - Facing.d[l], block);
        }
    }

    public static int b(int i) {
        return i & 7;
    }
}
