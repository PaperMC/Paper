package net.minecraft.server;


import java.util.Random;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.LeavesDecayEvent;


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

                        if (i2 == Block.K.bh) {
                            world.c(i + j1, j + k1, k + l1, 7);
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
        if (world.b(i, j, k) == 7) {
            byte byte0 = 4;
            int l = byte0 + 1;
            byte byte1 = 32;
            int i1 = byte1 * byte1;
            int j1 = byte1 / 2;

            if (b == null) {
                b = new int[byte1 * byte1 * byte1];
            }
            if (world.a(i - l, j - l, k - l, i + l, j + l, k + l)) {
                for (int k1 = -byte0; k1 <= byte0; k1++) {
                    for (int j2 = -byte0; j2 <= byte0; j2++) {
                        for (int l2 = -byte0; l2 <= byte0; l2++) {
                            int j3 = world.a(i + k1, j + j2, k + l2);

                            if (j3 == Block.J.bh) {
                                b[(k1 + j1) * i1 + (j2 + j1) * byte1 + (l2 + j1)] = 0;
                                continue;
                            }
                            if (j3 == Block.K.bh) {
                                b[(k1 + j1) * i1 + (j2 + j1) * byte1 + (l2 + j1)] = -2;
                            } else {
                                b[(k1 + j1) * i1 + (j2 + j1) * byte1 + (l2 + j1)] = -1;
                            }
                        }

                    }

                }

                for (int l1 = 1; l1 <= 4; l1++) {
                    for (int k2 = -byte0; k2 <= byte0; k2++) {
                        for (int i3 = -byte0; i3 <= byte0; i3++) {
                            for (int k3 = -byte0; k3 <= byte0; k3++) {
                                if (b[(k2 + j1) * i1 + (i3 + j1) * byte1 + (k3 + j1)] != l1 - 1) {
                                    continue;
                                }
                                if (b[((k2 + j1) - 1) * i1 + (i3 + j1) * byte1 + (k3 + j1)] == -2) {
                                    b[((k2 + j1) - 1) * i1 + (i3 + j1) * byte1 + (k3 + j1)] = l1;
                                }
                                if (b[(k2 + j1 + 1) * i1 + (i3 + j1) * byte1 + (k3 + j1)] == -2) {
                                    b[(k2 + j1 + 1) * i1 + (i3 + j1) * byte1 + (k3 + j1)] = l1;
                                }
                                if (b[(k2 + j1) * i1 + ((i3 + j1) - 1) * byte1 + (k3 + j1)] == -2) {
                                    b[(k2 + j1) * i1 + ((i3 + j1) - 1) * byte1 + (k3 + j1)] = l1;
                                }
                                if (b[(k2 + j1) * i1 + (i3 + j1 + 1) * byte1 + (k3 + j1)] == -2) {
                                    b[(k2 + j1) * i1 + (i3 + j1 + 1) * byte1 + (k3 + j1)] = l1;
                                }
                                if (b[(k2 + j1) * i1 + (i3 + j1) * byte1 + ((k3 + j1) - 1)] == -2) {
                                    b[(k2 + j1) * i1 + (i3 + j1) * byte1 + ((k3 + j1) - 1)] = l1;
                                }
                                if (b[(k2 + j1) * i1 + (i3 + j1) * byte1 + (k3 + j1 + 1)] == -2) {
                                    b[(k2 + j1) * i1 + (i3 + j1) * byte1 + (k3 + j1 + 1)] = l1;
                                }
                            }

                        }

                    }

                }

            }
            int i2 = b[j1 * i1 + j1 * byte1 + j1];

            if (i2 >= 0) {
                world.b(i, j, k, 0);
            } else {
                g(world, i, j, k);
            }
        }
    }

    private void g(World world, int i, int j, int k) {
        // Craftbukkit start
        CraftServer server = ((WorldServer)world).getServer();
        CraftWorld cworld = ((WorldServer)world).getWorld();
        LeavesDecayEvent event = new LeavesDecayEvent(Type.LEAVES_DECAY, cworld.getBlockAt(i, j, k));
        server.getPluginManager().callEvent(event);
        
        if (event.isCancelled()) {
            return;
        }
        // Craftbukkit end

        a_(world, i, j, k, world.b(i, j, k));
        world.d(i, j, k, 0);
    }

    public int a(Random random) {
        return random.nextInt(16) != 0 ? 0 : 1;
    }

    public int a(int i, Random random) {
        return Block.y.bh;
    }

    public boolean a() {
        return !a;
    }

    public void b(World world, int i, int j, int k, Entity entity) {
        super.b(world, i, j, k, entity);
    }
}

