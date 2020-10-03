package net.minecraft.server;

import java.util.OptionalInt;
import javax.annotation.Nullable;

public interface IMerchant {

    void setTradingPlayer(@Nullable EntityHuman entityhuman);

    @Nullable
    EntityHuman getTrader();

    MerchantRecipeList getOffers();

    void a(MerchantRecipe merchantrecipe);

    void k(ItemStack itemstack);

    World getWorld();

    int getExperience();

    void setForcedExperience(int i);

    boolean isRegularVillager();

    SoundEffect getTradeSound();

    default boolean fa() {
        return false;
    }

    default void openTrade(EntityHuman entityhuman, IChatBaseComponent ichatbasecomponent, int i) {
        OptionalInt optionalint = entityhuman.openContainer(new TileInventory((j, playerinventory, entityhuman1) -> {
            return new ContainerMerchant(j, playerinventory, this);
        }, ichatbasecomponent));

        if (optionalint.isPresent()) {
            MerchantRecipeList merchantrecipelist = this.getOffers();

            if (!merchantrecipelist.isEmpty()) {
                entityhuman.openTrade(optionalint.getAsInt(), merchantrecipelist, i, this.getExperience(), this.isRegularVillager(), this.fa());
            }
        }

    }

    org.bukkit.craftbukkit.inventory.CraftMerchant getCraftMerchant(); // CraftBukkit
}
