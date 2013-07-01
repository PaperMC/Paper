package net.minecraft.server;

public class ItemStep extends ItemBlock {

    private final boolean a;
    private final BlockStepAbstract b;
    private final BlockStepAbstract c;

    public ItemStep(int i, BlockStepAbstract blockstepabstract, BlockStepAbstract blockstepabstract1, boolean flag) {
        super(i);
        this.b = blockstepabstract;
        this.c = blockstepabstract1;
        this.a = flag;
        this.setMaxDurability(0);
        this.a(true);
    }

    public int filterData(int i) {
        return i;
    }

    public String d(ItemStack itemstack) {
        return this.b.c(itemstack.getData());
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (this.a) {
            return super.interactWith(itemstack, entityhuman, world, i, j, k, l, f, f1, f2);
        } else if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            int i1 = world.getTypeId(i, j, k);
            int j1 = world.getData(i, j, k);
            int k1 = j1 & 7;
            boolean flag = (j1 & 8) != 0;

            if ((l == 1 && !flag || l == 0 && flag) && i1 == this.b.id && k1 == itemstack.getData()) {
                // CraftBukkit start - world.setTypeIdAndData -> processBlockPlace()
                // if (world.b(this.c.b(world, i, j, k)) && world.setTypeIdAndData(i, j, k, this.c.id, k1, 3)) {
                if (world.b(this.c.b(world, i, j, k)) && processBlockPlace(world, entityhuman, null, i, j, k, this.c.id, k1, clickedX, clickedY, clickedZ)) {
                    // world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.c.stepSound.getPlaceSound(), (this.c.stepSound.getVolume1() + 1.0F) / 2.0F, this.c.stepSound.getVolume2() * 0.8F);
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
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
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

        int i1 = world.getTypeId(i, j, k);
        int j1 = world.getData(i, j, k);
        int k1 = j1 & 7;

        if (i1 == this.b.id && k1 == itemstack.getData()) {
            // CraftBukkit start - world.setTypeIdAndData -> processBlockPlace()
            // if (world.b(this.c.b(world, i, j, k)) && world.setTypeIdAndData(i, j, k, this.c.id, k1, 3)) {
            if (world.b(this.c.b(world, i, j, k)) && processBlockPlace(world, entityhuman, null, i, j, k, this.c.id, k1, clickedX, clickedY, clickedZ)) {
                // world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F, this.c.stepSound.getPlaceSound(), (this.c.stepSound.getVolume1() + 1.0F) / 2.0F, this.c.stepSound.getVolume2() * 0.8F);
                // CraftBukkit end
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }
}
