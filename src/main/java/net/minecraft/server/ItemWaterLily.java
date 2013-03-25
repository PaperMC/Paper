package net.minecraft.server;

public class ItemWaterLily extends ItemWithAuxData {

    public ItemWaterLily(int i) {
        super(i, false);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        MovingObjectPosition movingobjectposition = this.a(world, entityhuman, true);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            if (movingobjectposition.type == EnumMovingObjectType.TILE) {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;
                final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

                if (!world.a(entityhuman, i, j, k)) {
                    return itemstack;
                }

                if (!entityhuman.a(i, j, k, movingobjectposition.face, itemstack)) {
                    return itemstack;
                }

                if (world.getMaterial(i, j, k) == Material.WATER && world.getData(i, j, k) == 0 && world.isEmpty(i, j + 1, k)) {
                    // CraftBukkit start
                    // world.setTypeIdUpdate(i, j + 1, k, Block.WATER_LILY.id);
                    if (!processBlockPlace(world, entityhuman, null, i, j + 1, k, Block.WATER_LILY.id, 0, clickedX, clickedY, clickedZ)) {
                        return itemstack;
                    }
                    // CraftBukkit end

                    if (!entityhuman.abilities.canInstantlyBuild) {
                        --itemstack.count;
                    }
                }
            }

            return itemstack;
        }
    }
}
