package io.papermc.paper.event.player;

import org.bukkit.entity.AbstractVillager;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player trades with a villager or wandering trader
 */
@NullMarked
public interface PlayerTradeEvent extends PlayerPurchaseEvent {

    @Override
    AbstractVillager getMerchant();

    /**
     * Gets the Villager or Wandering trader associated with this event
     *
     * @return the villager or wandering trader
     * @see #getMerchant()
     */
    @ApiStatus.Obsolete(since = "1.21.11")
    AbstractVillager getVillager();

}
