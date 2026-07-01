package io.papermc.paper.event.player;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player trades with a villager or wandering trader
 */
@NullMarked
public class PlayerTradeEvent extends PlayerPurchaseEvent {

    @ApiStatus.Internal
    public PlayerTradeEvent(final Player player, final AbstractVillager villager, final MerchantRecipe trade, final boolean rewardExp, final boolean increaseTradeUses) {
        super(player, villager, trade, rewardExp, increaseTradeUses);
    }

    @Override
    public AbstractVillager getMerchant() {
        return (AbstractVillager) super.getMerchant();
    }

    /**
     * Gets the Villager or Wandering trader associated with this event
     *
     * @return the villager or wandering trader
     * @see #getMerchant()
     */
    @ApiStatus.Obsolete
    public AbstractVillager getVillager() {
        return getMerchant();
    }

}
