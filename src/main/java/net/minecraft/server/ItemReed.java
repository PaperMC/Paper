package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemReed extends Item {

    private int a;

    public ItemReed(int i, Block block) {
        super(i);
        this.a = block.id;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit;

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
            if (world.a(this.a, i, j, k, false)) {
                Block block = Block.byId[this.a];

                // CraftBukkit start - This executes the placement of the block
                BlockState replacedBlockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit
                /**
                 * @see net.minecraft.server.World#e(int i, int j, int k, int l)
                 * 
                 * This replaces world.e(IIII), we're doing this because we need to
                 * hook between the 'placement' and the informing to 'world' so we can
                 * sanely undo this.
                 *
                 * Whenever the call to 'world.e' changes we need to figure out again what to
                 * replace this with.
                 */
                if (world.setTypeId(i, j, k, this.a)) { // <-- world.e does this to place the block
                    BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, replacedBlockState, clickedX, clickedY, clickedZ, block);

                    if (event.isCancelled() || !event.canBuild()) {
                        // CraftBukkit Undo -- this only has reed, repeater and pie blocks
                        world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());
                    } else {
                        world.f(i, j, k, a); // <-- world.e does this on success (tell the world)

                        Block.byId[this.a].d(world, i, j, k, l);
                        Block.byId[this.a].a(world, i, j, k, (EntityLiving) entityhuman);
                        world.a((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.c(), (block.stepSound.a() + 1.0F) / 2.0F, block.stepSound.b() * 0.8F);
                        --itemstack.count;
                    }
                    // CraftBukkit end
                }
            }

            return true;
        }
    }
}
