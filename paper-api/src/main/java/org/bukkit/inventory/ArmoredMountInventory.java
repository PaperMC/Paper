package org.bukkit.inventory;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ArmoredMountInventory extends MountInventory {

    /**
     * Gets the item in the mount's armor slot.
     *
     * @return the armor item
     */
    @Nullable ItemStack getArmor();

    /**
     * Sets the item in the mount's armor slot.
     *
     * @param stack the new item
     */
    void setArmor(@Nullable ItemStack stack);
}
