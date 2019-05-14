package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IMerchant;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;
import net.minecraft.server.World;
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
            this.title = new ChatComponentText(title);
        }

        @Override
        public CraftMerchant getCraftMerchant() {
            return craftMerchant;
        }

        @Override
        public void setTradingPlayer(EntityHuman entityhuman) {
            this.tradingPlayer = entityhuman;
            if (entityhuman != null) {
                this.tradingWorld = entityhuman.world;
            }
        }

        @Override
        public EntityHuman getTrader() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantRecipeList getOffers() {
            return this.trades;
        }

        @Override
        public void a(MerchantRecipe merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.increaseUses();
        }

        @Override
        public void i(ItemStack itemstack) {
        }

        public IChatBaseComponent getScoreboardDisplayName() {
            return title;
        }

        @Override
        public World getWorld() {
            return this.tradingWorld;
        }

        @Override
        public int getExperience() {
            return 0; // xp
        }

        @Override
        public void r(int i) {
        }

        @Override
        public boolean ea() {
            return true;
        }
    }
}
