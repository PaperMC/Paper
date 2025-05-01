package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftMerchantCustom implements CraftMerchant {

    private MinecraftMerchant merchant;

    @Deprecated // Paper - Adventure
    public CraftMerchantCustom(String title) {
        this.merchant = new MinecraftMerchant(title);
        this.getMerchant().craftMerchant = this;
    }
    // Paper start
    public CraftMerchantCustom(net.kyori.adventure.text.Component title) {
        this.merchant = new MinecraftMerchant(title);
        getMerchant().craftMerchant = this;
    }

    public CraftMerchantCustom() {
        this.merchant = new MinecraftMerchant();
        getMerchant().craftMerchant = this;
    }
    // Paper end

    @Override
    public MinecraftMerchant getMerchant() {
        return this.merchant;
    }

    public static class MinecraftMerchant implements Merchant {

        private final Component title;
        private final MerchantOffers trades = new MerchantOffers();
        private Player tradingPlayer;
        protected CraftMerchant craftMerchant;

        @Deprecated // Paper - Adventure
        public MinecraftMerchant(String title) {
            Preconditions.checkArgument(title != null, "Title cannot be null");
            this.title = CraftChatMessage.fromString(title)[0];
        }
        // Paper start
        public MinecraftMerchant(net.kyori.adventure.text.Component title) {
            Preconditions.checkArgument(title != null, "Title cannot be null");
            this.title = io.papermc.paper.adventure.PaperAdventure.asVanilla(title);
        }

        public MinecraftMerchant() {
            this.title = EntityType.VILLAGER.getDescription();
        }
        // Paper end

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

        // Paper start - Add PlayerTradeEvent and PlayerPurchaseEvent
        @Override
        public void processTrade(MerchantOffer offer, @javax.annotation.Nullable io.papermc.paper.event.player.PlayerPurchaseEvent event) { // The MerchantRecipe passed in here is the one set by the PlayerPurchaseEvent
            /* Based on {@link net.minecraft.world.entity.npc.AbstractVillager#processTrade(MerchantOffer, io.papermc.paper.event.player.PlayerPurchaseEvent)} */
            if (getTradingPlayer() instanceof net.minecraft.server.level.ServerPlayer) {
                if (event == null || event.willIncreaseTradeUses()) {
                    offer.increaseUses();
                }
                if (event == null || event.isRewardingExp()) {
                    this.tradingPlayer.level().addFreshEntity(new net.minecraft.world.entity.ExperienceOrb(this.tradingPlayer.level(), this.tradingPlayer.getX(), this.tradingPlayer.getY(), this.tradingPlayer.getZ(), offer.getXp(), org.bukkit.entity.ExperienceOrb.SpawnReason.VILLAGER_TRADE, this.tradingPlayer, null));
                }
            }
            this.notifyTrade(offer);
        }
        // Paper end - Add PlayerTradeEvent and PlayerPurchaseEvent
        @Override
        public void notifyTrade(MerchantOffer offer) {
            // increase recipe's uses
            // offer.increaseUses(); // Paper - Add PlayerTradeEvent and PlayerPurchaseEvent; handled above in processTrade
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
