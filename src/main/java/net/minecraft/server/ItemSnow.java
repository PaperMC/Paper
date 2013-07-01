package net.minecraft.server;

public class ItemSnow extends ItemBlockWithAuxData {

    public ItemSnow(int i, Block block) {
        super(i, block);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            int i1 = world.getTypeId(i, j, k);

            if (i1 == Block.SNOW.id) {
                Block block = Block.byId[this.g()];
                int j1 = world.getData(i, j, k);
                int k1 = j1 & 7;

                // CraftBukkit start - Redirect to common handler
                if (k1 <= 6 && world.b(block.b(world, i, j, k)) && ItemBlock.processBlockPlace(world, entityhuman, itemstack, i, j, k, Block.SNOW.id, k1 + 1 | j1 & -8, clickedX, clickedY, clickedZ)) {
                    return true;
                }
                /*
                if (k1 <= 6 && world.b(block.b(world, i, j, k)) && world.setData(i, j, k, k1 + 1 | j1 & -8, 2)) {
                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                    --itemstack.count;
                    return true;
                }
                */
                // CraftBukkit end
            }

            return super.interactWith(itemstack, entityhuman, world, i, j, k, l, f, f1, f2);
        }
    }
}
