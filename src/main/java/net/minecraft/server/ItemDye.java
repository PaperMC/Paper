package net.minecraft.server;

import org.bukkit.entity.Player; // CraftBukkit

public class ItemDye extends Item {

    public static final String[] a = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final int[] b = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye(int i) {
        super(i);
        this.a(true);
        this.setMaxDurability(0);
    }

    public String a(ItemStack itemstack) {
        int i = MathHelper.a(itemstack.getData(), 0, 15);

        return super.getName() + "." + a[i];
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (!entityhuman.d(i, j, k)) {
            return false;
        } else {
            if (itemstack.getData() == 15) {
                int i1 = world.getTypeId(i, j, k);

                if (i1 == Block.SAPLING.id) {
                    if (!world.isStatic) {
                        // CraftBukkit start
                        Player player = (entityhuman instanceof EntityPlayer) ? (Player) entityhuman.getBukkitEntity() : null;
                        ((BlockSapling) Block.SAPLING).grow(world, i, j, k, world.random, true, player, itemstack);
                        //--itemstack.count; - called later if the bonemeal attempt was succesful
                        // CraftBukkit end
                    }

                    return true;
                }

                if (i1 == Block.BROWN_MUSHROOM.id || i1 == Block.RED_MUSHROOM.id) {
                    // CraftBukkit start
                    if (!world.isStatic) {
                        Player player = (entityhuman instanceof EntityPlayer) ? (Player) entityhuman.getBukkitEntity() : null;
                        ((BlockMushroom) Block.byId[i1]).grow(world, i, j, k, world.random, true, player, itemstack);
                        //--itemstack.count; - called later if the bonemeal attempt was succesful
                        // CraftBukkit end
                    }

                    return true;
                }

                if (i1 == Block.MELON_STEM.id || i1 == Block.PUMPKIN_STEM.id) {
                    if (!world.isStatic) {
                        ((BlockStem) Block.byId[i1]).g(world, i, j, k);
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.CROPS.id) {
                    if (!world.isStatic) {
                        ((BlockCrops) Block.CROPS).g(world, i, j, k);
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.GRASS.id) {
                    if (!world.isStatic) {
                        --itemstack.count;

                        label73:
                        for (int j1 = 0; j1 < 128; ++j1) {
                            int k1 = i;
                            int l1 = j + 1;
                            int i2 = k;

                            for (int j2 = 0; j2 < j1 / 16; ++j2) {
                                k1 += c.nextInt(3) - 1;
                                l1 += (c.nextInt(3) - 1) * c.nextInt(3) / 2;
                                i2 += c.nextInt(3) - 1;
                                if (world.getTypeId(k1, l1 - 1, i2) != Block.GRASS.id || world.e(k1, l1, i2)) {
                                    continue label73;
                                }
                            }

                            if (world.getTypeId(k1, l1, i2) == 0) {
                                if (c.nextInt(10) != 0) {
                                    world.setTypeIdAndData(k1, l1, i2, Block.LONG_GRASS.id, 1);
                                } else if (c.nextInt(3) != 0) {
                                    world.setTypeId(k1, l1, i2, Block.YELLOW_FLOWER.id);
                                } else {
                                    world.setTypeId(k1, l1, i2, Block.RED_ROSE.id);
                                }
                            }
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public void a(ItemStack itemstack, EntityLiving entityliving) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            int i = BlockCloth.d(itemstack.getData());

            if (!entitysheep.isSheared() && entitysheep.getColor() != i) {
                entitysheep.setColor(i);
                --itemstack.count;
            }
        }
    }
}
