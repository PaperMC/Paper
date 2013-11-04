package net.minecraft.server;

public class ItemHoe extends Item {

    protected EnumToolMaterial a;

    public ItemHoe(EnumToolMaterial enumtoolmaterial) {
        this.a = enumtoolmaterial;
        this.maxStackSize = 1;
        this.setMaxDurability(enumtoolmaterial.a());
        this.a(CreativeModeTab.i);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            Block block = world.getType(i, j, k);

            if (l != 0 && world.getType(i, j + 1, k).getMaterial() == Material.AIR && (block == Blocks.GRASS || block == Blocks.DIRT)) {
                Block block1 = Blocks.SOIL;

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block1.stepSound.getStepSound(), (block1.stepSound.getVolume1() + 1.0F) / 2.0F, block1.stepSound.getVolume2() * 0.8F);
                if (world.isStatic) {
                    return true;
                } else {
                    // CraftBukkit start - Hoes - blockface -1 for 'SELF'
                    // world.setTypeUpdate(i, j, k, block1);
                    if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, block1, 0, clickedX, clickedY, clickedZ)) {
                        return false;
                    }
                    // CraftBukkit end
                    itemstack.damage(1, entityhuman);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public String i() {
        return this.a.toString();
    }
}
