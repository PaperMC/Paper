package net.minecraft.server;

import java.util.Random;

public class BlockDragonEgg extends Block {

    public BlockDragonEgg(int i, int j) {
        super(i, j, Material.DRAGON_EGG);
    }

    public void onPlace(World world, int i, int j, int k) {
        world.c(i, j, k, this.id, this.d());
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        world.c(i, j, k, this.id, this.d());
    }

    public void a(World world, int i, int j, int k, Random random) {
        this.g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
        if (BlockSand.canFall(world, i, j - 1, k) && j >= 0) {
            byte b0 = 32;

            if (!BlockSand.instaFall && world.a(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
                // CraftBukkit - added data
                EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.id, world.getData(i, j, k));

                world.addEntity(entityfallingblock);
            } else {
                world.setTypeId(i, j, k, 0);

                while (BlockSand.canFall(world, i, j - 1, k) && j > 0) {
                    --j;
                }

                if (j > 0) {
                    world.setTypeId(i, j, k, this.id);
                }
            }
        }
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.h(world, i, j, k);
        return true;
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.h(world, i, j, k);
    }

    private void h(World world, int i, int j, int k) {
        if (world.getTypeId(i, j, k) == this.id) {
            if (!world.isStatic) {
                for (int l = 0; l < 1000; ++l) {
                    int i1 = i + world.random.nextInt(16) - world.random.nextInt(16);
                    int j1 = j + world.random.nextInt(8) - world.random.nextInt(8);
                    int k1 = k + world.random.nextInt(16) - world.random.nextInt(16);

                    if (world.getTypeId(i1, j1, k1) == 0) {
                        // CraftBukkit start
                        org.bukkit.block.Block from = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.Block to = world.getWorld().getBlockAt(i1, j1, k1);
                        org.bukkit.event.block.BlockFromToEvent event = new org.bukkit.event.block.BlockFromToEvent(from, to);
                        org.bukkit.Bukkit.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            i1 = event.getToBlock().getX();
                            j1 = event.getToBlock().getY();
                            k1 = event.getToBlock().getZ();
                        } else return;
                        // CraftBukkit end

                        world.setTypeIdAndData(i1, j1, k1, this.id, world.getData(i, j, k));
                        world.setTypeId(i, j, k, 0);
                        short short1 = 128;

                        for (int l1 = 0; l1 < short1; ++l1) {
                            double d0 = world.random.nextDouble();
                            float f = (world.random.nextFloat() - 0.5F) * 0.2F;
                            float f1 = (world.random.nextFloat() - 0.5F) * 0.2F;
                            float f2 = (world.random.nextFloat() - 0.5F) * 0.2F;
                            double d1 = (double) i1 + (double) (i - i1) * d0 + (world.random.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            double d2 = (double) j1 + (double) (j - j1) * d0 + world.random.nextDouble() * 1.0D - 0.5D;
                            double d3 = (double) k1 + (double) (k - k1) * d0 + (world.random.nextDouble() - 0.5D) * 1.0D + 0.5D;

                            world.a("portal", d1, d2, d3, (double) f, (double) f1, (double) f2);
                        }

                        return;
                    }
                }
            }
        }
    }

    public int d() {
        return 3;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return super.canPlace(world, i, j, k);
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int c() {
        return 27;
    }
}
