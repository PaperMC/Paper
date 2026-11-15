package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.item.ItemStackTemplate;
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
public interface ChargedProjectiles {

    @Contract(value = "_ -> new", pure = true)
    static ChargedProjectiles chargedProjectiles(final List<ItemStack> projectiles) {
        return chargedProjectiles().addAll(projectiles).build();
    }

    @Contract(value = "_ -> new", pure = true)
    static ChargedProjectiles chargedProjectilesFromTemplates(final List<ItemStackTemplate> projectiles) {
        return chargedProjectiles().addAllTemplates(projectiles).build();
    }

    @Contract(value = "-> new", pure = true)
    static ChargedProjectiles.Builder chargedProjectiles() {
        return ItemComponentTypesBridge.bridge().chargedProjectiles();
    }

    /**
     * Lists the projectiles that are currently loaded into this component.
     *
     * @return the loaded projectiles
     * @see #projectileTemplates()
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStack> projectiles();

    /**
     * Lists the projectiles that are currently loaded into this component.
     * <p>
     * Unlike {@link #projectiles()}, this is available while registries are
     * still being loaded, such as in registry events.
     *
     * @return the loaded projectiles
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable List<ItemStackTemplate> projectileTemplates();

    /**
     * Builder for {@link ChargedProjectiles}.
     */
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ChargedProjectiles> {

        /**
         * Adds a projectile to be loaded in this builder.
         *
         * @param stack projectile, cannot be {@link ItemStack#isEmpty() empty}
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStack stack);

        /**
         * Adds a projectile to be loaded in this builder.
         *
         * @param template projectile
         * @return the builder for chaining
         * @see #projectileTemplates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(ItemStackTemplate template);

        /**
         * Adds projectiles to be loaded in this builder.
         *
         * @param stacks projectiles, none can be {@link ItemStack#isEmpty() empty}
         * @return the builder for chaining
         * @see #projectiles()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<ItemStack> stacks);

        /**
         * Adds projectiles to be loaded in this builder.
         *
         * @param templates projectiles
         * @return the builder for chaining
         * @see #projectileTemplates()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAllTemplates(List<ItemStackTemplate> templates);
    }
}
