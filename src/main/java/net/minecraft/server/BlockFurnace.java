package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockFurnace extends BlockContainer {

    private final boolean a;

    protected BlockFurnace(int i, boolean flag) {
        super(i, Material.d);
        a = flag;
        bh = 45;
    }

    public int a(int i, Random random) {
        return Block.aB.bi;
    }

    public void e(World world, int i, int j, int k) {
        super.e(world, i, j, k);
        g(world, i, j, k);
    }

    private void g(World world, int i, int j, int k) {
        int l = world.a(i, j, k - 1);
        int i1 = world.a(i, j, k + 1);
        int j1 = world.a(i - 1, j, k);
        int k1 = world.a(i + 1, j, k);
        byte byte0 = 3;

        if (Block.o[l] && !Block.o[i1]) {
            byte0 = 3;
        }
        if (Block.o[i1] && !Block.o[l]) {
            byte0 = 2;
        }
        if (Block.o[j1] && !Block.o[k1]) {
            byte0 = 5;
        }
        if (Block.o[k1] && !Block.o[j1]) {
            byte0 = 4;
        }
        world.c(i, j, k, ((int) (byte0)));
    }

    public int a(int i) {
        if (i == 1) {
            return bh + 17;
        }
        if (i == 0) {
            return bh + 17;
        }
        if (i == 3) {
            return bh - 1;
        } else {
            return bh;
        }
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (world.z) {
            return true;
        } else {
            // CraftBukkit start - Interact Furnace
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftPlayer player = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, player);

            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);

            if (bie.isCancelled()) return true;
            // CraftBukkit end

            TileEntityFurnace tileentityfurnace = (TileEntityFurnace) world.m(i, j, k);

            entityplayer.a(tileentityfurnace);
            return true;
        }
    }

    public static void a(boolean flag, World world, int i, int j, int k) {
        int l = world.b(i, j, k);
        TileEntity tileentity = world.m(i, j, k);

        if (flag) {
            world.e(i, j, k, Block.aC.bi);
        } else {
            world.e(i, j, k, Block.aB.bi);
        }
        world.c(i, j, k, l);
        world.a(i, j, k, tileentity);
    }

    protected TileEntity a_() {
        return ((TileEntity) (new TileEntityFurnace()));
    }

    public void a(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.b((double) ((entityliving.v * 4F) / 360F) + 0.5D) & 3;

        if (l == 0) {
            world.c(i, j, k, 2);
        }
        if (l == 1) {
            world.c(i, j, k, 5);
        }
        if (l == 2) {
            world.c(i, j, k, 3);
        }
        if (l == 3) {
            world.c(i, j, k, 4);
        }
    }
}
