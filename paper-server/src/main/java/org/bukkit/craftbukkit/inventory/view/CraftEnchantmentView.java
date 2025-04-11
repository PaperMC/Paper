package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.view.EnchantmentView;
import org.jetbrains.annotations.NotNull;

public class CraftEnchantmentView extends CraftInventoryView<EnchantmentMenu, EnchantingInventory> implements EnchantmentView {

    public CraftEnchantmentView(final HumanEntity player, final EnchantingInventory viewing, final EnchantmentMenu container) {
        super(player, viewing, container);
    }

    @Override
    public int getEnchantmentSeed() {
        return this.container.getEnchantmentSeed();
    }

    @Override
    public void setEnchantmentSeed(int seed) {
        this.container.setEnchantmentSeed(seed);
    }

    @NotNull
    @Override
    public EnchantmentOffer[] getOffers() {
        IdMap<Holder<Enchantment>> registry = CraftRegistry.getMinecraftRegistry().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
        EnchantmentOffer[] offers = new EnchantmentOffer[3];
        for (int i = 0; i < 3; i++) {
            org.bukkit.enchantments.Enchantment enchantment = (this.container.enchantClue[i] >= 0) ? CraftEnchantment.minecraftHolderToBukkit(registry.byId(this.container.enchantClue[i])) : null;
            offers[i] = (enchantment != null) ? new EnchantmentOffer(enchantment, this.container.levelClue[i], this.container.costs[i]) : null;
        }
        return offers;
    }

    @Override
    public void setOffers(@NotNull final EnchantmentOffer[] offers) {
        Preconditions.checkArgument(offers.length == 3, "There must be 3 offers given");
        IdMap<Holder<Enchantment>> registry = CraftRegistry.getMinecraftRegistry().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
        for (int i = 0; i < offers.length; i++) {
            final EnchantmentOffer offer = offers[i];
            if (offer == null) {
                this.container.enchantClue[i] = -1;
                this.container.levelClue[i] = -1;
                this.container.costs[i] = 0;
                continue;
            }

            this.container.enchantClue[i] = registry.getIdOrThrow(CraftEnchantment.bukkitToMinecraftHolder(offer.getEnchantment()));
            this.container.levelClue[i] = offer.getEnchantmentLevel();
            this.container.costs[i] = offer.getCost();
        }
    }
}
