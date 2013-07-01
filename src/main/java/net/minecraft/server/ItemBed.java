package net.minecraft.server;

public class ItemBed extends Item {

    public ItemBed(int i) {
        super(i);
        this.a(CreativeModeTab.c);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (world.isStatic) {
            return true;
        } else if (l != 1) {
            return false;
        } else {
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

            if (entityhuman.a(i, j, k, l, itemstack) && entityhuman.a(i + b0, j, k + b1, l, itemstack)) {
                if (world.isEmpty(i, j, k) && world.isEmpty(i + b0, j, k + b1) && world.w(i, j - 1, k) && world.w(i + b0, j - 1, k + b1)) {
                    // CraftBukkit start
                    // world.setTypeIdAndData(i, j, k, blockbed.id, i1, 3);
                    if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, blockbed.id, i1, clickedX, clickedY, clickedZ)) {
                        return false;
                    }
                    // CraftBukkit end

                    if (world.getTypeId(i, j, k) == blockbed.id) {
                        world.setTypeIdAndData(i + b0, j, k + b1, blockbed.id, i1 + 8, 3);
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
