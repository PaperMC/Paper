package net.minecraft.server;

public class ItemFireball extends Item {

    public ItemFireball(int i) {
        super(i);
        this.a(CreativeModeTab.f);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
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

            if (!entityhuman.a(i, j, k, l, itemstack)) {
                return false;
            } else {
                int i1 = world.getTypeId(i, j, k);

                if (i1 == 0) {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, i, j, k, org.bukkit.event.block.BlockIgniteEvent.IgniteCause.FIREBALL, entityhuman).isCancelled()) {
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            --itemstack.count;
                        }
                        return false;
                    }
                    // CraftBukkit end

                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "fire.ignite", 1.0F, Item.f.nextFloat() * 0.4F + 0.8F); // CraftBukkit - Fix compilation 
                    world.setTypeIdUpdate(i, j, k, Block.FIRE.id);
                }

                if (!entityhuman.abilities.canInstantlyBuild) {
                    --itemstack.count;
                }

                return true;
            }
        }
    }
}
