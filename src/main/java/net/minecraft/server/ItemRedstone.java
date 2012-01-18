package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemRedstone extends Item {

    public ItemRedstone(int i) {
        super(i);
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

        if (world.getTypeId(i, j, k) != Block.SNOW.id) {
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
            }
        }

        if (!entityhuman.d(i, j, k)) {
            return false;
        } else {
            if (Block.REDSTONE_WIRE.canPlace(world, i, j, k)) {
                CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                world.setRawTypeId(i, j, k, Block.REDSTONE_WIRE.id); // CraftBukkit - We update after the event

                // CraftBukkit start - redstone
                BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ, Block.REDSTONE_WIRE);

                if (event.isCancelled() || !event.canBuild()) {
                    event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                    return false;
                }

                Block.REDSTONE_WIRE.postPlace( world, i, j, k, Block.REDSTONE_WIRE.id); // Call postPlace since it is now used.
                world.update(i, j, k, Block.REDSTONE_WIRE.id); // Must take place after BlockPlaceEvent, we need to update all other blocks.
                // CraftBukkit end

                --itemstack.count; // CraftBukkit - ORDER MATTERS
            }

            return true;
        }
    }
}
