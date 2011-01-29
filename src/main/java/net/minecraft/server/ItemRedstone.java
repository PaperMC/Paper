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

public class ItemRedstone extends Item {

    public ItemRedstone(int i) {
        super(i);
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        // CraftBukkit start - store the clicked block
        CraftWorld craftWorld = ((WorldServer) world).getWorld();
        CraftServer craftServer = ((WorldServer) world).getServer();
        org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
        // CraftBukkit end

        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }

        if (!world.isEmpty(i, j, k)) {
            return false;
        } else {
            if (Block.REDSTONE_WIRE.a(world, i, j, k)) {
                // CraftBukkit start - Redstone
                Type eventType = Type.PLAYER_ITEM;
                Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
                BlockFace blockface = CraftBlock.notchToBlockFace(1);

                PlayerItemEvent event = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockface);
                craftServer.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end

                --itemstack.count;
                world.e(i, j, k, Block.REDSTONE_WIRE.id);
            }

            return true;
        }
    }
}
