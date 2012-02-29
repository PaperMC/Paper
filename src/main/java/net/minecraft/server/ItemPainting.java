package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.craftbukkit.block.CraftBlock;
// CraftBukkit end

public class ItemPainting extends Item {

    public ItemPainting(int i) {
        super(i);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (l == 0) {
            return false;
        } else if (l == 1) {
            return false;
        } else {
            byte b0 = 0;

            if (l == 4) {
                b0 = 1;
            }

            if (l == 3) {
                b0 = 2;
            }

            if (l == 5) {
                b0 = 3;
            }

            if (!entityhuman.d(i, j, k)) {
                return false;
            } else {
                EntityPainting entitypainting = new EntityPainting(world, i, j, k, b0);

                if (entitypainting.survives()) {
                    if (!world.isStatic) {
                        // CraftBukkit start
                        Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();

                        org.bukkit.block.Block blockClicked = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.BlockFace blockFace = CraftBlock.notchToBlockFace(l);

                        PaintingPlaceEvent event = new PaintingPlaceEvent((org.bukkit.entity.Painting) entitypainting.getBukkitEntity(), who, blockClicked, blockFace);
                        world.getServer().getPluginManager().callEvent(event);

                        if (event.isCancelled()) {
                            return false;
                        }
                        // CraftBukkit end
                        world.addEntity(entitypainting);
                    }

                    --itemstack.count;
                }

                return true;
            }
        }
    }
}
