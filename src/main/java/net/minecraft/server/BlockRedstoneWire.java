package net.minecraft.server;

import java.util.Random;
import org.bukkit.BlockFace;
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginLoader;

public class BlockRedstoneWire extends Block {

    private boolean a;

    public BlockRedstoneWire(int i, int j) {
        super(i, j, Material.n);
        a = true;
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
        int l = world.b(i, j, k);
        int i1 = 0;

        a = false;
        boolean flag = world.o(i, j, k);

        a = true;
        if (flag) {
            i1 = 15;
        } else {
            for (int j1 = 0; j1 < 4; j1++) {
                int l1 = i;
                int j2 = k;

                if (j1 == 0) {
                    l1--;
                }
                if (j1 == 1) {
                    l1++;
                }
                if (j1 == 2) {
                    j2--;
                }
                if (j1 == 3) {
                    j2++;
                }
                i1 = f(world, l1, j, j2, i1);
                if (world.d(l1, j, j2) && !world.d(i, j + 1, k)) {
                    i1 = f(world, l1, j + 1, j2, i1);
                    continue;
                }
                if (!world.d(l1, j, j2)) {
                    i1 = f(world, l1, j - 1, j2, i1);
                }
            }

            if (i1 > 0) {
                i1--;
            } else {
                i1 = 0;
            }
        }
        //Allow redstone wire current changes
        if (l != i1) {
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            BlockRedstoneEvent bre = new BlockRedstoneEvent(block, BlockFace.Self, l, i1);
            ((WorldServer) world).getServer().getPluginManager().callEvent(bre);
            i1 = bre.getNewCurrent();
        }
        if (l != i1) {
            world.b(i, j, k, i1);
            world.b(i, j, k, i, j, k);
            if (i1 > 0) {
                i1--;
            }
            for (int k1 = 0; k1 < 4; k1++) {
                int i2 = i;
                int k2 = k;
                int l2 = j - 1;

                if (k1 == 0) {
                    i2--;
                }
                if (k1 == 1) {
                    i2++;
                }
                if (k1 == 2) {
                    k2--;
                }
                if (k1 == 3) {
                    k2++;
                }
                if (world.d(i2, j, k2)) {
                    l2 += 2;
                }
                int i3 = f(world, i2, j, k2, -1);

                if (i3 >= 0 && i3 != i1) {
                    g(world, i2, j, k2);
                }
                i3 = f(world, i2, l2, k2, -1);
                if (i3 >= 0 && i3 != i1) {
                    g(world, i2, l2, k2);
                }
            }

            if (l == 0 || i1 == 0) {
                world.g(i, j, k, bh);
                world.g(i - 1, j, k, bh);
                world.g(i + 1, j, k, bh);
                world.g(i, j, k - 1, bh);
                world.g(i, j, k + 1, bh);
                world.g(i, j - 1, k, bh);
                world.g(i, j + 1, k, bh);
            }
        }
    }

    private void h(World world, int i, int j, int k) {
        if (world.a(i, j, k) != bh) {
            return;
        } else {
            world.g(i, j, k, bh);
            world.g(i - 1, j, k, bh);
            world.g(i + 1, j, k, bh);
            world.g(i, j, k - 1, bh);
            world.g(i, j, k + 1, bh);
            world.g(i, j - 1, k, bh);
            world.g(i, j + 1, k, bh);
            return;
        }
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        if (world.z) {
            return;
        }
        g(world, i, j, k);
        world.g(i, j + 1, k, bh);
        world.g(i, j - 1, k, bh);
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
        world.g(i, j + 1, k, bh);
        world.g(i, j - 1, k, bh);
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
        if (world.a(i, j, k) != bh) {
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
            world.d(i, j, k, 0);
        } else {
            g(world, i, j, k);
        }
        super.b(world, i, j, k, l);
    }

    public int a(int i, Random random) {
        return Item.aA.aW;
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

        if (l == Block.av.bh) {
            return true;
        }
        if (l == 0) {
            return false;
        }
        return Block.m[l].c();
    }
}
