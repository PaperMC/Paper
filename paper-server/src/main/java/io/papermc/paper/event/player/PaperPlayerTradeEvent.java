package io.papermc.paper.event.player;

import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.ApiStatus;

public class PaperPlayerTradeEvent extends PaperPlayerPurchaseEvent implements PlayerTradeEvent {

    public PaperPlayerTradeEvent(final Player player, final AbstractVillager villager, final MerchantRecipe trade, final boolean rewardExp, final boolean increaseTradeUses) {
        super(player, villager, trade, rewardExp, increaseTradeUses);
    }

    @Override
    public AbstractVillager getMerchant() {
        return (AbstractVillager) super.getMerchant();
    }

    @ApiStatus.Obsolete
    public AbstractVillager getVillager() {
        return this.getMerchant();
    }
}
