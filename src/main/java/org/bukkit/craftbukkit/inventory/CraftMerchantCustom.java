package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IMerchant;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;
import net.minecraft.server.World;

public class CraftMerchantCustom extends CraftMerchant {

    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
    }

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    private static class MinecraftMerchant implements IMerchant {

        private final String title;
        private final MerchantRecipeList trades = new MerchantRecipeList();
        private EntityHuman tradingPlayer;

        public MinecraftMerchant(String title) {
            this.title = title;
        }

        @Override
        public void setTradingPlayer(EntityHuman entityhuman) {
            this.tradingPlayer = entityhuman;
        }

        @Override
        public EntityHuman getTrader() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantRecipeList getOffers(EntityHuman entityhuman) {
            return this.trades;
        }

        @Override
        public void a(MerchantRecipe merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.increaseUses();
        }

        @Override
        public void a(ItemStack itemstack) {
        }

        @Override
        public IChatBaseComponent getScoreboardDisplayName() {
            return new ChatComponentText(title);
        }

        @Override
        public World u_() {
            return null;
        }

        @Override
        public BlockPosition v_() {
            return null;
        }
    }
}
