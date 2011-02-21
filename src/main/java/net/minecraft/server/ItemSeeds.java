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

public class ItemSeeds extends Item {

    private int a;

    public ItemSeeds(int i, int j) {
        super(i);
        this.a = j;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (l != 1) {
            return false;
        } else {
            int i1 = world.getTypeId(i, j, k);

            if (i1 == Block.SOIL.id && world.isEmpty(i, j + 1, k)) {
                // CraftBukkit start - Seeds
                CraftWorld craftWorld = ((WorldServer) world).getWorld();
                CraftServer craftServer = ((WorldServer) world).getServer();

                Type eventType = Type.PLAYER_ITEM;
                Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
                org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
                BlockFace blockface = CraftBlock.notchToBlockFace(1);

                PlayerItemEvent event = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockface);
                craftServer.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end

                world.e(i, j + 1, k, this.a);
                --itemstack.count;
                return true;
            } else {
                return false;
            }
        }
    }
}
