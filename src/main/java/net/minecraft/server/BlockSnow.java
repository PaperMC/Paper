package net.minecraft.server;

import java.util.Random;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockSnow extends Block {

    protected BlockSnow(int i, int j) {
        super(i, j, Material.SNOW_LAYER);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.a(true);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        int l = world.getData(i, j, k) & 7;

        return l >= 3 ? AxisAlignedBB.b((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) ((float) j + 0.5F), (double) k + this.maxZ) : null;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k) & 7;
        float f = (float) (2 * (1 + l)) / 16.0F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j - 1, k);

        return l != 0 && Block.byId[l].a() ? world.getMaterial(i, j - 1, k).isSolid() : false;
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        this.g(world, i, j, k);
    }

    private boolean g(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
            return false;
        } else {
            return true;
        }
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        int i1 = Item.SNOW_BALL.id;
        float f = 0.7F;
        double d0 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
        double d1 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
        double d2 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double) i + d0, (double) j + d1, (double) k + d2, new ItemStack(i1, 1, 0));

        entityitem.pickupDelay = 10;
        world.addEntity(entityitem);
        world.setTypeId(i, j, k, 0);
        entityhuman.a(StatisticList.C[this.id], 1);
    }

    public int a(int i, Random random) {
        return Item.SNOW_BALL.id;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.a(EnumSkyBlock.BLOCK, i, j, k) > 11) {
            // CraftBukkit start
            if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(i, j, k), 0).isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
        }
    }
}
