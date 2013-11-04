package net.minecraft.server;

public class ItemStep extends ItemBlock {

    private final boolean b;
    private final BlockStepAbstract c;
    private final BlockStepAbstract d;

    public ItemStep(Block block, BlockStepAbstract blockstepabstract, BlockStepAbstract blockstepabstract1, boolean flag) {
        super(block);
        this.c = blockstepabstract;
        this.d = blockstepabstract1;
        this.b = flag;
        this.setMaxDurability(0);
        this.a(true);
    }

    public int filterData(int i) {
        return i;
    }

    public String a(ItemStack itemstack) {
        return this.c.b(itemstack.getData());
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (this.b) {
            return super.interactWith(itemstack, entityhuman, world, i, j, k, l, f, f1, f2);
        } else if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            Block block = world.getType(i, j, k);
            int i1 = world.getData(i, j, k);
            int j1 = i1 & 7;
            boolean flag = (i1 & 8) != 0;

            if ((l == 1 && !flag || l == 0 && flag) && block == this.c && j1 == itemstack.getData()) {
                // CraftBukkit start - world.setTypeIdAndData -> processBlockPlace()
                // if (world.b(this.d.a(world, i, j, k)) && world.setTypeAndData(i, j, k, this.d, j1, 3)) {
                if (world.b(this.d.a(world, i, j, k)) && processBlockPlace(world, entityhuman, null, i, j, k, this.d, j1, clickedX, clickedY, clickedZ)) {
                    // world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.d.stepSound.getPlaceSound(), (this.d.stepSound.getVolume1() + 1.0F) / 2.0F, this.d.stepSound.getVolume2() * 0.8F);
                    // CraftBukkit end
                    --itemstack.count;
                }

                return true;
            } else {
                return this.a(itemstack, entityhuman, world, i, j, k, l) ? true : super.interactWith(itemstack, entityhuman, world, i, j, k, l, f, f1, f2);
            }
        }
    }

    private boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
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

        Block block = world.getType(i, j, k);
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 7;

        if (block == this.c && j1 == itemstack.getData()) {
            if (world.b(this.d.a(world, i, j, k)) && world.setTypeAndData(i, j, k, this.d, j1, 3)) {
                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.d.stepSound.getPlaceSound(), (this.d.stepSound.getVolume1() + 1.0F) / 2.0F, this.d.stepSound.getVolume2() * 0.8F);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }
}
