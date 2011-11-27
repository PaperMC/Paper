package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnchantmentManager {

    private static final Random random = new Random();
    private static final EnchantmentModifier1 b = new EnchantmentModifier1((EmptyClass3) null);
    private static final EnchantmentModifier2 c = new EnchantmentModifier2((EmptyClass3) null);

    public EnchantmentManager() {}

    public static int b(int i, ItemStack itemstack) { // CraftBukkit - priv to pub
        if (itemstack == null) {
            return 0;
        } else {
            NBTTagList nbttaglist = itemstack.p();

            if (nbttaglist == null) {
                return 0;
            } else {
                for (int j = 0; j < nbttaglist.d(); ++j) {
                    short short1 = ((NBTTagCompound) nbttaglist.a(j)).e("id");
                    short short2 = ((NBTTagCompound) nbttaglist.a(j)).e("lvl");

                    if (short1 == i) {
                        return short2;
                    }
                }

                return 0;
            }
        }
    }

    private static int a(int i, ItemStack[] aitemstack) {
        int j = 0;
        ItemStack[] aitemstack1 = aitemstack;
        int k = aitemstack.length;

        for (int l = 0; l < k; ++l) {
            ItemStack itemstack = aitemstack1[l];
            int i1 = b(i, itemstack);

            if (i1 > j) {
                j = i1;
            }
        }

        return j;
    }

    private static void a(EnchantmentModifier enchantmentmodifier, ItemStack itemstack) {
        if (itemstack != null) {
            NBTTagList nbttaglist = itemstack.p();

            if (nbttaglist != null) {
                for (int i = 0; i < nbttaglist.d(); ++i) {
                    short short1 = ((NBTTagCompound) nbttaglist.a(i)).e("id");
                    short short2 = ((NBTTagCompound) nbttaglist.a(i)).e("lvl");

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

    public static int a(InventoryPlayer inventoryplayer, DamageSource damagesource) {
        b.a = 0;
        b.b = damagesource;
        a((EnchantmentModifier) b, inventoryplayer.armor);
        if (b.a > 25) {
            b.a = 25;
        }

        return (b.a + 1 >> 1) + random.nextInt((b.a >> 1) + 1);
    }

    public static int a(InventoryPlayer inventoryplayer, EntityLiving entityliving) {
        c.a = 0;
        c.b = entityliving;
        a((EnchantmentModifier) c, inventoryplayer.getItemInHand());
        return c.a > 0 ? 1 + random.nextInt(c.a) : 0;
    }

    public static int b(InventoryPlayer inventoryplayer, EntityLiving entityliving) {
        return b(Enchantment.KNOCKBACK.id, inventoryplayer.getItemInHand());
    }

    public static int c(InventoryPlayer inventoryplayer, EntityLiving entityliving) {
        return b(Enchantment.FIRE_ASPECT.id, inventoryplayer.getItemInHand());
    }

    public static int a(InventoryPlayer inventoryplayer) {
        return a(Enchantment.OXYGEN.id, inventoryplayer.armor);
    }

    public static int b(InventoryPlayer inventoryplayer) {
        return b(Enchantment.DIG_SPEED.id, inventoryplayer.getItemInHand());
    }

    public static int c(InventoryPlayer inventoryplayer) {
        return b(Enchantment.DURABILITY.id, inventoryplayer.getItemInHand());
    }

    public static boolean d(InventoryPlayer inventoryplayer) {
        return b(Enchantment.SILK_TOUCH.id, inventoryplayer.getItemInHand()) > 0;
    }

    public static int e(InventoryPlayer inventoryplayer) {
        return b(Enchantment.LOOT_BONUS_BLOCKS.id, inventoryplayer.getItemInHand());
    }

    public static int f(InventoryPlayer inventoryplayer) {
        return b(Enchantment.LOOT_BONUS_MOBS.id, inventoryplayer.getItemInHand());
    }

    public static boolean g(InventoryPlayer inventoryplayer) {
        return a(Enchantment.WATER_WORKER.id, inventoryplayer.armor) > 0;
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
                WeightedRandomChoiceEnchantment weightedrandomchoiceenchantment = (WeightedRandomChoiceEnchantment) WeightedRandom.a(random, map.values());

                if (weightedrandomchoiceenchantment != null) {
                    arraylist = new ArrayList();
                    arraylist.add(weightedrandomchoiceenchantment);

                    for (int i1 = l >> 1; random.nextInt(50) <= i1; i1 >>= 1) {
                        Iterator iterator = map.keySet().iterator();

                        while (iterator.hasNext()) {
                            Integer integer = (Integer) iterator.next();
                            boolean flag = true;
                            Iterator iterator1 = arraylist.iterator();

                            while (true) {
                                if (iterator1.hasNext()) {
                                    WeightedRandomChoiceEnchantment weightedrandomchoiceenchantment1 = (WeightedRandomChoiceEnchantment) iterator1.next();

                                    if (weightedrandomchoiceenchantment1.a.a(Enchantment.byId[integer.intValue()])) {
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
                            WeightedRandomChoiceEnchantment weightedrandomchoiceenchantment2 = (WeightedRandomChoiceEnchantment) WeightedRandom.a(random, map.values());

                            arraylist.add(weightedrandomchoiceenchantment2);
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

            if (enchantment != null && enchantment.slot.a(item)) {
                for (int l = enchantment.getStartLevel(); l <= enchantment.getMaxLevel(); ++l) {
                    if (i >= enchantment.a(l) && i <= enchantment.b(l)) {
                        if (hashmap == null) {
                            hashmap = new HashMap();
                        }

                        hashmap.put(Integer.valueOf(enchantment.id), new WeightedRandomChoiceEnchantment(enchantment, l));
                    }
                }
            }
        }

        return hashmap;
    }
}
