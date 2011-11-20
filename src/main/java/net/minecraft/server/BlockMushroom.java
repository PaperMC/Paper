package net.minecraft.server;

import java.util.Random;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockMushroom extends BlockFlower {

    protected BlockMushroom(int i, int j) {
        super(i, j);
        float f = 0.2F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.a(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (random.nextInt(25) == 0) {
            byte b0 = 4;
            int l = 5;

            int i1;
            int j1;
            int k1;

            for (i1 = i - b0; i1 <= i + b0; ++i1) {
                for (j1 = k - b0; j1 <= k + b0; ++j1) {
                    for (k1 = j - 1; k1 <= j + 1; ++k1) {
                        if (world.getTypeId(i1, k1, j1) == this.id) {
                            --l;
                            if (l <= 0) {
                                return;
                            }
                        }
                    }
                }
            }

            i1 = i + random.nextInt(3) - 1;
            j1 = j + random.nextInt(2) - random.nextInt(2);
            k1 = k + random.nextInt(3) - 1;

            for (int l1 = 0; l1 < 4; ++l1) {
                if (world.isEmpty(i1, j1, k1) && this.f(world, i1, j1, k1)) {
                    i = i1;
                    j = j1;
                    k = k1;
                }

                i1 = i + random.nextInt(3) - 1;
                j1 = j + random.nextInt(2) - random.nextInt(2);
                k1 = k + random.nextInt(3) - 1;
            }

            if (world.isEmpty(i1, j1, k1) && this.f(world, i1, j1, k1)) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.BlockState blockState = bworld.getBlockAt(i1, j1, k1).getState();
                blockState.setTypeId(this.id);

                BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(i, j, k), blockState);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    blockState.update(true);
                }
                // CraftBukkit end
            }
        }
    }

    protected boolean d(int i) {
        return Block.o[i];
    }

    public boolean f(World world, int i, int j, int k) {
        if (j >= 0 && j < world.height) {
            int l = world.getTypeId(i, j - 1, k);

            return l == Block.MYCEL.id || world.k(i, j, k) < 13 && this.d(l);
        } else {
            return false;
        }
    }

    public boolean b(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        world.setRawTypeId(i, j, k, 0);
        WorldGenHugeMushroom worldgenhugemushroom = null;

        if (this.id == Block.BROWN_MUSHROOM.id) {
            worldgenhugemushroom = new WorldGenHugeMushroom(0);
        } else if (this.id == Block.RED_MUSHROOM.id) {
            worldgenhugemushroom = new WorldGenHugeMushroom(1);
        }

        if (worldgenhugemushroom != null && worldgenhugemushroom.a(world, random, i, j, k)) {
            return true;
        } else {
            world.setRawTypeIdAndData(i, j, k, this.id, l);
            return false;
        }
    }
}
