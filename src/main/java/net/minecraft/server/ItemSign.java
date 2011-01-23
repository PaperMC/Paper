package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
// CraftBukkit end

public class ItemSign extends Item {

    public ItemSign(int i) {
        super(i);
        bc = 64;
        bb = 1;
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        if (l == 0) {
            return false;
        }
        if (!world.c(i, j, k).a()) {
            return false;
        }

        // CraftBukkit - store the clicked block

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
        if (!Block.aD.a(world, i, j, k)) {
            return false;
        }

        // CraftBukkit start
        // Signs
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

        if (l == 1) {
            world.b(i, j, k, Block.aD.bi, MathHelper.b((double) (((entityplayer.v + 180F) * 16F) / 360F) + 0.5D) & 0xf);
        } else {
            world.b(i, j, k, Block.aI.bi, l);
        }
        itemstack.a--;
        TileEntitySign tileentitysign = (TileEntitySign) world.m(i, j, k);

        if (tileentitysign != null) {
            entityplayer.a(tileentitysign);
        }
        return true;
    }
}
