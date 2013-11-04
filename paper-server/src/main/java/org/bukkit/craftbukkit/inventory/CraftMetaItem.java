package org.bukkit.craftbukkit.inventory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagInt;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagLong;
import net.minecraft.server.NBTTagString;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.Overridden;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey.Specific;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Children must include the following:
 *
 * <li> Constructor(CraftMetaItem meta)
 * <li> Constructor(NBTTagCompound tag)
 * <li> Constructor(Map<String, Object> map)
 * <br><br>
 * <li> void applyToItem(NBTTagCompound tag)
 * <li> boolean applicableTo(Material type)
 * <br><br>
 * <li> boolean equalsCommon(CraftMetaItem meta)
 * <li> boolean notUncommon(CraftMetaItem meta)
 * <br><br>
 * <li> boolean isEmpty()
 * <li> boolean is{Type}Empty()
 * <br><br>
 * <li> int applyHash()
 * <li> public Class clone()
 * <br><br>
 * <li> Builder<String, Object> serialize(Builder<String, Object> builder)
 * <li> SerializableMeta.Deserializers deserializer()
 */
@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
class CraftMetaItem implements ItemMeta, Repairable {

    static class ItemMetaKey {

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        @interface Specific {
            enum To {
                BUKKIT,
                NBT,
                ;
            }
            To value();
        }

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(final String both) {
            this(both, both);
        }

        ItemMetaKey(final String nbt, final String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }
    }

    @SerializableAs("ItemMeta")
    public static class SerializableMeta implements ConfigurationSerializable {
        static final String TYPE_FIELD = "meta-type";

        static final ImmutableMap<Class<? extends CraftMetaItem>, String> classMap;
        static final ImmutableMap<String, Constructor<? extends CraftMetaItem>> constructorMap;

        static {
            classMap = ImmutableMap.<Class<? extends CraftMetaItem>, String>builder()
                    .put(CraftMetaBook.class, "BOOK")
                    .put(CraftMetaSkull.class, "SKULL")
                    .put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR")
                    .put(CraftMetaMap.class, "MAP")
                    .put(CraftMetaPotion.class, "POTION")
                    .put(CraftMetaEnchantedBook.class, "ENCHANTED")
                    .put(CraftMetaFirework.class, "FIREWORK")
                    .put(CraftMetaCharge.class, "FIREWORK_EFFECT")
                    .put(CraftMetaItem.class, "UNSPECIFIC")
                    .build();

            final ImmutableMap.Builder<String, Constructor<? extends CraftMetaItem>> classConstructorBuilder = ImmutableMap.builder();
            for (Map.Entry<Class<? extends CraftMetaItem>, String> mapping : classMap.entrySet()) {
                try {
                    classConstructorBuilder.put(mapping.getValue(), mapping.getKey().getDeclaredConstructor(Map.class));
                } catch (NoSuchMethodException e) {
                    throw new AssertionError(e);
                }
            }
            constructorMap = classConstructorBuilder.build();
        }

        private SerializableMeta() {
        }

        public static ItemMeta deserialize(Map<String, Object> map) throws Throwable {
            Validate.notNull(map, "Cannot deserialize null map");

            String type = getString(map, TYPE_FIELD, false);
            Constructor<? extends CraftMetaItem> constructor = constructorMap.get(type);

            if (constructor == null) {
                throw new IllegalArgumentException(type + " is not a valid " + TYPE_FIELD);
            }

            try {
                return constructor.newInstance(map);
            } catch (final InstantiationException e) {
                throw new AssertionError(e);
            } catch (final IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (final InvocationTargetException e) {
                throw e.getCause();
            }
        }

        public Map<String, Object> serialize() {
            throw new AssertionError();
        }

        static String getString(Map<?, ?> map, Object field, boolean nullable) {
            return getObject(String.class, map, field, nullable);
        }

        static boolean getBoolean(Map<?, ?> map, Object field) {
            Boolean value = getObject(Boolean.class, map, field, true);
            return value != null && value;
        }

        static <T> T getObject(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable) {
            final Object object = map.get(field);

            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
            if (object == null) {
                if (!nullable) {
                    throw new NoSuchElementException(map + " does not contain " + field);
                }
                return null;
            }
            throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
        }
    }

