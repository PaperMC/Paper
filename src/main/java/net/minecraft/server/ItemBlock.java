package net.minecraft.server;

public class ItemBlock extends Item {

    protected final Block block;

    public ItemBlock(Block block) {
        this.block = block;
    }

    public ItemBlock b(String s) {
        super.c(s);
        return this;
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

        if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else if (j == 255 && this.block.getMaterial().isBuildable()) {
            return false;
        } else if (world.mayPlace(this.block, i, j, k, false, l, entityhuman, itemstack)) {
            int i1 = this.filterData(itemstack.getData());
            int j1 = this.block.getPlacedData(world, i, j, k, l, f, f1, f2, i1);

            // CraftBukkit start - Redirect to common function handler
            /*
            if (world.setTypeAndData(i, j, k, this.block, j1, 3)) {
                if (world.getType(i, j, k) == this.block) {
                    this.block.postPlace(world, i, j, k, entityhuman, itemstack);
                    this.block.postPlace(world, i, j, k, j1);
                }

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume1() + 1.0F) / 2.0F, this.block.stepSound.getVolume2() * 0.8F);
                --itemstack.count;
            }

            return true;
            */
            return processBlockPlace(world, entityhuman, itemstack, i, j, k, this.block, j1, clickedX, clickedY, clickedZ);
            // CraftBukkit end
        } else {
            return false;
        }
    }

    // CraftBukkit start - Add method to process block placement
    static boolean processBlockPlace(final World world, final EntityHuman entityhuman, final ItemStack itemstack, final int x, final int y, final int z, final Block id, final int data, final int clickedX, final int clickedY, final int clickedZ) {
        org.bukkit.block.BlockState blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(world, x, y, z);

        world.callingPlaceEvent = true;
        // Sign is now 3 not 2.
        int flag = (id == Blocks.SIGN_POST || id == Blocks.WALL_SIGN) ? 3 : 2;
        world.setTypeAndData(x, y, z, id, data, flag);

        org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockstate, clickedX, clickedY, clickedZ);
        if (event.isCancelled() || !event.canBuild()) {
            blockstate.update(true, false);
            world.callingPlaceEvent = false;
            return false;
        }

        world.callingPlaceEvent = false;

        Block block = world.getType(x, y, z);
        int newData = world.getData(x, y, z);

        if (block != null && !(block instanceof BlockContainer)) { // Containers get placed automatically
            block.onPlace(world, x, y, z);
        }

        world.update(x, y, z, block);

        // Skulls don't get block data applied to them
        if (block != null && block != Blocks.SKULL) {
            block.postPlace(world, x, y, z, entityhuman, itemstack);
            block.postPlace(world, x, y, z, newData);

            world.makeSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
        }

        if (itemstack != null) {
            --itemstack.count;
        }

        return true;
    }
    // CraftBukkit end

    public String a(ItemStack itemstack) {
        return this.block.a();
    }

    public String getName() {
        return this.block.a();
    }

    public Item c(String s) {
        return this.b(s);
    }
}
