package org.bukkit.inventory;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.material.MaterialData;

/**
 * Represents a stack of items
 */
public class ItemStack implements Serializable, ConfigurationSerializable {
    private int type;
    private int amount = 0;
    private MaterialData data = null;
    private short durability = 0;

    public ItemStack(final int type) {
        this(type, 0);
    }

    public ItemStack(final Material type) {
        this(type, 0);
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

    /**
     * Gets the type of this item
     *
     * @return Type of the items in this stack
     */
    public Material getType() {
        return Material.getMaterial(type);
    }

    /**
     * Sets the type of this item<br />
     * <br />
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
     * Sets the type id of this item<br />
     * <br />
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
        if (Material.getMaterial(getTypeId()).getData() != null) {
            data = Material.getMaterial(getTypeId()).getNewData((byte) this.durability);
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

        return item.getAmount() == getAmount() && item.getTypeId() == getTypeId();
    }

    @Override
    public ItemStack clone() {
        return new ItemStack(type, amount, durability);
    }

    @Override
    public int hashCode() {
        int hash = 11;

        hash = hash * 19 + 7 * getTypeId(); // Overriding hashCode since equals is overridden, it's just
        hash = hash * 7 + 23 * getAmount(); // too bad these are mutable values... Q_Q
        return hash;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        
        result.put("type", getType());
        
        if (durability != 0) {
            result.put("damage", durability);
        }
        
        if (amount != 1) {
            result.put("amount", amount);
        }
        
        return result;
    }
    
    public static ItemStack deserialize(Map<String, Object> args) {
        Material type = Material.getMaterial((String)args.get("type"));
        short damage = 0;
        int amount = 1;
        
        if (args.containsKey("damage")) {
            damage = (Short)args.get("damage");
        }
        
        if (args.containsKey("amount")) {
            amount = (Integer)args.get("amount");
        }
        
        return new ItemStack(type, amount, damage);
    }
}
