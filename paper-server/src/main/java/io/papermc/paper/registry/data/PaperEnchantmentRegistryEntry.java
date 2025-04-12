package io.papermc.paper.registry.data;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Checks;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asArgumentMin;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperEnchantmentRegistryEntry implements EnchantmentRegistryEntry {

    // Top level
    protected @Nullable Component description;

    // Definition
    protected @Nullable HolderSet<Item> supportedItems;
    protected @Nullable HolderSet<Item> primaryItems;
    protected OptionalInt weight = OptionalInt.empty();
    protected OptionalInt maxLevel = OptionalInt.empty();
    protected Enchantment.@Nullable Cost minimumCost;
    protected Enchantment.@Nullable Cost maximumCost;
    protected OptionalInt anvilCost = OptionalInt.empty();
    protected @Nullable List<EquipmentSlotGroup> activeSlots;

    // Exclusive
    protected HolderSet<Enchantment> exclusiveWith = HolderSet.empty(); // Paper added default to empty.

    // Effects
    protected DataComponentMap effects;

    protected final Conversions conversions;

    public PaperEnchantmentRegistryEntry(
        final Conversions conversions,
        final @Nullable Enchantment internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.effects = DataComponentMap.EMPTY;
            return;
        }

        // top level
        this.description = internal.description();

        // definition
        final Enchantment.EnchantmentDefinition definition = internal.definition();
        this.supportedItems = definition.supportedItems();
        this.primaryItems = definition.primaryItems().orElse(null);
        this.weight = OptionalInt.of(definition.weight());
        this.maxLevel = OptionalInt.of(definition.maxLevel());
        this.minimumCost = definition.minCost();
        this.maximumCost = definition.maxCost();
        this.anvilCost = OptionalInt.of(definition.anvilCost());
        this.activeSlots = definition.slots();

        // exclusive
        this.exclusiveWith = internal.exclusiveSet();

        // effects
        this.effects = internal.effects();
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return this.conversions.asAdventure(asConfigured(this.description, "description"));
    }

    @Override
    public RegistryKeySet<ItemType> supportedItems() {
        return PaperRegistrySets.convertToApi(RegistryKey.ITEM, asConfigured(this.supportedItems, "supportedItems"));
    }

    @Override
    public @Nullable RegistryKeySet<ItemType> primaryItems() {
        return this.primaryItems == null ? null : PaperRegistrySets.convertToApi(RegistryKey.ITEM, this.primaryItems);
    }

    @Override
    public @Range(from = 1, to = 1024) int weight() {
        return asConfigured(this.weight, "weight");
    }

    @Override
    public @Range(from = 1, to = 255) int maxLevel() {
        return asConfigured(this.maxLevel, "maxLevel");
    }

    @Override
    public EnchantmentCost minimumCost() {
        final Enchantment.Cost cost = asConfigured(this.minimumCost, "minimumCost");
        return EnchantmentRegistryEntry.EnchantmentCost.of(cost.base(), cost.perLevelAboveFirst());
    }

    @Override
    public EnchantmentCost maximumCost() {
        final Enchantment.Cost cost = asConfigured(this.maximumCost, "maximumCost");
        return EnchantmentRegistryEntry.EnchantmentCost.of(cost.base(), cost.perLevelAboveFirst());
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int anvilCost() {
        return asConfigured(this.anvilCost, "anvilCost");
    }

    @Override
    public List<org.bukkit.inventory.EquipmentSlotGroup> activeSlots() {
        return Collections.unmodifiableList(Lists.transform(asConfigured(this.activeSlots, "activeSlots"), CraftEquipmentSlot::getSlotGroup));
    }

    @Override
    public RegistryKeySet<org.bukkit.enchantments.Enchantment> exclusiveWith() {
        return PaperRegistrySets.convertToApi(RegistryKey.ENCHANTMENT, this.exclusiveWith);
    }

    public static final class PaperBuilder extends PaperEnchantmentRegistryEntry implements EnchantmentRegistryEntry.Builder,
        PaperRegistryBuilder<Enchantment, org.bukkit.enchantments.Enchantment> {

        public PaperBuilder(final Conversions conversions, final @Nullable Enchantment internal) {
            super(conversions, internal);
        }

        @Override
        public Builder description(final net.kyori.adventure.text.Component description) {
            this.description = this.conversions.asVanilla(asArgument(description, "description"));
            return this;
        }

        @Override
        public Builder supportedItems(final RegistryKeySet<ItemType> supportedItems) {
            this.supportedItems = PaperRegistrySets.convertToNms(Registries.ITEM, this.conversions.lookup(), asArgument(supportedItems, "supportedItems"));
            return this;
        }

        @Override
        public Builder primaryItems(final @Nullable RegistryKeySet<ItemType> primaryItems) {
            this.primaryItems = primaryItems == null ? null : PaperRegistrySets.convertToNms(Registries.ITEM, this.conversions.lookup(), primaryItems);
            return this;
        }

        @Override
        public Builder weight(final @Range(from = 1, to = 1024) int weight) {
            this.weight = OptionalInt.of(Checks.asArgumentRange(weight, "weight", 1, 1024));
            return this;
        }

        @Override
        public Builder maxLevel(final @Range(from = 1, to = 255) int maxLevel) {
            this.maxLevel = OptionalInt.of(Checks.asArgumentRange(maxLevel, "maxLevel", 1, 255));
            return this;
        }

        @Override
        public Builder minimumCost(final EnchantmentCost minimumCost) {
            final EnchantmentCost validCost = asArgument(minimumCost, "minimumCost");
            this.minimumCost = Enchantment.dynamicCost(validCost.baseCost(), validCost.additionalPerLevelCost());
            return this;
        }

        @Override
        public Builder maximumCost(final EnchantmentCost maximumCost) {
            final EnchantmentCost validCost = asArgument(maximumCost, "maximumCost");
            this.maximumCost = Enchantment.dynamicCost(validCost.baseCost(), validCost.additionalPerLevelCost());
            return this;
        }

        @Override
        public Builder anvilCost(final @Range(from = 0, to = Integer.MAX_VALUE) int anvilCost) {
            this.anvilCost = OptionalInt.of(asArgumentMin(anvilCost, "anvilCost", 0));
            return this;
        }

        @Override
        public Builder activeSlots(final Iterable<org.bukkit.inventory.EquipmentSlotGroup> activeSlots) {
            this.activeSlots = Lists.newArrayList(Iterables.transform(asArgument(activeSlots, "activeSlots"), CraftEquipmentSlot::getNMSGroup));
            return this;
        }

        @Override
        public Builder exclusiveWith(final RegistryKeySet<org.bukkit.enchantments.Enchantment> exclusiveWith) {
            this.exclusiveWith = PaperRegistrySets.convertToNms(Registries.ENCHANTMENT, this.conversions.lookup(), asArgument(exclusiveWith, "exclusiveWith"));
            return this;
        }

        @Override
        public Enchantment build() {
            final Enchantment.EnchantmentDefinition def = new Enchantment.EnchantmentDefinition(
                asConfigured(this.supportedItems, "supportedItems"),
                Optional.ofNullable(this.primaryItems),
                this.weight(),
                this.maxLevel(),
                asConfigured(this.minimumCost, "minimumCost"),
                asConfigured(this.maximumCost, "maximumCost"),
                this.anvilCost(),
                Collections.unmodifiableList(asConfigured(this.activeSlots, "activeSlots"))
            );
            return new Enchantment(
                asConfigured(this.description, "description"),
                def,
                this.exclusiveWith,
                this.effects
            );
        }
    }
}
