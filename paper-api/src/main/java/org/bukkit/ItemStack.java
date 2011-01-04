
package org.bukkit;

/**
 * Represents a stack of items
 */
public class ItemStack {
    private int type;
    private int amount = 0;
    private byte damage = 0;

    public ItemStack(final int type) {
        this.type = type;
    }

    public ItemStack(final Material type) {
        this(type.getID());
    }

    public ItemStack(final int type, final int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ItemStack(final Material type, final int amount) {
        this(type.getID(), amount);
    }

    public ItemStack(final int type, final int amount, final byte damage) {
        this.type = type;
        this.amount = amount;
        this.damage = damage;
    }

    public ItemStack(final Material type, final int amount, final byte damage) {
        this(type.getID(), amount, damage);
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
     *
     * @param type New type to set the items in this stack to
     */
    public void setType(Material type) {
        this.type = type.getID();
    }

    /**
     * Gets the type ID of this item
     *
     * @return Type ID of the items in this stack
     */
    public int getTypeID() {
        return type;
    }

    /**
     * Sets the type ID of this item
     *
     * @param type New type ID to set the items in this stack to
     */
    public void setTypeID(int type) {
        this.type = type;
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
}
