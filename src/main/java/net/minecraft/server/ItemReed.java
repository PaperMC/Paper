package net.minecraft.server;

public class ItemReed extends Item {

    private Block block;

    public ItemReed(Block block) {
        this.block = block;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        Block block = world.getType(i, j, k);

        if (block == Blocks.SNOW && (world.getData(i, j, k) & 7) < 1) {
            l = 1;
        } else if (block != Blocks.VINE && block != Blocks.LONG_GRASS && block != Blocks.DEAD_BUSH) {
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
        }

        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else if (itemstack.count == 0) {
            return false;
        } else {
            if (world.mayPlace(this.block, i, j, k, false, l, (Entity) null, itemstack)) {
                int i1 = this.block.getPlacedData(world, i, j, k, l, f, f1, f2, 0);
                // CraftBukkit start - Redirect to common handler
                ItemBlock.processBlockPlace(world, entityhuman, itemstack, i, j, k, this.block, i1, clickedX, clickedY, clickedZ);
                /*
                if (world.setTypeAndData(i, j, k, this.block, i1, 3)) {
                    if (world.getType(i, j, k) == this.block) {
                        this.block.postPlace(world, i, j, k, entityhuman, itemstack);
                        this.block.postPlace(world, i, j, k, i1);
                    }

                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume1() + 1.0F) / 2.0F, this.block.stepSound.getVolume2() * 0.8F);
                    --itemstack.count;
                }
                // CraftBukkit end */
            }

            return true;
        }
    }
}
