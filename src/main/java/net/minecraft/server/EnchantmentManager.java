package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnchantmentManager {

    private static final Random random = new Random();
    private static final EnchantmentModifierProtection b = new EnchantmentModifierProtection((EmptyClass3) null);
    private static final EnchantmentModifierDamage c = new EnchantmentModifierDamage((EmptyClass3) null);

    public EnchantmentManager() {}

    public static int getEnchantmentLevel(int i, ItemStack itemstack) { // CraftBukkit - priv to pub
        if (itemstack == null) {
            return 0;
        } else {
            NBTTagList nbttaglist = itemstack.getEnchantments();

            if (nbttaglist == null) {
                return 0;
            } else {
                for (int j = 0; j < nbttaglist.size(); ++j) {
                    short short1 = ((NBTTagCompound) nbttaglist.get(j)).getShort("id");
                    short short2 = ((NBTTagCompound) nbttaglist.get(j)).getShort("lvl");

                    if (short1 == i) {
                        return short2;
                    }
                }

                return 0;
            }
        }
    }

    private static int getEnchantmentLevel(int i, ItemStack[] aitemstack) {
        int j = 0;
        ItemStack[] aitemstack1 = aitemstack;
        int k = aitemstack.length;

        for (int l = 0; l < k; ++l) {
            ItemStack itemstack = aitemstack1[l];
            int i1 = getEnchantmentLevel(i, itemstack);

            if (i1 > j) {
                j = i1;
            }
        }

        return j;
    }

    private static void a(EnchantmentModifier enchantmentmodifier, ItemStack itemstack) {
        if (itemstack != null) {
            NBTTagList nbttaglist = itemstack.getEnchantments();

            if (nbttaglist != null) {
                for (int i = 0; i < nbttaglist.size(); ++i) {
                    short short1 = ((NBTTagCompound) nbttaglist.get(i)).getShort("id");
                    short short2 = ((NBTTagCompound) nbttaglist.get(i)).getShort("lvl");

                    if (Enchantment.byId[short1] != null) {
                        enchantmentmodifier.a(Enchantment.byId[short1], short2);
                    }
                }
            }
        }
    }

    private static void a(EnchantmentModifier enchantmentmodifier, ItemStack[] aitemstack) {
        ItemStack[] aitemstack1 = aitemstack;
        int i = aitemstack.length;

        for (int j = 0; j < i; ++j) {
            ItemStack itemstack = aitemstack1[j];

            a(enchantmentmodifier, itemstack);
        }
    }

    public static int a(PlayerInventory playerinventory, DamageSource damagesource) {
        b.a = 0;
        b.b = damagesource;
        a((EnchantmentModifier) b, playerinventory.armor);
        if (b.a > 25) {
            b.a = 25;
        }

        return (b.a + 1 >> 1) + random.nextInt((b.a >> 1) + 1);
    }

    public static int a(PlayerInventory playerinventory, EntityLiving entityliving) {
        c.a = 0;
        c.b = entityliving;
        a((EnchantmentModifier) c, playerinventory.getItemInHand());
        return c.a > 0 ? 1 + random.nextInt(c.a) : 0;
    }

    public static int getKnockbackEnchantmentLevel(PlayerInventory playerinventory, EntityLiving entityliving) {
        return getEnchantmentLevel(Enchantment.KNOCKBACK.id, playerinventory.getItemInHand());
    }

    public static int getFireAspectEnchantmentLevel(PlayerInventory playerinventory, EntityLiving entityliving) {
        return getEnchantmentLevel(Enchantment.FIRE_ASPECT.id, playerinventory.getItemInHand());
    }

    public static int getOxygenEnchantmentLevel(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.OXYGEN.id, playerinventory.armor);
    }

    public static int getDigSpeedEnchantmentLevel(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.DIG_SPEED.id, playerinventory.getItemInHand());
    }

    public static int getDurabilityEnchantmentLevel(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.DURABILITY.id, playerinventory.getItemInHand());
    }

    public static boolean hasSilkTouchEnchantment(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.SILK_TOUCH.id, playerinventory.getItemInHand()) > 0;
    }

    public static int getBonusBlockLootEnchantmentLevel(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS.id, playerinventory.getItemInHand());
    }

    public static int getBonusMonsterLootEnchantmentLevel(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS.id, playerinventory.getItemInHand());
    }

    public static boolean hasWaterWorkerEnchantment(PlayerInventory playerinventory) {
        return getEnchantmentLevel(Enchantment.WATER_WORKER.id, playerinventory.armor) > 0;
    }

    public static int a(Random random, int i, int j, ItemStack itemstack) {
        Item item = itemstack.getItem();
        int k = item.c();

        if (k <= 0) {
            return 0;
        } else {
            if (j > 30) {
                j = 30;
            }

            j = 1 + random.nextInt((j >> 1) + 1) + random.nextInt(j + 1);
            int l = random.nextInt(5) + j;

            return i == 0 ? (l >> 1) + 1 : (i == 1 ? l * 2 / 3 + 1 : l);
        }
    }

    public static List a(Random random, ItemStack itemstack, int i) {
        Item item = itemstack.getItem();
        int j = item.c();

        if (j <= 0) {
            return null;
        } else {
            j = 1 + random.nextInt((j >> 1) + 1) + random.nextInt((j >> 1) + 1);
            int k = j + i;
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.25F;
            int l = (int) ((float) k * (1.0F + f) + 0.5F);
            ArrayList arraylist = null;
            Map map = a(l, itemstack);

            if (map != null && !map.isEmpty()) {
                EnchantmentInstance enchantmentinstance = (EnchantmentInstance) WeightedRandom.a(random, map.values());

                if (enchantmentinstance != null) {
                    arraylist = new ArrayList();
                    arraylist.add(enchantmentinstance);

                    for (int i1 = l >> 1; random.nextInt(50) <= i1; i1 >>= 1) {
                        Iterator iterator = map.keySet().iterator();

                        while (iterator.hasNext()) {
                            Integer integer = (Integer) iterator.next();
                            boolean flag = true;
                            Iterator iterator1 = arraylist.iterator();

                            while (true) {
                                if (iterator1.hasNext()) {
                                    EnchantmentInstance enchantmentinstance1 = (EnchantmentInstance) iterator1.next();

                                    if (enchantmentinstance1.a.a(Enchantment.byId[integer.intValue()])) {
                                        continue;
                                    }

                                    flag = false;
                                }

                                if (!flag) {
                                    iterator.remove();
                                }
                                break;
                            }
                        }

                        if (!map.isEmpty()) {
                            EnchantmentInstance enchantmentinstance2 = (EnchantmentInstance) WeightedRandom.a(random, map.values());

                            arraylist.add(enchantmentinstance2);
                        }
                    }
                }
            }

            return arraylist;
        }
    }

    public static Map a(int i, ItemStack itemstack) {
        Item item = itemstack.getItem();
        HashMap hashmap = null;
        Enchantment[] aenchantment = Enchantment.byId;
        int j = aenchantment.length;

        for (int k = 0; k < j; ++k) {
            Enchantment enchantment = aenchantment[k];

            if (enchantment != null && enchantment.slot.canEnchant(item)) {
                for (int l = enchantment.getStartLevel(); l <= enchantment.getMaxLevel(); ++l) {
                    if (i >= enchantment.a(l) && i <= enchantment.b(l)) {
                        if (hashmap == null) {
                            hashmap = new HashMap();
                        }

                        hashmap.put(Integer.valueOf(enchantment.id), new EnchantmentInstance(enchantment, l));
                    }
                }
            }
        }

        return hashmap;
    }
}
