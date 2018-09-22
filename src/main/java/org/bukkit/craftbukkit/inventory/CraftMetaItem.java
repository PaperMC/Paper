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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import net.minecraft.server.EnumItemSlot;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.Overridden;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey.Specific;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.JsonParseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.NBTCompressedStreamTools;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
class CraftMetaItem implements ItemMeta, Damageable, Repairable {

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
                    .put(CraftMetaBanner.class, "BANNER")
                    .put(CraftMetaBlockState.class, "TILE_ENTITY")
                    .put(CraftMetaBook.class, "BOOK")
                    .put(CraftMetaBookSigned.class, "BOOK_SIGNED")
                    .put(CraftMetaSkull.class, "SKULL")
                    .put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR")
                    .put(CraftMetaMap.class, "MAP")
                    .put(CraftMetaPotion.class, "POTION")
                    .put(CraftMetaSpawnEgg.class, "SPAWN_EGG")
                    .put(CraftMetaEnchantedBook.class, "ENCHANTED")
                    .put(CraftMetaFirework.class, "FIREWORK")
                    .put(CraftMetaCharge.class, "FIREWORK_EFFECT")
                    .put(CraftMetaKnowledgeBook.class, "KNOWLEDGE_BOOK")
                    .put(CraftMetaTropicalFishBucket.class, "TROPICAL_FISH_BUCKET")
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
    static final ItemMetaKey LOCNAME = new ItemMetaKey("LocName", "loc-name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
    static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
    static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("Enchantments", "enchants");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
    static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
    static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers", "attribute-modifiers");
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
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_SLOT = new ItemMetaKey("Slot");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey UNBREAKABLE = new ItemMetaKey("Unbreakable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey DAMAGE = new ItemMetaKey("Damage");

    private IChatBaseComponent displayName;
    private IChatBaseComponent locName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
    private int repairCost;
    private int hideFlag;
    private boolean unbreakable;
    private int damage;

    private static final Set<String> HANDLED_TAGS = Sets.newHashSet();

    private NBTTagCompound internalTag;
    private final Map<String, NBTBase> unhandledTags = new HashMap<String, NBTBase>();

    CraftMetaItem(CraftMetaItem meta) {
        if (meta == null) {
            return;
        }

        this.displayName = meta.displayName;
        this.locName = meta.locName;

        if (meta.hasLore()) {
            this.lore = new ArrayList<String>(meta.lore);
        }

        if (meta.hasEnchants()) {
            this.enchantments = new HashMap<Enchantment, Integer>(meta.enchantments);
        }

        if (meta.hasAttributeModifiers()) {
            this.attributeModifiers = HashMultimap.create(meta.attributeModifiers);
        }

        this.repairCost = meta.repairCost;
        this.hideFlag = meta.hideFlag;
        this.unbreakable = meta.unbreakable;
        this.damage = meta.damage;
        this.unhandledTags.putAll(meta.unhandledTags);

        this.internalTag = meta.internalTag;
        if (this.internalTag != null) {
            deserializeInternal(internalTag, meta);
        }
    }

    CraftMetaItem(NBTTagCompound tag) {
        if (tag.hasKey(DISPLAY.NBT)) {
            NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

            if (display.hasKey(NAME.NBT)) {
                try {
                    displayName = IChatBaseComponent.ChatSerializer.a(display.getString(NAME.NBT));
                } catch (JsonParseException ex) {
                    // Ignore (stripped like Vanilla)
                }
            }

            if (display.hasKey(LOCNAME.NBT)) {
                try {
                    locName = IChatBaseComponent.ChatSerializer.a(display.getString(LOCNAME.NBT));
                } catch (JsonParseException ex) {
                    // Ignore (stripped like Vanilla)
                }
            }

            if (display.hasKey(LORE.NBT)) {
                NBTTagList list = display.getList(LORE.NBT, CraftMagicNumbers.NBT.TAG_STRING);
                lore = new ArrayList<String>(list.size());

                for (int index = 0; index < list.size(); index++) {
                    String line = list.getString(index);
                    lore.add(line);
                }
            }
        }

        this.enchantments = buildEnchantments(tag, ENCHANTMENTS);
        this.attributeModifiers = buildModifiers(tag, ATTRIBUTES);

        if (tag.hasKey(REPAIR.NBT)) {
            repairCost = tag.getInt(REPAIR.NBT);
        }

        if (tag.hasKey(HIDEFLAGS.NBT)) {
            hideFlag = tag.getInt(HIDEFLAGS.NBT);
        }
        if (tag.hasKey(UNBREAKABLE.NBT)) {
            unbreakable = tag.getBoolean(UNBREAKABLE.NBT);
        }
        if (tag.hasKey(DAMAGE.NBT)) {
            damage = tag.getInt(DAMAGE.NBT);
        }

        Set<String> keys = tag.getKeys();
        for (String key : keys) {
            if (!getHandledTags().contains(key)) {
                unhandledTags.put(key, tag.get(key));
            }
        }
    }

    static Map<Enchantment, Integer> buildEnchantments(NBTTagCompound tag, ItemMetaKey key) {
        if (!tag.hasKey(key.NBT)) {
            return null;
        }

        NBTTagList ench = tag.getList(key.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
        Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.size());

        for (int i = 0; i < ench.size(); i++) {
            String id = ((NBTTagCompound) ench.get(i)).getString(ENCHANTMENTS_ID.NBT);
            int level = 0xffff & ((NBTTagCompound) ench.get(i)).getShort(ENCHANTMENTS_LVL.NBT);

            Enchantment enchant = Enchantment.getByKey(CraftNamespacedKey.fromStringOrNull(id));
            if (enchant != null) {
                enchantments.put(enchant, level);
            }
        }

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(NBTTagCompound tag, ItemMetaKey key) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        if (!tag.hasKey(key.NBT)) {
            return modifiers;
        }
        NBTTagList mods = tag.getList(key.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
        int size = mods.size();

        for (int i = 0; i < size; i++) {
            NBTTagCompound entry = mods.getCompound(i);
            if (entry.isEmpty()) {
                // entry is not an actual NBTTagCompound. getCompound returns empty NBTTagCompound in that case
                continue;
            }
            net.minecraft.server.AttributeModifier nmsModifier = GenericAttributes.a(entry);
            if (nmsModifier == null) {
                continue;
            }

            AttributeModifier attribMod = CraftAttributeInstance.convert(nmsModifier);

            String attributeName = entry.getString(ATTRIBUTES_IDENTIFIER.NBT);
            if (attributeName == null || attributeName.isEmpty()) {
                continue;
            }

            Attribute attribute = CraftAttributeMap.fromMinecraft(attributeName);
            if (attribute == null) {
                continue;
            }

            if (entry.hasKeyOfType(ATTRIBUTES_SLOT.NBT, CraftMagicNumbers.NBT.TAG_STRING)) {
                String slotName = entry.getString(ATTRIBUTES_SLOT.NBT);
                if (slotName == null || slotName.isEmpty()) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                EquipmentSlot slot = CraftEquipmentSlot.getSlot(EnumItemSlot.a(slotName.toLowerCase(Locale.ROOT))); // PAIL rename fromName
                if (slot == null) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                attribMod = new AttributeModifier(attribMod.getUniqueId(), attribMod.getName(), attribMod.getAmount(), attribMod.getOperation(), slot);
            }
            modifiers.put(attribute, attribMod);
        }
        return modifiers;
    }

    CraftMetaItem(Map<String, Object> map) {
        setDisplayName(SerializableMeta.getString(map, NAME.BUKKIT, true));
        setLocalizedName(SerializableMeta.getString(map, LOCNAME.BUKKIT, true));

        Iterable<?> lore = SerializableMeta.getObject(Iterable.class, map, LORE.BUKKIT, true);
        if (lore != null) {
            safelyAdd(lore, this.lore = new ArrayList<String>(), Integer.MAX_VALUE);
        }

        enchantments = buildEnchantments(map, ENCHANTMENTS);
        attributeModifiers = buildModifiers(map, ATTRIBUTES);

        Integer repairCost = SerializableMeta.getObject(Integer.class, map, REPAIR.BUKKIT, true);
        if (repairCost != null) {
            setRepairCost(repairCost);
        }

        Iterable<?> hideFlags = SerializableMeta.getObject(Iterable.class, map, HIDEFLAGS.BUKKIT, true);
        if (hideFlags != null) {
            for (Object hideFlagObject : hideFlags) {
                String hideFlagString = (String) hideFlagObject;
                try {
                    ItemFlag hideFlatEnum = ItemFlag.valueOf(hideFlagString);
                    addItemFlags(hideFlatEnum);
                } catch (IllegalArgumentException ex) {
                    // Ignore when we got a old String which does not map to a Enum value anymore
                }
            }
        }

        Boolean unbreakable = SerializableMeta.getObject(Boolean.class, map, UNBREAKABLE.BUKKIT, true);
        if (unbreakable != null) {
            setUnbreakable(unbreakable);
        }

        Integer damage = SerializableMeta.getObject(Integer.class, map, DAMAGE.BUKKIT, true);
        if (damage != null) {
            setDamage(damage);
        }

        String internal = SerializableMeta.getString(map, "internal", true);
        if (internal != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.decodeBase64(internal));
            try {
                internalTag = NBTCompressedStreamTools.a(buf);
                deserializeInternal(internalTag, map);
                Set<String> keys = internalTag.getKeys();
                for (String key : keys) {
                    if (!getHandledTags().contains(key)) {
                        unhandledTags.put(key, internalTag.get(key));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void deserializeInternal(NBTTagCompound tag, Object context) {
    }

    static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }

        Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(ench.size());
        for (Map.Entry<?, ?> entry : ench.entrySet()) {
            // Doctor older enchants
            String enchantKey = entry.getKey().toString();
            if (enchantKey.equals("SWEEPING")) {
                enchantKey = "SWEEPING_EDGE";
            }

            Enchantment enchantment = Enchantment.getByName(enchantKey);
            if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                enchantments.put(enchantment, (Integer) entry.getValue());
            }
        }

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> mods = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        Multimap<Attribute, AttributeModifier> result = HashMultimap.create();
        if (mods == null) {
            return result;
        }

        for (Object obj : mods.keySet()) {
            if (!(obj instanceof String)) {
                continue;
            }
            String attributeName = (String) obj;
            if (Strings.isNullOrEmpty(attributeName)) {
                continue;
            }
            List<?> list = SerializableMeta.getObject(List.class, mods, attributeName, true);
            if (list == null || list.isEmpty()) {
                return result;
            }

            for (Object o : list) {
                if (!(o instanceof AttributeModifier)) { // this catches null
                    continue;
                }
                AttributeModifier modifier = (AttributeModifier) o;
                Attribute attribute = EnumUtils.getEnum(Attribute.class, attributeName.toUpperCase(Locale.ROOT));
                if (attribute == null) {
                    continue;
                }

                result.put(attribute, modifier);
            }
        }
        return result;
    }

    @Overridden
    void applyToItem(NBTTagCompound itemTag) {
        if (hasDisplayName()) {
            setDisplayTag(itemTag, NAME.NBT, new NBTTagString(CraftChatMessage.toJSON(displayName)));
        }
        if (hasLocalizedName()){
            setDisplayTag(itemTag, LOCNAME.NBT, new NBTTagString(CraftChatMessage.toJSON(locName)));
        }

        if (hasLore()) {
            setDisplayTag(itemTag, LORE.NBT, createStringList(lore));
        }

        if (hideFlag != 0) {
            itemTag.setInt(HIDEFLAGS.NBT, hideFlag);
        }

        applyEnchantments(enchantments, itemTag, ENCHANTMENTS);
        applyModifiers(attributeModifiers, itemTag, ATTRIBUTES);

        if (hasRepairCost()) {
            itemTag.setInt(REPAIR.NBT, repairCost);
        }

        if (isUnbreakable()) {
            itemTag.setBoolean(UNBREAKABLE.NBT, unbreakable);
        }

        if (hasDamage()) {
            itemTag.setInt(DAMAGE.NBT, damage);
        }

        for (Map.Entry<String, NBTBase> e : unhandledTags.entrySet()) {
            itemTag.set(e.getKey(), e.getValue());
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

            subtag.setString(ENCHANTMENTS_ID.NBT, entry.getKey().getKey().toString());
            subtag.setShort(ENCHANTMENTS_LVL.NBT, entry.getValue().shortValue());

            list.add(subtag);
        }

        tag.set(key.NBT, list);
    }

    static void applyModifiers(Multimap<Attribute, AttributeModifier> modifiers, NBTTagCompound tag, ItemMetaKey key) {
        if (modifiers == null || modifiers.isEmpty()) {
            return;
        }

        NBTTagList list = new NBTTagList();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            net.minecraft.server.AttributeModifier nmsModifier = CraftAttributeInstance.convert(entry.getValue());
            NBTTagCompound sub = GenericAttributes.a(nmsModifier);
            if (sub.isEmpty()) {
                continue;
            }

            String name = CraftAttributeMap.toMinecraft(entry.getKey());
            if (name == null || name.isEmpty()) {
                continue;
            }

            sub.setString(ATTRIBUTES_IDENTIFIER.NBT, name); // Attribute Name
            if (entry.getValue().getSlot() != null) {
                EnumItemSlot slot = CraftEquipmentSlot.getNMS(entry.getValue().getSlot());
                if (slot != null) {
                    sub.setString(ATTRIBUTES_SLOT.NBT, slot.d()); // PAIL rename getSlotName, getName
                }
            }
            list.add(sub);
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
        return !(hasDisplayName() || hasLocalizedName() || hasEnchants() || hasLore() || hasRepairCost() || !unhandledTags.isEmpty() || hideFlag != 0 || isUnbreakable() || hasDamage() || hasAttributeModifiers());
    }

    public String getDisplayName() {
        return CraftChatMessage.fromComponent(displayName, EnumChatFormat.WHITE);
    }

    public final void setDisplayName(String name) {
        this.displayName = CraftChatMessage.wrapOrNull(name);
    }

    public boolean hasDisplayName() {
        return displayName != null;
    }

    @Override
    public String getLocalizedName() {
        return CraftChatMessage.fromComponent(locName, EnumChatFormat.WHITE);
    }

    @Override
    public void setLocalizedName(String name) {
        this.locName = CraftChatMessage.wrapOrNull(name);
    }

    @Override
    public boolean hasLocalizedName() {
        return locName != null;
    }

    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    public boolean hasRepairCost() {
        return repairCost > 0;
    }

    public boolean hasEnchant(Enchantment ench) {
        Validate.notNull(ench, "Enchantment cannot be null");
        return hasEnchants() && enchantments.containsKey(ench);
    }

    public int getEnchantLevel(Enchantment ench) {
        Validate.notNull(ench, "Enchantment cannot be null");
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
        Validate.notNull(ench, "Enchantment cannot be null");
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
        Validate.notNull(ench, "Enchantment cannot be null");
        return hasEnchants() && enchantments.remove(ench) != null;
    }

    public boolean hasEnchants() {
        return !(enchantments == null || enchantments.isEmpty());
    }

    public boolean hasConflictingEnchant(Enchantment ench) {
        return checkConflictingEnchants(enchantments, ench);
    }

    @Override
    public void addItemFlags(ItemFlag... hideFlags) {
        for (ItemFlag f : hideFlags) {
            this.hideFlag |= getBitModifier(f);
        }
    }

    @Override
    public void removeItemFlags(ItemFlag... hideFlags) {
        for (ItemFlag f : hideFlags) {
            this.hideFlag &= ~getBitModifier(f);
        }
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);

        for (ItemFlag f : ItemFlag.values()) {
            if (hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        int bitModifier = getBitModifier(flag);
        return (this.hideFlag & bitModifier) == bitModifier;
    }

    private byte getBitModifier(ItemFlag hideFlag) {
        return (byte) (1 << hideFlag.ordinal());
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
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public boolean hasAttributeModifiers() {
        return attributeModifiers != null && !attributeModifiers.isEmpty();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return hasAttributeModifiers() ? ImmutableMultimap.copyOf(attributeModifiers) : null;
    }

    private void checkAttributeList() {
        if (attributeModifiers == null) {
            attributeModifiers = HashMultimap.create();
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable EquipmentSlot slot) {
        checkAttributeList();
        SetMultimap<Attribute, AttributeModifier> result = HashMultimap.create();
        for (Map.Entry<Attribute, AttributeModifier> entry : attributeModifiers.entries()) {
            if (entry.getValue().getSlot() == null || entry.getValue().getSlot() == slot) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@Nonnull Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        return attributeModifiers.containsKey(attribute) ? ImmutableList.copyOf(attributeModifiers.get(attribute)) : null;
    }

    @Override
    public boolean addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        Preconditions.checkNotNull(modifier, "AttributeModifier cannot be null");
        checkAttributeList();
        for (Map.Entry<Attribute, AttributeModifier> entry : attributeModifiers.entries()) {
            Preconditions.checkArgument(!entry.getValue().getUniqueId().equals(modifier.getUniqueId()), "Cannot register AttributeModifier. Modifier is already applied! %s", modifier);
        }
        return attributeModifiers.put(attribute, modifier);
    }

    @Override
    public void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        if (attributeModifiers == null || attributeModifiers.isEmpty()) {
            this.attributeModifiers = HashMultimap.create();
            return;
        }
        Iterator<Map.Entry<Attribute, AttributeModifier>> iterator = attributeModifiers.entries().iterator();
        this.attributeModifiers.clear();
        while (iterator.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> next = iterator.next();

            if (next.getKey() == null || next.getValue() == null) {
                iterator.remove();
                continue;
            }
            this.attributeModifiers.put(next.getKey(), next.getValue());
        }
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        checkAttributeList();
        return !attributeModifiers.removeAll(attribute).isEmpty();
    }

    @Override
    public boolean removeAttributeModifier(@Nullable EquipmentSlot slot) {
        checkAttributeList();
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            // Explicitly match against null because (as of MC 1.13) AttributeModifiers without a -
            // set slot are active in any slot.
            if (entry.getValue().getSlot() == null || entry.getValue().getSlot() == slot) {
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        Preconditions.checkNotNull(modifier, "AttributeModifier cannot be null");
        checkAttributeList();
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            if (entry.getKey() == null || entry.getValue() == null) {
                iter.remove();
                ++removed;
                continue; // remove all null values while we are here
            }

            if (entry.getKey() == attribute && entry.getValue().getUniqueId().equals(modifier.getUniqueId())) {
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    private static boolean compareModifiers(Multimap<Attribute, AttributeModifier> first, Multimap<Attribute, AttributeModifier> second) {
        if (first == null || second == null) {
            return false;
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : first.entries()) {
            if (!second.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : second.entries()) {
            if (!first.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasDamage() {
        return damage > 0;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
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
                && (this.hasLocalizedName()? that.hasLocalizedName()&& this.locName.equals(that.locName) : !that.hasLocalizedName())
                && (this.hasEnchants() ? that.hasEnchants() && this.enchantments.equals(that.enchantments) : !that.hasEnchants())
                && (this.hasLore() ? that.hasLore() && this.lore.equals(that.lore) : !that.hasLore())
                && (this.hasRepairCost() ? that.hasRepairCost() && this.repairCost == that.repairCost : !that.hasRepairCost())
                && (this.hasAttributeModifiers() ? that.hasAttributeModifiers() && compareModifiers(this.attributeModifiers, that.attributeModifiers) : !that.hasAttributeModifiers())
                && (this.unhandledTags.equals(that.unhandledTags))
                && (this.hideFlag == that.hideFlag)
                && (this.isUnbreakable() == that.isUnbreakable())
                && (this.hasDamage() ? that.hasDamage() && this.damage == that.damage : !that.hasDamage());
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
        hash = 61 * hash + (hasLocalizedName()? this.locName.hashCode() : 0);
        hash = 61 * hash + (hasLore() ? this.lore.hashCode() : 0);
        hash = 61 * hash + (hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + unhandledTags.hashCode();
        hash = 61 * hash + hideFlag;
        hash = 61 * hash + (isUnbreakable() ? 1231 : 1237);
        hash = 61 * hash + (hasDamage() ? this.damage : 0);
        hash = 61 * hash + (hasAttributeModifiers() ? this.attributeModifiers.hashCode() : 0);
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
            if (this.hasAttributeModifiers()) {
                clone.attributeModifiers = HashMultimap.create(this.attributeModifiers);
            }
            clone.hideFlag = this.hideFlag;
            clone.unbreakable = this.unbreakable;
            clone.damage = this.damage;
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
            builder.put(NAME.BUKKIT, CraftChatMessage.fromComponent(displayName));
        }
        if (hasLocalizedName()) {
            builder.put(LOCNAME.BUKKIT, CraftChatMessage.fromComponent(locName));
        }

        if (hasLore()) {
            builder.put(LORE.BUKKIT, ImmutableList.copyOf(lore));
        }

        serializeEnchantments(enchantments, builder, ENCHANTMENTS);
        serializeModifiers(attributeModifiers, builder, ATTRIBUTES);

        if (hasRepairCost()) {
            builder.put(REPAIR.BUKKIT, repairCost);
        }

        List<String> hideFlags = new ArrayList<String>();
        for (ItemFlag hideFlagEnum : getItemFlags()) {
            hideFlags.add(hideFlagEnum.name());
        }
        if (!hideFlags.isEmpty()) {
            builder.put(HIDEFLAGS.BUKKIT, hideFlags);
        }

        if (isUnbreakable()) {
            builder.put(UNBREAKABLE.BUKKIT, unbreakable);
        }

        if (hasDamage()) {
            builder.put(DAMAGE.BUKKIT, damage);
        }

        final Map<String, NBTBase> internalTags = new HashMap<String, NBTBase>(unhandledTags);
        serializeInternal(internalTags);
        if (!internalTags.isEmpty()) {
            NBTTagCompound internal = new NBTTagCompound();
            for (Map.Entry<String, NBTBase> e : internalTags.entrySet()) {
                internal.set(e.getKey(), e.getValue());
            }
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NBTCompressedStreamTools.a(internal, buf);
                builder.put("internal", Base64.encodeBase64String(buf.toByteArray()));
            } catch (IOException ex) {
                Logger.getLogger(CraftMetaItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return builder;
    }

    void serializeInternal(final Map<String, NBTBase> unhandledTags) {
    }

    Material updateMaterial(Material material) {
        return material;
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

    static void serializeModifiers(Multimap<Attribute, AttributeModifier> modifiers, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (modifiers == null || modifiers.isEmpty()) {
            return;
        }

        Map<String, List<Object>> mods = new HashMap<>();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null) {
                continue;
            }
            Collection<AttributeModifier> modCollection = modifiers.get(entry.getKey());
            if (modCollection == null || modCollection.isEmpty()) {
                continue;
            }
            mods.put(entry.getKey().name(), new ArrayList<>(modCollection));
        }
        builder.put(key.BUKKIT, mods);
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

    public static Set<String> getHandledTags() {
        synchronized (HANDLED_TAGS) {
            if (HANDLED_TAGS.isEmpty()) {
                HANDLED_TAGS.addAll(Arrays.asList(
                        DISPLAY.NBT,
                        REPAIR.NBT,
                        ENCHANTMENTS.NBT,
                        HIDEFLAGS.NBT,
                        UNBREAKABLE.NBT,
                        DAMAGE.NBT,
                        ATTRIBUTES.NBT,
                        ATTRIBUTES_IDENTIFIER.NBT,
                        ATTRIBUTES_NAME.NBT,
                        ATTRIBUTES_VALUE.NBT,
                        ATTRIBUTES_UUID_HIGH.NBT,
                        ATTRIBUTES_UUID_LOW.NBT,
                        ATTRIBUTES_SLOT.NBT,
                        CraftMetaMap.MAP_SCALING.NBT,
                        CraftMetaMap.MAP_ID.NBT,
                        CraftMetaPotion.POTION_EFFECTS.NBT,
                        CraftMetaPotion.DEFAULT_POTION.NBT,
                        CraftMetaPotion.POTION_COLOR.NBT,
                        CraftMetaSkull.SKULL_OWNER.NBT,
                        CraftMetaSkull.SKULL_PROFILE.NBT,
                        CraftMetaSpawnEgg.ENTITY_TAG.NBT,
                        CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT,
                        CraftMetaBook.BOOK_TITLE.NBT,
                        CraftMetaBook.BOOK_AUTHOR.NBT,
                        CraftMetaBook.BOOK_PAGES.NBT,
                        CraftMetaBook.RESOLVED.NBT,
                        CraftMetaBook.GENERATION.NBT,
                        CraftMetaFirework.FIREWORKS.NBT,
                        CraftMetaEnchantedBook.STORED_ENCHANTMENTS.NBT,
                        CraftMetaCharge.EXPLOSION.NBT,
                        CraftMetaBlockState.BLOCK_ENTITY_TAG.NBT,
                        CraftMetaKnowledgeBook.BOOK_RECIPES.NBT,
                        CraftMetaTropicalFishBucket.VARIANT.NBT
                ));
            }
            return HANDLED_TAGS;
        }
    }
}
