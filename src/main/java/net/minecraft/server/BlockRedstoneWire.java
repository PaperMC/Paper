package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginLoader;

public class BlockRedstoneWire extends Block {

    private boolean a;
    private Set b;

    public BlockRedstoneWire(int i, int j) {
        super(i, j, Material.n);
        a = true;
        b = ((Set) (new HashSet()));
        a(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public boolean a(World world, int i, int j, int k) {
        return world.d(i, j - 1, k);
    }

    private void g(World world, int i, int j, int k) {
        a(world, i, j, k, i, j, k);
        ArrayList arraylist = new ArrayList(((java.util.Collection) (b)));

        b.clear();
        for (int l = 0; l < ((List) (arraylist)).size(); l++) {
            ChunkPosition chunkposition = (ChunkPosition) ((List) (arraylist)).get(l);

            world.h(chunkposition.a, chunkposition.b, chunkposition.c, bi);
        }
    }

    private void a(World world, int i, int j, int k, int l, int i1, int j1) {
        int k1 = world.b(i, j, k);
        int l1 = 0;

        a = false;
        boolean flag = world.p(i, j, k);

        a = true;
        if (flag) {
            l1 = 15;
        } else {
            for (int i2 = 0; i2 < 4; i2++) {
                int k2 = i;
                int i3 = k;

                if (i2 == 0) {
                    k2--;
                }
                if (i2 == 1) {
                    k2++;
                }
                if (i2 == 2) {
                    i3--;
                }
                if (i2 == 3) {
                    i3++;
                }
                if (k2 != l || j != i1 || i3 != j1) {
                    l1 = f(world, k2, j, i3, l1);
                }
                if (world.d(k2, j, i3) && !world.d(i, j + 1, k)) {
                    if (k2 != l || j + 1 != i1 || i3 != j1) {
                        l1 = f(world, k2, j + 1, i3, l1);
                    }
                    continue;
                }
                if (!world.d(k2, j, i3) && (k2 != l || j - 1 != i1 || i3 != j1)) {
                    l1 = f(world, k2, j - 1, i3, l1);
                }
            }

            if (l1 > 0) {
                l1--;
            } else {
                l1 = 0;
            }
        }

        // Craftbukkit start
        if (k1 != l1) {
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            BlockRedstoneEvent bre = new BlockRedstoneEvent(block, BlockFace.SELF, k1, l1);
            ((WorldServer) world).getServer().getPluginManager().callEvent(bre);
            l1 = bre.getNewCurrent();
        }
        // Craftbukkit end
        
        if (k1 != l1) {
            world.i = true;
            world.c(i, j, k, l1);
            world.b(i, j, k, i, j, k);
            world.i = false;
            for (int j2 = 0; j2 < 4; j2++) {
                int l2 = i;
                int j3 = k;
                int k3 = j - 1;

                if (j2 == 0) {
                    l2--;
                }
                if (j2 == 1) {
                    l2++;
                }
                if (j2 == 2) {
                    j3--;
                }
                if (j2 == 3) {
                    j3++;
                }
                if (world.d(l2, j, j3)) {
                    k3 += 2;
                }
                int l3 = 0;

                l3 = f(world, l2, j, j3, -1);
                l1 = world.b(i, j, k);
                if (l1 > 0) {
                    l1--;
                }
                if (l3 >= 0 && l3 != l1) {
                    a(world, l2, j, j3, i, j, k);
                }
                l3 = f(world, l2, k3, j3, -1);
                l1 = world.b(i, j, k);
                if (l1 > 0) {
                    l1--;
                }
                if (l3 >= 0 && l3 != l1) {
                    a(world, l2, k3, j3, i, j, k);
                }
            }

            if (k1 == 0 || l1 == 0) {
                b.add(((new ChunkPosition(i, j, k))));
                b.add(((new ChunkPosition(i - 1, j, k))));
                b.add(((new ChunkPosition(i + 1, j, k))));
                b.add(((new ChunkPosition(i, j - 1, k))));
                b.add(((new ChunkPosition(i, j + 1, k))));
                b.add(((new ChunkPosition(i, j, k - 1))));
                b.add(((new ChunkPosition(i, j, k + 1))));
            }
        }
    }

    private void h(World world, int i, int j, int k) {
        if (world.a(i, j, k) != bi) {
            return;
        } else {
            world.h(i, j, k, bi);
            world.h(i - 1, j, k, bi);
            world.h(i + 1, j, k, bi);
            world.h(i, j, k - 1, bi);
            world.h(i, j, k + 1, bi);
            world.h(i, j - 1, k, bi);
            world.h(i, j + 1, k, bi);
            return;
        }
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        if (world.z) {
            return;
        }
        g(world, i, j, k);
        world.h(i, j + 1, k, bi);
        world.h(i, j - 1, k, bi);
        h(world, i - 1, j, k);
        h(world, i + 1, j, k);
        h(world, i, j, k - 1);
        h(world, i, j, k + 1);
        if (world.d(i - 1, j, k)) {
            h(world, i - 1, j + 1, k);
        } else {
            h(world, i - 1, j - 1, k);
        }
        if (world.d(i + 1, j, k)) {
            h(world, i + 1, j + 1, k);
        } else {
            h(world, i + 1, j - 1, k);
        }
        if (world.d(i, j, k - 1)) {
            h(world, i, j + 1, k - 1);
        } else {
            h(world, i, j - 1, k - 1);
        }
        if (world.d(i, j, k + 1)) {
            h(world, i, j + 1, k + 1);
        } else {
            h(world, i, j - 1, k + 1);
        }
    }

    public void b(World world, int i, int j, int k) {
        super.b(world, i, j, k);
        if (world.z) {
            return;
        }
        world.h(i, j + 1, k, bi);
        world.h(i, j - 1, k, bi);
        g(world, i, j, k);
        h(world, i - 1, j, k);
        h(world, i + 1, j, k);
        h(world, i, j, k - 1);
        h(world, i, j, k + 1);
        if (world.d(i - 1, j, k)) {
            h(world, i - 1, j + 1, k);
        } else {
            h(world, i - 1, j - 1, k);
        }
        if (world.d(i + 1, j, k)) {
            h(world, i + 1, j + 1, k);
        } else {
            h(world, i + 1, j - 1, k);
        }
        if (world.d(i, j, k - 1)) {
            h(world, i, j + 1, k - 1);
        } else {
            h(world, i, j - 1, k - 1);
        }
        if (world.d(i, j, k + 1)) {
            h(world, i, j + 1, k + 1);
        } else {
            h(world, i, j - 1, k + 1);
        }
    }

    private int f(World world, int i, int j, int k, int l) {
        if (world.a(i, j, k) != bi) {
            return l;
        }
        int i1 = world.b(i, j, k);

        if (i1 > l) {
            return i1;
        } else {
            return l;
        }
    }

    public void b(World world, int i, int j, int k, int l) {
        if (world.z) {
            return;
        }
        int i1 = world.b(i, j, k);
        boolean flag = a(world, i, j, k);

        if (!flag) {
            a_(world, i, j, k, i1);
            world.e(i, j, k, 0);
        } else {
            g(world, i, j, k);
        }
        super.b(world, i, j, k, l);
    }

    public int a(int i, Random random) {
        return Item.aA.ba;
    }

    public boolean d(World world, int i, int j, int k, int l) {
        if (!a) {
            return false;
        } else {
            return b(((IBlockAccess) (world)), i, j, k, l);
        }
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!a) {
            return false;
        }
        if (iblockaccess.b(i, j, k) == 0) {
            return false;
        }
        if (l == 1) {
            return true;
        }
        boolean flag = b(iblockaccess, i - 1, j, k) || !iblockaccess.d(i - 1, j, k) && b(iblockaccess, i - 1, j - 1, k);
        boolean flag1 = b(iblockaccess, i + 1, j, k) || !iblockaccess.d(i + 1, j, k) && b(iblockaccess, i + 1, j - 1, k);
        boolean flag2 = b(iblockaccess, i, j, k - 1) || !iblockaccess.d(i, j, k - 1) && b(iblockaccess, i, j - 1, k - 1);
        boolean flag3 = b(iblockaccess, i, j, k + 1) || !iblockaccess.d(i, j, k + 1) && b(iblockaccess, i, j - 1, k + 1);

        if (!iblockaccess.d(i, j + 1, k)) {
            if (iblockaccess.d(i - 1, j, k) && b(iblockaccess, i - 1, j + 1, k)) {
                flag = true;
            }
            if (iblockaccess.d(i + 1, j, k) && b(iblockaccess, i + 1, j + 1, k)) {
                flag1 = true;
            }
            if (iblockaccess.d(i, j, k - 1) && b(iblockaccess, i, j + 1, k - 1)) {
                flag2 = true;
            }
            if (iblockaccess.d(i, j, k + 1) && b(iblockaccess, i, j + 1, k + 1)) {
                flag3 = true;
            }
        }
        if (!flag2 && !flag1 && !flag && !flag3 && l >= 2 && l <= 5) {
            return true;
        }
        if (l == 2 && flag2 && !flag && !flag1) {
            return true;
        }
        if (l == 3 && flag3 && !flag && !flag1) {
            return true;
        }
        if (l == 4 && flag && !flag2 && !flag3) {
            return true;
        }
        return l == 5 && flag1 && !flag2 && !flag3;
    }

    public boolean c() {
        return a;
    }

    public static boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.a(i, j, k);

        if (l == Block.av.bi) {
            return true;
        }
        if (l == 0) {
            return false;
        }
        return Block.m[l].c();
    }
}
