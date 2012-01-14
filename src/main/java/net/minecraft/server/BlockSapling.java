package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;
// CraftBukkit start
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockSapling extends BlockFlower {

    protected BlockSapling(int i, int j) {
        super(i, j);
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            super.a(world, i, j, k, random);
            if (world.getLightLevel(i, j + 1, k) >= 9 && random.nextInt(7) == 0) {
                int l = world.getData(i, j, k);

                if ((l & 8) == 0) {
                    world.setData(i, j, k, l | 8);
                } else {
                    this.grow(world, i, j, k, random, false, null, null); // CraftBukkit - added bonemeal, player and itemstack
                }
            }
        }
    }

    public int a(int i, int j) {
        j &= 3;
        return j == 1 ? 63 : (j == 2 ? 79 : super.a(i, j));
    }

    // CraftBukkit - added bonemeal, player and itemstack
    public void grow(World world, int i, int j, int k, Random random, boolean bonemeal, Player player, ItemStack itemstack) {
        int l = world.getData(i, j, k) & 3;

        world.setRawTypeId(i, j, k, 0);
        // CraftBukkit start - fixes client updates on recently grown trees
        boolean grownTree;
        BlockChangeWithNotify delegate = new BlockChangeWithNotify(world);
        StructureGrowEvent event = null;
        Location location = new Location(world.getWorld(), i, j, k);
        // All of these are 'false' because we need the 'raw' calls so the block-delegate works
        if (l == 1) {
            event = new StructureGrowEvent(location, TreeType.REDWOOD, bonemeal, player, new ArrayList<BlockState>());
            grownTree = new WorldGenTaiga2(false).generate(delegate, random, i, j, k, event, itemstack, world.getWorld());
        } else if (l == 2) {
            event = new StructureGrowEvent(location, TreeType.BIRCH, bonemeal, player, new ArrayList<BlockState>());
            grownTree = new WorldGenForest(false).generate(delegate, random, i, j, k, event, itemstack, world.getWorld());
        } else {
            if (random.nextInt(10) == 0) {
                event = new StructureGrowEvent(location, TreeType.BIG_TREE, bonemeal, player, new ArrayList<BlockState>());
                grownTree = new WorldGenBigTree(false).generate(delegate, random, i, j, k, event, itemstack, world.getWorld());
            } else {
                event = new StructureGrowEvent(location, TreeType.TREE, bonemeal, player, new ArrayList<BlockState>());
                grownTree = new WorldGenTrees(false).generate(delegate, random, i, j, k, event, itemstack, world.getWorld());
            }
        }
        if (event == null) {
            return;
        }
        if (event.isFromBonemeal() && itemstack != null) {
            --itemstack.count;
        }
        if (!grownTree || event.isCancelled()) {
            // CraftBukkit end
            world.setRawTypeIdAndData(i, j, k, this.id, l);
        }
    }

    protected int getDropData(int i) {
        return i & 3;
    }

    // CraftBukkit start
    private class BlockChangeWithNotify implements BlockChangeDelegate {

        World world;

        BlockChangeWithNotify(World world) {
            this.world = world;
        }

        public boolean setRawTypeId(int x, int y, int z, int type) {
            return this.world.setTypeId(x, y, z, type);
        }

        public boolean setRawTypeIdAndData(int x, int y, int z, int type, int data) {
            return this.world.setTypeIdAndData(x, y, z, type, data);
        }

        public int getTypeId(int x, int y, int z) {
            return this.world.getTypeId(x, y, z);
        }

        public int getHeight() {
            return world.height;
        }
    }
    // CraftBukkit end
}
