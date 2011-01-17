
package org.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * Represents a stack of items
 */
public class ItemStack {
    private int type;
    private int amount = 0;
    private MaterialData data = null;
    private byte damage = 0;

    public ItemStack(final int type) {
        this(type, 0);
    }

    public ItemStack(final Material type) {
        this(type, 0);
    }

    public ItemStack(final int type, final int amount) {
        this(type, amount, (byte) 0);
    }

    public ItemStack(final Material type, final int amount) {
        this(type.getId(), amount);
    }

    public ItemStack(final int type, final int amount, final byte damage) {
        this(type, amount, damage, null);
    }

    public ItemStack(final Material type, final int amount, final byte damage) {
        this(type.getId(), amount, damage);
    }

    public ItemStack(final int type, final int amount, final byte damage, final Byte data) {
        this.type = type;
        this.amount = amount;
        this.damage = damage;
        if (data != null) {
            createData(data);
            this.damage = data;
        }
    }

    public ItemStack(final Material type, final int amount, final byte damage, final Byte data) {
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
        createData((byte)0);
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
        return data;
    }

    /**
     * Sets the MaterialData for this stack of items
     *
     * @param amount New MaterialData for this item
     */
    public void setData(MaterialData data) {
        Material mat = getType();

        if ((mat == null) || (mat.getData() == null)) {
            this.data = data;
        } else {
            if ((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class)) {
                this.data = data;
            } else {
                throw new IllegalArgumentException("Provided data is not of type "
                        + mat.getData().getName() + ", found " + data.getClass().getName());
            }
        }
    }

    /**
     * Sets the damage of this item<br /><br />
     *
     * 0x00 represents an item which cannot be damaged<br />
     * 0x01 represents an item at maximum health<br />
     * 0x32 represents an item with no health left
     *
     * @param damage Damage of this item
     */
    public void setDamage(final byte damage) {
        this.damage = damage;
    }

    /**
     * Gets the damage of this item<br /><br />
     *
     * 0x00 represents an item which cannot be damaged<br />
     * 0x01 represents an item at maximum health<br />
     * 0x32 represents an item with no health left
     *
     * @return Damage of this item
     */
    public byte getDamage() {
        return damage;
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
        return "ItemStack{"+getType().name()+" x "+getAmount()+"}";
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }

    public boolean equals(ItemStack item) {
        return item.getAmount() == getAmount() && item.getTypeId() == getTypeId();
    }
}
