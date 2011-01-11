package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftItemStack;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerItemEvent;
// CraftBukkit end

public class ItemSign extends Item {

    public ItemSign(int i) {
        super(i);
        aY = 64;
        aX = 1;
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        if (l == 0) {
            return false;
        }
        if (!world.c(i, j, k).a()) {
            return false;
        }

        // CraftBukkit - store the clicked block
        CraftBlock blockClicked = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);

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
        CraftItemStack itemInHand = new CraftItemStack(itemstack);
        CraftPlayer thePlayer = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
        PlayerItemEvent pie = new PlayerItemEvent(Type.PLAYER_ITEM, thePlayer, itemInHand, blockClicked, CraftBlock.notchToBlockFace(l));

        ((WorldServer) world).getServer().getPluginManager().callEvent(pie);

        if (pie.isCancelled()) return false;
        // CraftBukkit end

        if (l == 1) {
            world.b(i, j, k, Block.aD.bh, MathHelper.b((double) (((entityplayer.v + 180F) * 16F) / 360F) + 0.5D) & 0xf);
        } else {
            world.b(i, j, k, Block.aI.bh, l);
        }
        itemstack.a--;
        TileEntitySign tileentitysign = (TileEntitySign) world.l(i, j, k);

        if (tileentitysign != null) {
            entityplayer.a(tileentitysign);
        }
        return true;
    }
}
