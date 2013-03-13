package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockFromToEvent; // CraftBukkit

public class BlockDragonEgg extends Block {

    public BlockDragonEgg(int i) {
        super(i, Material.DRAGON_EGG);
        this.a(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    public void onPlace(World world, int i, int j, int k) {
        world.a(i, j, k, this.id, this.a(world));
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        world.a(i, j, k, this.id, this.a(world));
    }

    public void a(World world, int i, int j, int k, Random random) {
        this.k(world, i, j, k);
    }

    private void k(World world, int i, int j, int k) {
        if (BlockSand.canFall(world, i, j - 1, k) && j >= 0) {
            byte b0 = 32;

            if (!BlockSand.instaFall && world.e(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
                // CraftBukkit - added data
                EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.id, world.getData(i, j, k));

                world.addEntity(entityfallingblock);
            } else {
                world.setAir(i, j, k);

                while (BlockSand.canFall(world, i, j - 1, k) && j > 0) {
                    --j;
                }

                if (j > 0) {
                    world.setTypeIdAndData(i, j, k, this.id, 0, 2);
                }
            }
        }
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        this.m(world, i, j, k);
        return true;
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.m(world, i, j, k);
    }

    private void m(World world, int i, int j, int k) {
        if (world.getTypeId(i, j, k) == this.id) {
            for (int l = 0; l < 1000; ++l) {
                int i1 = i + world.random.nextInt(16) - world.random.nextInt(16);
                int j1 = j + world.random.nextInt(8) - world.random.nextInt(8);
                int k1 = k + world.random.nextInt(16) - world.random.nextInt(16);

                if (world.getTypeId(i1, j1, k1) == 0) {
                    // CraftBukkit start
                    org.bukkit.block.Block from = world.getWorld().getBlockAt(i, j, k);
                    org.bukkit.block.Block to = world.getWorld().getBlockAt(i1, j1, k1);
                    BlockFromToEvent event = new BlockFromToEvent(from, to);
                    org.bukkit.Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }

                    i1 = event.getToBlock().getX();
                    j1 = event.getToBlock().getY();
                    k1 = event.getToBlock().getZ();
                    // CraftBukkit end

                    if (!world.isStatic) {
                        world.setTypeIdAndData(i1, j1, k1, this.id, world.getData(i, j, k), 2);
                        world.setAir(i, j, k);
                    } else {
                        short short1 = 128;

                        for (int l1 = 0; l1 < short1; ++l1) {
                            double d0 = world.random.nextDouble();
                            float f = (world.random.nextFloat() - 0.5F) * 0.2F;
                            float f1 = (world.random.nextFloat() - 0.5F) * 0.2F;
                            float f2 = (world.random.nextFloat() - 0.5F) * 0.2F;
                            double d1 = (double) i1 + (double) (i - i1) * d0 + (world.random.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            double d2 = (double) j1 + (double) (j - j1) * d0 + world.random.nextDouble() * 1.0D - 0.5D;
                            double d3 = (double) k1 + (double) (k - k1) * d0 + (world.random.nextDouble() - 0.5D) * 1.0D + 0.5D;

                            world.addParticle("portal", d1, d2, d3, (double) f, (double) f1, (double) f2);
                        }
                    }

                    return;
                }
            }
        }
    }

    public int a(World world) {
        return 5;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int d() {
        return 27;
    }
}
