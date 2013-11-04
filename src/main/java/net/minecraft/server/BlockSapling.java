package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.CraftBlockChangeDelegate;
import org.bukkit.craftbukkit.util.StructureGrowDelegate;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockSapling extends BlockPlant implements IBlockFragilePlantElement {

    public static final String[] a = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "roofed_oak"};
    private static final IIcon[] b = new IIcon[a.length];

    protected BlockSapling() {
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
        int l = world.getData(i, j, k) & 7;
        // CraftBukkit start - Records tree generation and calls StructureGrowEvent
        StructureGrowDelegate delegate = new StructureGrowDelegate(world);
        TreeType treeType = null;
        boolean grownTree = false;
        // Turn ternary operator into if statement to set treeType
        //Object object = random.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        TreeGenerator object; // Changed to TreeGenerator
        if (random.nextInt(10) == 0) {
            treeType = TreeType.BIG_TREE;
            object = new WorldGenBigTree(true);
        } else {
            treeType = TreeType.TREE;
            object = new WorldGenTrees(true);
        }
        // CraftBukkit end
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;

        switch (l) {
        case 0:
        default:
            break;

        case 1:
            treeType = TreeType.REDWOOD; // CraftBukkit
            label78:
            for (i1 = 0; i1 >= -1; --i1) {
                for (j1 = 0; j1 >= -1; --j1) {
                    if (this.a(world, i + i1, j, k + j1, 1) && this.a(world, i + i1 + 1, j, k + j1, 1) && this.a(world, i + i1, j, k + j1 + 1, 1) && this.a(world, i + i1 + 1, j, k + j1 + 1, 1)) {
                        object = new WorldGenMegaTree(false, random.nextBoolean());
                        flag = true;
                        break label78;
                    }
                }
            }

            if (!flag) {
                j1 = 0;
                i1 = 0;
                object = new WorldGenTaiga2(true);
            }
            break;

        case 2:
            treeType = TreeType.BIRCH; // CraftBukkit
            object = new WorldGenForest(true, false);
            break;

        case 3:
            label93:
            for (i1 = 0; i1 >= -1; --i1) {
                for (j1 = 0; j1 >= -1; --j1) {
                    if (this.a(world, i + i1, j, k + j1, 3) && this.a(world, i + i1 + 1, j, k + j1, 3) && this.a(world, i + i1, j, k + j1 + 1, 3) && this.a(world, i + i1 + 1, j, k + j1 + 1, 3)) {
                        treeType = TreeType.JUNGLE; // CraftBukkit
                        object = new WorldGenJungleTree(true, 10, 20, 3, 3);
                        flag = true;
                        break label93;
                    }
                }
            }

            if (!flag) {
                j1 = 0;
                i1 = 0;
                treeType = TreeType.SMALL_JUNGLE; // CraftBukkit
                object = new WorldGenTrees(true, 4 + random.nextInt(7), 3, 3, false);
            }
            break;

        case 4:
            treeType = TreeType.ACACIA; // CraftBukkit
            object = new WorldGenAcaciaTree(true);
            break;

        case 5:
            label108:
            for (i1 = 0; i1 >= -1; --i1) {
                for (j1 = 0; j1 >= -1; --j1) {
                    if (this.a(world, i + i1, j, k + j1, 5) && this.a(world, i + i1 + 1, j, k + j1, 5) && this.a(world, i + i1, j, k + j1 + 1, 5) && this.a(world, i + i1 + 1, j, k + j1 + 1, 5)) {
                        object = new WorldGenForestTree(true);
                        treeType = TreeType.DARK_OAK; // CraftBukkit
                        flag = true;
                        break label108;
                    }
                }
            }

            if (!flag) {
                return;
            }
        }

        Block block = Blocks.AIR;

        if (flag) {
            world.setTypeAndData(i + i1, j, k + j1, block, 0, 4);
            world.setTypeAndData(i + i1 + 1, j, k + j1, block, 0, 4);
            world.setTypeAndData(i + i1, j, k + j1 + 1, block, 0, 4);
            world.setTypeAndData(i + i1 + 1, j, k + j1 + 1, block, 0, 4);
        } else {
            world.setTypeAndData(i, j, k, block, 0, 4);
        }

        // CraftBukkit start
        grownTree = object.generate(new CraftBlockChangeDelegate(delegate), random, i + i1, j, k + j1);
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
        // No need to generate the tree again.
        if (!grownTree) {
            // CraftBukkit end
            if (flag) {
                world.setTypeAndData(i + i1, j, k + j1, this, l, 4);
                world.setTypeAndData(i + i1 + 1, j, k + j1, this, l, 4);
                world.setTypeAndData(i + i1, j, k + j1 + 1, this, l, 4);
                world.setTypeAndData(i + i1 + 1, j, k + j1 + 1, this, l, 4);
            } else {
                world.setTypeAndData(i, j, k, this, l, 4);
            }
        }
    }

    public boolean a(World world, int i, int j, int k, int l) {
        return world.getType(i, j, k) == this && (world.getData(i, j, k) & 7) == l;
    }

    public int getDropData(int i) {
        return MathHelper.a(i & 7, 0, 5);
    }

    public boolean a(World world, int i, int j, int k, boolean flag) {
        return true;
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        return (double) world.random.nextFloat() < 0.45D;
    }

    public void b(World world, Random random, int i, int j, int k) {
        this.grow(world, i, j, k, random, false, null, null); // CraftBukkit - added bonemeal, player and itemstack
    }

    // CraftBukkit start
    public interface TreeGenerator {

        public boolean a(World world, Random random, int i, int j, int k);

        public boolean generate(org.bukkit.craftbukkit.CraftBlockChangeDelegate world, Random random, int i, int j, int k);
    }
    // CraftBukkit end
}
