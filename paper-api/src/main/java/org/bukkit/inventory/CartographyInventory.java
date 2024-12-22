package org.bukkit.inventory;

/**
 * Interface to the inventory of a Cartography table.
 *
 * @since 1.14
 */
public interface CartographyInventory extends Inventory {
    // Paper begin - add getResult/setResult to CartographyInventory
    /**
     * Check what item is in the result slot of this smithing table.
     *
     * @return the result item
     * @since 1.20.4
     */
    @org.jetbrains.annotations.Nullable
    default ItemStack getResult() {
        return this.getItem(2); // net.minecraft.world.inventory.CartographyTableMenu.RESULT_SLOT
    }

    /**
     * Set the item in the result slot of the smithing table
     *
     * @param newResult the new result item
     * @since 1.20.4
     */
    default void setResult(final @org.jetbrains.annotations.Nullable ItemStack newResult) {
        this.setItem(2, newResult); // net.minecraft.world.inventory.CartographyTableMenu.RESULT_SLOT
    }
    // Paper end - add getResult/setResult to CartographyInventory
}
