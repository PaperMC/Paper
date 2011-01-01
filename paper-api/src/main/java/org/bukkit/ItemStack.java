
package org.bukkit;

/**
 * Represents a stack of items
 */
public class ItemStack {
    private int type;
    private int amount = 0;

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
}
