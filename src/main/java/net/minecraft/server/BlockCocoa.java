package net.minecraft.server;

import java.util.Random;

public class BlockCocoa extends BlockDirectional {

    public static final String[] a = new String[] { "cocoa_0", "cocoa_1", "cocoa_2"};

    public BlockCocoa(int i) {
        super(i, Material.PLANT);
        this.b(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!this.f(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeIdAndData(i, j, k, 0, 0, 2);
        } else if (world.random.nextInt(5) == 0) {
            int l = world.getData(i, j, k);
            int i1 = c(l);

            if (i1 < 2) {
                ++i1;
                org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this.id, i1 << 2 | j(l)); // CraftBukkit
            }
        }
    }

    public boolean f(World world, int i, int j, int k) {
        int l = j(world.getData(i, j, k));

        i += Direction.a[l];
        k += Direction.b[l];
        int i1 = world.getTypeId(i, j, k);

        return i1 == Block.LOG.id && BlockLog.f(world.getData(i, j, k)) == 3;
    }

    public int d() {
        return 28;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        this.updateShape(world, i, j, k);
        return super.b(world, i, j, k);
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        int i1 = j(l);
        int j1 = c(l);
        int k1 = 4 + j1 * 2;
        int l1 = 5 + j1 * 2;
        float f = (float) k1 / 2.0F;

        switch (i1) {
        case 0:
            this.a((8.0F - f) / 16.0F, (12.0F - (float) l1) / 16.0F, (15.0F - (float) k1) / 16.0F, (8.0F + f) / 16.0F, 0.75F, 0.9375F);
            break;

        case 1:
            this.a(0.0625F, (12.0F - (float) l1) / 16.0F, (8.0F - f) / 16.0F, (1.0F + (float) k1) / 16.0F, 0.75F, (8.0F + f) / 16.0F);
            break;

        case 2:
            this.a((8.0F - f) / 16.0F, (12.0F - (float) l1) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F, (1.0F + (float) k1) / 16.0F);
            break;

        case 3:
            this.a((15.0F - (float) k1) / 16.0F, (12.0F - (float) l1) / 16.0F, (8.0F - f) / 16.0F, 0.9375F, 0.75F, (8.0F + f) / 16.0F);
        }
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        int l = ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 0) % 4;

        world.setData(i, j, k, l, 2);
    }

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
        if (l == 1 || l == 0) {
            l = 2;
        }

        return Direction.f[Direction.e[l]];
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!this.f(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeIdAndData(i, j, k, 0, 0, 2);
        }
    }

    public static int c(int i) {
        return (i & 12) >> 2;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        int j1 = c(l);
        byte b0 = 1;

        if (j1 >= 2) {
            b0 = 3;
        }

        for (int k1 = 0; k1 < b0; ++k1) {
            this.b(world, i, j, k, new ItemStack(Item.INK_SACK, 1, 3));
        }
    }

    public int getDropData(World world, int i, int j, int k) {
        return 3;
    }
}
