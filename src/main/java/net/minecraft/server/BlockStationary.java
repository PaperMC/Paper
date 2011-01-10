package net.minecraft.server;

import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import java.util.Random;

public class BlockStationary extends BlockFluids {

    protected BlockStationary(int k, Material material) {
        super(k, material);
        a(false);
        if (material == Material.g) {
            a(true);
        }
    }

    public void b(World world, int k, int l, int i1, int j1) {
        super.b(world, k, l, i1, j1);
        if (world.a(k, l, i1) == bh) {
            i(world, k, l, i1);
        }
    }

    private void i(World world, int k, int l, int i1) {
        int j1 = world.b(k, l, i1);

        world.i = true;
        world.a(k, l, i1, bh - 1, j1);
        world.b(k, l, i1, k, l, i1);
        world.h(k, l, i1, bh - 1);
        world.i = false;
    }

    @Override
    public void a(World world, int k, int l, int i1, Random random) {
        if (bs == Material.g) {
            int j1 = random.nextInt(3);
            // CraftBukkit: prevent lava putting something on fire.
            CraftServer server = ((WorldServer)world).getServer();
            CraftWorld cworld = ((WorldServer)world).getWorld();
            org.bukkit.Block bblock = (cworld.getBlockAt(k, l, i1));
            BlockIgniteEvent event = new BlockIgniteEvent((org.bukkit.Block) bblock, BlockIgniteEvent.IgniteCause.LAVA, null);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                ;
            } else {
                for (int k1 = 0; k1 < j1; k1++) {
                    k += random.nextInt(3) - 1;
                    l++;
                    i1 += random.nextInt(3) - 1;
                    int l1 = world.a(k, l, i1);

                    if (l1 == 0) {
                        if (j(world, k - 1, l, i1) || j(world, k + 1, l, i1) || j(world, k, l, i1 - 1) || j(world, k, l, i1 + 1) || j(world, k, l - 1, i1) || j(world, k, l + 1, i1)) {
                            world.d(k, l, i1, Block.ar.bh);
                            return;
                        }
                        continue;
                    }
                    if (Block.m[l1].bs.c()) {
                        return;
                    }
                }
            }
        }
    }

    private boolean j(World world, int k, int l, int i1) {
        return world.c(k, l, i1).e();
    }
}
