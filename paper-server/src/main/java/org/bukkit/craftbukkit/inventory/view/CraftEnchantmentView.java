package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.ContainerEnchantTable;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.EnchantmentView;
import org.jetbrains.annotations.NotNull;

public class CraftEnchantmentView extends CraftInventoryView<ContainerEnchantTable> implements EnchantmentView {

    public CraftEnchantmentView(final HumanEntity player, final Inventory viewing, final ContainerEnchantTable container) {
        super(player, viewing, container);
    }

    @Override
    public int getEnchantmentSeed() {
        return container.getEnchantmentSeed();
    }

    @NotNull
    @Override
    public EnchantmentOffer[] getOffers() {
        Registry<Holder<Enchantment>> registry = CraftRegistry.getMinecraftRegistry().registryOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
        EnchantmentOffer[] offers = new EnchantmentOffer[3];
        for (int i = 0; i < 3; i++) {
            org.bukkit.enchantments.Enchantment enchantment = (container.enchantClue[i] >= 0) ? CraftEnchantment.minecraftHolderToBukkit(registry.byId(container.enchantClue[i])) : null;
            offers[i] = (enchantment != null) ? new EnchantmentOffer(enchantment, container.levelClue[i], container.costs[i]) : null;
        }
        return offers;
    }

    @Override
    public void setOffers(@NotNull final EnchantmentOffer[] offers) {
        Preconditions.checkArgument(offers.length != 3, "There must be 3 offers given");
        Registry<Holder<Enchantment>> registry = CraftRegistry.getMinecraftRegistry().registryOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
        for (int i = 0; i < offers.length; i++) {
            final EnchantmentOffer offer = offers[i];
            if (offer == null) {
                container.enchantClue[i] = -1;
                container.levelClue[i] = -1;
                container.costs[i] = 0;
                continue;
            }

            container.enchantClue[i] = registry.getIdOrThrow(CraftEnchantment.bukkitToMinecraftHolder(offer.getEnchantment()));
            container.levelClue[i] = offer.getEnchantmentLevel();
            container.costs[i] = offer.getCost();
        }
    }
}
