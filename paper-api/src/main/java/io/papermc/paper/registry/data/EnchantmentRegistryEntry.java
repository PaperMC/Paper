package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryComposeEvent;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link Enchantment} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EnchantmentRegistryEntry {

    /**
     * Provides the description of this enchantment entry as displayed to the client, e.g. "Sharpness" for the sharpness
     * enchantment.
     *
     * @return the description component.
     */
    Component description();

    /**
     * Provides the registry key set referencing the items this enchantment is supported on.
     *
     * @return the registry key set.
     */
    RegistryKeySet<ItemType> supportedItems();

    /**
     * Provides the registry key set referencing the item types this enchantment can be applied to when
     * enchanting in an enchantment table.
     * <p>
     * If this value is {@code null}, {@link #supportedItems()} will be sourced instead in the context of an enchantment table.
     * Additionally, the tag {@link io.papermc.paper.registry.keys.tags.EnchantmentTagKeys#IN_ENCHANTING_TABLE} defines
     * which enchantments can even show up in an enchantment table.
     *
     * @return the registry key set.
     */
    @Nullable RegistryKeySet<ItemType> primaryItems();

    /**
     * Provides the weight of this enchantment used by the weighted random when selecting enchantments.
     *
     * @return the weight value.
     * @see <a href="https://minecraft.wiki/w/Enchanting">https://minecraft.wiki/w/Enchanting</a> for examplary weights.
     */
    @Range(from = 1, to = 1024) int weight();

    /**
     * Provides the maximum level this enchantment can have when applied.
     *
     * @return the maximum level.
     */
    @Range(from = 1, to = 255) int maxLevel();

    /**
     * Provides the minimum cost needed to enchant an item with this enchantment.
     * <p>
     * Note that a cost is not directly related to the consumed xp.
     *
     * @return the enchantment cost.
     * @see <a href="https://minecraft.wiki/w/Enchanting/Levels">https://minecraft.wiki/w/Enchanting/Levels</a> for
     * examplary costs.
     */
    EnchantmentCost minimumCost();

    /**
     * Provides the maximum cost allowed to enchant an item with this enchantment.
     * <p>
     * Note that a cost is not directly related to the consumed xp.
     *
     * @return the enchantment cost.
     * @see <a href="https://minecraft.wiki/w/Enchanting/Levels">https://minecraft.wiki/w/Enchanting/Levels</a> for
     * examplary costs.
     */
    EnchantmentCost maximumCost();

    /**
     * Provides the cost of applying this enchantment using an anvil.
     * <p>
     * Note that this is halved when using an enchantment book, and is multiplied by the level of the enchantment.
     * See <a href="https://minecraft.wiki/w/Anvil_mechanics">https://minecraft.wiki/w/Anvil_mechanics</a> for more
     * information.
     * </p>
     *
     * @return the anvil cost of this enchantment
     */
    @Range(from = 0, to = Integer.MAX_VALUE) int anvilCost();

    /**
     * Provides a list of slot groups this enchantment may be active in.
     * <p>
     * If the item enchanted with this enchantment is equipped in a slot not covered by the returned list and its
     * groups, the enchantment's effects, like attribute modifiers, will not activate.
     *
     * @return a list of equipment slot groups.
     * @see Enchantment#getActiveSlotGroups()
     */
    @Unmodifiable List<EquipmentSlotGroup> activeSlots();

    /**
     * Provides the registry key set of enchantments that this enchantment is exclusive with.
     * <p>
     * Exclusive enchantments prohibit the application of this enchantment to an item if they are already present on
     * said item.
     *
     * @return a registry set of enchantments exclusive to this one.
     */
    RegistryKeySet<Enchantment> exclusiveWith();

    /**
     * A mutable builder for the {@link EnchantmentRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #description(Component)}</li>
     *     <li>{@link #supportedItems(RegistryKeySet)}</li>
     *     <li>{@link #weight(int)}</li>
     *     <li>{@link #maxLevel(int)}</li>
     *     <li>{@link #minimumCost(EnchantmentCost)}</li>
     *     <li>{@link #maximumCost(EnchantmentCost)}</li>
     *     <li>{@link #anvilCost(int)}</li>
     *     <li>{@link #activeSlots(Iterable)}</li>
     * </ul>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends EnchantmentRegistryEntry, RegistryBuilder<Enchantment> {

        /**
         * Configures the description of this enchantment entry as displayed to the client, e.g. "Sharpness" for the
         * sharpness enchantment.
         *
         * @param description the description component.
         * @return this builder instance.
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder description(Component description);

        /**
         * Configures the set of supported items this enchantment can be applied on. This
         * can be a {@link RegistryKeySet} created via {@link RegistrySet#keySet(io.papermc.paper.registry.RegistryKey, Iterable)} or
         * a tag obtained via {@link RegistryComposeEvent#getOrCreateTag(TagKey)} with
         * tag keys found in {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys} such as
         * {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys#ENCHANTABLE_ARMOR} and
         * {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys#ENCHANTABLE_SWORD}.
         *
         * @param supportedItems the registry key set representing the supported items.
         * @return this builder instance.
         * @see RegistrySet#keySet(RegistryKey, TypedKey[])
         * @see RegistryComposeEvent#getOrCreateTag(TagKey)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder supportedItems(RegistryKeySet<ItemType> supportedItems);

        /**
         * Configures a set of item types this enchantment can naturally be applied to, when enchanting in an
         * enchantment table.This can be a {@link RegistryKeySet} created via
         * {@link RegistrySet#keySet(io.papermc.paper.registry.RegistryKey, Iterable)} or a tag obtained via
         * {@link RegistryComposeEvent#getOrCreateTag(TagKey)} with
         * tag keys found in {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys} such as
         * {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys#ENCHANTABLE_ARMOR} and
         * {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys#ENCHANTABLE_SWORD}.
         * <p>
         * Defaults to {@code null} which means all {@link #supportedItems()} are considered primary items.
         * Additionally, the tag {@link io.papermc.paper.registry.keys.tags.EnchantmentTagKeys#IN_ENCHANTING_TABLE} defines
         * which enchantments can even show up in an enchantment table.
         *
         * @param primaryItems the registry key set representing the primary items.
         * @return this builder instance.
         * @see RegistrySet#keySet(RegistryKey, TypedKey[])
         * @see RegistryComposeEvent#getOrCreateTag(TagKey)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder primaryItems(@Nullable RegistryKeySet<ItemType> primaryItems);

        /**
         * Configures the weight of this enchantment used by the weighted random when selecting enchantments.
         *
         * @param weight the weight value.
         * @return this builder instance.
         * @see <a href="https://minecraft.wiki/w/Enchanting">https://minecraft.wiki/w/Enchanting</a> for examplary weights.
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder weight(@Range(from = 1, to = 1024) int weight);

        /**
         * Configures the maximum level this enchantment can have when applied.
         *
         * @param maxLevel the maximum level.
         * @return this builder instance.
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder maxLevel(@Range(from = 1, to = 255) int maxLevel);

        /**
         * Configures the minimum cost needed to enchant an item with this enchantment.
         * <p>
         * Note that a cost is not directly related to the consumed xp.
         *
         * @param minimumCost the enchantment cost.
         * @return this builder instance.
         * @see <a href="https://minecraft.wiki/w/Enchanting/Levels">https://minecraft.wiki/w/Enchanting/Levels</a> for
         * examplary costs.
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder minimumCost(EnchantmentCost minimumCost);

        /**
         * Configures the maximum cost to enchant an item with this enchantment.
         * <p>
         * Note that a cost is not directly related to the consumed xp.
         *
         * @param maximumCost the enchantment cost.
         * @return this builder instance.
         * @see <a href="https://minecraft.wiki/w/Enchanting/Levels">https://minecraft.wiki/w/Enchanting/Levels</a> for
         * examplary costs.
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder maximumCost(EnchantmentCost maximumCost);

        /**
         * Configures the cost of applying this enchantment using an anvil.
         * <p>
         * Note that this is halved when using an enchantment book, and is multiplied by the level of the enchantment.
         * See <a href="https://minecraft.wiki/w/Anvil_mechanics">https://minecraft.wiki/w/Anvil_mechanics</a> for more information.
         * </p>
         *
         * @param anvilCost the anvil cost of this enchantment
         * @return this builder instance.
         * @see Enchantment#getAnvilCost()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder anvilCost(@Range(from = 0, to = Integer.MAX_VALUE) int anvilCost);

        /**
         * Configures the list of slot groups this enchantment may be active in.
         * <p>
         * If the item enchanted with this enchantment is equipped in a slot not covered by the returned list and its
         * groups, the enchantment's effects, like attribute modifiers, will not activate.
         *
         * @param activeSlots a list of equipment slot groups.
         * @return this builder instance.
         * @see Enchantment#getActiveSlotGroups()
         */
        @Contract(value = "_ -> this", mutates = "this")
        default Builder activeSlots(final EquipmentSlotGroup... activeSlots) {
            return this.activeSlots(List.of(activeSlots));
        }

        /**
         * Configures the list of slot groups this enchantment may be active in.
         * <p>
         * If the item enchanted with this enchantment is equipped in a slot not covered by the returned list and its
         * groups, the enchantment's effects, like attribute modifiers, will not activate.
         *
         * @param activeSlots a list of equipment slot groups.
         * @return this builder instance.
         * @see Enchantment#getActiveSlotGroups()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder activeSlots(Iterable<EquipmentSlotGroup> activeSlots);

        /**
         * Configures the registry key set of enchantments that this enchantment is exclusive with.
         * <p>
         * Exclusive enchantments prohibit the application of this enchantment to an item if they are already present on
         * said item.
         * <p>
         * Defaults to an empty set allowing this enchantment to be applied regardless of other enchantments.
         *
         * @param exclusiveWith a registry set of enchantments exclusive to this one.
         * @return this builder instance.
         * @see RegistrySet#keySet(RegistryKey, TypedKey[])
         * @see RegistryComposeEvent#getOrCreateTag(TagKey)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder exclusiveWith(RegistryKeySet<Enchantment> exclusiveWith);
    }

    /**
     * The enchantment cost interface represents the cost of applying an enchantment, split up into its different components.
     */
    interface EnchantmentCost {

        /**
         * Returns the base cost of this enchantment cost, no matter what level the enchantment has.
         *
         * @return the cost in levels.
         */
        int baseCost();

        /**
         * Returns the additional cost added per level of the enchantment to be applied.
         * This cost is applied per level above the first.
         *
         * @return the cost added to the {@link #baseCost()} for each level above the first.
         */
        int additionalPerLevelCost();

        /**
         * Creates a new enchantment cost instance based on the passed values.
         *
         * @param baseCost the base cost of the enchantment cost as returned by {@link #baseCost()}
         * @param additionalPerLevelCost the additional cost per level, as returned by {@link #additionalPerLevelCost()}
         * @return the created instance.
         */
        @Contract(value = "_,_ -> new", pure = true)
        static EnchantmentCost of(final int baseCost, final int additionalPerLevelCost) {
            record Impl(int baseCost, int additionalPerLevelCost) implements EnchantmentCost {
            }

            return new Impl(baseCost, additionalPerLevelCost);
        }
    }
}
