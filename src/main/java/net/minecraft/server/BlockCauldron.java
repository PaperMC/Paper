package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;

public class BlockCauldron extends Block {

    public BlockCauldron(int i) {
        super(i, Material.ORE);
        this.textureId = 154;
    }

    public int a(int i, int j) {
        return i == 1 ? 138 : (i == 0 ? 155 : 154);
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, arraylist);
        float f = 0.125F;

        this.a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, arraylist);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.a(world, i, j, k, axisalignedbb, arraylist);
        this.a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, arraylist);
        this.a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.a(world, i, j, k, axisalignedbb, arraylist);
        this.f();
    }

    public void f() {
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean a() {
        return false;
    }

    public int c() {
        return 24;
    }

    public boolean b() {
        return false;
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            ItemStack itemstack = entityhuman.inventory.getItemInHand();

            if (itemstack == null) {
                return true;
            } else {
                int l = world.getData(i, j, k);

                if (itemstack.id == Item.WATER_BUCKET.id) {
                    if (l < 3) {
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, new ItemStack(Item.BUCKET));
                        }

                        world.setData(i, j, k, 3);
                    }

                    return true;
                } else {
                    if (itemstack.id == Item.GLASS_BOTTLE.id && l > 0) {
                        ItemStack itemstack1 = new ItemStack(Item.POTION, 1, 0);

                        if (!entityhuman.inventory.pickup(itemstack1)) {
                            world.addEntity(new EntityItem(world, (double) i + 0.5D, (double) j + 1.5D, (double) k + 0.5D, itemstack1));
                        } else if (entityhuman instanceof EntityPlayer) { // CraftBukkit
                            ((EntityPlayer) entityhuman).updateInventory(entityhuman.defaultContainer); // CraftBukkit
                        }

                        --itemstack.count;
                        if (itemstack.count <= 0) {
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                        }

                        world.setData(i, j, k, l - 1);
                    }

                    return true;
                }
            }
        }
    }

    public int getDropType(int i, Random random, int j) {
        return Item.CAULDRON.id;
    }
}