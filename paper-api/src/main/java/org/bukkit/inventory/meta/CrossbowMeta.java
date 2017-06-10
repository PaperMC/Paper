package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CrossbowMeta extends ItemMeta {

    /**
     * Returns whether the item has any charged projectiles.
     *
     * @return whether charged projectiles are present
     */
    boolean hasChargedProjectiles();

    /**
     * Returns an immutable list of the projectiles charged on this item.
     *
     * @return charged projectiles
     */
    @NotNull
    List<ItemStack> getChargedProjectiles();

    /**
     * Sets the projectiles charged on this item.
     *
     * Removes all projectiles when given null.
     *
     * @param projectiles the projectiles to set
     * @throws IllegalArgumentException if one of the projectiles is empty
     */
    void setChargedProjectiles(@Nullable List<ItemStack> projectiles);

    /**
     * Adds a charged projectile to this item.
     *
     * @param item projectile
     * @throws IllegalArgumentException if the projectile is empty
     */
    void addChargedProjectile(@NotNull ItemStack item);
}
