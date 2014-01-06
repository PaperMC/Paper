package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import java.util.List;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockSapling extends BlockPlant implements IBlockFragilePlantElement {

    public static final String[] a = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "roofed_oak"};
    private static final IIcon[] b = new IIcon[a.length];
    public static TreeType treeType; // CraftBukkit

    protected BlockSapling() {
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.a(CreativeModeTab.c);
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            super.a(world, i, j, k, random);
            if (world.getLightLevel(i, j + 1, k) >= 9 && random.nextInt(7) == 0) {
                // CraftBukkit start
                world.captureTreeGeneration = true;
                // CraftBukkit end
                this.grow(world, i, j, k, random);
                // CraftBukkit start
                world.captureTreeGeneration = false;
                if (world.capturedBlockStates.size() > 0)
                {
                    TreeType treeType = BlockSapling.treeType;
                    BlockSapling.treeType = null;
                    Location location = new Location(world.getWorld(), i, j, k);
                    List<BlockState> blocks = (List<BlockState>) world.capturedBlockStates.clone();
                    world.capturedBlockStates.clear();
                    StructureGrowEvent event = null;
                    if (treeType != null) {
                        event = new StructureGrowEvent(location, treeType, false, null, blocks);
                        org.bukkit.Bukkit.getPluginManager().callEvent(event);
                    }
                    if (event == null || !event.isCancelled()) {
                        for (BlockState blockstate : blocks) {
                            blockstate.update(true);
                        }
                    }
                }
                // CraftBukkit end
            }
        }
    }

    public void grow(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        if ((l & 8) == 0) {
            world.setData(i, j, k, l | 8, 4);
        } else {
            this.d(world, i, j, k, random);
        }
    }

    public void d(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k) & 7;
        // CraftBukkit start - Turn ternary operator into if statement to set treeType
        // Object object = random.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        Object object;
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
            label78:
            for (i1 = 0; i1 >= -1; --i1) {
                for (j1 = 0; j1 >= -1; --j1) {
                    if (this.a(world, i + i1, j, k + j1, 1) && this.a(world, i + i1 + 1, j, k + j1, 1) && this.a(world, i + i1, j, k + j1 + 1, 1) && this.a(world, i + i1 + 1, j, k + j1 + 1, 1)) {
                        treeType = TreeType.TALL_REDWOOD; // CraftBukkit
                        object = new WorldGenMegaTree(false, random.nextBoolean());
                        flag = true;
                        break label78;
                    }
                }
            }

            if (!flag) {
                j1 = 0;
                i1 = 0;
                treeType = TreeType.REDWOOD; // CraftBukkit
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

        if (!((WorldGenerator) object).a(world, random, i + i1, j, k + j1)) {
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
        this.grow(world, i, j, k, random);
    }
}
