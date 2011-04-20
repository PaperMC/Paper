package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemRedstone extends Item {

    public ItemRedstone(int i) {
        super(i);
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit;

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
            if (Block.REDSTONE_WIRE.canPlace(world, i, j, k)) {
                BlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                world.setTypeId(i, j, k, Block.REDSTONE_WIRE.id);

                // CraftBukkit start - redstone
                BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ, Block.REDSTONE_WIRE);

                if (event.isCancelled() || !event.canBuild()) {
                    event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                    return false;
                }
                // CraftBukkit end

                --itemstack.count; // CraftBukkit -- ORDER MATTERS
            }

            return true;
        }
    }
}
