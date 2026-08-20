package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

public class PaperPlayerPurchaseEvent extends CraftPlayerEvent implements PlayerPurchaseEvent {

    private final Merchant merchant;
    private boolean rewardExp;
    private boolean increaseTradeUses;
    private MerchantRecipe trade;

    private boolean cancelled;

    public PaperPlayerPurchaseEvent(final Player player, final Merchant merchant, final MerchantRecipe trade, final boolean rewardExp, final boolean increaseTradeUses) {
        super(player);
        this.merchant = merchant;
        this.trade = trade;
        this.rewardExp = rewardExp;
        this.increaseTradeUses = increaseTradeUses;
    }

    @Override
    public Merchant getMerchant() {
        return this.merchant;
    }

    @Override
    public MerchantRecipe getTrade() {
        return this.trade;
    }

    @Override
    public void setTrade(final MerchantRecipe trade) {
        Preconditions.checkArgument(trade != null, "Trade cannot be null!");
        this.trade = trade;
    }

    @Override
    public boolean isRewardingExp() {
        return this.rewardExp;
    }

    @Override
    public void setRewardExp(final boolean rewardExp) {
        this.rewardExp = rewardExp;
    }

    @Override
    public boolean willIncreaseTradeUses() {
        return this.increaseTradeUses;
    }

    @Override
    public void setIncreaseTradeUses(final boolean increaseTradeUses) {
        this.increaseTradeUses = increaseTradeUses;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPurchaseEvent.getHandlerList();
    }
}
