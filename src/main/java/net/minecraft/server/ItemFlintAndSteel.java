package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.block.BlockIgniteEvent;
// CraftBukkit end

public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel(int i) {
        super(i);
        bb = 1;
        bc = 64;
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        // CraftBukkit - get the clicked block
        CraftBlock blockClicked = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);

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
            CraftItemStack itemInHand = new CraftItemStack(itemstack);
            CraftPlayer thePlayer = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            PlayerItemEvent pie = new PlayerItemEvent(Type.PLAYER_ITEM, thePlayer, itemInHand, blockClicked, CraftBlock.notchToBlockFace(l));
            ((WorldServer) world).getServer().getPluginManager().callEvent(pie);

            org.bukkit.block.Block pblock = (((WorldServer) world).getWorld().getBlockAt(i, j, k));
            BlockIgniteEvent bie = new BlockIgniteEvent((org.bukkit.block.Block) pblock, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, thePlayer);
            ((WorldServer) world).getServer().getPluginManager().callEvent(bie);

            boolean preventLighter = pie.isCancelled();
            boolean preventFire = bie.isCancelled();

            if (preventLighter || preventFire) {
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
