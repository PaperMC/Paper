package net.minecraft.server;

public class ItemSign extends Item {

    public ItemSign(int i) {
        super(i);
        this.maxStackSize = 16;
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (l == 0) {
            return false;
        } else if (!world.getMaterial(i, j, k).isBuildable()) {
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
            } else if (!Block.SIGN_POST.canPlace(world, i, j, k)) {
                return false;
            } else if (world.isStatic) {
                return true;
            } else {
                // CraftBukkit start
                final Block block;
                if (l == 1) {
                    int i1 = MathHelper.floor((double) ((entityhuman.yaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;

                    // world.setTypeIdAndData(i, j, k, Block.SIGN_POST.id, i1, 3);
                    block = Block.SIGN_POST;
                    l = i1;
                } else {
                    // world.setTypeIdAndData(i, j, k, Block.WALL_SIGN.id, l, 3);
                    block = Block.WALL_SIGN;
                }
                if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, block.id, l, clickedX, clickedY, clickedZ)) {
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
