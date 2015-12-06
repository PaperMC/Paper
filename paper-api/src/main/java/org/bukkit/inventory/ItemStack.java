package org.bukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * Represents a stack of items
 */
public class ItemStack implements Cloneable, ConfigurationSerializable {
    private int type = 0;
    private int amount = 0;
    private MaterialData data = null;
    private short durability = 0;
    private ItemMeta meta;

    @Utility
    protected ItemStack() {}

    /**
     * Defaults stack size to 1, with no extra data
     *
     * @param type item material id
     * @deprecated Magic value
     */
    @Deprecated
    public ItemStack(final int type) {
        this(type, 1);
    }

    /**
     * Defaults stack size to 1, with no extra data
     *
     * @param type item material
     */
    public ItemStack(final Material type) {
        this(type, 1);
    }

    /**
     * An item stack with no extra data
     *
     * @param type item material id
     * @param amount stack size
     * @deprecated Magic value
     */
    @Deprecated
    public ItemStack(final int type, final int amount) {
        this(type, amount, (short) 0);
    }

    /**
     * An item stack with no extra data
     *
     * @param type item material
     * @param amount stack size
     */
    public ItemStack(final Material type, final int amount) {
        this(type.getId(), amount);
    }

    /**
     * An item stack with the specified damage / durability
     *
     * @param type item material id
     * @param amount stack size
     * @param damage durability / damage
     * @deprecated Magic value
     */
    @Deprecated
    public ItemStack(final int type, final int amount, final short damage) {
        this.type = type;
        this.amount = amount;
        this.durability = damage;
    }

    /**
     * An item stack with the specified damage / durabiltiy
     *
     * @param type item material
     * @param amount stack size
     * @param damage durability / damage
     */
    public ItemStack(final Material type, final int amount, final short damage) {
        this(type.getId(), amount, damage);
    }

    /**
     * @param type the raw type id
     * @param amount the amount in the stack
     * @param damage the damage value of the item
     * @param data the data value or null
     * @deprecated this method uses an ambiguous data byte object
     */
    @Deprecated
    public ItemStack(final int type, final int amount, final short damage, final Byte data) {
        this.type = type;
        this.amount = amount;
        this.durability = damage;
        if (data != null) {
            createData(data);
            this.durability = data;
        }
    }

    /**
     * @param type the type
     * @param amount the amount in the stack
     * @param damage the damage value of the item
     * @param data the data value or null
     * @deprecated this method uses an ambiguous data byte object
     */
    @Deprecated
    public ItemStack(final Material type, final int amount, final short damage, final Byte data) {
        this(type.getId(), amount, damage, data);
    }

    /**
     * Creates a new item stack derived from the specified stack
     *
     * @param stack the stack to copy
     * @throws IllegalArgumentException if the specified stack is null or
     *     returns an item meta not created by the item factory
     */
    public ItemStack(final ItemStack stack) throws IllegalArgumentException {
        Validate.notNull(stack, "Cannot copy null stack");
        this.type = stack.getTypeId();
        this.amount = stack.getAmount();
        this.durability = stack.getDurability();
        this.data = stack.getData();
        if (stack.hasItemMeta()) {
            setItemMeta0(stack.getItemMeta(), getType0());
        }
    }

    /**
     * Gets the type of this item
     *
     * @return Type of the items in this stack
     */
    @Utility
    public Material getType() {
        return getType0(getTypeId());
    }

    private Material getType0() {
        return getType0(this.type);
    }

    private static Material getType0(int id) {
        Material material = Material.getMaterial(id);
        return material == null ? Material.AIR : material;
    }

    /**
     * Sets the type of this item
     * <p>
     * Note that in doing so you will reset the MaterialData for this stack
     *
     * @param type New type to set the items in this stack to
     */
    @Utility
    public void setType(Material type) {
        Validate.notNull(type, "Material cannot be null");
        setTypeId(type.getId());
    }

    /**
     * Gets the type id of this item
     *
     * @return Type Id of the items in this stack
     * @deprecated Magic value
     */
    @Deprecated
    public int getTypeId() {
        return type;
    }

    /**
     * Sets the type id of this item
     * <p>
     * Note that in doing so you will reset the MaterialData for this stack
     *
     * @param type New type id to set the items in this stack to
     * @deprecated Magic value
     */
    @Deprecated
    public void setTypeId(int type) {
        this.type = type;
        if (this.meta != null) {
            this.meta = Bukkit.getItemFactory().asMetaFor(meta, getType0());
        }
        createData((byte) 0);
    }

