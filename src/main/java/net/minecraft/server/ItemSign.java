package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemSign extends Item {

    public ItemSign(int i) {
        super(i);
        this.maxStackSize = 16;
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l == 0) {
            return false;
        } else if (!world.getMaterial(i, j, k).isBuildable()) {
            return false;
        } else {
            int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

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
            } else if (!Block.SIGN_POST.canPlace(world, i, j, k)) {
                return false;
            } else {
                CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                if (l == 1) {
                    int i1 = MathHelper.floor((double) ((entityhuman.yaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;

                    world.setTypeIdAndData(i, j, k, Block.SIGN_POST.id, i1);
                } else {
                    world.setTypeIdAndData(i, j, k, Block.WALL_SIGN.id, l);
                }

                // CraftBukkit start - sign
                org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ);

                if (event.isCancelled() || !event.canBuild()) {
                    event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                    return false;
                }
                // CraftBukkit end

                --itemstack.count;
                TileEntitySign tileentitysign = (TileEntitySign) world.getTileEntity(i, j, k);

                if (tileentitysign != null) {
                    entityhuman.a((TileEntity) tileentitysign);
                }

                return true;
            }
        }
    }
}
