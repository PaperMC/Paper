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

public class ItemMinecart extends Item {

    public int a;

    public ItemMinecart(int i, int j) {
        super(i);
        bb = 1;
        a = j;
    }

    public boolean a(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
        int i1 = world.a(i, j, k);

        if (i1 == Block.aG.bi) {
            // CraftBukkit start - Minecarts
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer craftServer = ((WorldServer) world).getServer();
            
            Type eventType = Type.PLAYER_ITEM;
            Player who = (entityplayer == null)?null:(Player)entityplayer.getBukkitEntity();
            org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
            org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
            BlockFace blockFace = CraftBlock.notchToBlockFace(1);
            
            PlayerItemEvent pie = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockFace);
            craftServer.getPluginManager().callEvent(pie);
            
            if (pie.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            if (!world.z) {
                world.a(((Entity) (new EntityMinecart(world, (float) i + 0.5F, (float) j + 0.5F, (float) k + 0.5F, a))));
            }
            itemstack.a--;
            return true;
        } else {
            return false;
        }
    }
}
