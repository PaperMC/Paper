package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.entity.SheepDyeWoolEvent;
// CraftBukkit end

public class ItemDye extends Item {

    public static final String[] a = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final String[] b = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white"};
    public static final int[] c = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye(int i) {
        super(i);
        this.a(true);
        this.setMaxDurability(0);
        this.a(CreativeModeTab.l);
    }

    public String d(ItemStack itemstack) {
        int i = MathHelper.a(itemstack.getData(), 0, 15);

        return super.getName() + "." + a[i];
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else {
            if (itemstack.getData() == 15) {
                if (a(itemstack, world, i, j, k, entityhuman)) { // CraftBukkit - pass entity for StructureGrowEvent
                    if (!world.isStatic) {
                        world.triggerEffect(2005, i, j, k, 0);
                    }

                    return true;
                }
            } else if (itemstack.getData() == 3) {
                int i1 = world.getTypeId(i, j, k);
                int j1 = world.getData(i, j, k);

                if (i1 == Block.LOG.id && BlockLog.f(j1) == 3) {
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
                        int k1 = Block.byId[Block.COCOA.id].getPlacedData(world, i, j, k, l, f, f1, f2, 0);

                        world.setTypeIdAndData(i, j, k, Block.COCOA.id, k1, 2);
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

    // CraftBukkit start
    public static boolean a(ItemStack itemstack, World world, int i, int j, int k) {
        return a(itemstack, world, i, j, k, null);
    }

    public static boolean a(ItemStack itemstack, World world, int i, int j, int k, EntityHuman entityhuman) {
        // CraftBukkit end
        int l = world.getTypeId(i, j, k);

        if (l == Block.SAPLING.id) {
            if (!world.isStatic) {
                if ((double) world.random.nextFloat() < 0.45D) {
                    // CraftBukkit start
                    Player player = (entityhuman instanceof EntityPlayer) ? (Player) entityhuman.getBukkitEntity() : null;
                    ((BlockSapling) Block.SAPLING).grow(world, i, j, k, world.random, true, player, null);
                    // CraftBukkit end
                }

                --itemstack.count;
            }

            return true;
        } else if (l != Block.BROWN_MUSHROOM.id && l != Block.RED_MUSHROOM.id) {
            if (l != Block.MELON_STEM.id && l != Block.PUMPKIN_STEM.id) {
                if (l > 0 && Block.byId[l] instanceof BlockCrops) {
                    if (world.getData(i, j, k) == 7) {
                        return false;
                    } else {
                        if (!world.isStatic) {
                            ((BlockCrops) Block.byId[l]).e_(world, i, j, k);
                            --itemstack.count;
                        }

                        return true;
                    }
                } else {
                    int i1;
                    int j1;
                    int k1;

                    if (l == Block.COCOA.id) {
                        i1 = world.getData(i, j, k);
                        j1 = BlockDirectional.j(i1);
                        k1 = BlockCocoa.c(i1);
                        if (k1 >= 2) {
                            return false;
                        } else {
                            if (!world.isStatic) {
                                ++k1;
                                world.setData(i, j, k, k1 << 2 | j1, 2);
                                --itemstack.count;
                            }

                            return true;
                        }
                    } else if (l != Block.GRASS.id) {
                        return false;
                    } else {
                        if (!world.isStatic) {
                            --itemstack.count;

                            label102:
                            for (i1 = 0; i1 < 128; ++i1) {
                                j1 = i;
                                k1 = j + 1;
                                int l1 = k;

                                for (int i2 = 0; i2 < i1 / 16; ++i2) {
                                    j1 += f.nextInt(3) - 1;
                                    k1 += (f.nextInt(3) - 1) * f.nextInt(3) / 2;
                                    l1 += f.nextInt(3) - 1;
                                    if (world.getTypeId(j1, k1 - 1, l1) != Block.GRASS.id || world.u(j1, k1, l1)) {
                                        continue label102;
                                    }
                                }

                                if (world.getTypeId(j1, k1, l1) == 0) {
                                    if (f.nextInt(10) != 0) {
                                        if (Block.LONG_GRASS.f(world, j1, k1, l1)) {
                                            world.setTypeIdAndData(j1, k1, l1, Block.LONG_GRASS.id, 1, 3);
                                        }
                                    } else if (f.nextInt(3) != 0) {
                                        if (Block.YELLOW_FLOWER.f(world, j1, k1, l1)) {
                                            world.setTypeIdUpdate(j1, k1, l1, Block.YELLOW_FLOWER.id);
                                        }
                                    } else if (Block.RED_ROSE.f(world, j1, k1, l1)) {
                                        world.setTypeIdUpdate(j1, k1, l1, Block.RED_ROSE.id);
                                    }
                                }
                            }
                        }

                        return true;
                    }
                }
            } else if (world.getData(i, j, k) == 7) {
                return false;
            } else {
                if (!world.isStatic) {
                    ((BlockStem) Block.byId[l]).k(world, i, j, k);
                    --itemstack.count;
                }

                return true;
            }
        } else {
            if (!world.isStatic) {
                if ((double) world.random.nextFloat() < 0.4D) {
                    // CraftBukkit start - Validate
                    Player player = (entityhuman instanceof EntityPlayer) ? (Player) entityhuman.getBukkitEntity() : null;
                    ((BlockMushroom) Block.byId[l]).grow(world, i, j, k, world.random, true, player, itemstack);
                }

                //--itemstack.count; - called later if the bonemeal attempt was not cancelled by a plugin
                // CraftBukkit end
            }

            return true;
        }
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            int i = BlockCloth.j_(itemstack.getData());

            if (!entitysheep.isSheared() && entitysheep.getColor() != i) {
                // CraftBukkit start
                byte bColor = (byte) i;
                SheepDyeWoolEvent event = new SheepDyeWoolEvent((org.bukkit.entity.Sheep) entitysheep.getBukkitEntity(), org.bukkit.DyeColor.getByData(bColor));
                entitysheep.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                i = (byte) event.getColor().getWoolData();
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
