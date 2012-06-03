package org.bukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

/**
 * Represents a stack of items
 */
public class ItemStack implements Cloneable, ConfigurationSerializable {
    private int type;
    private int amount = 0;
    private MaterialData data = null;
    private short durability = 0;
    private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();

    public ItemStack(final int type) {
        this(type, 1);
    }

    public ItemStack(final Material type) {
        this(type, 1);
    }

    public ItemStack(final int type, final int amount) {
        this(type, amount, (short) 0);
    }

    public ItemStack(final Material type, final int amount) {
        this(type.getId(), amount);
    }

    public ItemStack(final int type, final int amount, final short damage) {
        this(type, amount, damage, null);
    }

    public ItemStack(final Material type, final int amount, final short damage) {
        this(type.getId(), amount, damage);
    }

    public ItemStack(final int type, final int amount, final short damage, final Byte data) {
        this.type = type;
        this.amount = amount;
        this.durability = damage;
        if (data != null) {
            createData(data);
            this.durability = data;
        }
    }

    public ItemStack(final Material type, final int amount, final short damage, final Byte data) {
        this(type.getId(), amount, damage, data);
    }

    public ItemStack(final ItemStack stack) {
        this.type = stack.type;
        this.amount = stack.amount;
        this.durability = stack.durability;
        if (stack.data != null) {
            this.data = stack.data.clone();
        }
        this.addUnsafeEnchantments(stack.getEnchantments());
    }

    /**
     * Gets the type of this item
     *
     * @return Type of the items in this stack
     */
    public Material getType() {
        return Material.getMaterial(type);
    }

    /**
     * Sets the type of this item
     * <p />
     * Note that in doing so you will reset the MaterialData for this stack
     *
     * @param type New type to set the items in this stack to
     */
    public void setType(Material type) {
        setTypeId(type.getId());
    }

    /**
     * Gets the type id of this item
     *
     * @return Type Id of the items in this stack
     */
    public int getTypeId() {
        return type;
    }

    /**
     * Sets the type id of this item
     * <p />
     * Note that in doing so you will reset the MaterialData for this stack
     *
     * @param type New type id to set the items in this stack to
     */
    public void setTypeId(int type) {
        this.type = type;
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
        Material mat = Material.getMaterial(getTypeId());
        if (mat != null && mat.getData() != null) {
            data = mat.getNewData((byte) this.durability);
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

        if ((mat == null) || (mat.getData() == null)) {
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
     * Get the maximum stacksize for the material hold in this ItemStack
     * Returns -1 if it has no idea.
     *
     * @return The maximum you can stack this material to.
     */
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
    public String toString() {
        return "ItemStack{" + getType().name() + " x " + getAmount() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemStack)) {
            return false;
        }

        ItemStack item = (ItemStack) obj;

        return item.getAmount() == getAmount() && item.getTypeId() == getTypeId() && getDurability() == item.getDurability() && getEnchantments().equals(item.getEnchantments());
    }

    @Override
    public ItemStack clone() {
        try {
            ItemStack itemStack = (ItemStack) super.clone();

            itemStack.enchantments = new HashMap<Enchantment, Integer>(this.enchantments);
            if (this.data != null) {
                itemStack.data = this.data.clone();
            }

            return itemStack;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public int hashCode() {
        int hash = 11;

        hash = hash * 19 + 7 * getTypeId(); // Overriding hashCode since equals is overridden, it's just
        hash = hash * 7 + 23 * getAmount(); // too bad these are mutable values... Q_Q
        return hash;
    }

    /**
     * Checks if this ItemStack contains the given {@link Enchantment}
     *
     * @param ench Enchantment to test
     * @return True if this has the given enchantment
     */
    public boolean containsEnchantment(Enchantment ench) {
        return enchantments.containsKey(ench);
    }

    /**
     * Gets the level of the specified enchantment on this item stack
     *
     * @param ench Enchantment to check
     * @return Level of the enchantment, or 0
     */
    public int getEnchantmentLevel(Enchantment ench) {
        return enchantments.get(ench);
    }

    /**
     * Gets a map containing all enchantments and their levels on this item.
     *
     * @return Map of enchantments.
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return ImmutableMap.copyOf(enchantments);
    }

    /**
     * Adds the specified enchantments to this item stack.
     * <p />
     * This method is the same as calling {@link #addEnchantment(org.bukkit.enchantments.Enchantment, int)}
     * for each element of the map.
     *
     * @param enchantments Enchantments to add
     */
    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p />
     * If this item stack already contained the given enchantment (at any level), it will be replaced.
     *
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     */
    public void addEnchantment(Enchantment ench, int level) {
        if ((level < ench.getStartLevel()) || (level > ench.getMaxLevel())) {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + ench.getStartLevel() + " to " + ench.getMaxLevel());
        } else if (!ench.canEnchantItem(this)) {
            throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
        }

        addUnsafeEnchantment(ench, level);
    }

    /**
     * Adds the specified enchantments to this item stack in an unsafe manner.
     * <p />
     * This method is the same as calling {@link #addUnsafeEnchantment(org.bukkit.enchantments.Enchantment, int)}
     * for each element of the map.
     *
     * @param enchantments Enchantments to add
     */
    public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p />
     * If this item stack already contained the given enchantment (at any level), it will be replaced.
     * <p />
     * This method is unsafe and will ignore level restrictions or item type. Use at your own
     * discretion.
     *
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     */
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        enchantments.put(ench, level);
    }

    /**
     * Removes the specified {@link Enchantment} if it exists on this item stack
     *
     * @param ench Enchantment to remove
     * @return Previous level, or 0
     */
    public int removeEnchantment(Enchantment ench) {
        Integer previous = enchantments.remove(ench);
        return (previous == null) ? 0 : previous;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("type", getType().name());

        if (durability != 0) {
            result.put("damage", durability);
        }

        if (amount != 1) {
            result.put("amount", amount);
        }

        Map<Enchantment, Integer> enchants = getEnchantments();

        if (enchants.size() > 0) {
            Map<String, Integer> safeEnchants = new HashMap<String, Integer>();

            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                safeEnchants.put(entry.getKey().getName(), entry.getValue());
            }

            result.put("enchantments", safeEnchants);
        }

        return result;
    }

    public static ItemStack deserialize(Map<String, Object> args) {
        Material type = Material.getMaterial((String) args.get("type"));
        short damage = 0;
        int amount = 1;

        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }

        if (args.containsKey("amount")) {
            amount = (Integer) args.get("amount");
        }

        ItemStack result = new ItemStack(type, amount, damage);

        if (args.containsKey("enchantments")) {
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
        }

        return result;
    }
}
