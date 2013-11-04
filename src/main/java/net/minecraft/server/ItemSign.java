package net.minecraft.server;

public class ItemSign extends Item {

    public ItemSign() {
        this.maxStackSize = 16;
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (l == 0) {
            return false;
        } else if (!world.getType(i, j, k).getMaterial().isBuildable()) {
            return false;
        } else {
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
            } else if (!Blocks.SIGN_POST.canPlace(world, i, j, k)) {
                return false;
            } else if (world.isStatic) {
                return true;
            } else {
                // CraftBukkit start
                final Block block;
                if (l == 1) {
                    int i1 = MathHelper.floor((double) ((entityhuman.yaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;

                    // world.setTypeAndData(i, j, k, Blocks.SIGN_POST, i1, 3);
                    block = Blocks.SIGN_POST;
                    l = i1;
                } else {
                    // world.setTypeAndData(i, j, k, Blocks.WALL_SIGN, l, 3);
                    block = Blocks.WALL_SIGN;
                }
                if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, block, l, clickedX, clickedY, clickedZ)) {
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
