package net.minecraft.server;

import java.util.Random;

public class BlockSnow extends Block {

    protected BlockSnow(int i) {
        super(i, Material.SNOW_LAYER);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.b(true);
        this.a(CreativeModeTab.c);
        this.d(0);
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        int l = world.getData(i, j, k) & 7;
        float f = 0.125F;

        return AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) ((float) j + (float) l * f), (double) k + this.maxZ);
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public void g() {
        this.d(0);
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        this.d(iblockaccess.getData(i, j, k));
    }

    protected void d(int i) {
        int j = i & 7;
        float f = (float) (2 * (1 + j)) / 16.0F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j - 1, k);

        return l == 0 ? false : (l == this.id && (world.getData(i, j - 1, k) & 7) == 7 ? true : (l != Block.LEAVES.id && !Block.byId[l].c() ? false : world.getMaterial(i, j - 1, k).isSolid()));
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        this.m(world, i, j, k);
    }

    private boolean m(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            return false;
        } else {
            return true;
        }
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        int i1 = Item.SNOW_BALL.id;
        int j1 = l & 7;

        this.b(world, i, j, k, new ItemStack(i1, j1 + 1, 0));
        world.setAir(i, j, k);
        entityhuman.a(StatisticList.C[this.id], 1);
    }

    public int getDropType(int i, Random random, int j) {
        return Item.SNOW_BALL.id;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.b(EnumSkyBlock.BLOCK, i, j, k) > 11) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(i, j, k), 0).isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
        }
    }
}
