package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public abstract class BlockPressurePlateAbstract extends Block {

    private String a;

    protected BlockPressurePlateAbstract(String s, Material material) {
        super(material);
        this.a = s;
        this.a(CreativeModeTab.d);
        this.a(true);
        this.b(this.d(15));
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        this.b(iblockaccess.getData(i, j, k));
    }

    protected void b(int i) {
        boolean flag = this.c(i) > 0;
        float f = 0.0625F;

        if (flag) {
            this.a(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
        } else {
            this.a(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
        }
    }

    public int a(World world) {
        return 20;
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return true;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return World.a((IBlockAccess) world, i, j - 1, k) || BlockFence.a(world.getType(i, j - 1, k));
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        boolean flag = false;

        if (!World.a((IBlockAccess) world, i, j - 1, k) && !BlockFence.a(world.getType(i, j - 1, k))) {
            flag = true;
        }

        if (flag) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = this.c(world.getData(i, j, k));

            if (l > 0) {
                this.a(world, i, j, k, l);
            }
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (!world.isStatic) {
            int l = this.c(world.getData(i, j, k));

            if (l == 0) {
                this.a(world, i, j, k, l);
            }
        }
    }

    protected void a(World world, int i, int j, int k, int l) {
        int i1 = this.e(world, i, j, k);
        boolean flag = l > 0;
        boolean flag1 = i1 > 0;

        // CraftBukkit start - Interact Pressure Plate
        org.bukkit.World bworld = world.getWorld();
        org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();

        if (flag != flag1) {
            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bworld.getBlockAt(i, j, k), l, i1);
            manager.callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
            i1 = eventRedstone.getNewCurrent();
        }
        // CraftBukkit end

        if (l != i1) {
            world.setData(i, j, k, this.d(i1), 2);
            this.a_(world, i, j, k);
            world.c(i, j, k, i, j, k);
        }

        if (!flag1 && flag) {
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
        } else if (flag1 && !flag) {
            world.makeSound((double) i + 0.5D, (double) j + 0.1D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (flag1) {
            world.a(i, j, k, this, this.a(world));
        }
    }

    protected AxisAlignedBB a(int i, int j, int k) {
        float f = 0.125F;

        return AxisAlignedBB.a().a((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) j + 0.25D, (double) ((float) (k + 1) - f));
    }

    public void remove(World world, int i, int j, int k, Block block, int l) {
        if (this.c(l) > 0) {
            this.a_(world, i, j, k);
        }

        super.remove(world, i, j, k, block, l);
    }

    protected void a_(World world, int i, int j, int k) {
        world.applyPhysics(i, j, k, this);
        world.applyPhysics(i, j - 1, k, this);
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return this.c(iblockaccess.getData(i, j, k));
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return l == 1 ? this.c(iblockaccess.getData(i, j, k)) : 0;
    }

    public boolean isPowerSource() {
        return true;
    }

    public void g() {
        float f = 0.5F;
        float f1 = 0.125F;
        float f2 = 0.5F;

        this.a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }

    public int h() {
        return 1;
    }

    protected abstract int e(World world, int i, int j, int k);

    protected abstract int c(int i);

    protected abstract int d(int i);
}
