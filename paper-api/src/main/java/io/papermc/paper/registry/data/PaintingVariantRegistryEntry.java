package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Art;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link Art} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface PaintingVariantRegistryEntry {

    /**
     * Provides the width of this painting in blocks.
     *
     * @return the width.
     * @see Art#getBlockWidth()
     */
    @Range(from = 1, to = 16) int width();

    /**
     * Provides the height of this painting in blocks.
     *
     * @return the height.
     * @see Art#getBlockHeight()
     */
    @Range(from = 1, to = 16) int height();

    /**
     * Provides the title of the painting visible in the creative inventory.
     *
     * @return the title.
     * @see Art#title()
     */
    @Nullable Component title();

    /**
     * Provides the author of the painting visible in the creative inventory.
     *
     * @return the author.
     * @see Art#author()
     */
    @Nullable Component author();

    /**
     * Provides the asset id of the painting, which is the location of the sprite to use.
     *
     * @return the asset id.
     * @see Art#assetId()
     */
    Key assetId();

    /**
     * A mutable builder for the {@link PaintingVariantRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #width(int)}</li>
     *     <li>{@link #height(int)}</li>
     *     <li>{@link #assetId(Key)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends PaintingVariantRegistryEntry, RegistryBuilder<Art> {

        /**
         * Sets the width of the painting in blocks.
         *
         * @param width the width in blocks.
         * @return this builder instance.
         * @see PaintingVariantRegistryEntry#width()
         * @see Art#getBlockWidth()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder width(@Range(from = 1, to = 16) int width);

        /**
         * Sets the height of the painting in blocks.
         *
         * @param height the height in blocks.
         * @return this builder instance.
         * @see PaintingVariantRegistryEntry#height()
         * @see Art#getBlockHeight()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder height(@Range(from = 1, to = 16) int height);

        /**
         * Sets the title of the painting.
         *
         * @param title the title.
         * @return this builder instance.
         * @see PaintingVariantRegistryEntry#title()
         * @see Art#title()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder title(@Nullable Component title);

        /**
         * Sets the author of the painting.
         *
         * @param author the author.
         * @return this builder instance.
         * @see PaintingVariantRegistryEntry#author()
         * @see Art#author()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder author(@Nullable Component author);

        /**
         * Sets the asset id of the painting, which is the location of the sprite to use.
         *
         * @param assetId the asset id.
         * @return this builder instance.
         * @see PaintingVariantRegistryEntry#assetId()
         * @see Art#assetId()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder assetId(Key assetId);
    }
}
