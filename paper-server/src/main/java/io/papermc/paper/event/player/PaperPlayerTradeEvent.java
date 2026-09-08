package io.papermc.paper.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.trading.MerchantOffer;
import org.bukkit.entity.AbstractVillager;

public class PaperPlayerTradeEvent extends PaperPlayerPurchaseEvent implements PlayerTradeEvent {

    public PaperPlayerTradeEvent(
        final ServerPlayer player,
        final net.minecraft.world.entity.npc.villager.AbstractVillager villager,
        final MerchantOffer offer,
        final boolean rewardExp,
        final boolean increaseTradeUses
    ) {
        super(player, villager, offer, rewardExp, increaseTradeUses);
    }

    @Override
    public AbstractVillager getMerchant() {
        return (AbstractVillager) super.getMerchant();
    }
}
