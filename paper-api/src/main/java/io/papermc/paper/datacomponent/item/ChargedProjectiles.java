package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds all projectiles that have been loaded into a Crossbow.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#CHARGED_PROJECTILES
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ChargedProjectiles {

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
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ChargedProjectiles> {

        /**
         * Adds a projectile to be loaded in this builder.
         *
         * @param stack projectile
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack stack);

        /**
         * Adds projectiles to be loaded in this builder.
         *
         * @param stacks projectiles
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> stacks);
    }
}
