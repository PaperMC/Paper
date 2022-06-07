package org.bukkit.craftbukkit.inventory;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.IMerchant;
import net.minecraft.world.item.trading.MerchantRecipe;
import net.minecraft.world.item.trading.MerchantRecipeList;
import net.minecraft.world.level.World;
import org.apache.commons.lang.Validate;

public class CraftMerchantCustom extends CraftMerchant {

    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
        getMerchant().craftMerchant = this;
    }

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    @Override
    public MinecraftMerchant getMerchant() {
        return (MinecraftMerchant) super.getMerchant();
    }

    public static class MinecraftMerchant implements IMerchant {

        private final IChatBaseComponent title;
        private final MerchantRecipeList trades = new MerchantRecipeList();
        private EntityHuman tradingPlayer;
        private World tradingWorld;
        protected CraftMerchant craftMerchant;

        public MinecraftMerchant(String title) {
            Validate.notNull(title, "Title cannot be null");
            this.title = IChatBaseComponent.literal(title);
        }

        @Override
        public CraftMerchant getCraftMerchant() {
            return craftMerchant;
        }

        @Override
        public void setTradingPlayer(EntityHuman entityhuman) {
            this.tradingPlayer = entityhuman;
            if (entityhuman != null) {
                this.tradingWorld = entityhuman.level;
            }
        }

        @Override
        public EntityHuman getTradingPlayer() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantRecipeList getOffers() {
            return this.trades;
        }

        @Override
        public void notifyTrade(MerchantRecipe merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.increaseUses();
        }

        @Override
        public void notifyTradeUpdated(ItemStack itemstack) {
        }

        public IChatBaseComponent getScoreboardDisplayName() {
            return title;
        }

        @Override
        public int getVillagerXp() {
            return 0; // xp
        }

        @Override
        public void overrideXp(int i) {
        }

        @Override
        public boolean showProgressBar() {
            return false; // is-regular-villager flag (hides some gui elements: xp bar, name suffix)
        }

        @Override
        public SoundEffect getNotifyTradeSound() {
            return SoundEffects.VILLAGER_YES;
        }

        @Override
        public void overrideOffers(MerchantRecipeList merchantrecipelist) {
        }

        @Override
        public boolean isClientSide() {
            return false;
        }
    }
}
