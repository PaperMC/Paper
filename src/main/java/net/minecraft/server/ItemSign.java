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
        this.durability = 64;
        this.maxStackSize = 1;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (l == 0) {
            return false;
        } else if (!world.getMaterial(i, j, k).isBuildable()) {
            return false;
        } else {
            // CraftBukkit start - store the clicked block
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer craftServer = ((WorldServer) world).getServer();
            org.bukkit.block.Block blockClicked = craftWorld.getBlockAt(i, j, k);
            //CraftBukkit end

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

            if (!Block.SIGN_POST.a(world, i, j, k)) {
                return false;
            } else {
                // CraftBukkit start
                // Signs
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

                if (l == 1) {
                    world.b(i, j, k, Block.SIGN_POST.id, MathHelper.b((double) ((entityhuman.yaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15);
                } else {
                    world.b(i, j, k, Block.WALL_SIGN.id, l);
                }

                --itemstack.count;
                TileEntitySign tileentitysign = (TileEntitySign) world.getTileEntity(i, j, k);

                if (tileentitysign != null) {
                    entityhuman.a(tileentitysign);
                }

                return true;
            }
        }
    }
}
