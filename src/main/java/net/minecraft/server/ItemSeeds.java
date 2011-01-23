package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
// CraftBukkit end

public class ItemSeeds extends Item {

    private int a;

    public ItemSeeds(int i, int j) {
        super(i);
        a = j;
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        if (l != 1) {
            return false;
        }
        int i1 = world.a(i, j, k);

        if (i1 == Block.aA.bi && world.e(i, j + 1, k)) {
            // CraftBukkit start - Seeds
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer craftServer = ((WorldServer) world).getServer();
            
            Type eventType = Type.PLAYER_ITEM;
            Player who = (entityplayer == null)?null:(Player)entityplayer.getBukkitEntity();
            org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
            org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
            BlockFace blockface = CraftBlock.notchToBlockFace(1);
            
            PlayerItemEvent pie = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockface);
            craftServer.getPluginManager().callEvent(pie);
            
            if (pie.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            world.e(i, j + 1, k, a);
            itemstack.a--;
            return true;
        } else {
            return false;
        }
    }
}
