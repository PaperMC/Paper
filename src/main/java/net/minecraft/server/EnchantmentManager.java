package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

public class EnchantmentManager {

    public static int getEnchantmentLevel(Enchantment enchantment, ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return 0;
        } else {
            MinecraftKey minecraftkey = IRegistry.ENCHANTMENT.getKey(enchantment);
            NBTTagList nbttaglist = itemstack.getEnchantments();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
                MinecraftKey minecraftkey1 = MinecraftKey.a(nbttagcompound.getString("id"));

                if (minecraftkey1 != null && minecraftkey1.equals(minecraftkey)) {
                    return MathHelper.clamp(nbttagcompound.getInt("lvl"), 0, 255);
                }
            }

            return 0;
        }
    }

    public static Map<Enchantment, Integer> a(ItemStack itemstack) {
        NBTTagList nbttaglist = itemstack.getItem() == Items.ENCHANTED_BOOK ? ItemEnchantedBook.d(itemstack) : itemstack.getEnchantments();

        return a(nbttaglist);
    }

    public static Map<Enchantment, Integer> a(NBTTagList nbttaglist) {
        Map<Enchantment, Integer> map = Maps.newLinkedHashMap();

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);

            IRegistry.ENCHANTMENT.getOptional(MinecraftKey.a(nbttagcompound.getString("id"))).ifPresent((enchantment) -> {
                Integer integer = (Integer) map.put(enchantment, nbttagcompound.getInt("lvl"));
            });
        }

        return map;
    }

    public static void a(Map<Enchantment, Integer> map, ItemStack itemstack) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Enchantment, Integer> entry = (Entry) iterator.next();
            Enchantment enchantment = (Enchantment) entry.getKey();

            if (enchantment != null) {
                int i = (Integer) entry.getValue();
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setString("id", String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment)));
                nbttagcompound.setShort("lvl", (short) i);
                nbttaglist.add(nbttagcompound);
                if (itemstack.getItem() == Items.ENCHANTED_BOOK) {
                    ItemEnchantedBook.a(itemstack, new WeightedRandomEnchant(enchantment, i));
                }
            }
        }

        if (nbttaglist.isEmpty()) {
            itemstack.removeTag("Enchantments");
        } else if (itemstack.getItem() != Items.ENCHANTED_BOOK) {
            itemstack.a("Enchantments", (NBTBase) nbttaglist);
        }

    }

    private static void a(EnchantmentManager.a enchantmentmanager_a, ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            NBTTagList nbttaglist = itemstack.getEnchantments();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                String s = nbttaglist.getCompound(i).getString("id");
                int j = nbttaglist.getCompound(i).getInt("lvl");

                IRegistry.ENCHANTMENT.getOptional(MinecraftKey.a(s)).ifPresent((enchantment) -> {
                    enchantmentmanager_a.accept(enchantment, j);
                });
            }

        }
    }

    private static void a(EnchantmentManager.a enchantmentmanager_a, Iterable<ItemStack> iterable) {
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            a(enchantmentmanager_a, itemstack);
        }

    }

    public static int a(Iterable<ItemStack> iterable, DamageSource damagesource) {
        MutableInt mutableint = new MutableInt();

        a((enchantment, i) -> {
            mutableint.add(enchantment.a(i, damagesource));
        }, iterable);
        return mutableint.intValue();
    }

    public static float a(ItemStack itemstack, EnumMonsterType enummonstertype) {
        MutableFloat mutablefloat = new MutableFloat();

        a((enchantment, i) -> {
            mutablefloat.add(enchantment.a(i, enummonstertype));
        }, itemstack);
        return mutablefloat.floatValue();
    }

    public static float a(EntityLiving entityliving) {
        int i = a(Enchantments.SWEEPING, entityliving);

        return i > 0 ? EnchantmentSweeping.e(i) : 0.0F;
    }

    public static void a(EntityLiving entityliving, Entity entity) {
        EnchantmentManager.a enchantmentmanager_a = (enchantment, i) -> {
            enchantment.b(entityliving, entity, i);
        };

        if (entityliving != null) {
            a(enchantmentmanager_a, entityliving.bo());
        }

        if (entity instanceof EntityHuman) {
            a(enchantmentmanager_a, entityliving.getItemInMainHand());
        }

    }

    public static void b(EntityLiving entityliving, Entity entity) {
        EnchantmentManager.a enchantmentmanager_a = (enchantment, i) -> {
            enchantment.a(entityliving, entity, i);
        };

        if (entityliving != null) {
            a(enchantmentmanager_a, entityliving.bo());
        }

        if (entityliving instanceof EntityHuman) {
            a(enchantmentmanager_a, entityliving.getItemInMainHand());
        }

    }

    public static int a(Enchantment enchantment, EntityLiving entityliving) {
        Iterable<ItemStack> iterable = enchantment.a(entityliving).values();

        if (iterable == null) {
            return 0;
        } else {
            int i = 0;
            Iterator iterator = iterable.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();
                int j = getEnchantmentLevel(enchantment, itemstack);

                if (j > i) {
                    i = j;
                }
            }

            return i;
        }
    }

    public static int b(EntityLiving entityliving) {
        return a(Enchantments.KNOCKBACK, entityliving);
    }

    public static int getFireAspectEnchantmentLevel(EntityLiving entityliving) {
        return a(Enchantments.FIRE_ASPECT, entityliving);
    }

    public static int getOxygenEnchantmentLevel(EntityLiving entityliving) {
        return a(Enchantments.OXYGEN, entityliving);
    }

    public static int e(EntityLiving entityliving) {
        return a(Enchantments.DEPTH_STRIDER, entityliving);
    }

    public static int getDigSpeedEnchantmentLevel(EntityLiving entityliving) {
        return a(Enchantments.DIG_SPEED, entityliving);
    }

    public static int b(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.LUCK, itemstack);
    }

    public static int c(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.LURE, itemstack);
    }

    public static int g(EntityLiving entityliving) {
        return a(Enchantments.LOOT_BONUS_MOBS, entityliving);
    }

    public static boolean h(EntityLiving entityliving) {
        return a(Enchantments.WATER_WORKER, entityliving) > 0;
    }

    public static boolean i(EntityLiving entityliving) {
        return a(Enchantments.FROST_WALKER, entityliving) > 0;
    }

    public static boolean j(EntityLiving entityliving) {
        return a(Enchantments.SOUL_SPEED, entityliving) > 0;
    }

    public static boolean d(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.BINDING_CURSE, itemstack) > 0;
    }

    public static boolean shouldNotDrop(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.VANISHING_CURSE, itemstack) > 0;
    }

    public static int f(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.LOYALTY, itemstack);
    }

    public static int g(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.RIPTIDE, itemstack);
    }

    public static boolean h(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.CHANNELING, itemstack) > 0;
    }

    @Nullable
    public static Entry<EnumItemSlot, ItemStack> b(Enchantment enchantment, EntityLiving entityliving) {
        return a(enchantment, entityliving, (itemstack) -> {
            return true;
        });
    }

    @Nullable
    public static Entry<EnumItemSlot, ItemStack> a(Enchantment enchantment, EntityLiving entityliving, Predicate<ItemStack> predicate) {
        Map<EnumItemSlot, ItemStack> map = enchantment.a(entityliving);

        if (map.isEmpty()) {
            return null;
        } else {
            List<Entry<EnumItemSlot, ItemStack>> list = Lists.newArrayList();
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<EnumItemSlot, ItemStack> entry = (Entry) iterator.next();
                ItemStack itemstack = (ItemStack) entry.getValue();

                if (!itemstack.isEmpty() && getEnchantmentLevel(enchantment, itemstack) > 0 && predicate.test(itemstack)) {
                    list.add(entry);
                }
            }

            return list.isEmpty() ? null : (Entry) list.get(entityliving.getRandom().nextInt(list.size()));
        }
    }

    public static int a(Random random, int i, int j, ItemStack itemstack) {
        Item item = itemstack.getItem();
        int k = item.c();

        if (k <= 0) {
            return 0;
        } else {
            if (j > 15) {
                j = 15;
            }

            int l = random.nextInt(8) + 1 + (j >> 1) + random.nextInt(j + 1);

            return i == 0 ? Math.max(l / 3, 1) : (i == 1 ? l * 2 / 3 + 1 : Math.max(l, j * 2));
        }
    }

    public static ItemStack a(Random random, ItemStack itemstack, int i, boolean flag) {
        List<WeightedRandomEnchant> list = b(random, itemstack, i, flag);
        boolean flag1 = itemstack.getItem() == Items.BOOK;

        if (flag1) {
            itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            WeightedRandomEnchant weightedrandomenchant = (WeightedRandomEnchant) iterator.next();

            if (flag1) {
                ItemEnchantedBook.a(itemstack, weightedrandomenchant);
            } else {
                itemstack.addEnchantment(weightedrandomenchant.enchantment, weightedrandomenchant.level);
            }
        }

        return itemstack;
    }

    public static List<WeightedRandomEnchant> b(Random random, ItemStack itemstack, int i, boolean flag) {
        List<WeightedRandomEnchant> list = Lists.newArrayList();
        Item item = itemstack.getItem();
        int j = item.c();

        if (j <= 0) {
            return list;
        } else {
            i += 1 + random.nextInt(j / 4 + 1) + random.nextInt(j / 4 + 1);
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;

            i = MathHelper.clamp(Math.round((float) i + (float) i * f), 1, Integer.MAX_VALUE);
            List<WeightedRandomEnchant> list1 = a(i, itemstack, flag);

            if (!list1.isEmpty()) {
                list.add(WeightedRandom.a(random, list1));

                while (random.nextInt(50) <= i) {
                    a(list1, (WeightedRandomEnchant) SystemUtils.a((List) list));
                    if (list1.isEmpty()) {
                        break;
                    }

                    list.add(WeightedRandom.a(random, list1));
                    i /= 2;
                }
            }

            return list;
        }
    }

    public static void a(List<WeightedRandomEnchant> list, WeightedRandomEnchant weightedrandomenchant) {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            if (!weightedrandomenchant.enchantment.isCompatible(((WeightedRandomEnchant) iterator.next()).enchantment)) {
                iterator.remove();
            }
        }

    }

    public static boolean a(Collection<Enchantment> collection, Enchantment enchantment) {
        Iterator iterator = collection.iterator();

        Enchantment enchantment1;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            enchantment1 = (Enchantment) iterator.next();
        } while (enchantment1.isCompatible(enchantment));

        return false;
    }

    public static List<WeightedRandomEnchant> a(int i, ItemStack itemstack, boolean flag) {
        List<WeightedRandomEnchant> list = Lists.newArrayList();
        Item item = itemstack.getItem();
        boolean flag1 = itemstack.getItem() == Items.BOOK;
        Iterator iterator = IRegistry.ENCHANTMENT.iterator();

        while (iterator.hasNext()) {
            Enchantment enchantment = (Enchantment) iterator.next();

            if ((!enchantment.isTreasure() || flag) && enchantment.i() && (enchantment.itemTarget.canEnchant(item) || flag1)) {
                for (int j = enchantment.getMaxLevel(); j > enchantment.getStartLevel() - 1; --j) {
                    if (i >= enchantment.a(j) && i <= enchantment.b(j)) {
                        list.add(new WeightedRandomEnchant(enchantment, j));
                        break;
                    }
                }
            }
        }

        return list;
    }

    @FunctionalInterface
    interface a {

        void accept(Enchantment enchantment, int i);
    }
}
