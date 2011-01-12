package net.minecraft.server;

import java.util.*;
import org.bukkit.BlockFace;
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneTorch extends BlockTorch {

    private boolean a;
    private static List b = new ArrayList();

    private boolean a(World world, int i, int j, int k, boolean flag) {
        if (flag) {
            b.add(((new RedstoneUpdateInfo(i, j, k, world.e))));
        }
        int l = 0;

        for (int i1 = 0; i1 < b.size(); i1++) {
            RedstoneUpdateInfo redstoneupdateinfo = (RedstoneUpdateInfo) b.get(i1);

            if (redstoneupdateinfo.a == i && redstoneupdateinfo.b == j && redstoneupdateinfo.c == k && ++l >= 8) {
                return true;
            }
        }

        return false;
    }

    protected BlockRedstoneTorch(int i, int j, boolean flag) {
        super(i, j);
        a = false;
        a = flag;
        a(true);
    }

    public int b() {
        return 2;
    }

    public void e(World world, int i, int j, int k) {
        if (world.b(i, j, k) == 0) {
            super.e(world, i, j, k);
        }
        if (a) {
            world.g(i, j - 1, k, bh);
            world.g(i, j + 1, k, bh);
            world.g(i - 1, j, k, bh);
            world.g(i + 1, j, k, bh);
            world.g(i, j, k - 1, bh);
            world.g(i, j, k + 1, bh);
        }
    }

    public void b(World world, int i, int j, int k) {
        if (a) {
            world.g(i, j - 1, k, bh);
            world.g(i, j + 1, k, bh);
            world.g(i - 1, j, k, bh);
            world.g(i + 1, j, k, bh);
            world.g(i, j, k - 1, bh);
            world.g(i, j, k + 1, bh);
        }
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!a) {
            return false;
        }
        int i1 = iblockaccess.b(i, j, k);

        if (i1 == 5 && l == 1) {
            return false;
        }
        if (i1 == 3 && l == 3) {
            return false;
        }
        if (i1 == 4 && l == 2) {
            return false;
        }
        if (i1 == 1 && l == 5) {
            return false;
        }
        return i1 != 2 || l != 4;
    }

    private boolean g(World world, int i, int j, int k) {
        int l = world.b(i, j, k);

        if (l == 5 && world.j(i, j - 1, k, 0)) {
            return true;
        }
        if (l == 3 && world.j(i, j, k - 1, 2)) {
            return true;
        }
        if (l == 4 && world.j(i, j, k + 1, 3)) {
            return true;
        }
        if (l == 1 && world.j(i - 1, j, k, 4)) {
            return true;
        }
        return l == 2 && world.j(i + 1, j, k, 5);
    }

    public void a(World world, int i, int j, int k, Random random) {
        boolean flag = g(world, i, j, k);

        for (; b.size() > 0 && world.e - ((RedstoneUpdateInfo) b.get(0)).d > 100L; b.remove(0)) {
            ;
        }
        //Added by craftbukkit
        CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
        BlockRedstoneEvent bre = new BlockRedstoneEvent(block, BlockFace.Self, flag ? 15 : 0, flag ? 0 : 15);
        ((WorldServer) world).getServer().getPluginManager().callEvent(bre);
        if ((bre.getNewCurrent() != 0) == flag) {
            return;
        }
        if (a) {
            if (flag) {
                world.b(i, j, k, Block.aP.bh, world.b(i, j, k));

                if (a(world, i, j, k, true)) {
                    world.a((float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.l.nextFloat() - world.l.nextFloat()) * 0.8F);
                    for (int l = 0; l < 5; l++) {
                        double d1 = (double) i + random.nextDouble() * 0.59999999999999998D + 0.20000000000000001D;
                        double d2 = (double) j + random.nextDouble() * 0.59999999999999998D + 0.20000000000000001D;
                        double d3 = (double) k + random.nextDouble() * 0.59999999999999998D + 0.20000000000000001D;

                        world.a("smoke", d1, d2, d3, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        } else if (!flag
                && !a(world, i, j, k,
                false)) {
            world.b(i, j, k, Block.aQ.bh, world.b(i, j, k));
        }
    }

    public void b(World world, int i, int j, int k, int l) {
        super.b(world, i, j, k, l);
        world.h(i, j, k, bh);
    }

    public boolean d(World world, int i, int j, int k, int l) {
        if (l == 0) {
            return b(((IBlockAccess) (world)), i, j, k, l);
        } else {
            return false;
        }
    }

    public int a(int i, Random random) {
        return Block.aQ.bh;
    }

    public boolean c() {
        return true;
    }
}
