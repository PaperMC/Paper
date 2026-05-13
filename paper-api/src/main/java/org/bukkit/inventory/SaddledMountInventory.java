package org.bukkit.inventory;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface SaddledMountInventory extends MountInventory {

    /**
     * Gets the item in the mount's saddle slot.
     *
     * @return the saddle item
     */
    @Nullable ItemStack getSaddle();

    /**
     * Sets the item in the mount's saddle slot.
     *
     * @param stack the new item
     */
    void setSaddle(@Nullable ItemStack stack);
}