    /**
     * Gets the amount of items in this stack
     *
     * @return Amount of items in this stick
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of items in this stack
     *
     * @param amount New amount of items in this stack
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the MaterialData for this stack of items
     *
     * @return MaterialData for this item
     */
    public MaterialData getData() {
        Material mat = getType();
        if (data == null && mat != null && mat.getData() != null) {
            data = mat.getNewData((byte) this.getDurability());
        }

        return data;
    }

    /**
     * Sets the MaterialData for this stack of items
     *
     * @param data New MaterialData for this item
     */
    public void setData(MaterialData data) {
        Material mat = getType();

        if (data == null || mat == null || mat.getData() == null) {
            this.data = data;
        } else {
            if ((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class)) {
                this.data = data;
            } else {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }
        }
    }

    /**
     * Sets the durability of this item
     *
     * @param durability Durability of this item
     */
    public void setDurability(final short durability) {
        this.durability = durability;
    }

    /**
     * Gets the durability of this item
     *
     * @return Durability of this item
     */
    public short getDurability() {
        return durability;
    }

    /**
     * Get the maximum stacksize for the material hold in this ItemStack.
     * (Returns -1 if it has no idea)
     *
     * @return The maximum you can stack this material to.
     */
    @Utility
    public int getMaxStackSize() {
        Material material = getType();
        if (material != null) {
            return material.getMaxStackSize();
        }
        return -1;
    }

    private void createData(final byte data) {
        Material mat = Material.getMaterial(type);

        if (mat == null) {
            this.data = new MaterialData(type, data);
        } else {
            this.data = mat.getNewData(data);
        }
    }

    @Override
    @Utility
    public String toString() {
        StringBuilder toString = new StringBuilder("ItemStack{").append(getType().name()).append(" x ").append(getAmount());
        if (hasItemMeta()) {
            toString.append(", ").append(getItemMeta());
        }
        return toString.append('}').toString();
    }

