package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemReed extends Item {

    private int id;

    public ItemReed(int i, Block block) {
        super(i);
        this.id = block.id;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.SNOW.id) {
            l = 1;
        } else if (i1 != Block.VINE.id && i1 != Block.LONG_GRASS.id && i1 != Block.DEAD_BUSH.id) {
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
            if (world.mayPlace(this.id, i, j, k, false, l, (Entity) null)) {
                Block block = Block.byId[this.id];

                // CraftBukkit start - This executes the placement of the block
                CraftBlockState replacedBlockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit
                /*
                 * @see net.minecraft.server.World#setTypeId(int i, int j, int k, int l)
                 *
                 * This replaces world.setTypeId(IIII), we're doing this because we need to
                 * hook between the 'placement' and the informing to 'world' so we can
                 * sanely undo this.
                 *
                 * Whenever the call to 'world.setTypeId' changes we need to figure out again what to
                 * replace this with.
                 */
                if (world.setRawTypeId(i, j, k, this.id)) { // <-- world.e does this to place the block
                    org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, replacedBlockState, clickedX, clickedY, clickedZ);

                    if (event.isCancelled() || !event.canBuild()) {
                        // CraftBukkit - undo; this only has reed, repeater and pie blocks
                        world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());

                        return true;
                    }

                    world.update(i, j, k, this.id); // <-- world.setTypeId does this on success (tell the world)
                    // CraftBukkit end

                    if (world.getTypeId(i, j, k) == this.id) {
                        Block.byId[this.id].postPlace(world, i, j, k, l, f, f1, f2);
                        Block.byId[this.id].postPlace(world, i, j, k, entityhuman);
                    }

                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.b(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                    --itemstack.count;
                }
            }

            return true;
        }
    }
}
