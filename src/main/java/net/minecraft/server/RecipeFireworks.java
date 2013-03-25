package net.minecraft.server;

import java.util.ArrayList;

public class RecipeFireworks extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    private ItemStack a;

    // CraftBukkit start - Delegate to new parent class with bogus info
    public RecipeFireworks() {
        super(new ItemStack(Item.FIREWORKS, 0, 0), java.util.Arrays.asList(new ItemStack(Item.SULPHUR, 0, 5)));
    }
    // CraftBukkit end

    public boolean a(InventoryCrafting inventorycrafting, World world) {
        this.a = null;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = 0; k1 < inventorycrafting.getSize(); ++k1) {
            ItemStack itemstack = inventorycrafting.getItem(k1);

            if (itemstack != null) {
                if (itemstack.id == Item.SULPHUR.id) {
                    ++j;
                } else if (itemstack.id == Item.FIREWORKS_CHARGE.id) {
                    ++l;
                } else if (itemstack.id == Item.INK_SACK.id) {
                    ++k;
                } else if (itemstack.id == Item.PAPER.id) {
                    ++i;
                } else if (itemstack.id == Item.GLOWSTONE_DUST.id) {
                    ++i1;
                } else if (itemstack.id == Item.DIAMOND.id) {
                    ++i1;
                } else if (itemstack.id == Item.FIREBALL.id) {
                    ++j1;
                } else if (itemstack.id == Item.FEATHER.id) {
                    ++j1;
                } else if (itemstack.id == Item.GOLD_NUGGET.id) {
                    ++j1;
                } else {
                    if (itemstack.id != Item.SKULL.id) {
                        return false;
                    }

                    ++j1;
                }
            }
        }

        i1 += k + j1;
        if (j <= 3 && i <= 1) {
            NBTTagCompound nbttagcompound;
            NBTTagCompound nbttagcompound1;

            if (j >= 1 && i == 1 && i1 == 0) {
                this.a = new ItemStack(Item.FIREWORKS);
                if (l > 0) {
                    nbttagcompound = new NBTTagCompound();
                    nbttagcompound1 = new NBTTagCompound("Fireworks");
                    NBTTagList nbttaglist = new NBTTagList("Explosions");

                    for (int l1 = 0; l1 < inventorycrafting.getSize(); ++l1) {
                        ItemStack itemstack1 = inventorycrafting.getItem(l1);

                        if (itemstack1 != null && itemstack1.id == Item.FIREWORKS_CHARGE.id && itemstack1.hasTag() && itemstack1.getTag().hasKey("Explosion")) {
                            nbttaglist.add(itemstack1.getTag().getCompound("Explosion"));
                        }
                    }

                    nbttagcompound1.set("Explosions", nbttaglist);
                    nbttagcompound1.setByte("Flight", (byte) j);
                    nbttagcompound.set("Fireworks", nbttagcompound1);
                    this.a.setTag(nbttagcompound);
                }

                return true;
            } else if (j == 1 && i == 0 && l == 0 && k > 0 && j1 <= 1) {
                this.a = new ItemStack(Item.FIREWORKS_CHARGE);
                nbttagcompound = new NBTTagCompound();
                nbttagcompound1 = new NBTTagCompound("Explosion");
                byte b0 = 0;
                ArrayList arraylist = new ArrayList();

                for (int i2 = 0; i2 < inventorycrafting.getSize(); ++i2) {
                    ItemStack itemstack2 = inventorycrafting.getItem(i2);

                    if (itemstack2 != null) {
                        if (itemstack2.id == Item.INK_SACK.id) {
                            arraylist.add(Integer.valueOf(ItemDye.c[itemstack2.getData()]));
                        } else if (itemstack2.id == Item.GLOWSTONE_DUST.id) {
                            nbttagcompound1.setBoolean("Flicker", true);
                        } else if (itemstack2.id == Item.DIAMOND.id) {
                            nbttagcompound1.setBoolean("Trail", true);
                        } else if (itemstack2.id == Item.FIREBALL.id) {
                            b0 = 1;
                        } else if (itemstack2.id == Item.FEATHER.id) {
                            b0 = 4;
                        } else if (itemstack2.id == Item.GOLD_NUGGET.id) {
                            b0 = 2;
                        } else if (itemstack2.id == Item.SKULL.id) {
                            b0 = 3;
                        }
                    }
                }

                int[] aint = new int[arraylist.size()];

                for (int j2 = 0; j2 < aint.length; ++j2) {
                    aint[j2] = ((Integer) arraylist.get(j2)).intValue();
                }

                nbttagcompound1.setIntArray("Colors", aint);
                nbttagcompound1.setByte("Type", b0);
                nbttagcompound.set("Explosion", nbttagcompound1);
                this.a.setTag(nbttagcompound);
                return true;
            } else if (j == 0 && i == 0 && l == 1 && k > 0 && k == i1) {
                ArrayList arraylist1 = new ArrayList();

                for (int k2 = 0; k2 < inventorycrafting.getSize(); ++k2) {
                    ItemStack itemstack3 = inventorycrafting.getItem(k2);

                    if (itemstack3 != null) {
                        if (itemstack3.id == Item.INK_SACK.id) {
                            arraylist1.add(Integer.valueOf(ItemDye.c[itemstack3.getData()]));
                        } else if (itemstack3.id == Item.FIREWORKS_CHARGE.id) {
                            this.a = itemstack3.cloneItemStack();
                            this.a.count = 1;
                        }
                    }
                }

                int[] aint1 = new int[arraylist1.size()];

                for (int l2 = 0; l2 < aint1.length; ++l2) {
                    aint1[l2] = ((Integer) arraylist1.get(l2)).intValue();
                }

                if (this.a != null && this.a.hasTag()) {
                    NBTTagCompound nbttagcompound2 = this.a.getTag().getCompound("Explosion");

                    if (nbttagcompound2 == null) {
                        return false;
                    } else {
                        nbttagcompound2.setIntArray("FadeColors", aint1);
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public ItemStack a(InventoryCrafting inventorycrafting) {
        return this.a.cloneItemStack();
    }

    public int a() {
        return 10;
    }

    public ItemStack b() {
        return this.a;
    }
}
