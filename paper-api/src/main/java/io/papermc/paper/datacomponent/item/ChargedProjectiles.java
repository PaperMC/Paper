package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * Holds all projectiles that have been loaded into a Crossbow.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CHARGED_PROJECTILES
 */
@ApiStatus.NonExtendable
public interface ChargedProjectiles extends BuildableDataComponent<ChargedProjectiles, ChargedProjectiles.Builder> {

    @Contract(value = "_ -> new", pure = true)
    static ChargedProjectiles chargedProjectiles(final List<ItemStack> projectiles) {
        return chargedProjectiles().addAll(projectiles).build();
    }

    @Contract(value = "-> new", pure = true)
    static ChargedProjectiles.Builder chargedProjectiles() {
        return ItemComponentTypesBridge.bridge().chargedProjectiles();
    }

    /**
     * Lists the projectiles that are currently loaded into this component.
     *
     * @return the loaded projectiles
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStack> projectiles();

    /**
     * Builder for {@link ChargedProjectiles}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ChargedProjectiles> {

        /**
         * Adds a projectile to be loaded in this builder.
         *
         * @param projectile projectile
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack projectile);

        /**
         * Adds projectiles to be loaded in this builder.
         *
         * @param projectiles projectiles
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> projectiles);

        /**
         * Sets the projectiles to be loaded in this builder.
         *
         * @param projectiles projectiles
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder projectiles(List<ItemStack> projectiles);
    }
}
