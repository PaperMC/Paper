package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
// CraftBukkit end

public class BlockFire extends Block {

    private int[] a = new int[256];
    private int[] b = new int[256];

    protected BlockFire(int i) {
        super(i, Material.FIRE);
        this.b(true);
    }

    public void s_() {
        this.a(Block.WOOD.id, 5, 20);
        this.a(Block.WOOD_DOUBLE_STEP.id, 5, 20);
        this.a(Block.WOOD_STEP.id, 5, 20);
        this.a(Block.FENCE.id, 5, 20);
        this.a(Block.WOOD_STAIRS.id, 5, 20);
        this.a(Block.BIRCH_WOOD_STAIRS.id, 5, 20);
        this.a(Block.SPRUCE_WOOD_STAIRS.id, 5, 20);
        this.a(Block.JUNGLE_WOOD_STAIRS.id, 5, 20);
        this.a(Block.LOG.id, 5, 5);
        this.a(Block.LEAVES.id, 30, 60);
        this.a(Block.BOOKSHELF.id, 30, 20);
        this.a(Block.TNT.id, 15, 100);
        this.a(Block.LONG_GRASS.id, 60, 100);
        this.a(Block.WOOL.id, 30, 60);
        this.a(Block.VINE.id, 15, 100);
        this.a(Block.COAL_BLOCK.id, 5, 5);
        this.a(Block.HAY_BLOCK.id, 60, 20);
    }

