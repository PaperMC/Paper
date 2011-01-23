package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
// CraftBukkit end

public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel(int i) {
        super(i);
        bb = 1;
        bc = 64;
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        // CraftBukkit - store the clicked block
        CraftWorld craftWorld = ((WorldServer) world).getWorld();
        CraftServer craftServer = ((WorldServer) world).getServer();
        org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
        
        if (l == 0) {
            j--;
        }
        if (l == 1) {
            j++;
        }
        if (l == 2) {
            k--;
        }
        if (l == 3) {
            k++;
        }
        if (l == 4) {
            i--;
        }
        if (l == 5) {
            i++;
        }
        int i1 = world.a(i, j, k);

        if (i1 == 0) {
            // CraftBukkit start - Flint and steel
            Type eventType = Type.PLAYER_ITEM;
            Player thePlayer = (Player) entityplayer.getBukkitEntity();
            CraftItemStack itemInHand = new CraftItemStack(itemstack);
            BlockFace blockFace = CraftBlock.notchToBlockFace(l);
            
            PlayerItemEvent pie = new PlayerItemEvent(eventType, thePlayer, itemInHand, blockClicked, blockFace);
            craftServer.getPluginManager().callEvent(pie);

            boolean preventLighter = pie.isCancelled();
            
            
            IgniteCause igniteCause = BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL;
            BlockIgniteEvent bie = new BlockIgniteEvent(blockClicked, igniteCause, thePlayer);
            craftServer.getPluginManager().callEvent(bie);
            boolean preventFire = bie.isCancelled();


            if (preventLighter) {
                return false;
            }
            
            if (preventFire) {
                itemstack.b(1);
                return false;
            }
            // CraftBukkit end

            world.a((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "fire.ignite", 1.0F, b.nextFloat() * 0.4F + 0.8F);
            world.e(i, j, k, Block.ar.bi);
        }
        itemstack.b(1);
        return true;
    }
}
