package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftBlock;
import org.bukkit.craftbukkit.CraftItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
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
            CraftBlock blockClicked = (CraftBlock) ((WorldServer) world).getWorld().getBlockAt(i, j, k);
            CraftItemStack itemInHand = new CraftItemStack(itemstack);
            CraftPlayer thePlayer = new CraftPlayer(((WorldServer) world).getServer(), (EntityPlayerMP) entityplayer);
            PlayerItemEvent pie = new PlayerItemEvent(Type.PLAYER_ITEM, thePlayer, itemInHand, blockClicked, CraftBlock.notchToBlockFace(l));

            ((WorldServer) world).getServer().getPluginManager().callEvent(pie);

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
