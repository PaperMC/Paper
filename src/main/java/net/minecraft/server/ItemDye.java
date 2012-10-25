package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.entity.SheepDyeWoolEvent;
// CraftBukkit end

public class ItemDye extends Item {

    public static final String[] a = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final int[] b = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye(int i) {
        super(i);
        this.a(true);
        this.setMaxDurability(0);
        this.a(CreativeModeTab.l);
    }

    public String c_(ItemStack itemstack) {
        int i = MathHelper.a(itemstack.getData(), 0, 15);

        return super.getName() + "." + a[i];
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            int i1;
            int j1;

            if (itemstack.getData() == 15) {
                i1 = world.getTypeId(i, j, k);
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
                    if (world.getData(i, j, k) == 7) {
                        return false;
                    }

                    if (!world.isStatic) {
                        ((BlockStem) Block.byId[i1]).l(world, i, j, k);
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 > 0 && Block.byId[i1] instanceof BlockCrops) {
                    if (world.getData(i, j, k) == 7) {
                        return false;
                    }

                    if (!world.isStatic) {
                        ((BlockCrops) Block.byId[i1]).c_(world, i, j, k);
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.COCOA.id) {
                    if (!world.isStatic) {
                        world.setData(i, j, k, 8 | BlockDirectional.e(world.getData(i, j, k)));
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.GRASS.id) {
                    if (!world.isStatic) {
                        --itemstack.count;

                        label137:
                        for (j1 = 0; j1 < 128; ++j1) {
                            int k1 = i;
                            int l1 = j + 1;
                            int i2 = k;

                            for (int j2 = 0; j2 < j1 / 16; ++j2) {
                                k1 += d.nextInt(3) - 1;
                                l1 += (d.nextInt(3) - 1) * d.nextInt(3) / 2;
                                i2 += d.nextInt(3) - 1;
                                if (world.getTypeId(k1, l1 - 1, i2) != Block.GRASS.id || world.s(k1, l1, i2)) {
                                    continue label137;
                                }
                            }

                            if (world.getTypeId(k1, l1, i2) == 0) {
                                if (d.nextInt(10) != 0) {
                                    if (Block.LONG_GRASS.d(world, k1, l1, i2)) {
                                        world.setTypeIdAndData(k1, l1, i2, Block.LONG_GRASS.id, 1);
                                    }
                                } else if (d.nextInt(3) != 0) {
                                    if (Block.YELLOW_FLOWER.d(world, k1, l1, i2)) {
                                        world.setTypeId(k1, l1, i2, Block.YELLOW_FLOWER.id);
                                    }
                                } else if (Block.RED_ROSE.d(world, k1, l1, i2)) {
                                    world.setTypeId(k1, l1, i2, Block.RED_ROSE.id);
                                }
                            }
                        }
                    }

                    return true;
                }
            } else if (itemstack.getData() == 3) {
                i1 = world.getTypeId(i, j, k);
                j1 = world.getData(i, j, k);
                if (i1 == Block.LOG.id && BlockLog.e(j1) == 3) {
                    if (l == 0) {
                        return false;
                    }

                    if (l == 1) {
                        return false;
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

                    if (world.isEmpty(i, j, k)) {
                        world.setTypeId(i, j, k, Block.COCOA.id);
                        if (world.getTypeId(i, j, k) == Block.COCOA.id) {
                            Block.byId[Block.COCOA.id].postPlace(world, i, j, k, l, f, f1, f2);
                        }

                        if (!entityhuman.abilities.canInstantlyBuild) {
                            --itemstack.count;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public boolean a(ItemStack itemstack, EntityLiving entityliving) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            int i = BlockCloth.e_(itemstack.getData());

            if (!entitysheep.isSheared() && entitysheep.getColor() != i) {
                // CraftBukkit start
                byte bColor = (byte) i;
                SheepDyeWoolEvent event = new SheepDyeWoolEvent((org.bukkit.entity.Sheep) entitysheep.getBukkitEntity(), org.bukkit.DyeColor.getByData(bColor));
                entitysheep.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                i = (byte) event.getColor().getData();
                // CraftBukkit end

                entitysheep.setColor(i);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }
}
