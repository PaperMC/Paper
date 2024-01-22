package io.papermc.paper.inventory.tooltip;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Context for computing itemstack tooltips via
 * {@link org.bukkit.inventory.ItemStack#computeTooltipLines(TooltipContext, Player)}
 */
@NullMarked
public interface TooltipContext {

    /**
     * Creates a new context with the given advanced and creative
     * mode settings.
     *
     * @param advanced whether the context is for advanced tooltips
     * @param creative whether the context is for the creative inventory
     * @return a new context
     */
    @Contract("_, _ -> new")
    static TooltipContext create(final boolean advanced, final boolean creative) {
        return new TooltipContextImpl(advanced, creative);
    }

    /**
     * Creates a new context that is neither advanced nor creative.
     *
     * @return a new context
     */
    @Contract("-> new")
    static TooltipContext create() {
        return new TooltipContextImpl(false, false);
    }

    /**
     * Returns whether the context is for advanced
     * tooltips.
     * <p>
     * Advanced tooltips are shown by default
     * when a player has {@code F3+H} enabled.
     *
     * @return true if for advanced tooltips
     */
    boolean isAdvanced();

    /**
     * Returns whether the context is for the creative
     * mode inventory.
     * <p>
     * Creative tooltips are shown by default when a player is
     * in the creative inventory.
     *
     * @return true if for creative mode inventory
     */
    boolean isCreative();

    /**
     * Returns a new context with {@link #isAdvanced()}
     * set to true.
     *
     * @return a new context
     */
    @Contract("-> new")
    TooltipContext asAdvanced();

    /**
     * Returns a new context with {@link #isCreative()}
     * set to true.
     *
     * @return a new context
     */
    @Contract("-> new")
    TooltipContext asCreative();
}
