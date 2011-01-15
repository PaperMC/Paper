package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
// CraftBukkit end

public class BlockWorkbench extends Block {

    protected BlockWorkbench(int i) {
        super(i, Material.c);
        bh = 59;
    }

    public int a(int i) {
        if (i == 1) {
            return bh - 16;
        }
        if (i == 0) {
            return Block.x.a(0);
        }
        if (i == 2 || i == 4) {
            return bh + 1;
        } else {
            return bh;
        }
    }

    public boolean a(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (world.z) {
            return true;
        } else {
            // CraftBukkit start - Interact Workbench
            CraftBlock block = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftPlayer player = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            BlockInteractEvent bie = new BlockInteractEvent(Type.BLOCK_INTERACT, block, player);

            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);

            if (bie.isCancelled()) return true;
            // CraftBukkit end

            entityplayer.a(i, j, k);
            return true;
        }
    }
}
