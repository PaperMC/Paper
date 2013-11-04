package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftBlockChangeDelegate;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
// CraftBukkit end

public class WorldGenHugeMushroom extends WorldGenerator implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    private int a = -1;

    public WorldGenHugeMushroom(int i) {
        super(true);
        this.a = i;
    }

    public WorldGenHugeMushroom() {
        super(false);
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return grow(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k, null, null, null);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        return grow(world, random, i, j, k, null, null, null);
    }

    public boolean grow(CraftBlockChangeDelegate world, Random random, int i, int j, int k, org.bukkit.event.world.StructureGrowEvent event, ItemStack itemstack, org.bukkit.craftbukkit.CraftWorld bukkitWorld) {
        // CraftBukkit end
        int l = random.nextInt(2);

        if (this.a >= 0) {
            l = this.a;
        }

        int i1 = random.nextInt(3) + 4;
        boolean flag = true;

        if (j >= 1 && j + i1 + 1 < 256) {
            int j1;
            int k1;

            for (int l1 = j; l1 <= j + 1 + i1; ++l1) {
                byte b0 = 3;

                if (l1 <= j + 3) {
                    b0 = 0;
                }

                for (j1 = i - b0; j1 <= i + b0 && flag; ++j1) {
                    for (k1 = k - b0; k1 <= k + b0 && flag; ++k1) {
                        if (l1 >= 0 && l1 < 256) {
                            Block block = world.getType(j1, l1, k1);

                            if (block.getMaterial() != Material.AIR && block.getMaterial() != Material.LEAVES) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                Block block1 = world.getType(i, j - 1, k);

                if (block1 != Blocks.DIRT && block1 != Blocks.GRASS && block1 != Blocks.MYCEL) {
                    return false;
                } else {
                    // CraftBukkit start
                    if (event == null) {
                        this.setTypeAndData(world, i, j - 1, k, Blocks.DIRT, 0);
                    } else {
                        BlockState dirtState = bukkitWorld.getBlockAt(i, j - 1, k).getState();
                        dirtState.setTypeId(Block.b(Blocks.DIRT));
                        event.getBlocks().add(dirtState);
                    }
                    // CraftBukkit end

                    int i2 = j + i1;

                    if (l == 1) {
                        i2 = j + i1 - 3;
                    }

                    for (j1 = i2; j1 <= j + i1; ++j1) {
                        k1 = 1;
                        if (j1 < j + i1) {
                            ++k1;
                        }

                        if (l == 0) {
                            k1 = 3;
                        }

                        for (int j2 = i - k1; j2 <= i + k1; ++j2) {
                            for (int k2 = k - k1; k2 <= k + k1; ++k2) {
                                int l2 = 5;

                                if (j2 == i - k1) {
                                    --l2;
                                }

                                if (j2 == i + k1) {
                                    ++l2;
                                }

                                if (k2 == k - k1) {
                                    l2 -= 3;
                                }

                                if (k2 == k + k1) {
                                    l2 += 3;
                                }

                                if (l == 0 || j1 < j + i1) {
                                    if ((j2 == i - k1 || j2 == i + k1) && (k2 == k - k1 || k2 == k + k1)) {
                                        continue;
                                    }

                                    if (j2 == i - (k1 - 1) && k2 == k - k1) {
                                        l2 = 1;
                                    }

                                    if (j2 == i - k1 && k2 == k - (k1 - 1)) {
                                        l2 = 1;
                                    }

                                    if (j2 == i + (k1 - 1) && k2 == k - k1) {
                                        l2 = 3;
                                    }

                                    if (j2 == i + k1 && k2 == k - (k1 - 1)) {
                                        l2 = 3;
                                    }

                                    if (j2 == i - (k1 - 1) && k2 == k + k1) {
                                        l2 = 7;
                                    }

                                    if (j2 == i - k1 && k2 == k + (k1 - 1)) {
                                        l2 = 7;
                                    }

                                    if (j2 == i + (k1 - 1) && k2 == k + k1) {
                                        l2 = 9;
                                    }

                                    if (j2 == i + k1 && k2 == k + (k1 - 1)) {
                                        l2 = 9;
                                    }
                                }

                                if (l2 == 5 && j1 < j + i1) {
                                    l2 = 0;
                                }

                                if ((l2 != 0 || j >= j + i1 - 1) && !world.getType(j2, j1, k2).j()) {
                                    // CraftBukkit start
                                    if (event == null) {
                                        this.setTypeAndData(world, j2, j1, k2, Block.e(Block.b(Blocks.BIG_MUSHROOM_1) + l), l2);
                                    } else {
                                        BlockState state = bukkitWorld.getBlockAt(i2, k1, k2).getState();
                                        state.setTypeId(Block.b(Blocks.BIG_MUSHROOM_1) + l);
                                        state.setData(new MaterialData(Block.b(Blocks.BIG_MUSHROOM_1) + l, (byte) l2));
                                        event.getBlocks().add(state);
                                    }
                                    // CraftBukkit end
                                }
                            }
                        }
                    }

                    for (j1 = 0; j1 < i1; ++j1) {
                        Block block2 = world.getType(i, j + j1, k);

                        if (!block2.j()) {
                            // CraftBukkit start
                            if (event == null) {
                                this.setTypeAndData(world, i, j + j1, k, Block.e(Block.b(Blocks.BIG_MUSHROOM_1) + l), 10);
                            } else {
                                BlockState state = bukkitWorld.getBlockAt(i, j + j1, k).getState();
                                state.setTypeId(Block.b(Blocks.BIG_MUSHROOM_1) + l);
                                state.setData(new MaterialData(Block.b(Blocks.BIG_MUSHROOM_1) + l, (byte) 10));
                                event.getBlocks().add(state);
                            }
                            // CraftBukkit end
                        }
                    }
                    // CraftBukkit start
                    if (event != null) {
                        org.bukkit.Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            for (BlockState state : event.getBlocks()) {
                                state.update(true);
                            }
                        }
                    }
                    // CraftBukkit end
                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
