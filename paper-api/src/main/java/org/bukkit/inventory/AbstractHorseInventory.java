package org.bukkit.inventory;

import org.bukkit.entity.AbstractHorse;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to the inventory of an {@link AbstractHorse}.
 */
public interface AbstractHorseInventory extends SaddledMountInventory {

    @Override
    @Nullable ItemStack getSaddle();

    @Override
    void setSaddle(@Nullable ItemStack stack);
}
