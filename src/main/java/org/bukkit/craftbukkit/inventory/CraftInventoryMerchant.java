package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.IMerchant;
import net.minecraft.server.InventoryMerchant;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {

    private final IMerchant merchant;

    public CraftInventoryMerchant(IMerchant merchant, InventoryMerchant inventory) {
        super(inventory);
        this.merchant = merchant;
    }

    @Override
    public int getSelectedRecipeIndex() {
        return getInventory().selectedIndex;
    }

    @Override
    public MerchantRecipe getSelectedRecipe() {
        net.minecraft.server.MerchantRecipe nmsRecipe = getInventory().getRecipe();
        return (nmsRecipe == null) ? null : nmsRecipe.asBukkit();
    }

    @Override
    public InventoryMerchant getInventory() {
        return (InventoryMerchant) inventory;
    }

    @Override
    public Merchant getMerchant() {
        return merchant.getCraftMerchant();
    }
}
