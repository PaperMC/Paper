package net.minecraft.server;

public class ItemStep extends ItemBlock {

    public ItemStep(int i) {
        super(i);
        this.setMaxDurability(0);
        this.a(true);
    }

    public int filterData(int i) {
        return i;
    }

    public String a(ItemStack itemstack) {
        int i = itemstack.getData();

        if (i < 0 || i >= BlockStep.a.length) {
            i = 0;
        }

        return super.getName() + "." + BlockStep.a[i];
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (l != 1) {
            ;
        }

        if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.d(i, j, k)) {
            return false;
        } else {
            int i1 = world.getTypeId(i, j, k);
            int j1 = world.getData(i, j, k);

            if (l == 1 && i1 == Block.STEP.id && j1 == itemstack.getData()) {
                // CraftBukkit start - handle this in super
                /*
                if (world.setTypeIdAndData(i, j, k, Block.DOUBLE_STEP.id, j1)) {
                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), Block.DOUBLE_STEP.stepSound.getName(), (Block.DOUBLE_STEP.stepSound.getVolume1() + 1.0F) / 2.0F, Block.DOUBLE_STEP.stepSound.getVolume2() * 0.8F);
                    --itemstack.count;
                }
                */
                return super.a(itemstack, entityhuman, world, i, j, k, -1);
                // CraftBukkit end
            } else {
                return super.a(itemstack, entityhuman, world, i, j, k, l);
            }
        }
    }
}
