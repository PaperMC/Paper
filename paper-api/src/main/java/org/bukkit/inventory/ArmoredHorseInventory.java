package org.bukkit.inventory;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ArmoredHorseInventory extends AbstractHorseInventory, ArmoredSaddledMountInventory {

    @Override
    @Nullable ItemStack getArmor();

    @Override
    void setArmor(@Nullable ItemStack stack);
}
