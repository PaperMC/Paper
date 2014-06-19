package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class BlockCactus extends Block {

    protected BlockCactus() {
        super(Material.CACTUS);
        this.a(true);
        this.a(CreativeModeTab.c);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.isEmpty(i, j + 1, k)) {
            int l;

            for (l = 1; world.getType(i, j - l, k) == this; ++l) {
                ;
            }

            if (l < 3) {
                int i1 = world.getData(i, j, k);

                if (i1 == 15) {
                    CraftEventFactory.handleBlockGrowEvent(world, i, j + 1, k, this, 0); // CraftBukkit
                    world.setData(i, j, k, 0, 4);
                    this.doPhysics(world, i, j + 1, k, this);
                } else {
                    world.setData(i, j, k, i1 + 1, 4);
                }
            }
        }
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        float f = 0.0625F;

        return AxisAlignedBB.a((double) ((float) i + f), (double) j, (double) ((float) k + f), (double) ((float) (i + 1) - f), (double) ((float) (j + 1) - f), (double) ((float) (k + 1) - f));
    }

    public boolean d() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int b() {
        return 13;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return !super.canPlace(world, i, j, k) ? false : this.j(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (!this.j(world, i, j, k)) {
            world.setAir(i, j, k, true);
        }
    }

    public boolean j(World world, int i, int j, int k) {
        if (world.getType(i - 1, j, k).getMaterial().isBuildable()) {
            return false;
        } else if (world.getType(i + 1, j, k).getMaterial().isBuildable()) {
            return false;
        } else if (world.getType(i, j, k - 1).getMaterial().isBuildable()) {
            return false;
        } else if (world.getType(i, j, k + 1).getMaterial().isBuildable()) {
            return false;
        } else {
            Block block = world.getType(i, j - 1, k);

            return block == Blocks.CACTUS || block == Blocks.SAND;
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        CraftEventFactory.blockDamage = world.getWorld().getBlockAt(i, j, k); // CraftBukkit
        entity.damageEntity(DamageSource.CACTUS, 1.0F);
        CraftEventFactory.blockDamage = null; // CraftBukkit
    }
}
