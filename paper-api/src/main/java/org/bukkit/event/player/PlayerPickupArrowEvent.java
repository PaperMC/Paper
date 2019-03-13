package org.bukkit.event.player;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a player picks up an arrow from the ground.
 */
public class PlayerPickupArrowEvent extends PlayerPickupItemEvent {

    private final Arrow arrow;

    public PlayerPickupArrowEvent(@NotNull final Player player, @NotNull final Item item, @NotNull final Arrow arrow) {
        super(player, item, 0);
        this.arrow = arrow;
    }

    /**
     * Get the arrow being picked up by the player
     *
     * @return The arrow being picked up
     */
    @NotNull
    public Arrow getArrow() {
        return arrow;
    }
}
