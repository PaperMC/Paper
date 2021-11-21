package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.trading.IMerchant;
import net.minecraft.world.item.trading.MerchantRecipeList;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

public class CraftMerchant implements Merchant {

    protected final IMerchant merchant;

    public CraftMerchant(IMerchant merchant) {
        this.merchant = merchant;
    }

    public IMerchant getMerchant() {
        return merchant;
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(merchant.getOffers(), new Function<net.minecraft.world.item.trading.MerchantRecipe, MerchantRecipe>() {
            @Override
            public MerchantRecipe apply(net.minecraft.world.item.trading.MerchantRecipe recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    public void setRecipes(List<MerchantRecipe> recipes) {
        MerchantRecipeList recipesList = merchant.getOffers();
        recipesList.clear();
        for (MerchantRecipe recipe : recipes) {
            recipesList.add(CraftMerchantRecipe.fromBukkit(recipe).toMinecraft());
        }
    }

    @Override
    public MerchantRecipe getRecipe(int i) {
        return merchant.getOffers().get(i).asBukkit();
    }

    @Override
    public void setRecipe(int i, MerchantRecipe merchantRecipe) {
        merchant.getOffers().set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    public int getRecipeCount() {
        return merchant.getOffers().size();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        EntityHuman eh = merchant.getTradingPlayer();
        return eh == null ? null : eh.getBukkitEntity();
    }

    @Override
    public int hashCode() {
        return merchant.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CraftMerchant && ((CraftMerchant) obj).merchant.equals(this.merchant);
    }
}
