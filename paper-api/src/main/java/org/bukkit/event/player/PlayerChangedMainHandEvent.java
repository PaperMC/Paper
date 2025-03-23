package org.bukkit.event.player;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.MainHand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player changes their main hand in the client settings.
 *
 * @apiNote Obsolete and replaced by {@link PlayerClientOptionsChangeEvent}.
 */
@ApiStatus.Obsolete
public class PlayerChangedMainHandEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final MainHand newMainHand;

    @ApiStatus.Internal
    public PlayerChangedMainHandEvent(@NotNull Player player, @NotNull MainHand newMainHand) {
        super(player);
        this.newMainHand = newMainHand;
    }

    /**
     * Gets the new main hand of the player. The old hand is still momentarily
     * available via {@link Player#getMainHand()}.
     *
     * @return the new {@link MainHand} of the player
     * @deprecated has never been functional since its implementation and simply returns the old main hand.
     * The method is left in this broken state to not break compatibility with plugins that relied on this fact.
     * Use {@link #getNewMainHand()} instead or migrate to {@link PlayerClientOptionsChangeEvent#getMainHand()}.
     */
    @NotNull
    @Deprecated(since = "1.21.4", forRemoval = true)
    public MainHand getMainHand() {
        return this.newMainHand == MainHand.LEFT ? MainHand.RIGHT : MainHand.LEFT;
    }

    /**
     * Gets the new main hand of the player.
     *
     * @return the new {@link MainHand} of the player
     */
    @NotNull
    public MainHand getNewMainHand() {
        return this.newMainHand;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
