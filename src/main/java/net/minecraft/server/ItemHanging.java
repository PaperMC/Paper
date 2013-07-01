package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
// CraftBukkit end

public class ItemHanging extends Item {

    private final Class a;

    public ItemHanging(int i, Class oclass) {
        super(i);
        this.a = oclass;
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l == 0) {
            return false;
        } else if (l == 1) {
            return false;
        } else {
            int i1 = Direction.e[l];
            EntityHanging entityhanging = this.a(world, i, j, k, i1);

            if (!entityhuman.a(i, j, k, l, itemstack)) {
                return false;
            } else {
                if (entityhanging != null && entityhanging.survives()) {
                    if (!world.isStatic) {
                        // CraftBukkit start
                        Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();
                        org.bukkit.block.Block blockClicked = world.getWorld().getBlockAt(i, j, k);
                        org.bukkit.block.BlockFace blockFace = org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(l);

                        HangingPlaceEvent event = new HangingPlaceEvent((org.bukkit.entity.Hanging) entityhanging.getBukkitEntity(), who, blockClicked, blockFace);
                        world.getServer().getPluginManager().callEvent(event);

                        PaintingPlaceEvent paintingEvent = null;
                        if (entityhanging instanceof EntityPainting) {
                            // Fire old painting event until it can be removed
                            paintingEvent = new PaintingPlaceEvent((org.bukkit.entity.Painting) entityhanging.getBukkitEntity(), who, blockClicked, blockFace);
                            paintingEvent.setCancelled(event.isCancelled());
                            world.getServer().getPluginManager().callEvent(paintingEvent);
                        }

                        if (event.isCancelled() || (paintingEvent != null && paintingEvent.isCancelled())) {
                            return false;
                        }
                        // CraftBukkit end

                        world.addEntity(entityhanging);
                    }

                    --itemstack.count;
                }

                return true;
            }
        }
    }

    private EntityHanging a(World world, int i, int j, int k, int l) {
        return (EntityHanging) (this.a == EntityPainting.class ? new EntityPainting(world, i, j, k, l) : (this.a == EntityItemFrame.class ? new EntityItemFrame(world, i, j, k, l) : null));
    }
}
