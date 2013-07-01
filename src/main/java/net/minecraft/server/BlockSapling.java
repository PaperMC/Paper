package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.util.StructureGrowDelegate;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockSapling extends BlockFlower {

    public static final String[] a = new String[] { "oak", "spruce", "birch", "jungle"};

    protected BlockSapling(int i) {
        super(i);
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.a(CreativeModeTab.c);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            super.a(world, i, j, k, random);
            if (world.getLightLevel(i, j + 1, k) >= 9 && random.nextInt(7) == 0) {
                this.grow(world, i, j, k, random, false, null, null); // CraftBukkit - added bonemeal, player and itemstack
            }
        }
    }

    // CraftBukkit - added bonemeal, player and itemstack
    public void grow(World world, int i, int j, int k, Random random, boolean bonemeal, Player player, ItemStack itemstack) {
        int l = world.getData(i, j, k);

        if ((l & 8) == 0) {
            world.setData(i, j, k, l | 8, 4);
        } else {
            this.d(world, i, j, k, random, bonemeal, player, itemstack); // CraftBukkit
        }
    }

    // CraftBukkit - Added bonemeal, player and itemstack
    public void d(World world, int i, int j, int k, Random random, boolean bonemeal, Player player, ItemStack itemstack) {
        int l = world.getData(i, j, k) & 3;
        Object object = null;
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;
        // CraftBukkit start - Records tree generation and calls StructureGrowEvent
        StructureGrowDelegate delegate = new StructureGrowDelegate(world);
        TreeType treeType = null;
        TreeGenerator gen = null;
        boolean grownTree = false;

        if (l == 1) {
            treeType = TreeType.REDWOOD;
            gen = new WorldGenTaiga2(false);
        } else if (l == 2) {
            treeType = TreeType.BIRCH;
            gen = new WorldGenForest(false);
        } else if (l == 3) {
            for (i1 = 0; i1 >= -1; --i1) {
                for (j1 = 0; j1 >= -1; --j1) {
                    if (this.d(world, i + i1, j, k + j1, 3) && this.d(world, i + i1 + 1, j, k + j1, 3) && this.d(world, i + i1, j, k + j1 + 1, 3) && this.d(world, i + i1 + 1, j, k + j1 + 1, 3)) {
                        treeType = TreeType.JUNGLE;
                        gen = new WorldGenMegaTree(false, 10 + random.nextInt(20), 3, 3);
                        flag = true;
                        break;
                    }
                }

                if (gen != null) {
                    break;
                }
            }

            if (gen == null) {
                j1 = 0;
                i1 = 0;
                treeType = TreeType.SMALL_JUNGLE;
                gen = new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, false);
            }
        } else {
            treeType = TreeType.TREE;
            gen = new WorldGenTrees(false);
            if (random.nextInt(10) == 0) {
                treeType = TreeType.BIG_TREE;
                gen = new WorldGenBigTree(false);
            }
        }

        if (flag) {
            world.setTypeIdAndData(i + i1, j, k + j1, 0, 0, 4);
            world.setTypeIdAndData(i + i1 + 1, j, k + j1, 0, 0, 4);
            world.setTypeIdAndData(i + i1, j, k + j1 + 1, 0, 0, 4);
            world.setTypeIdAndData(i + i1 + 1, j, k + j1 + 1, 0, 0, 4);
        } else {
            world.setTypeIdAndData(i, j, k, 0, 0, 4);
        }

        grownTree = gen.generate(delegate, random, i + i1, j, k + j1);
        if (grownTree) {
            Location location = new Location(world.getWorld(), i, j, k);
            StructureGrowEvent event = new StructureGrowEvent(location, treeType, bonemeal, player, delegate.getBlocks());
            org.bukkit.Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                grownTree = false;
            } else {
                for (org.bukkit.block.BlockState state : event.getBlocks()) {
                    state.update(true);
                }
                if (event.isFromBonemeal() && itemstack != null) {
                    --itemstack.count;
                }
            }
        } else if (bonemeal && itemstack != null) {
            // We always consume bonemeal when trying to grow
            --itemstack.count;
        }

        if (!grownTree) {
            if (flag) {
                world.setTypeIdAndData(i + i1, j, k + j1, this.id, l, 4);
                world.setTypeIdAndData(i + i1 + 1, j, k + j1, this.id, l, 4);
                world.setTypeIdAndData(i + i1, j, k + j1 + 1, this.id, l, 4);
                world.setTypeIdAndData(i + i1 + 1, j, k + j1 + 1, this.id, l, 4);
            } else {
                world.setTypeIdAndData(i, j, k, this.id, l, 4);
            }
        }
        // CraftBukkit end
    }

    public boolean d(World world, int i, int j, int k, int l) {
        return world.getTypeId(i, j, k) == this.id && (world.getData(i, j, k) & 3) == l;
    }

    public int getDropData(int i) {
        return i & 3;
    }

    // CraftBukkit start
    public interface TreeGenerator {

        public boolean a(World world, Random random, int i, int j, int k);

        public boolean generate(org.bukkit.BlockChangeDelegate world, Random random, int i, int j, int k);
    }
    // CraftBukkit end
}
