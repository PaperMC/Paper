package net.minecraft.server;

public class ItemRedstone extends Item {

    public ItemRedstone() {
        this.a(CreativeModeTab.d);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (world.getType(i, j, k) != Blocks.SNOW) {
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

        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            if (Blocks.REDSTONE_WIRE.canPlace(world, i, j, k)) {
                // CraftBukkit start
                // --itemstack.count;
                // world.setTypeUpdate(i, j, k, Blocks.REDSTONE_WIRE);
                if (!ItemBlock.processBlockPlace(world, entityhuman, itemstack, i, j, k, Blocks.REDSTONE_WIRE, 0, clickedX, clickedY, clickedZ)) {
                    return false;
                }
                // CraftBukkit end
            }

            return true;
        }
    }
}
