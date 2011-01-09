package net.minecraft.server;

import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;


public class BlockWorkbench extends Block {

    protected BlockWorkbench(int i) {
        super(i, Material.c);
        bg = 59;
    }

    public int a(int i) {
        if (i == 1) {
            return bg - 16;
        }
        if (i == 0) {
            return Block.x.a(0);
        }
        if (i == 2 || i == 4) {
            return bg + 1;
        } else {
            return bg;
        }
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (world.z) {
            return true;
        } else {
            // Craftbukkit start - Interact Workbench
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftPlayer player = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, player);
            
            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);
            
            if (bie.isCancelled()) return true;
            
            entityplayer.a(i, j, k);
            return true;
        }
    }
}

