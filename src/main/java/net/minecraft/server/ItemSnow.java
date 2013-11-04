package net.minecraft.server;

public class ItemSnow extends ItemBlockWithAuxData {

    public ItemSnow(Block block, Block block1) {
        super(block, block1);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            Block block = world.getType(i, j, k);

            if (block == Blocks.SNOW) {
                int i1 = world.getData(i, j, k);
                int j1 = i1 & 7;

                // CraftBukkit start - Redirect to common handler
                if (j1 <= 6 && world.b(this.block.a(world, i, j, k)) && ItemBlock.processBlockPlace(world, entityhuman, itemstack, i, j, k, block, j1 + 1 | i1 & -8, clickedX, clickedY, clickedZ)) {
                    return true;
                }
                /*
                if (j1 <= 6 && world.b(this.block.a(world, i, j, k)) && world.setData(i, j, k, j1 + 1 | i1 & -8, 2)) {
                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume1() + 1.0F) / 2.0F, this.block.stepSound.getVolume2() * 0.8F);
                    --itemstack.count;
                    return true;
                }
                // CraftBukkit end */
            }

            return super.interactWith(itemstack, entityhuman, world, i, j, k, l, f, f1, f2);
        }
    }
}
