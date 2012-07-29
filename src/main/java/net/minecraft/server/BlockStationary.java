package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;
// CraftBukkit end

public class BlockStationary extends BlockFluids {

    protected BlockStationary(int i, Material material) {
        super(i, material);
        this.b(false);
        if (material == Material.LAVA) {
            this.b(true);
        }
    }

    public boolean c(IBlockAccess iblockaccess, int i, int j, int k) {
        return this.material != Material.LAVA;
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        super.doPhysics(world, i, j, k, l);
        if (world.getTypeId(i, j, k) == this.id) {
            this.l(world, i, j, k);
        }
    }

    private void l(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.suppressPhysics = true;
        world.setRawTypeIdAndData(i, j, k, this.id - 1, l);
        world.d(i, j, k, i, j, k);
        world.a(i, j, k, this.id - 1, this.p_());
        world.suppressPhysics = false;
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (this.material == Material.LAVA) {
            int l = random.nextInt(3);

            int i1;
            int j1;

            // CraftBukkit start - prevent lava putting something on fire
            org.bukkit.World bworld = world.getWorld();
            BlockIgniteEvent.IgniteCause igniteCause = BlockIgniteEvent.IgniteCause.LAVA;
            // CraftBukkit end

            for (i1 = 0; i1 < l; ++i1) {
                i += random.nextInt(3) - 1;
                ++j;
                k += random.nextInt(3) - 1;
                j1 = world.getTypeId(i, j, k);
                if (j1 == 0) {
                    if (this.n(world, i - 1, j, k) || this.n(world, i + 1, j, k) || this.n(world, i, j, k - 1) || this.n(world, i, j, k + 1) || this.n(world, i, j - 1, k) || this.n(world, i, j + 1, k)) {
                        // CraftBukkit start - prevent lava putting something on fire
                        org.bukkit.block.Block block = bworld.getBlockAt(i, j, k);
                        if (block.getTypeId() != Block.FIRE.id) {
                            if (CraftEventFactory.callEvent(new BlockIgniteEvent(block, igniteCause, null)).isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeId(i, j, k, Block.FIRE.id);
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
                    if (world.isEmpty(i, j + 1, k) && this.n(world, i, j, k)) {
                        // CraftBukkit start - prevent lava putting something on fire
                        org.bukkit.block.Block block = bworld.getBlockAt(i, j + 1, k);
                        if (block.getTypeId() != Block.FIRE.id) {
                            if (CraftEventFactory.callEvent(new BlockIgniteEvent(block, igniteCause, null)).isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeId(i, j + 1, k, Block.FIRE.id);
                    }
                }
            }
        }
    }

    private boolean n(World world, int i, int j, int k) {
        return world.getMaterial(i, j, k).isBurnable();
    }
}
