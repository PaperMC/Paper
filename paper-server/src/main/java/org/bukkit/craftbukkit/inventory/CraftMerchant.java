package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

public interface CraftMerchant extends Merchant {

    net.minecraft.world.item.trading.Merchant getMerchant();

    @Override
    default List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(this.getMerchant().getOffers(), new Function<net.minecraft.world.item.trading.MerchantOffer, MerchantRecipe>() {
            @Override
            public MerchantRecipe apply(net.minecraft.world.item.trading.MerchantOffer recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    default void setRecipes(List<MerchantRecipe> recipes) {
        MerchantOffers recipesList = this.getMerchant().getOffers();
        recipesList.clear();
        for (MerchantRecipe recipe : recipes) {
            recipesList.add(CraftMerchantRecipe.fromBukkit(recipe).toMinecraft());
        }
    }

    @Override
    default MerchantRecipe getRecipe(int i) {
        return this.getMerchant().getOffers().get(i).asBukkit();
    }

    @Override
    default void setRecipe(int i, MerchantRecipe merchantRecipe) {
        this.getMerchant().getOffers().set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    default int getRecipeCount() {
        return this.getMerchant().getOffers().size();
    }

    @Override
    default boolean isTrading() {
        return this.getTrader() != null;
    }

    @Override
    default HumanEntity getTrader() {
        Player eh = this.getMerchant().getTradingPlayer();
        return eh == null ? null : eh.getBukkitEntity();
    }
}
