package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
// CraftBukkit end

public class ItemBucket extends Item {

    private int a;

    public ItemBucket(int i, int j) {
        super(i);
        this.maxStackSize = 1;
        this.a = j;
        this.a(CreativeModeTab.f);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        boolean flag = this.a == 0;
        MovingObjectPosition movingobjectposition = this.a(world, entityhuman, flag);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            if (movingobjectposition.type == EnumMovingObjectType.TILE) {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;

                if (!world.a(entityhuman, i, j, k)) {
                    return itemstack;
                }

                if (this.a == 0) {
                    if (!entityhuman.a(i, j, k, movingobjectposition.face, itemstack)) {
                        return itemstack;
                    }

                    if (world.getMaterial(i, j, k) == Material.WATER && world.getData(i, j, k) == 0) {
                        // CraftBukkit start
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, i, j, k, -1, itemstack, Item.WATER_BUCKET);

                        if (event.isCancelled()) {
                            return itemstack;
                        }
                        // CraftBukkit end
                        world.setAir(i, j, k);
                        if (entityhuman.abilities.canInstantlyBuild) {
                            return itemstack;
                        }

                        ItemStack result = CraftItemStack.asNMSCopy(event.getItemStack()); // CraftBukkit - TODO: Check this stuff later... Not sure how this behavior should work
                        if (--itemstack.count <= 0) {
                            return result; // CraftBukkit
                        }

                        if (!entityhuman.inventory.pickup(result)) { // CraftBukkit
                            entityhuman.drop(CraftItemStack.asNMSCopy(event.getItemStack())); // CraftBukkit
                        }

                        return itemstack;
                    }

                    if (world.getMaterial(i, j, k) == Material.LAVA && world.getData(i, j, k) == 0) {
                        // CraftBukkit start
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, i, j, k, -1, itemstack, Item.LAVA_BUCKET);

                        if (event.isCancelled()) {
                            return itemstack;
                        }
                        // CraftBukkit end
                        world.setAir(i, j, k);
                        if (entityhuman.abilities.canInstantlyBuild) {
                            return itemstack;
                        }

                        ItemStack result = CraftItemStack.asNMSCopy(event.getItemStack()); // CraftBukkit - TODO: Check this stuff later... Not sure how this behavior should work
                        if (--itemstack.count <= 0) {
                            return result; // CraftBukkit
                        }

                        if (!entityhuman.inventory.pickup(result)) { // CraftBukkit
                            entityhuman.drop(CraftItemStack.asNMSCopy(event.getItemStack())); // CraftBukkit
                        }

                        return itemstack;
                    }
                } else {
                    if (this.a < 0) {
                        // CraftBukkit start
                        PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, i, j, k, movingobjectposition.face, itemstack);

                        if (event.isCancelled()) {
                            return itemstack;
                        }

                        return CraftItemStack.asNMSCopy(event.getItemStack());
                    }

                    int clickedX = i, clickedY = j, clickedZ = k;
                    // CraftBukkit end

                    if (movingobjectposition.face == 0) {
                        --j;
                    }

                    if (movingobjectposition.face == 1) {
                        ++j;
                    }

                    if (movingobjectposition.face == 2) {
                        --k;
                    }

                    if (movingobjectposition.face == 3) {
                        ++k;
                    }

                    if (movingobjectposition.face == 4) {
                        --i;
                    }

                    if (movingobjectposition.face == 5) {
                        ++i;
                    }

                    if (!entityhuman.a(i, j, k, movingobjectposition.face, itemstack)) {
                        return itemstack;
                    }

                    // CraftBukkit start
                    PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, clickedX, clickedY, clickedZ, movingobjectposition.face, itemstack);

                    if (event.isCancelled()) {
                        return itemstack;
                    }
                    // CraftBukkit end

                    if (this.a(world, i, j, k) && !entityhuman.abilities.canInstantlyBuild) {
                        return CraftItemStack.asNMSCopy(event.getItemStack()); // CraftBukkit
                    }
                }
            }

            return itemstack;
        }
    }

    public boolean a(World world, int i, int j, int k) {
        if (this.a <= 0) {
            return false;
        } else {
            Material material = world.getMaterial(i, j, k);
            boolean flag = !material.isBuildable();

            if (!world.isEmpty(i, j, k) && !flag) {
                return false;
            } else {
                if (world.worldProvider.f && this.a == Block.WATER.id) {
                    world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        world.addParticle("largesmoke", (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                } else {
                    if (!world.isStatic && flag && !material.isLiquid()) {
                        world.setAir(i, j, k, true);
                    }

                    world.setTypeIdAndData(i, j, k, this.a, 0, 3);
                }

                return true;
            }
        }
    }
}
