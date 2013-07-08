package net.minecraft.server;

public class ItemBlock extends Item {

    private int id;

    public ItemBlock(int i) {
        super(i);
        this.id = i + 256;
    }

    public int g() {
        return this.id;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.SNOW.id && (world.getData(i, j, k) & 7) < 1) {
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

        if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else if (j == 255 && Block.byId[this.id].material.isBuildable()) {
            return false;
        } else if (world.mayPlace(this.id, i, j, k, false, l, entityhuman, itemstack)) {
            Block block = Block.byId[this.id];
            int j1 = this.filterData(itemstack.getData());
            int k1 = Block.byId[this.id].getPlacedData(world, i, j, k, l, f, f1, f2, j1);

            // CraftBukkit start - Redirect to common function handler
            /*
            if (world.setTypeIdAndData(i, j, k, this.id, k1, 3)) {
                if (world.getTypeId(i, j, k) == this.id) {
                    Block.byId[this.id].postPlace(world, i, j, k, entityhuman, itemstack);
                    Block.byId[this.id].postPlace(world, i, j, k, k1);
                }

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                --itemstack.count;
            }

            return true;
            */
            return processBlockPlace(world, entityhuman, itemstack, i, j, k, this.id, k1, clickedX, clickedY, clickedZ);
            // CraftBukkit end
        } else {
            return false;
        }
    }

    // CraftBukkit start - Add method to process block placement
    static boolean processBlockPlace(final World world, final EntityHuman entityhuman, final ItemStack itemstack, final int x, final int y, final int z, final int id, final int data, final int clickedX, final int clickedY, final int clickedZ) {
        org.bukkit.block.BlockState blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(world, x, y, z);

        world.callingPlaceEvent = true;
        // Sign is now 3 not 2.
        int flag = (id == Block.SIGN_POST.id || id == Block.WALL_SIGN.id) ? 3 : 2;
        world.setTypeIdAndData(x, y, z, id, data, flag);

        org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockstate, clickedX, clickedY, clickedZ);
        if (event.isCancelled() || !event.canBuild()) {
            blockstate.update(true, false);
            world.callingPlaceEvent = false;
            return false;
        }

        world.callingPlaceEvent = false;

        int newId = world.getTypeId(x, y, z);
        int newData = world.getData(x, y, z);

        Block block = Block.byId[newId];
        if (block != null && !(block instanceof BlockContainer)) { // Containers get placed automatically
            block.onPlace(world, x, y, z);
        }

        world.update(x, y, z, newId);

        // Skulls don't get block data applied to them
        if (block != null && block != Block.SKULL) {
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

    public String d(ItemStack itemstack) {
        return Block.byId[this.id].a();
    }

    public String getName() {
        return Block.byId[this.id].a();
    }
}
