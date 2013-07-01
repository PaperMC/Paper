package net.minecraft.server;

public class ItemSeeds extends Item {

    private int id;
    private int b;

    public ItemSeeds(int i, int j, int k) {
        super(i);
        this.id = j;
        this.b = k;
        this.a(CreativeModeTab.l);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (l != 1) {
            return false;
        } else if (entityhuman.a(i, j, k, l, itemstack) && entityhuman.a(i, j + 1, k, l, itemstack)) {
            int i1 = world.getTypeId(i, j, k);

            if (i1 == this.b && world.isEmpty(i, j + 1, k)) {
                // CraftBukkit start - Seeds
                // world.setTypeIdUpdate(i, j + 1, k, this.id);
                if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j + 1, k, this.id, 0, clickedX, clickedY, clickedZ)) {
                    return false;
                }
                // CraftBukkit end

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
