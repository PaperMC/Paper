package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftMerchantCustom implements CraftMerchant {

    private MinecraftMerchant merchant;

    public CraftMerchantCustom(String title) {
        this.merchant = new MinecraftMerchant(title);
        this.getMerchant().craftMerchant = this;
    }

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    @Override
    public MinecraftMerchant getMerchant() {
        return this.merchant;
    }

    public static class MinecraftMerchant implements Merchant {

        private final Component title;
        private final MerchantOffers trades = new MerchantOffers();
        private Player tradingPlayer;
        protected CraftMerchant craftMerchant;

        public MinecraftMerchant(String title) {
            Preconditions.checkArgument(title != null, "Title cannot be null");
            this.title = CraftChatMessage.fromString(title)[0];
        }

        @Override
        public CraftMerchant getCraftMerchant() {
            return this.craftMerchant;
        }

        @Override
        public void setTradingPlayer(Player customer) {
            this.tradingPlayer = customer;
        }

        @Override
        public Player getTradingPlayer() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantOffers getOffers() {
            return this.trades;
        }

        @Override
        public void notifyTrade(MerchantOffer offer) {
            // increase recipe's uses
            offer.increaseUses();
        }

        @Override
        public void notifyTradeUpdated(ItemStack stack) {
        }

        public Component getScoreboardDisplayName() {
            return this.title;
        }

        @Override
        public int getVillagerXp() {
            return 0; // xp
        }

        @Override
        public void overrideXp(int experience) {
        }

        @Override
        public boolean showProgressBar() {
            return false; // is-regular-villager flag (hides some gui elements: xp bar, name suffix)
        }

        @Override
        public SoundEvent getNotifyTradeSound() {
            return SoundEvents.VILLAGER_YES;
        }

        @Override
        public void overrideOffers(MerchantOffers offers) {
        }

        @Override
        public boolean isClientSide() {
            return false;
        }

        @Override
        public boolean stillValid(Player player) {
            return this.tradingPlayer == player;
        }
    }
}
