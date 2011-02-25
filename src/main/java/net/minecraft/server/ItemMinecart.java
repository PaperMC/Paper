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
        this.maxStackSize = 1;
        this.a = j;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.RAILS.id) {
            if (!world.isStatic) {
                // CraftBukkit start - Minecarts
                CraftWorld craftWorld = ((WorldServer) world).getWorld();
                CraftServer craftServer = ((WorldServer) world).getServer();

                Type eventType = Type.PLAYER_ITEM;
                Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                org.bukkit.inventory.ItemStack itemInHand = new CraftItemStack(itemstack);
                org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
                BlockFace blockFace = CraftBlock.notchToBlockFace(l);

                PlayerItemEvent event = new PlayerItemEvent(eventType, who, itemInHand, blockClicked, blockFace);
                craftServer.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end

                world.a((Entity) (new EntityMinecart(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.a)));
            }

            --itemstack.count;
            return true;
        } else {
            return false;
        }
    }
}
