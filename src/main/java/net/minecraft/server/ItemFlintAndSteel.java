package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;
// CraftBukkit end

public class ItemFlintAndSteel extends Item {

    public ItemFlintAndSteel(int i) {
        super(i);
        this.maxStackSize = 1;
        this.setMaxDurability(64);
        this.a(CreativeModeTab.i);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

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

        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            int i1 = world.getTypeId(i, j, k);

            if (i1 == 0) {
                // CraftBukkit start - store the clicked block
                org.bukkit.block.Block blockClicked = world.getWorld().getBlockAt(i, j, k);
                Player thePlayer = (Player) entityhuman.getBukkitEntity();

                BlockIgniteEvent eventIgnite = new BlockIgniteEvent(blockClicked, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, thePlayer);
                world.getServer().getPluginManager().callEvent(eventIgnite);

                if (eventIgnite.isCancelled()) {
                    itemstack.damage(1, entityhuman);
                    return false;
                }

                CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k);
                // CraftBukkit end

                world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "fire.ignite", 1.0F, e.nextFloat() * 0.4F + 0.8F);
                world.setTypeIdUpdate(i, j, k, Block.FIRE.id);

                // CraftBukkit start
                org.bukkit.event.block.BlockPlaceEvent placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ);

                if (placeEvent.isCancelled() || !placeEvent.canBuild()) {
                    placeEvent.getBlockPlaced().setTypeIdAndData(0, (byte) 0, false);
                    return false;
                }
                // CraftBukkit end
            }

            itemstack.damage(1, entityhuman);
            return true;
        }
    }
}
