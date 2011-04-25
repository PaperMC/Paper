package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.painting.PaintingPlaceEvent;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
// CraftBukkit end

public class ItemPainting extends Item {

    public ItemPainting(int i) {
        super(i);
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
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

            EntityPainting entitypainting = new EntityPainting(world, i, j, k, b0);

            if (entitypainting.h()) {
                if (!world.isStatic) {
                    // CraftBukkit start
                    CraftWorld craftWorld = ((WorldServer) world).getWorld();
                    Painting painting = (Painting) entitypainting.getBukkitEntity();
                    Player who = (entityhuman == null) ? null : (Player) entityhuman.getBukkitEntity();

                    Block blockClicked = craftWorld.getBlockAt(i, j, k);
                    BlockFace blockFace = CraftBlock.notchToBlockFace(l);

                    PaintingPlaceEvent event = new PaintingPlaceEvent(painting, who, blockClicked, blockFace);
                    Bukkit.getServer().getPluginManager().callEvent(event);
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