    private void a(int i, int j, int k) {
        this.a[i] = j;
        this.b[i] = k;
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int d() {
        return 3;
    }

    public int a(Random random) {
        return 0;
    }

    public int a(World world) {
        return 30;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.getGameRules().getBoolean("doFireTick")) {
            boolean flag = world.getTypeId(i, j - 1, k) == Block.NETHERRACK.id;

            if (world.worldProvider instanceof WorldProviderTheEnd && world.getTypeId(i, j - 1, k) == Block.BEDROCK.id) {
                flag = true;
            }

            if (!this.canPlace(world, i, j, k)) {
                fireExtinguished(world, i, j, k); // CraftBukkit - invalid place location
            }

            if (!flag && world.Q() && (world.isRainingAt(i, j, k) || world.isRainingAt(i - 1, j, k) || world.isRainingAt(i + 1, j, k) || world.isRainingAt(i, j, k - 1) || world.isRainingAt(i, j, k + 1))) {
                fireExtinguished(world, i, j, k); // CraftBukkit - extinguished by rain
            } else {
                int l = world.getData(i, j, k);

                if (l < 15) {
                    world.setData(i, j, k, l + random.nextInt(3) / 2, 4);
                }

                world.a(i, j, k, this.id, this.a(world) + random.nextInt(10));
                if (!flag && !this.k(world, i, j, k)) {
                    if (!world.w(i, j - 1, k) || l > 3) {
                        fireExtinguished(world, i, j, k); // CraftBukkit - burn out of inflammable block
                    }
                } else if (!flag && !this.d((IBlockAccess) world, i, j - 1, k) && l == 15 && random.nextInt(4) == 0) {
                    fireExtinguished(world, i, j, k); // CraftBukkit - burn out
                } else {
                    boolean flag1 = world.G(i, j, k);
                    byte b0 = 0;

                    if (flag1) {
                        b0 = -50;
                    }

                    this.a(world, i + 1, j, k, 300 + b0, random, l);
                    this.a(world, i - 1, j, k, 300 + b0, random, l);
                    this.a(world, i, j - 1, k, 250 + b0, random, l);
                    this.a(world, i, j + 1, k, 250 + b0, random, l);
                    this.a(world, i, j, k - 1, 300 + b0, random, l);
                    this.a(world, i, j, k + 1, 300 + b0, random, l);

                    for (int i1 = i - 1; i1 <= i + 1; ++i1) {
                        for (int j1 = k - 1; j1 <= k + 1; ++j1) {
                            for (int k1 = j - 1; k1 <= j + 4; ++k1) {
                                if (i1 != i || k1 != j || j1 != k) {
                                    int l1 = 100;

                                    if (k1 > j + 1) {
                                        l1 += (k1 - (j + 1)) * 100;
                                    }

                                    int i2 = this.m(world, i1, k1, j1);

                                    if (i2 > 0) {
                                        int j2 = (i2 + 40 + world.difficulty * 7) / (l + 30);

                                        if (flag1) {
                                            j2 /= 2;
                                        }

                                        if (j2 > 0 && random.nextInt(l1) <= j2 && (!world.Q() || !world.isRainingAt(i1, k1, j1)) && !world.isRainingAt(i1 - 1, k1, k) && !world.isRainingAt(i1 + 1, k1, j1) && !world.isRainingAt(i1, k1, j1 - 1) && !world.isRainingAt(i1, k1, j1 + 1)) {
                                            int k2 = l + random.nextInt(5) / 4;

                                            if (k2 > 15) {
                                                k2 = 15;
                                            }

                                            // CraftBukkit start - Call to stop spread of fire
                                            if (world.getTypeId(i1, k1, j1) != Block.FIRE.id) {
                                                if (CraftEventFactory.callBlockIgniteEvent(world, i1, k1, j1, i, j, k).isCancelled()) {
                                                    continue;
                                                }

                                                org.bukkit.Server server = world.getServer();
                                                org.bukkit.World bworld = world.getWorld();
                                                org.bukkit.block.BlockState blockState = bworld.getBlockAt(i1, k1, j1).getState();
                                                blockState.setTypeId(this.id);
                                                blockState.setData(new org.bukkit.material.MaterialData(this.id, (byte) k2));

                                                BlockSpreadEvent spreadEvent = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(i, j, k), blockState);
                                                server.getPluginManager().callEvent(spreadEvent);

                                                if (!spreadEvent.isCancelled()) {
                                                    blockState.update(true);
                                                }
                                            }
                                            // CraftBukkit end
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean l() {
        return false;
    }

    private void a(World world, int i, int j, int k, int l, Random random, int i1) {
        int j1 = this.b[world.getTypeId(i, j, k)];

        if (random.nextInt(l) < j1) {
            boolean flag = world.getTypeId(i, j, k) == Block.TNT.id;

            // CraftBukkit start
            org.bukkit.block.Block theBlock = world.getWorld().getBlockAt(i, j, k);

            BlockBurnEvent event = new BlockBurnEvent(theBlock);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (random.nextInt(i1 + 10) < 5 && !world.isRainingAt(i, j, k)) {
                int k1 = i1 + random.nextInt(5) / 4;

                if (k1 > 15) {
                    k1 = 15;
                }

                world.setTypeIdAndData(i, j, k, this.id, k1, 3);
            } else {
                world.setAir(i, j, k);
            }

            if (flag) {
                Block.TNT.postBreak(world, i, j, k, 1);
            }
        }
    }

    private boolean k(World world, int i, int j, int k) {
        return this.d((IBlockAccess) world, i + 1, j, k) ? true : (this.d((IBlockAccess) world, i - 1, j, k) ? true : (this.d((IBlockAccess) world, i, j - 1, k) ? true : (this.d((IBlockAccess) world, i, j + 1, k) ? true : (this.d((IBlockAccess) world, i, j, k - 1) ? true : this.d((IBlockAccess) world, i, j, k + 1)))));
    }

    private int m(World world, int i, int j, int k) {
        byte b0 = 0;

        if (!world.isEmpty(i, j, k)) {
            return 0;
        } else {
            int l = this.d(world, i + 1, j, k, b0);

            l = this.d(world, i - 1, j, k, l);
            l = this.d(world, i, j - 1, k, l);
            l = this.d(world, i, j + 1, k, l);
            l = this.d(world, i, j, k - 1, l);
            l = this.d(world, i, j, k + 1, l);
            return l;
        }
    }

    public boolean m() {
        return false;
    }

    public boolean d(IBlockAccess iblockaccess, int i, int j, int k) {
        return this.a[iblockaccess.getTypeId(i, j, k)] > 0;
    }

    public int d(World world, int i, int j, int k, int l) {
        int i1 = this.a[world.getTypeId(i, j, k)];

        return i1 > l ? i1 : l;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.w(i, j - 1, k) || this.k(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.w(i, j - 1, k) && !this.k(world, i, j, k)) {
            fireExtinguished(world, i, j, k); // CraftBukkit - fuel block gone
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        if (world.worldProvider.dimension > 0 || world.getTypeId(i, j - 1, k) != Block.OBSIDIAN.id || !Block.PORTAL.o_(world, i, j, k)) {
            if (!world.w(i, j - 1, k) && !this.k(world, i, j, k)) {
                fireExtinguished(world, i, j, k); // CraftBukkit - fuel block broke
            } else {
                world.a(i, j, k, this.id, this.a(world) + world.random.nextInt(10));
            }
        }
    }

    // CraftBukkit start
    private void fireExtinguished(World world, int x, int y, int z) {
        if (!CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(x, y, z), 0).isCancelled()) {
            world.setAir(x, y, z);
        }
    }
    // CraftBukkit end
}
