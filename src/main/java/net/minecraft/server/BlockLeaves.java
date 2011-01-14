package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.LeavesDecayEvent;
// CraftBukkit end

public class BlockLeaves extends BlockLeavesBase {

    private int c;
    int b[];

    protected BlockLeaves(int i, int j) {
        super(i, j, Material.h, false);
        c = j;
        a(true);
    }

    public void b(World world, int i, int j, int k) {
        int l = 1;
        int i1 = l + 1;

        if (world.a(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
            for (int j1 = -l; j1 <= l; j1++) {
                for (int k1 = -l; k1 <= l; k1++) {
                    for (int l1 = -l; l1 <= l; l1++) {
                        int i2 = world.a(i + j1, j + k1, k + l1);

                        if (i2 == Block.K.bi) {
                            int j2 = world.b(i + j1, j + k1, k + l1);

                            world.d(i + j1, j + k1, k + l1, j2 | 4);
                        }
                    }
                }
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (world.z) {
            return;
        }
        int l = world.b(i, j, k);

        if ((l & 4) != 0) {
            byte byte0 = 4;
            int i1 = byte0 + 1;
            byte byte1 = 32;
            int j1 = byte1 * byte1;
            int k1 = byte1 / 2;

            if (b == null) {
                b = new int[byte1 * byte1 * byte1];
            }
            if (world.a(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
                for (int l1 = -byte0; l1 <= byte0; l1++) {
                    for (int k2 = -byte0; k2 <= byte0; k2++) {
                        for (int i3 = -byte0; i3 <= byte0; i3++) {
                            int k3 = world.a(i + l1, j + k2, k + i3);

                            if (k3 == Block.J.bi) {
                                b[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = 0;
                                continue;
                            }
                            if (k3 == Block.K.bi) {
                                b[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -2;
                            } else {
                                b[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -1;
                            }
                        }
                    }
                }

                for (int i2 = 1; i2 <= 4; i2++) {
                    for (int l2 = -byte0; l2 <= byte0; l2++) {
                        for (int j3 = -byte0; j3 <= byte0; j3++) {
                            for (int l3 = -byte0; l3 <= byte0; l3++) {
                                if (b[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] != i2 - 1) {
                                    continue;
                                }
                                if (b[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2) {
                                    b[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (b[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2) {
                                    b[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (b[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] == -2) {
                                    b[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (b[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] == -2) {
                                    b[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (b[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] == -2) {
                                    b[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] = i2;
                                }
                                if (b[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] == -2) {
                                    b[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] = i2;
                                }
                            }
                        }
                    }
                }
            }
            int j2 = b[k1 * j1 + k1 * byte1 + k1];

            if (j2 >= 0) {
                world.c(i, j, k, l & -5);
            } else {
                g(world, i, j, k);
            }
        }
    }

    private void g(World world, int i, int j, int k) {
        // CraftBukkit start
        CraftServer server = ((WorldServer) world).getServer();
        CraftWorld cworld = ((WorldServer) world).getWorld();
        LeavesDecayEvent event = new LeavesDecayEvent(Type.LEAVES_DECAY, cworld.getBlockAt(i, j, k));
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;
        // CraftBukkit end

        a_(world, i, j, k, world.b(i, j, k));
        world.e(i, j, k, 0);
    }

    public int a(Random random) {
        return random.nextInt(16) != 0 ? 0 : 1;
    }

    public int a(int i, Random random) {
        return Block.y.bi;
    }

    public boolean a() {
        return !a;
    }

    public void b(World world, int i, int j, int k, Entity entity) {
        super.b(world, i, j, k, entity);
    }
}
