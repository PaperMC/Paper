package net.minecraft.server;


import java.util.Random;

import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;


public class BlockFurnace extends BlockContainer {

    private final boolean a;

    protected BlockFurnace(int i, boolean flag) {
        super(i, Material.d);
        a = flag;
        bg = 45;
    }

    public int a(int i, Random random) {
        return Block.aB.bh;
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
        world.b(i, j, k, byte0);
    }

    public int a(int i) {
        if (i == 1) {
            return Block.t.bh;
        }
        if (i == 0) {
            return Block.t.bh;
        }
        if (i == 3) {
            return bg - 1;
        } else {
            return bg;
        }
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (world.z) {
            return true;
        } else {
            // Craftbukkit start - Interact Furnace
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftPlayer player = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, player);
            
            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);
            
            if (bie.isCancelled()) return true;
            
            TileEntityFurnace tileentityfurnace = (TileEntityFurnace) world.l(i, j, k);

            entityplayer.a(tileentityfurnace);
            return true;
        }
    }

    public static void a(boolean flag, World world, int i, int j, int k) {
        int l = world.b(i, j, k);
        TileEntity tileentity = world.l(i, j, k);

        if (flag) {
            world.d(i, j, k, Block.aC.bh);
        } else {
            world.d(i, j, k, Block.aB.bh);
        }
        world.b(i, j, k, l);
        world.a(i, j, k, tileentity);
    }

    protected TileEntity a_() {
        return new TileEntityFurnace();
    }

    public void a(World world, int i, int j, int k, EntityLiving entityliving) {
        int l = MathHelper.b((double) ((entityliving.v * 4F) / 360F) + 0.5D) & 3;

        if (l == 0) {
            world.b(i, j, k, 2);
        }
        if (l == 1) {
            world.b(i, j, k, 5);
        }
        if (l == 2) {
            world.b(i, j, k, 3);
        }
        if (l == 3) {
            world.b(i, j, k, 4);
        }
    }
}

