package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory;  // CraftBukkit

public class BlockStationary extends BlockFluids {

    protected BlockStationary(Material material) {
        super(material);
        this.a(false);
        if (material == Material.LAVA) {
            this.a(true);
        }
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        super.doPhysics(world, i, j, k, block);
        if (world.getType(i, j, k) == this) {
            this.n(world, i, j, k);
        }
    }

    private void n(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.setTypeAndData(i, j, k, Block.e(Block.b((Block) this) - 1), l, 2);
        world.a(i, j, k, Block.e(Block.b((Block) this) - 1), this.a(world));
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (this.material == Material.LAVA) {
            int l = random.nextInt(3);

            int i1;

            // CraftBukkit start - Prevent lava putting something on fire, remember igniter block coords
            int x = i;
            int y = j;
            int z = k;
            // CraftBukkit end

            for (i1 = 0; i1 < l; ++i1) {
                i += random.nextInt(3) - 1;
                ++j;
                k += random.nextInt(3) - 1;
                Block block = world.getType(i, j, k);

                if (block.material == Material.AIR) {
                    if (this.o(world, i - 1, j, k) || this.o(world, i + 1, j, k) || this.o(world, i, j, k - 1) || this.o(world, i, j, k + 1) || this.o(world, i, j - 1, k) || this.o(world, i, j + 1, k)) {
                        // CraftBukkit start - Prevent lava putting something on fire
                        if (world.getType(i, j, k) != Blocks.FIRE) {
                            if (CraftEventFactory.callBlockIgniteEvent(world, i, j, k, x, y, z).isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeUpdate(i, j, k, Blocks.FIRE);
                        return;
                    }
                } else if (block.material.isSolid()) {
                    return;
                }
            }

            if (l == 0) {
                i1 = i;
                int j1 = k;

                for (int k1 = 0; k1 < 3; ++k1) {
                    i = i1 + random.nextInt(3) - 1;
                    k = j1 + random.nextInt(3) - 1;
                    if (world.isEmpty(i, j + 1, k) && this.o(world, i, j, k)) {
                        // CraftBukkit start - Prevent lava putting something on fire
                        if (world.getType(i, j + 1, k) != Blocks.FIRE) {
                            if (CraftEventFactory.callBlockIgniteEvent(world, i, j + 1, k, x, y, z).isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeUpdate(i, j + 1, k, Blocks.FIRE);
                    }
                }
            }
        }
    }

    private boolean o(World world, int i, int j, int k) {
        return world.getType(i, j, k).getMaterial().isBurnable();
    }
}
