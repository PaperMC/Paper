package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.List;
import org.bukkit.block.banner.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the layers of patterns on a banner.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BANNER_PATTERNS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BannerPatternLayers {

    @Contract(value = "_ -> new", pure = true)
    static BannerPatternLayers bannerPatternLayers(final List<Pattern> patterns) {
        return bannerPatternLayers().addAll(patterns).build();
    }

    @Contract(value = "-> new", pure = true)
    static BannerPatternLayers.Builder bannerPatternLayers() {
        return ItemComponentTypesBridge.bridge().bannerPatternLayers();
    }

    /**
     * Gets the patterns on the banner.
     *
     * @return the patterns
     */
    @Contract(pure = true)
    @Unmodifiable List<Pattern> patterns();

    /**
     * Builder for {@link BannerPatternLayers}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BannerPatternLayers> {

        /**
         * Adds a pattern to the banner.
         *
         * @param pattern the pattern
         * @return the builder for chaining
         * @see #patterns()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder add(Pattern pattern);

        /**
         * Adds multiple patterns to the banner.
         *
         * @param patterns the patterns
         * @return the builder for chaining
         * @see #patterns()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addAll(List<Pattern> patterns);
    }
}
