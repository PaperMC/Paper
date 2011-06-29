package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemBlock extends Item {

    private int id;

    public ItemBlock(int i) {
        super(i);
        this.id = i + 256;
        this.b(Block.byId[i + 256].a(2));
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
        } else if (j == 127 && Block.byId[this.id].material.isBuildable()) {
            return false;
        } else if (world.a(this.id, i, j, k, false, l)) {
            Block block = Block.byId[this.id];

            // CraftBukkit start - This executes the placement of the block
            CraftBlockState replacedBlockState = CraftBlockState.getBlockState(world, i, j, k);

            // There are like 30 combinations you can mix and match steps and double steps
            // of different materials, so there are a lot of different cases of what
            // would happen if you place x step onto another y step, so let's just keep
            // track of the entire state
            CraftBlockState blockStateBelow = null;
            if ((world.getTypeId(i, j - 1, k) == Block.STEP.id || world.getTypeId(i, j - 1, k) == Block.DOUBLE_STEP.id)
                    && (itemstack.id == Block.DOUBLE_STEP.id || itemstack.id == Block.STEP.id)) {
                blockStateBelow = CraftBlockState.getBlockState(world, i, j - 1, k);
            }

            /**
            * @see net.minecraft.server.World#setTypeIdAndData(int i, int j, int k, int l, int i1)
            *
            * This replaces world.setTypeIdAndData(IIIII), we're doing this because we need to
            * hook between the 'placement' and the informing to 'world' so we can
            * sanely undo this.
            *
            * Whenever the call to 'world.setTypeIdAndData' changes we need to figure out again what to
            * replace this with.
            */
            if (world.setRawTypeIdAndData(i, j, k, this.id, this.filterData(itemstack.getData()))) { // <-- world.setTypeIdAndData does this to place the block
                BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, replacedBlockState, clickedX, clickedY, clickedZ, block);

                if (event.isCancelled() || !event.canBuild()) {
                    if (blockStateBelow != null) { // Used for steps
                        world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());
                        world.setTypeIdAndData(i, j - 1, k, blockStateBelow.getTypeId(), blockStateBelow.getRawData());

                    } else {

                        if (this.id == Block.ICE.id) {
                            // Ice will explode if we set straight to 0
                            world.setTypeId(i, j, k, 20);
                        }

                        world.setTypeIdAndData(i, j, k, replacedBlockState.getTypeId(), replacedBlockState.getRawData());
                    }
                    return true;

                }
                world.update(i, j, k, this.id); // <-- world.setTypeIdAndData does this on success (tell the world)

                // CraftBukkit end

                Block.byId[this.id].postPlace(world, i, j, k, l);
                Block.byId[this.id].postPlace(world, i, j, k, entityhuman);
                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getName(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }

    public String a() {
        return Block.byId[this.id].l();
    }
}
