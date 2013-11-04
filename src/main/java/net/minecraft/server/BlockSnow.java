package net.minecraft.server;

import java.util.Random;

public class BlockSnow extends Block {

    protected BlockSnow() {
        super(Material.PACKED_ICE);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.a(true);
        this.a(CreativeModeTab.c);
        this.b(0);
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        int l = world.getData(i, j, k) & 7;
        float f = 0.125F;

        return AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) ((float) j + (float) l * f), (double) k + this.maxZ);
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public void g() {
        this.b(0);
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        this.b(iblockaccess.getData(i, j, k));
    }

    protected void b(int i) {
        int j = i & 7;
        float f = (float) (2 * (1 + j)) / 16.0F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        Block block = world.getType(i, j - 1, k);

        return block != Blocks.ICE && block != Blocks.PACKED_ICE ? (block.getMaterial() == Material.LEAVES ? true : (block == this && (world.getData(i, j - 1, k) & 7) == 7 ? true : block.c() && block.material.isSolid())) : false;
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        this.m(world, i, j, k);
    }

    private boolean m(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            return false;
        } else {
            return true;
        }
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        int i1 = l & 7;

        this.a(world, i, j, k, new ItemStack(Items.SNOW_BALL, i1 + 1, 0));
        world.setAir(i, j, k);
        entityhuman.a(StatisticList.C[Block.b((Block) this)], 1);
    }

    public Item getDropType(int i, Random random, int j) {
        return Items.SNOW_BALL;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.b(EnumSkyBlock.BLOCK, i, j, k) > 11) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(i, j, k), Blocks.AIR).isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
        }
    }
}
