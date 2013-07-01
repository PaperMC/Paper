package net.minecraft.server;

public class ItemHoe extends Item {

    protected EnumToolMaterial a;

    public ItemHoe(int i, EnumToolMaterial enumtoolmaterial) {
        super(i);
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
            int i1 = world.getTypeId(i, j, k);
            int j1 = world.getTypeId(i, j + 1, k);

            if (l != 0 && j1 == 0 && (i1 == Block.GRASS.id || i1 == Block.DIRT.id)) {
                Block block = Block.SOIL;

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getStepSound(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                if (world.isStatic) {
                    return true;
                } else {
                    // CraftBukkit start - Hoes - blockface -1 for 'SELF'
                    // world.setTypeIdUpdate(i, j, k, block.id);
                    if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, block.id, 0, clickedX, clickedY, clickedZ)) {
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

    public String g() {
        return this.a.toString();
    }
}
