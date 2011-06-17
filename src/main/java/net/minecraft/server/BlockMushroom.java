package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.block.BlockSpreadEvent;
// CraftBukkit end

public class BlockMushroom extends BlockFlower {

    protected BlockMushroom(int i, int j) {
        super(i, j);
        float f = 0.2F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.a(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (random.nextInt(100) == 0) {
            int l = i + random.nextInt(3) - 1;
            int i1 = j + random.nextInt(2) - random.nextInt(2);
            int j1 = k + random.nextInt(3) - 1;

            if (world.isEmpty(l, i1, j1) && this.f(world, l, i1, j1)) {
                int k1 = i + (random.nextInt(3) - 1);

                k1 = k + (random.nextInt(3) - 1);
                if (world.isEmpty(l, i1, j1) && this.f(world, l, i1, j1)) {
                    // CraftBukkit start
                    Server server = world.getServer();
                    CraftWorld craftworld = world.getWorld();
                    BlockState blockState = craftworld.getBlockAt(l, i1, j1).getState();
                    blockState.setTypeId(this.id);

                    BlockSpreadEvent event = new BlockSpreadEvent(craftworld.getBlockAt(l, i1, j1), craftworld.getBlockAt(i, j, k), blockState);

                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        blockState.update(true);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    protected boolean c(int i) {
        return Block.o[i];
    }

    public boolean f(World world, int i, int j, int k) {
        return j >= 0 && j < 128 ? world.j(i, j, k) < 13 && this.c(world.getTypeId(i, j - 1, k)) : false;
    }
}
