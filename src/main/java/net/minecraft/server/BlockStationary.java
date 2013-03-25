package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.event.CraftEventFactory;  // CraftBukkit

public class BlockStationary extends BlockFluids {

    protected BlockStationary(int i, Material material) {
        super(i, material);
        this.b(false);
        if (material == Material.LAVA) {
            this.b(true);
        }
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return this.material != Material.LAVA;
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        super.doPhysics(world, i, j, k, l);
        if (world.getTypeId(i, j, k) == this.id) {
            this.k(world, i, j, k);
        }
    }

    private void k(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.setTypeIdAndData(i, j, k, this.id - 1, l, 2);
        world.a(i, j, k, this.id - 1, this.a(world));
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (this.material == Material.LAVA) {
            int l = random.nextInt(3);

            int i1;
            int j1;

            // CraftBukkit start - Prevent lava putting something on fire, remember igniter block coords
            int x = i;
            int y = j;
            int z = k;
            // CraftBukkit end

            for (i1 = 0; i1 < l; ++i1) {
                i += random.nextInt(3) - 1;
                ++j;
                k += random.nextInt(3) - 1;
                j1 = world.getTypeId(i, j, k);
                if (j1 == 0) {
                    if (this.m(world, i - 1, j, k) || this.m(world, i + 1, j, k) || this.m(world, i, j, k - 1) || this.m(world, i, j, k + 1) || this.m(world, i, j - 1, k) || this.m(world, i, j + 1, k)) {
                        // CraftBukkit start - Prevent lava putting something on fire
                        if (world.getTypeId(i, j, k) != Block.FIRE.id) {
                            if (CraftEventFactory.callBlockIgniteEvent(world, i, j, k, x, y, z).isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeIdUpdate(i, j, k, Block.FIRE.id);
                        return;
                    }
                } else if (Block.byId[j1].material.isSolid()) {
                    return;
                }
            }

            if (l == 0) {
                i1 = i;
                j1 = k;

                for (int k1 = 0; k1 < 3; ++k1) {
                    i = i1 + random.nextInt(3) - 1;
                    k = j1 + random.nextInt(3) - 1;
                    if (world.isEmpty(i, j + 1, k) && this.m(world, i, j, k)) {
                        // CraftBukkit start - Prevent lava putting something on fire
                        if (world.getTypeId(i, j + 1, k) != Block.FIRE.id) {
                            if (CraftEventFactory.callBlockIgniteEvent(world, i, j + 1, k, x, y, z).isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeIdUpdate(i, j + 1, k, Block.FIRE.id);
                    }
                }
            }
        }
    }

    private boolean m(World world, int i, int j, int k) {
        return world.getMaterial(i, j, k).isBurnable();
    }
}
