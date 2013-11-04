package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;
// CraftBukkit end

public class BlockFlowing extends BlockFluids {

    int a;
    boolean[] b = new boolean[4];
    int[] M = new int[4];

    protected BlockFlowing(Material material) {
        super(material);
    }

    private void n(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.setTypeAndData(i, j, k, Block.e(Block.b((Block) this) + 1), l, 2);
    }

    public void a(World world, int i, int j, int k, Random random) {
        // CraftBukkit start
        org.bukkit.World bworld = world.getWorld();
        org.bukkit.Server server = world.getServer();
        org.bukkit.block.Block source = bworld == null ? null : bworld.getBlockAt(i, j, k);
        // CraftBukkit end

        int l = this.e(world, i, j, k);
        byte b0 = 1;

        if (this.material == Material.LAVA && !world.worldProvider.f) {
            b0 = 2;
        }

        boolean flag = true;
        int i1 = this.a(world);
        int j1;

        if (l > 0) {
            byte b1 = -100;

            this.a = 0;
            int k1 = this.a(world, i - 1, j, k, b1);

            k1 = this.a(world, i + 1, j, k, k1);
            k1 = this.a(world, i, j, k - 1, k1);
            k1 = this.a(world, i, j, k + 1, k1);
            j1 = k1 + b0;
            if (j1 >= 8 || k1 < 0) {
                j1 = -1;
            }

            if (this.e(world, i, j + 1, k) >= 0) {
                int l1 = this.e(world, i, j + 1, k);

                if (l1 >= 8) {
                    j1 = l1;
                } else {
                    j1 = l1 + 8;
                }
            }

            if (this.a >= 2 && this.material == Material.WATER) {
                if (world.getType(i, j - 1, k).getMaterial().isBuildable()) {
                    j1 = 0;
                } else if (world.getType(i, j - 1, k).getMaterial() == this.material && world.getData(i, j - 1, k) == 0) {
                    j1 = 0;
                }
            }

            if (this.material == Material.LAVA && l < 8 && j1 < 8 && j1 > l && random.nextInt(4) != 0) {
                i1 *= 4;
            }

            if (j1 == l) {
                if (flag) {
                    this.n(world, i, j, k);
                }
            } else {
                l = j1;
                if (j1 < 0) {
                    world.setAir(i, j, k);
                } else {
                    world.setData(i, j, k, j1, 2);
                    world.a(i, j, k, this, i1);
                    world.applyPhysics(i, j, k, this);
                }
            }
        } else {
            this.n(world, i, j, k);
        }

        if (this.q(world, i, j - 1, k)) {
            // CraftBukkit start - Send "down" to the server
            BlockFromToEvent event = new BlockFromToEvent(source, BlockFace.DOWN);
            if (server != null) {
                server.getPluginManager().callEvent(event);
            }

            if (!event.isCancelled()) {
                if (this.material == Material.LAVA && world.getType(i, j - 1, k).getMaterial() == Material.WATER) {
                    world.setTypeUpdate(i, j - 1, k, Blocks.STONE);
                    this.fizz(world, i, j - 1, k);
                    return;
                }

                if (l >= 8) {
                    this.flow(world, i, j - 1, k, l);
                } else {
                    this.flow(world, i, j - 1, k, l + 8);
                }
            }
            // CraftBukkit end
        } else if (l >= 0 && (l == 0 || this.p(world, i, j - 1, k))) {
            boolean[] aboolean = this.o(world, i, j, k);

            j1 = l + b0;
            if (l >= 8) {
                j1 = 1;
            }

            if (j1 >= 8) {
                return;
            }

            // CraftBukkit start - All four cardinal directions. Do not change the order!
            BlockFace[] faces = new BlockFace[] { BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH };
            int index = 0;

            for (BlockFace currentFace : faces) {
                if (aboolean[index]) {
                    BlockFromToEvent event = new BlockFromToEvent(source, currentFace);

                    if (server != null) {
                        server.getPluginManager().callEvent(event);
                    }

                    if (!event.isCancelled()) {
                        this.flow(world, i + currentFace.getModX(), j, k + currentFace.getModZ(), j1);
                    }
                }
                index++;
            }
            // CraftBukkit end
        }
    }

