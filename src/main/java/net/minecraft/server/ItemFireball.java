package net.minecraft.server;

public class ItemFireball extends Item {

    public ItemFireball(int i) {
        super(i);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (world.isStatic) {
            return true;
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

            if (!entityhuman.d(i, j, k)) {
                return false;
            } else {
                int i1 = world.getTypeId(i, j, k);

                if (i1 == 0) {
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "fire.ignite", 1.0F, c.nextFloat() * 0.4F + 0.8F);
                    world.setTypeId(i, j, k, Block.FIRE.id);
                }

                if (!entityhuman.abilities.canInstantlyBuild) {
                    --itemstack.count;
                }

                return true;
            }
        }
    }
}
