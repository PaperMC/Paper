package io.papermc.paper.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

/**
 * Called when a player trades with a standalone merchant GUI.
 */
public interface PlayerPurchaseEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the merchant that the player is trading with
     *
     * @return the merchant
     */
    Merchant getMerchant();

    /**
     * Gets the associated trade with this event
     *
     * @return the trade
     */
    MerchantRecipe getTrade();

    /**
     * Sets the trade. This is then used to determine the next prices
     *
     * @param trade the trade to use
     */
    void setTrade(MerchantRecipe trade);

    /**
     * @return will trade try to reward exp
     */
    boolean isRewardingExp();

    /**
     * Sets whether the trade will try to reward exp
     *
     * @param rewardExp try to reward exp
     */
    void setRewardExp(boolean rewardExp);

    /**
     * @return whether the trade will count as a use of the trade
     */
    boolean willIncreaseTradeUses();

    /**
     * Sets whether the trade will count as a use
     *
     * @param increaseTradeUses {@code true} to count, {@code false} otherwise
     */
    void setIncreaseTradeUses(boolean increaseTradeUses);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

}
