package org.bukkit.craftbukkit.inventory.view;

import net.minecraft.world.inventory.ContainerMerchant;
import net.minecraft.world.item.trading.IMerchant;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.view.MerchantView;
import org.jetbrains.annotations.NotNull;

public class CraftMerchantView extends CraftInventoryView<ContainerMerchant> implements MerchantView {

    private final IMerchant trader;

    public CraftMerchantView(final HumanEntity player, final Inventory viewing, final ContainerMerchant container, final IMerchant trader) {
        super(player, viewing, container);
        this.trader = trader;
    }

    @NotNull
    @Override
    public Merchant getMerchant() {
        return this.trader.getCraftMerchant();
    }
}
