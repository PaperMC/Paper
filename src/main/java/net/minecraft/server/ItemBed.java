package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemBed extends Item {

    public ItemBed(int i) {
        super(i);
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l != 1) {
            return false;
        } else {
            int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

            ++j;
            BlockBed blockbed = (BlockBed) Block.BED;
            int i1 = MathHelper.floor((double) (entityhuman.yaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;

            if (i1 == 0) {
                b1 = 1;
            }

            if (i1 == 1) {
                b0 = -1;
            }

            if (i1 == 2) {
                b1 = -1;
            }

            if (i1 == 3) {
                b0 = 1;
            }

            if (entityhuman.e(i, j, k) && entityhuman.e(i + b0, j, k + b1)) {
                if (world.isEmpty(i, j, k) && world.isEmpty(i + b0, j, k + b1) && world.t(i, j - 1, k) && world.t(i + b0, j - 1, k + b1)) {
                    CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                    world.setTypeIdAndData(i, j, k, blockbed.id, i1);

                    // CraftBukkit start - bed
                    org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ);

                    if (event.isCancelled() || !event.canBuild()) {
                        event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                        return false;
                    }
                    // CraftBukkit end

                    if (world.getTypeId(i, j, k) == blockbed.id) {
                        world.setTypeIdAndData(i + b0, j, k + b1, blockbed.id, i1 + 8);
                    }

                    --itemstack.count;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
