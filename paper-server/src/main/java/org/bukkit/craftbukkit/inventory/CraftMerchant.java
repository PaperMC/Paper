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

public interface CraftMerchant extends Merchant {

    IMerchant getMerchant();

    @Override
    default List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(getMerchant().getOffers(), new Function<net.minecraft.world.item.trading.MerchantRecipe, MerchantRecipe>() {
            @Override
            public MerchantRecipe apply(net.minecraft.world.item.trading.MerchantRecipe recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    default void setRecipes(List<MerchantRecipe> recipes) {
        MerchantRecipeList recipesList = getMerchant().getOffers();
        recipesList.clear();
        for (MerchantRecipe recipe : recipes) {
            recipesList.add(CraftMerchantRecipe.fromBukkit(recipe).toMinecraft());
        }
    }

    @Override
    default MerchantRecipe getRecipe(int i) {
        return getMerchant().getOffers().get(i).asBukkit();
    }

    @Override
    default void setRecipe(int i, MerchantRecipe merchantRecipe) {
        getMerchant().getOffers().set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    default int getRecipeCount() {
        return getMerchant().getOffers().size();
    }

    @Override
    default boolean isTrading() {
        return getTrader() != null;
    }

    @Override
    default HumanEntity getTrader() {
        EntityHuman eh = getMerchant().getTradingPlayer();
        return eh == null ? null : eh.getBukkitEntity();
    }
}
