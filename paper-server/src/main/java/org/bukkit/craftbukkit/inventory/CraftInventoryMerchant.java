package org.bukkit.craftbukkit.inventory;

import net.minecraft.server.InventoryMerchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {

    public CraftInventoryMerchant(InventoryMerchant merchant) {
        super(merchant);
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
}
