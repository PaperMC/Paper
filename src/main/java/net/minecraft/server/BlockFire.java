package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.block.BlockBurnEvent;
// CraftBukkit end

public class BlockFire extends Block {

    private int[] a = new int[256];
    private int[] b = new int[256];

    protected BlockFire(int i, int j) {
        super(i, j, Material.FIRE);
        this.a(Block.WOOD.id, 5, 20);
        this.a(Block.LOG.id, 5, 5);
        this.a(Block.LEAVES.id, 30, 60);
        this.a(Block.BOOKSHELF.id, 30, 20);
        this.a(Block.TNT.id, 15, 100);
        this.a(Block.WOOL.id, 30, 60);
        this.a(true);
    }

    private void a(int i, int j, int k) {
        this.a[i] = j;
        this.b[i] = k;
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public int a(Random random) {
        return 0;
    }

    public int b() {
        return 10;
    }

    public void a(World world, int i, int j, int k, Random random) {
        boolean flag = world.getTypeId(i, j - 1, k) == Block.NETHERRACK.id;
        int l = world.getData(i, j, k);

        if (l < 15) {
            world.setData(i, j, k, l + 1);
            world.c(i, j, k, this.id, this.b());
        }

        if (!flag && !this.g(world, i, j, k)) {
            if (!world.d(i, j - 1, k) || l > 3) {
                world.setTypeId(i, j, k, 0);
            }
        } else if (!flag && !this.b(world, i, j - 1, k) && l == 15 && random.nextInt(4) == 0) {
            world.setTypeId(i, j, k, 0);
        } else {
            if (l % 2 == 0 && l > 2) {
                this.a(world, i + 1, j, k, 300, random);
                this.a(world, i - 1, j, k, 300, random);
                this.a(world, i, j - 1, k, 250, random);
                this.a(world, i, j + 1, k, 250, random);
                this.a(world, i, j, k - 1, 300, random);
                this.a(world, i, j, k + 1, 300, random);

                // CraftBukkit start - Call to stop spread of fire.
                Server server = ((WorldServer) world).getServer();
                CraftWorld cworld = ((WorldServer) world).getWorld();

                IgniteCause igniteCause = BlockIgniteEvent.IgniteCause.SPREAD;
                Player thePlayer = null;
                // CraftBukkit

                for (int i1 = i - 1; i1 <= i + 1; ++i1) {
                    for (int j1 = k - 1; j1 <= k + 1; ++j1) {
                        for (int k1 = j - 1; k1 <= j + 4; ++k1) {
                            if (i1 != i || k1 != j || j1 != k) {
                                int l1 = 100;

                                if (k1 > j + 1) {
                                    l1 += (k1 - (j + 1)) * 100;
                                }

                                int i2 = this.h(world, i1, k1, j1);

                                if (i2 > 0 && random.nextInt(l1) <= i2) {
                                    // CraftBukkit start - Call to stop spread of fire.
                                    org.bukkit.block.Block theBlock = cworld.getBlockAt(i1, k1, j1);

                                    if (theBlock.getTypeId() != Block.FIRE.id) {
                                        BlockIgniteEvent event = new BlockIgniteEvent(theBlock, igniteCause, thePlayer);
                                        server.getPluginManager().callEvent(event);
                                        if (event.isCancelled()) {
                                            continue;
                                        }
                                    }
                                    // CraftBukkit end

                                    world.setTypeId(i1, k1, j1, this.id);
                                }
                            }
                        }
                    }
                }
            }

            if (l == 15) {
                this.a(world, i + 1, j, k, 1, random);
                this.a(world, i - 1, j, k, 1, random);
                this.a(world, i, j - 1, k, 1, random);
                this.a(world, i, j + 1, k, 1, random);
                this.a(world, i, j, k - 1, 1, random);
                this.a(world, i, j, k + 1, 1, random);
            }
        }
    }

    private void a(World world, int i, int j, int k, int l, Random random) {
        int i1 = this.b[world.getTypeId(i, j, k)];

        if (random.nextInt(l) < i1) {
            boolean flag = world.getTypeId(i, j, k) == Block.TNT.id;
            // CraftBukkit start
            Server server = ((WorldServer) world).getServer();
            CraftWorld cworld = ((WorldServer) world).getWorld();
            org.bukkit.block.Block theBlock = cworld.getBlockAt(i, j, k);
            BlockBurnEvent event = new BlockBurnEvent(theBlock);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (random.nextInt(2) == 0) {
                world.setTypeId(i, j, k, this.id);
            } else {
                world.setTypeId(i, j, k, 0);
            }

            if (flag) {
                Block.TNT.postBreak(world, i, j, k, 0);
            }
        }
    }

    private boolean g(World world, int i, int j, int k) {
        return this.b(world, i + 1, j, k) ? true : (this.b(world, i - 1, j, k) ? true : (this.b(world, i, j - 1, k) ? true : (this.b(world, i, j + 1, k) ? true : (this.b(world, i, j, k - 1) ? true : this.b(world, i, j, k + 1)))));
    }

    private int h(World world, int i, int j, int k) {
        byte b0 = 0;

        if (!world.isEmpty(i, j, k)) {
            return 0;
        } else {
            int l = this.f(world, i + 1, j, k, b0);

            l = this.f(world, i - 1, j, k, l);
            l = this.f(world, i, j - 1, k, l);
            l = this.f(world, i, j + 1, k, l);
            l = this.f(world, i, j, k - 1, l);
            l = this.f(world, i, j, k + 1, l);
            return l;
        }
    }

    public boolean d() {
        return false;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return this.a[iblockaccess.getTypeId(i, j, k)] > 0;
    }

    public int f(World world, int i, int j, int k, int l) {
        int i1 = this.a[world.getTypeId(i, j, k)];

        return i1 > l ? i1 : l;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.d(i, j - 1, k) || this.g(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.d(i, j - 1, k) && !this.g(world, i, j, k)) {
            world.setTypeId(i, j, k, 0);
        }
    }

    public void e(World world, int i, int j, int k) {
        if (world.getTypeId(i, j - 1, k) != Block.OBSIDIAN.id || !Block.PORTAL.a_(world, i, j, k)) {
            if (!world.d(i, j - 1, k) && !this.g(world, i, j, k)) {
                world.setTypeId(i, j, k, 0);
            } else {
                world.c(i, j, k, this.id, this.b());
            }
        }
    }
}