    static final ItemMetaKey NAME = new ItemMetaKey("Name", "display-name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
    static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
    static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("ench", "enchants");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
    static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_NAME = new ItemMetaKey("Name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");

    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private int repairCost;
    private final NBTTagList attributes;

    CraftMetaItem(CraftMetaItem meta) {
        if (meta == null) {
            attributes = null;
            return;
        }

        this.displayName = meta.displayName;

        if (meta.hasLore()) {
            this.lore = new ArrayList<String>(meta.lore);
        }

        if (meta.hasEnchants()) {
            this.enchantments = new HashMap<Enchantment, Integer>(meta.enchantments);
        }

        this.repairCost = meta.repairCost;
        this.attributes = meta.attributes;
    }

    CraftMetaItem(NBTTagCompound tag) {
        if (tag.hasKey(DISPLAY.NBT)) {
            NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

            if (display.hasKey(NAME.NBT)) {
                displayName = display.getString(NAME.NBT);
            }

            if (display.hasKey(LORE.NBT)) {
                NBTTagList list = display.getList(LORE.NBT, 8);
                lore = new ArrayList<String>(list.size());

                for (int index = 0; index < list.size(); index++) {
                    String line = list.f(index);
                    lore.add(line);
                }
            }
        }

        this.enchantments = buildEnchantments(tag, ENCHANTMENTS);

        if (tag.hasKey(REPAIR.NBT)) {
            repairCost = tag.getInt(REPAIR.NBT);
        }


        if (tag.get(ATTRIBUTES.NBT) instanceof NBTTagList) {
            NBTTagList save = null;
            NBTTagList nbttaglist = tag.getList(ATTRIBUTES.NBT, 10);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                if (!(nbttaglist.get(i) instanceof NBTTagCompound)) {
                    continue;
                }
                NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.get(i);

                if (!(nbttagcompound.get(ATTRIBUTES_UUID_HIGH.NBT) instanceof NBTTagLong)) {
                    continue;
                }
                if (!(nbttagcompound.get(ATTRIBUTES_UUID_LOW.NBT) instanceof NBTTagLong)) {
                    continue;
                }
                if (!(nbttagcompound.get(ATTRIBUTES_IDENTIFIER.NBT) instanceof NBTTagString) || !CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES.contains(nbttagcompound.getString(ATTRIBUTES_IDENTIFIER.NBT))) {
                    continue;
                }
                if (!(nbttagcompound.get(ATTRIBUTES_NAME.NBT) instanceof NBTTagString) || nbttagcompound.getString(ATTRIBUTES_NAME.NBT).isEmpty()) {
                    continue;
                }
                if (!(nbttagcompound.get(ATTRIBUTES_VALUE.NBT) instanceof NBTTagDouble)) {
                    continue;
                }
                if (!(nbttagcompound.get(ATTRIBUTES_TYPE.NBT) instanceof NBTTagInt) || nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT) < 0 || nbttagcompound.getInt(ATTRIBUTES_TYPE.NBT) > 2) {
                    continue;
                }

                if (save == null) {
                    save = new NBTTagList();
                }

                NBTTagCompound entry = new NBTTagCompound();
                entry.set(ATTRIBUTES_UUID_HIGH.NBT, nbttagcompound.get(ATTRIBUTES_UUID_HIGH.NBT));
                entry.set(ATTRIBUTES_UUID_LOW.NBT, nbttagcompound.get(ATTRIBUTES_UUID_LOW.NBT));
                entry.set(ATTRIBUTES_IDENTIFIER.NBT, nbttagcompound.get(ATTRIBUTES_IDENTIFIER.NBT));
                entry.set(ATTRIBUTES_NAME.NBT, nbttagcompound.get(ATTRIBUTES_NAME.NBT));
                entry.set(ATTRIBUTES_VALUE.NBT, nbttagcompound.get(ATTRIBUTES_VALUE.NBT));
                entry.set(ATTRIBUTES_TYPE.NBT, nbttagcompound.get(ATTRIBUTES_TYPE.NBT));
                save.add(entry);
            }

            attributes = save;
        } else {
            attributes = null;
        }
    }

    static Map<Enchantment, Integer> buildEnchantments(NBTTagCompound tag, ItemMetaKey key) {
        if (!tag.hasKey(key.NBT)) {
            return null;
        }

        NBTTagList ench = tag.getList(key.NBT, 10);
        Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.size());