    @Override
    @Utility
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemStack)) {
            return false;
        }

        ItemStack stack = (ItemStack) obj;
        return getAmount() == stack.getAmount() && isSimilar(stack);
    }

    /**
     * This method is the same as equals, but does not consider stack size
     * (amount).
     *
     * @param stack the item stack to compare to
     * @return true if the two stacks are equal, ignoring the amount
     */
    @Utility
    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        return getTypeId() == stack.getTypeId() && getDurability() == stack.getDurability() && hasItemMeta() == stack.hasItemMeta() && (hasItemMeta() ? Bukkit.getItemFactory().equals(getItemMeta(), stack.getItemMeta()) : true);
    }

    @Override
    public ItemStack clone() {
        try {
            ItemStack itemStack = (ItemStack) super.clone();

            if (this.meta != null) {
                itemStack.meta = this.meta.clone();
            }

            if (this.data != null) {
                itemStack.data = this.data.clone();
            }

            return itemStack;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    @Utility
    public final int hashCode() {
        int hash = 1;

        hash = hash * 31 + getTypeId();
        hash = hash * 31 + getAmount();
        hash = hash * 31 + (getDurability() & 0xffff);
        hash = hash * 31 + (hasItemMeta() ? (meta == null ? getItemMeta().hashCode() : meta.hashCode()) : 0);

        return hash;
    }

    /**
     * Checks if this ItemStack contains the given {@link Enchantment}
     *
     * @param ench Enchantment to test
     * @return True if this has the given enchantment
     */
    public boolean containsEnchantment(Enchantment ench) {
        return meta == null ? false : meta.hasEnchant(ench);
    }

    /**
     * Gets the level of the specified enchantment on this item stack
     *
     * @param ench Enchantment to check
     * @return Level of the enchantment, or 0
     */
    public int getEnchantmentLevel(Enchantment ench) {
        return meta == null ? 0 : meta.getEnchantLevel(ench);
    }

    /**
     * Gets a map containing all enchantments and their levels on this item.
     *
     * @return Map of enchantments.
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return meta == null ? ImmutableMap.<Enchantment, Integer>of() : meta.getEnchants();
    }

    /**
     * Adds the specified enchantments to this item stack.
     * <p>
     * This method is the same as calling {@link
     * #addEnchantment(org.bukkit.enchantments.Enchantment, int)} for each
     * element of the map.
     *
     * @param enchantments Enchantments to add
     * @throws IllegalArgumentException if the specified enchantments is null
     * @throws IllegalArgumentException if any specific enchantment or level
     *     is null. <b>Warning</b>: Some enchantments may be added before this
     *     exception is thrown.
     */
    @Utility
    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        Validate.notNull(enchantments, "Enchantments cannot be null");
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     *
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     * @throws IllegalArgumentException if enchantment null, or enchantment is
     *     not applicable
     */
    @Utility
    public void addEnchantment(Enchantment ench, int level) {
        Validate.notNull(ench, "Enchantment cannot be null");
        if ((level < ench.getStartLevel()) || (level > ench.getMaxLevel())) {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + ench.getStartLevel() + " to " + ench.getMaxLevel() + ")");
        } else if (!ench.canEnchantItem(this)) {
            throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
        }

        addUnsafeEnchantment(ench, level);
    }

    /**
     * Adds the specified enchantments to this item stack in an unsafe manner.
     * <p>
     * This method is the same as calling {@link
     * #addUnsafeEnchantment(org.bukkit.enchantments.Enchantment, int)} for
     * each element of the map.
     *
     * @param enchantments Enchantments to add
     */
    @Utility
    public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     * <p>
     * This method is unsafe and will ignore level restrictions or item type.
     * Use at your own discretion.
     *
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     */
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        (meta == null ? meta = Bukkit.getItemFactory().getItemMeta(getType0()) : meta).addEnchant(ench, level, true);
    }

    /**
     * Removes the specified {@link Enchantment} if it exists on this
     * ItemStack
     *
     * @param ench Enchantment to remove
     * @return Previous level, or 0
     */
    public int removeEnchantment(Enchantment ench) {
        int level = getEnchantmentLevel(ench);
        if (level == 0 || meta == null) {
            return level;
        }
        meta.removeEnchant(ench);
        return level;
    }

    @Utility
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("type", getType().name());

        if (getDurability() != 0) {
            result.put("damage", getDurability());
        }

        if (getAmount() != 1) {
            result.put("amount", getAmount());
        }

        ItemMeta meta = getItemMeta();
        if (!Bukkit.getItemFactory().equals(meta, null)) {
            result.put("meta", meta);
        }

        return result;
    }

    /**
     * Required method for configuration serialization
     *
     * @param args map to deserialize
     * @return deserialized item stack
     * @see ConfigurationSerializable
     */
    public static ItemStack deserialize(Map<String, Object> args) {
        Material type = Material.getMaterial((String) args.get("type"));
        short damage = 0;
        int amount = 1;

        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }

        if (args.containsKey("amount")) {
            amount = ((Number) args.get("amount")).intValue();
        }

        ItemStack result = new ItemStack(type, amount, damage);

        if (args.containsKey("enchantments")) { // Backward compatiblity, @deprecated
            Object raw = args.get("enchantments");

            if (raw instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) raw;

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Enchantment enchantment = Enchantment.getByName(entry.getKey().toString());

                    if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                        result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) { // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
            Object raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                result.setItemMeta((ItemMeta) raw);
            }
        }

        return result;
    }

    /**
     * Get a copy of this ItemStack's {@link ItemMeta}.
     *
     * @return a copy of the current ItemStack's ItemData
     */
    public ItemMeta getItemMeta() {
        return this.meta == null ? Bukkit.getItemFactory().getItemMeta(getType0()) : this.meta.clone();
    }

    /**
     * Checks to see if any meta data has been defined.
     *
     * @return Returns true if some meta data has been set for this item
     */
    public boolean hasItemMeta() {
        return !Bukkit.getItemFactory().equals(meta, null);
    }

    /**
     * Set the ItemMeta of this ItemStack.
     *
     * @param itemMeta new ItemMeta, or null to indicate meta data be cleared.
     * @return True if successfully applied ItemMeta, see {@link
     *     ItemFactory#isApplicable(ItemMeta, ItemStack)}
     * @throws IllegalArgumentException if the item meta was not created by
     *     the {@link ItemFactory}
     */
    public boolean setItemMeta(ItemMeta itemMeta) {
        return setItemMeta0(itemMeta, getType0());
    }

    /*
     * Cannot be overridden, so it's safe for constructor call
     */
    private boolean setItemMeta0(ItemMeta itemMeta, Material material) {
        if (itemMeta == null) {
            this.meta = null;
            return true;
        }
        if (!Bukkit.getItemFactory().isApplicable(itemMeta, material)) {
            return false;
        }
        this.meta = Bukkit.getItemFactory().asMetaFor(itemMeta, material);
        if (this.meta == itemMeta) {
            this.meta = itemMeta.clone();
        }

        return true;
    }
}
