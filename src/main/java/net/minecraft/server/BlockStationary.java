package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.craftbukkit.CraftWorld;
// CraftBukkit end

public class BlockStationary extends BlockFluids {

    protected BlockStationary(int i, Material material) {
        super(i, material);
        this.a(false);
        if (material == Material.LAVA) {
            this.a(true);
        }
    }

    public void a(World world, int i, int j, int k, int l) {
        super.a(world, i, j, k, l);
        if (world.getTypeId(i, j, k) == this.id) {
            this.i(world, i, j, k);
        }
    }

    private void i(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.h = true;
        world.setTypeIdAndData(i, j, k, this.id - 1, l);
        world.b(i, j, k, i, j, k);
        world.c(i, j, k, this.id - 1, this.b());
        world.h = false;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (this.material == Material.LAVA) {
            int l = random.nextInt(3);

            // CraftBukkit start: prevent lava putting something on fire.
            Server server = ((WorldServer) world).getServer();
            CraftWorld cworld = ((WorldServer) world).getWorld();

            IgniteCause igniteCause = BlockIgniteEvent.IgniteCause.LAVA;
            Player thePlayer = null;
            // CraftBukkit end

            for (int i1 = 0; i1 < l; ++i1) {
                i += random.nextInt(3) - 1;
                ++j;
                k += random.nextInt(3) - 1;
                int j1 = world.getTypeId(i, j, k);

                if (j1 == 0) {
                    if (this.j(world, i - 1, j, k) || this.j(world, i + 1, j, k) || this.j(world, i, j, k - 1) || this.j(world, i, j, k + 1) || this.j(world, i, j - 1, k) || this.j(world, i, j + 1, k)) {
                        // CraftBukkit start: prevent lava putting something on fire.
                        org.bukkit.block.Block theBlock = cworld.getBlockAt(i, j, k);

                        if (theBlock.getTypeId() != Block.FIRE.id) {
                            BlockIgniteEvent event = new BlockIgniteEvent(theBlock, igniteCause, thePlayer);
                            server.getPluginManager().callEvent(event);
                            if (event.isCancelled()) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.e(i, j, k, Block.FIRE.id);
                        return;
                    }
                } else if (Block.byId[j1].material.isSolid()) {
                    return;
                }
            }
        }
    }

    private boolean j(World world, int i, int j, int k) {
        return world.getMaterial(i, j, k).isBurnable();
    }
}
