package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockMushroom extends BlockFlower {

    protected BlockMushroom(int i, int j) {
        super(i, j);
        float f = 0.2F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.b(true);
    }

    public void b(World world, int i, int j, int k, Random random) {
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
                if (world.isEmpty(i1, j1, k1) && this.d(world, i1, j1, k1)) {
                    i = i1;
                    j = j1;
                    k = k1;
                }

                i1 = i + random.nextInt(3) - 1;
                j1 = j + random.nextInt(2) - random.nextInt(2);
                k1 = k + random.nextInt(3) - 1;
            }

            if (world.isEmpty(i1, j1, k1) && this.d(world, i1, j1, k1)) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                BlockState blockState = bworld.getBlockAt(i1, j1, k1).getState();
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

    public boolean canPlace(World world, int i, int j, int k) {
        return super.canPlace(world, i, j, k) && this.d(world, i, j, k);
    }

    protected boolean d_(int i) {
        return Block.q[i];
    }

    public boolean d(World world, int i, int j, int k) {
        if (j >= 0 && j < 256) {
            int l = world.getTypeId(i, j - 1, k);

            return l == Block.MYCEL.id || world.k(i, j, k) < 13 && this.d_(l);
        } else {
            return false;
        }
    }

    // CraftBukkit - added bonemeal, player and itemstack
    public boolean grow(World world, int i, int j, int k, Random random, boolean bonemeal, org.bukkit.entity.Player player, ItemStack itemstack) {
        int l = world.getData(i, j, k);

        world.setRawTypeId(i, j, k, 0);
        // CraftBukkit start
        boolean grown = false;
        StructureGrowEvent event = null;
        Location location = new Location(world.getWorld(), i, j, k);
        WorldGenHugeMushroom worldgenhugemushroom = null;

        if (this.id == Block.BROWN_MUSHROOM.id) {
            event = new StructureGrowEvent(location, TreeType.BROWN_MUSHROOM, bonemeal, player, new ArrayList<BlockState>());
            worldgenhugemushroom = new WorldGenHugeMushroom(0);
        } else if (this.id == Block.RED_MUSHROOM.id) {
            event = new StructureGrowEvent(location, TreeType.RED_MUSHROOM, bonemeal, player, new ArrayList<BlockState>());
            worldgenhugemushroom = new WorldGenHugeMushroom(1);
        }

        if (worldgenhugemushroom != null && event != null) {
            grown = worldgenhugemushroom.grow((org.bukkit.BlockChangeDelegate)world, random, i, j, k, event, itemstack, world.getWorld());
            if (event.isFromBonemeal() && itemstack != null) {
                --itemstack.count;
            }
        }
        if (!grown || event.isCancelled()) {
            world.setRawTypeIdAndData(i, j, k, this.id, l);
            return false;
        }
        return true;
        // CraftBukkit end
    }
}
