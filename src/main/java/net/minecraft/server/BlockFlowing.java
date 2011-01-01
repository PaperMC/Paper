package net.minecraft.server;


import java.util.Random;

import org.bukkit.BlockFace;
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFlowing extends BlockFluids {

    int a;
    boolean b[];
    int c[];

    protected BlockFlowing(int i1, Material material) {
        super(i1, material);
        a = 0;
        b = new boolean[4];
        c = new int[4];
    }

    private void i(World world, int i1, int j1, int k1) {
        int l1 = world.b(i1, j1, k1);

        world.a(i1, j1, k1, bh + 1, l1);
        world.b(i1, j1, k1, i1, j1, k1);
        world.g(i1, j1, k1);
    }

    public void a(World world, int i1, int j1, int k1, Random random) {
        int l1 = g(world, i1, j1, k1);
        byte byte0 = 1;

        if (bs == Material.g && !world.q.d) {
            byte0 = 2;
        }
        boolean flag = true;

        if (l1 > 0) {
            int i2 = -100;

            a = 0;
            i2 = e(world, i1 - 1, j1, k1, i2);
            i2 = e(world, i1 + 1, j1, k1, i2);
            i2 = e(world, i1, j1, k1 - 1, i2);
            i2 = e(world, i1, j1, k1 + 1, i2);
            int j2 = i2 + byte0;

            if (j2 >= 8 || i2 < 0) {
                j2 = -1;
            }
            if (g(world, i1, j1 + 1, k1) >= 0) {
                int l2 = g(world, i1, j1 + 1, k1);

                if (l2 >= 8) {
                    j2 = l2;
                } else {
                    j2 = l2 + 8;
                }
            }
            if (a >= 2 && bs == Material.f) {
                if (world.d(i1, j1 - 1, k1)) {
                    j2 = 0;
                } else if (world.c(i1, j1 - 1, k1) == bs && world.b(i1, j1, k1) == 0) {
                    j2 = 0;
                }
            }
            if (bs == Material.g && l1 < 8 && j2 < 8 && j2 > l1 && random.nextInt(4) != 0) {
                j2 = l1;
                flag = false;
            }
            if (j2 != l1) {
                l1 = j2;
                if (l1 < 0) {
                    world.d(i1, j1, k1, 0);
                } else {
                    world.b(i1, j1, k1, l1);
                    world.h(i1, j1, k1, bh);
                    world.g(i1, j1, k1, bh);
                }
            } else if (flag) {
                i(world, i1, j1, k1);
            }
        } else {
            i(world, i1, j1, k1);
        }
        
        // Craftbukkit start
        CraftBlock source = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i1, j1, k1);
        if (l(world, i1, j1 - 1, k1)) {
            // Craftbucket send "down" to the server
            BlockFromToEvent blockFlow = new BlockFromToEvent(Type.BLOCK_FLOW, source, BlockFace.Down);
            ((WorldServer) world).getServer().getPluginManager().callEvent(blockFlow);

            if (!blockFlow.isCancelled()) {
                if (l1 >= 8) {
                    world.b(i1, j1 - 1, k1, bh, l1);
                } else {
                    world.b(i1, j1 - 1, k1, bh, l1 + 8);
                }
            }
        } else if (l1 >= 0 && (l1 == 0 || k(world, i1, j1 - 1, k1))) {
            boolean aflag[] = j(world, i1, j1, k1);
            int k2 = l1 + byte0;

            if (l1 >= 8) {
                k2 = 1;
            }
            if (k2 >= 8) {
                return;
            }
            // Craftbukkit all four cardinal directions. Do not change the order!
            BlockFace[] faces = new BlockFace[]{ BlockFace.North, BlockFace.South, BlockFace.East, BlockFace.West };
            for (BlockFace currentFace : faces) {
                int index = 0;
                if (aflag[index]) {
                    BlockFromToEvent event = new BlockFromToEvent(Type.BLOCK_FLOW, source, currentFace);
                    ((WorldServer) world).getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled())
                        f(world, i1 + currentFace.getModX(), j1, k1 + currentFace.getModZ(), k2);
                }
                index++;
            }
        }
        // Craftbukkit stop
    }

    private void f(World world, int i1, int j1, int k1, int l1) {
        if (l(world, i1, j1, k1)) {
            int i2 = world.a(i1, j1, k1);

            if (i2 > 0) {
                if (bs == Material.g) {
                    h(world, i1, j1, k1);
                } else {
                    Block.m[i2].a_(world, i1, j1, k1, world.b(i1, j1, k1));
                }
            }
            world.b(i1, j1, k1, bh, l1);
        }
    }

    private int a(World world, int i1, int j1, int k1, int l1, int i2) {
        int j2 = 1000;

        for (int k2 = 0; k2 < 4; k2++) {
            if (k2 == 0 && i2 == 1 || k2 == 1 && i2 == 0 || k2 == 2 && i2 == 3 || k2 == 3 && i2 == 2) {
                continue;
            }
            int l2 = i1;
            int i3 = j1;
            int j3 = k1;

            if (k2 == 0) {
                l2--;
            }
            if (k2 == 1) {
                l2++;
            }
            if (k2 == 2) {
                j3--;
            }
            if (k2 == 3) {
                j3++;
            }
            if (k(world, l2, i3, j3) || world.c(l2, i3, j3) == bs && world.b(l2, i3, j3) == 0) {
                continue;
            }
            if (!k(world, l2, i3 - 1, j3)) {
                return l1;
            }
            if (l1 >= 4) {
                continue;
            }
            int k3 = a(world, l2, i3, j3, l1 + 1, k2);

            if (k3 < j2) {
                j2 = k3;
            }
        }

        return j2;
    }

    private boolean[] j(World world, int i1, int j1, int k1) {
        for (int l1 = 0; l1 < 4; l1++) {
            c[l1] = 1000;
            int j2 = i1;
            int i3 = j1;
            int j3 = k1;

            if (l1 == 0) {
                j2--;
            }
            if (l1 == 1) {
                j2++;
            }
            if (l1 == 2) {
                j3--;
            }
            if (l1 == 3) {
                j3++;
            }
            if (k(world, j2, i3, j3) || world.c(j2, i3, j3) == bs && world.b(j2, i3, j3) == 0) {
                continue;
            }
            if (!k(world, j2, i3 - 1, j3)) {
                c[l1] = 0;
            } else {
                c[l1] = a(world, j2, i3, j3, 1, l1);
            }
        }

        int i2 = c[0];

        for (int k2 = 1; k2 < 4; k2++) {
            if (c[k2] < i2) {
                i2 = c[k2];
            }
        }

        for (int l2 = 0; l2 < 4; l2++) {
            b[l2] = c[l2] == i2;
        }

        return b;
    }

    private boolean k(World world, int i1, int j1, int k1) {
        int l1 = world.a(i1, j1, k1);

        if (l1 == Block.aE.bh || l1 == Block.aL.bh || l1 == Block.aD.bh || l1 == Block.aF.bh || l1 == Block.aX.bh) {
            return true;
        }
        if (l1 == 0) {
            return false;
        }
        Material material = Block.m[l1].bs;

        return material.a();
    }

    protected int e(World world, int i1, int j1, int k1, int l1) {
        int i2 = g(world, i1, j1, k1);

        if (i2 < 0) {
            return l1;
        }
        if (i2 == 0) {
            a++;
        }
        if (i2 >= 8) {
            i2 = 0;
        }
        return l1 >= 0 && i2 >= l1 ? l1 : i2;
    }

    private boolean l(World world, int i1, int j1, int k1) {
        Material material = world.c(i1, j1, k1);

        if (material == bs) {
            return false;
        }
        if (material == Material.g) {
            return false;
        } else {
            return !k(world, i1, j1, k1);
        }
    }

    public void e(World world, int i1, int j1, int k1) {
        super.e(world, i1, j1, k1);
        if (world.a(i1, j1, k1) == bh) {
            world.h(i1, j1, k1, bh);
        }
    }
}