    private void flow(World world, int i, int j, int k, int l) {
        if (this.q(world, i, j, k)) {
            Block block = world.getType(i, j, k);

            if (this.material == Material.LAVA) {
                this.fizz(world, i, j, k);
            } else {
                block.b(world, i, j, k, world.getData(i, j, k), 0);
            }

            world.setTypeAndData(i, j, k, this, l, 3);
        }
    }

    private int c(World world, int i, int j, int k, int l, int i1) {
        int j1 = 1000;

        for (int k1 = 0; k1 < 4; ++k1) {
            if ((k1 != 0 || i1 != 1) && (k1 != 1 || i1 != 0) && (k1 != 2 || i1 != 3) && (k1 != 3 || i1 != 2)) {
                int l1 = i;
                int i2 = k;

                if (k1 == 0) {
                    l1 = i - 1;
                }

                if (k1 == 1) {
                    ++l1;
                }

                if (k1 == 2) {
                    i2 = k - 1;
                }

                if (k1 == 3) {
                    ++i2;
                }

                if (!this.p(world, l1, j, i2) && (world.getType(l1, j, i2).getMaterial() != this.material || world.getData(l1, j, i2) != 0)) {
                    if (!this.p(world, l1, j - 1, i2)) {
                        return l;
                    }

                    if (l < 4) {
                        int j2 = this.c(world, l1, j, i2, l + 1, k1);

                        if (j2 < j1) {
                            j1 = j2;
                        }
                    }
                }
            }
        }

        return j1;
    }

    private boolean[] o(World world, int i, int j, int k) {
        int l;
        int i1;

        for (l = 0; l < 4; ++l) {
            this.M[l] = 1000;
            i1 = i;
            int j1 = k;

            if (l == 0) {
                i1 = i - 1;
            }

            if (l == 1) {
                ++i1;
            }

            if (l == 2) {
                j1 = k - 1;
            }

            if (l == 3) {
                ++j1;
            }

            if (!this.p(world, i1, j, j1) && (world.getType(i1, j, j1).getMaterial() != this.material || world.getData(i1, j, j1) != 0)) {
                if (this.p(world, i1, j - 1, j1)) {
                    this.M[l] = this.c(world, i1, j, j1, 1, l);
                } else {
                    this.M[l] = 0;
                }
            }
        }

        l = this.M[0];

        for (i1 = 1; i1 < 4; ++i1) {
            if (this.M[i1] < l) {
                l = this.M[i1];
            }
        }

        for (i1 = 0; i1 < 4; ++i1) {
            this.b[i1] = this.M[i1] == l;
        }

        return this.b;
    }

    private boolean p(World world, int i, int j, int k) {
        Block block = world.getType(i, j, k);

        return block != Blocks.WOODEN_DOOR && block != Blocks.IRON_DOOR_BLOCK && block != Blocks.SIGN_POST && block != Blocks.LADDER && block != Blocks.SUGAR_CANE_BLOCK ? (block.material == Material.PORTAL ? true : block.material.isSolid()) : true;
    }

    protected int a(World world, int i, int j, int k, int l) {
        int i1 = this.e(world, i, j, k);

        if (i1 < 0) {
            return l;
        } else {
            if (i1 == 0) {
                ++this.a;
            }

            if (i1 >= 8) {
                i1 = 0;
            }

            return l >= 0 && i1 >= l ? l : i1;
        }
    }

    private boolean q(World world, int i, int j, int k) {
        Material material = world.getType(i, j, k).getMaterial();

        return material == this.material ? false : (material == Material.LAVA ? false : !this.p(world, i, j, k));
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        if (world.getType(i, j, k) == this) {
            world.a(i, j, k, this, this.a(world));
        }
    }

    public boolean L() {
        return true;
    }
}
