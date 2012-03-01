package net.minecraft.server;

import java.util.Random;
// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.StructureGrowDelegate;
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
        return j == 1 ? 63 : (j == 2 ? 79 : (j == 3 ? 30 : super.a(i, j)));
    }

    // CraftBukkit - added bonemeal, player and itemstack
    public void grow(World world, int i, int j, int k, Random random, boolean bonemeal, Player player, ItemStack itemstack) {
        int l = world.getData(i, j, k) & 3;

        world.setRawTypeId(i, j, k, 0);
        // CraftBukkit start - records tree generation and calls StructureGrowEvent
        StructureGrowDelegate delegate = new StructureGrowDelegate(world);
        TreeType treeType;
        boolean grownTree;
        // All of these are 'false' because we need the 'raw' calls so the block-delegate works
        if (l == 1) {
            treeType = TreeType.REDWOOD;
            grownTree = new WorldGenTaiga2(false).generate(delegate, random, i, j, k);
        } else if (l == 2) {
            treeType = TreeType.BIRCH;
            grownTree = new WorldGenForest(false).generate(delegate, random, i, j, k);
        } else {
            if (random.nextInt(10) == 0) {
                treeType = TreeType.BIG_TREE;
                grownTree = new WorldGenBigTree(false).generate(delegate, random, i, j, k);
            } else {
                treeType = TreeType.TREE;
                grownTree = new WorldGenTrees(false).generate(delegate, random, i, j, k);
            }
        }

        if (grownTree) {
            Location location = new Location(world.getWorld(), i, j, k);
            StructureGrowEvent event = new StructureGrowEvent(location, treeType, bonemeal, player, delegate.getBlocks());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                grownTree = false;
            } else {
                for (BlockState state : event.getBlocks()) {
                    state.update(true);
                }
                if (event.isFromBonemeal() && itemstack != null) {
                    --itemstack.count;
                }
            }
        }

        if (!grownTree) {
            // CraftBukkit end
            world.setRawTypeIdAndData(i, j, k, this.id, l);
        }
    }

    protected int getDropData(int i) {
        return i & 3;
    }
}
