package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemReed extends Item {

    private int id;

    public ItemReed(int i, Block block) {
        super(i);
        this.id = block.id;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit

        if (world.getTypeId(i, j, k) == Block.SNOW.id) {
            l = 0;
        } else {
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

        if (itemstack.count == 0) {
            return false;
        } else {
            if (world.a(this.id, i, j, k, false)) {
                Block block = Block.byId[this.id];

                // CraftBukkit start - This executes the placement of the block
                BlockState replacedBlockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit
                /**
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
                    BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, replacedBlockState, clickedX, clickedY, clickedZ, block);

                    if (event.isCancelled() || !event.canBuild()) {
                        // CraftBukkit - undo; this only has reed, repeater and pie blocks
                        world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());
                    } else {
                        world.update(i, j, k, this.id); // <-- world.setTypeId does this on success (tell the world)

                        Block.byId[this.id].postPlace(world, i, j, k, l);
                        Block.byId[this.id].postPlace(world, i, j, k, entityhuman);
                        world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getName(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                        --itemstack.count;
                    }
                    // CraftBukkit end
                }
            }

            return true;
        }
    }
}