        for (int i = 0; i < ench.size(); i++) {
            int id = 0xffff & ((NBTTagCompound) ench.get(i)).getShort(ENCHANTMENTS_ID.NBT);
            int level = 0xffff & ((NBTTagCompound) ench.get(i)).getShort(ENCHANTMENTS_LVL.NBT);

            enchantments.put(Enchantment.getById(id), level);
        }

        return enchantments;
    }

    CraftMetaItem(Map<String, Object> map) {
        setDisplayName(SerializableMeta.getString(map, NAME.BUKKIT, true));

        Iterable<?> lore = SerializableMeta.getObject(Iterable.class, map, LORE.BUKKIT, true);
        if (lore != null) {
            safelyAdd(lore, this.lore = new ArrayList<String>(), Integer.MAX_VALUE);
        }

        enchantments = buildEnchantments(map, ENCHANTMENTS);

        Integer repairCost = SerializableMeta.getObject(Integer.class, map, REPAIR.BUKKIT, true);
        if (repairCost != null) {
            setRepairCost(repairCost);
        }

        attributes = null;
    }

    static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }

        Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.size());
        for (Map.Entry<?, ?> entry : ench.entrySet()) {
            Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

            if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                enchantments.put(enchantment, (Integer) entry.getValue());
            }
        }

        return enchantments;
    }

    @Overridden
    void applyToItem(NBTTagCompound itemTag) {
        if (hasDisplayName()) {
            setDisplayTag(itemTag, NAME.NBT, new NBTTagString(displayName));
        }

        if (hasLore()) {
            setDisplayTag(itemTag, LORE.NBT, createStringList(lore));
        }

        applyEnchantments(enchantments, itemTag, ENCHANTMENTS);

        if (hasRepairCost()) {
            itemTag.setInt(REPAIR.NBT, repairCost);
        }

        if (attributes != null) {
            itemTag.set(ATTRIBUTES.NBT, attributes);
        }
    }

    static NBTTagList createStringList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        NBTTagList tagList = new NBTTagList();
        for (String value : list) {
            tagList.add(new NBTTagString(value));
        }

        return tagList;
    }

    static void applyEnchantments(Map<Enchantment, Integer> enchantments, NBTTagCompound tag, ItemMetaKey key) {
        if (enchantments == null || enchantments.size() == 0) {
            return;
        }

        NBTTagList list = new NBTTagList();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            NBTTagCompound subtag = new NBTTagCompound();

            subtag.setShort(ENCHANTMENTS_ID.NBT, (short) entry.getKey().getId());
            subtag.setShort(ENCHANTMENTS_LVL.NBT, entry.getValue().shortValue());

            list.add(subtag);
        }

        tag.set(key.NBT, list);
    }

    void setDisplayTag(NBTTagCompound tag, String key, NBTBase value) {
        final NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

        if (!tag.hasKey(DISPLAY.NBT)) {
            tag.set(DISPLAY.NBT, display);
        }

        display.set(key, value);
    }

    @Overridden
    boolean applicableTo(Material type) {
        return type != Material.AIR;
    }

    @Overridden
    boolean isEmpty() {
        return !(hasDisplayName() || hasEnchants() || hasLore() || hasAttributes());
    }

    public String getDisplayName() {
        return displayName;
    }

    public final void setDisplayName(String name) {
        this.displayName = name;
    }

    public boolean hasDisplayName() {
        return !Strings.isNullOrEmpty(displayName);
    }

    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    public boolean hasAttributes() {
        return this.attributes != null;
    }

    public boolean hasRepairCost() {
        return repairCost > 0;
    }

    public boolean hasEnchant(Enchantment ench) {
        return hasEnchants() && enchantments.containsKey(ench);
    }

    public int getEnchantLevel(Enchantment ench) {
        Integer level = hasEnchants() ? enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return hasEnchants() ? ImmutableMap.copyOf(enchantments) : ImmutableMap.<Enchantment, Integer>of();
    }

    public boolean addEnchant(Enchantment ench, int level, boolean ignoreRestrictions) {
        if (enchantments == null) {
            enchantments = new HashMap<Enchantment, Integer>(4);
        }

        if (ignoreRestrictions || level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            Integer old = enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }

    public boolean removeEnchant(Enchantment ench) {
        return hasEnchants() && enchantments.remove(ench) != null;
    }

    public boolean hasEnchants() {
        return !(enchantments == null || enchantments.isEmpty());
    }

    public boolean hasConflictingEnchant(Enchantment ench) {
        return checkConflictingEnchants(enchantments, ench);
    }

    public List<String> getLore() {
        return this.lore == null ? null : new ArrayList<String>(this.lore);
    }

    public void setLore(List<String> lore) { // too tired to think if .clone is better
        if (lore == null) {
            this.lore = null;
        } else {
            if (this.lore == null) {
                safelyAdd(lore, this.lore = new ArrayList<String>(lore.size()), Integer.MAX_VALUE);
            } else {
                this.lore.clear();
                safelyAdd(lore, this.lore, Integer.MAX_VALUE);
            }
        }
    }

    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(int cost) { // TODO: Does this have limits?
        repairCost = cost;
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof CraftMetaItem)) {
            return false;
        }
        return CraftItemFactory.instance().equals(this, (ItemMeta) object);
    }

    /**
     * This method is almost as weird as notUncommon.
     * Only return false if your common internals are unequal.
     * Checking your own internals is redundant if you are not common, as notUncommon is meant for checking those 'not common' variables.
     */
    @Overridden
    boolean equalsCommon(CraftMetaItem that) {
        return ((this.hasDisplayName() ? that.hasDisplayName() && this.displayName.equals(that.displayName) : !that.hasDisplayName()))
                && (this.hasEnchants() ? that.hasEnchants() && this.enchantments.equals(that.enchantments) : !that.hasEnchants())
                && (this.hasLore() ? that.hasLore() && this.lore.equals(that.lore) : !that.hasLore())
                && (this.hasAttributes() ? that.hasAttributes() && this.attributes.equals(that.attributes) : !that.hasAttributes())
                && (this.hasRepairCost() ? that.hasRepairCost() && this.repairCost == that.repairCost : !that.hasRepairCost());
    }

    /**
     * This method is a bit weird...
     * Return true if you are a common class OR your uncommon parts are empty.
     * Empty uncommon parts implies the NBT data would be equivalent if both were applied to an item
     */
    @Overridden
    boolean notUncommon(CraftMetaItem meta) {
        return true;
    }

    @Override
    public final int hashCode() {
        return applyHash();
    }

    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (hasLore() ? this.lore.hashCode() : 0);
        hash = 61 * hash + (hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (hasAttributes() ? this.attributes.hashCode() : 0);
        hash = 61 * hash + (hasRepairCost() ? this.repairCost : 0);
        return hash;
    }

    @Overridden
    @Override
    public CraftMetaItem clone() {
        try {
            CraftMetaItem clone = (CraftMetaItem) super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<String>(this.lore);
            }
            if (this.enchantments != null) {
                clone.enchantments = new HashMap<Enchantment, Integer>(this.enchantments);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public final Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
        map.put(SerializableMeta.TYPE_FIELD, SerializableMeta.classMap.get(getClass()));
        serialize(map);
        return map.build();
    }

    @Overridden
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        if (hasDisplayName()) {
            builder.put(NAME.BUKKIT, displayName);
        }

        if (hasLore()) {
            builder.put(LORE.BUKKIT, ImmutableList.copyOf(lore));
        }

        serializeEnchantments(enchantments, builder, ENCHANTMENTS);

        if (hasRepairCost()) {
            builder.put(REPAIR.BUKKIT, repairCost);
        }

        return builder;
    }

    static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }

        ImmutableMap.Builder<String, Integer> enchants = ImmutableMap.builder();
        for (Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put(enchant.getKey().getName(), enchant.getValue());
        }

        builder.put(key.BUKKIT, enchants.build());
    }

    static void safelyAdd(Iterable<?> addFrom, Collection<String> addTo, int maxItemLength) {
        if (addFrom == null) {
            return;
        }

        for (Object object : addFrom) {
            if (!(object instanceof String)) {
                if (object != null) {
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }

                addTo.add("");
            } else {
                String page = object.toString();

                if (page.length() > maxItemLength) {
                    page = page.substring(0, maxItemLength);
                }

                addTo.add(page);
            }
        }
    }

    static boolean checkConflictingEnchants(Map<Enchantment, Integer> enchantments, Enchantment ench) {
        if (enchantments == null || enchantments.isEmpty()) {
            return false;
        }

        for (Enchantment enchant : enchantments.keySet()) {
            if (enchant.conflictsWith(ench)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final String toString() {
        return SerializableMeta.classMap.get(getClass()) + "_META:" + serialize(); // TODO: cry
    }
}
