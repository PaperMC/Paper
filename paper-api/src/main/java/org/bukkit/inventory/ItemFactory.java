package org.bukkit.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An instance of the ItemFactory can be obtained with {@link
 * Server#getItemFactory()}.
 * <p>
 * The ItemFactory is solely responsible for creating item meta containers to
 * apply on item stacks.
 */
public interface ItemFactory {

    /**
     * This creates a new item meta for the material.
     *
     * @param material The material to consider as base for the meta
     * @return a new ItemMeta that could be applied to an item stack of the
     *     specified material
     */
    @org.bukkit.UndefinedNullability // Paper
    ItemMeta getItemMeta(@NotNull final Material material);

    /**
     * This method checks the item meta to confirm that it is applicable (no
     * data lost if applied) to the specified ItemStack.
     * <p>
     * A {@link SkullMeta} would not be valid for a sword, but a normal {@link
     * ItemMeta} from an enchanted dirt block would.
     *
     * @param meta Meta to check
     * @param stack Item that meta will be applied to
     * @return true if the meta can be applied without losing data, false
     *     otherwise
     * @throws IllegalArgumentException if the meta was not created by this
     *     factory
     */
    boolean isApplicable(@Nullable final ItemMeta meta, @Nullable final ItemStack stack) throws IllegalArgumentException;

    /**
     * This method checks the item meta to confirm that it is applicable (no
     * data lost if applied) to the specified Material.
     * <p>
     * A {@link SkullMeta} would not be valid for a sword, but a normal {@link
     * ItemMeta} from an enchanted dirt block would.
     *
     * @param meta Meta to check
     * @param material Material that meta will be applied to
     * @return true if the meta can be applied without losing data, false
     *     otherwise
     * @throws IllegalArgumentException if the meta was not created by this
     *     factory
     */
    boolean isApplicable(@Nullable final ItemMeta meta, @Nullable final Material material) throws IllegalArgumentException;

    /**
     * This method is used to compare two item meta data objects.
     *
     * @param meta1 First meta to compare, and may be null to indicate no data
     * @param meta2 Second meta to compare, and may be null to indicate no
     *     data
     * @return false if one of the meta has data the other does not, otherwise
     *     true
     * @throws IllegalArgumentException if either meta was not created by this
     *     factory
     */
    boolean equals(@Nullable final ItemMeta meta1, @Nullable final ItemMeta meta2) throws IllegalArgumentException;

    /**
     * Returns an appropriate item meta for the specified stack.
     * <p>
     * The item meta returned will always be a valid meta for a given
     * ItemStack of the specified material. It may be a more or less specific
     * meta, and could also be the same meta or meta type as the parameter.
     * The item meta returned will also always be the most appropriate meta.
     * <p>
     * Example, if a {@link SkullMeta} is being applied to a book, this method
     * would return a {@link BookMeta} containing all information in the
     * specified meta that is applicable to an {@link ItemMeta}, the highest
     * common interface.
     *
     * @param meta the meta to convert
     * @param stack the stack to convert the meta for
     * @return An appropriate item meta for the specified item stack. No
     *     guarantees are made as to if a copy is returned. This will be null
     *     for a stack of air.
     * @throws IllegalArgumentException if the specified meta was not created
     *     by this factory
     */
    @Nullable
    ItemMeta asMetaFor(@NotNull final ItemMeta meta, @NotNull final ItemStack stack) throws IllegalArgumentException;

    /**
     * Returns an appropriate item meta for the specified material.
     * <p>
     * The item meta returned will always be a valid meta for a given
     * ItemStack of the specified material. It may be a more or less specific
     * meta, and could also be the same meta or meta type as the parameter.
     * The item meta returned will also always be the most appropriate meta.
     * <p>
     * Example, if a {@link SkullMeta} is being applied to a book, this method
     * would return a {@link BookMeta} containing all information in the
     * specified meta that is applicable to an {@link ItemMeta}, the highest
     * common interface.
     *
     * @param meta the meta to convert
     * @param material the material to convert the meta for
     * @return An appropriate item meta for the specified item material. No
     *     guarantees are made as to if a copy is returned. This will be null for air.
     * @throws IllegalArgumentException if the specified meta was not created
     *     by this factory
     */
    @Nullable
    ItemMeta asMetaFor(@NotNull final ItemMeta meta, @NotNull final Material material) throws IllegalArgumentException;

    /**
     * Returns the default color for all leather armor.
     *
     * @return the default color for leather armor
     */
    @NotNull
    Color getDefaultLeatherColor();

    /**
     * Create a new {@link ItemStack} given the supplied input.
     * <p>
     * The input should match the same input as expected by Minecraft's {@code /give}
     * command. For example,
     * <pre>"minecraft:diamond_sword[minecraft:enchantments={levels:{"minecraft:sharpness": 3}}]"</pre>
     * would yield an ItemStack of {@link Material#DIAMOND_SWORD} with an {@link ItemMeta}
     * containing a level 3 {@link Enchantment#SHARPNESS} enchantment.
     *
     * @param input the item input string
     * @return the created ItemStack
     * @throws IllegalArgumentException if the input string was provided in an
     * invalid or unsupported format
     */
    @NotNull
    ItemStack createItemStack(@NotNull String input) throws IllegalArgumentException;

    /**
     * Gets a {@link Material} representing the spawn egg for the provided
     * {@link EntityType}. <br>
     * Will return null for EntityTypes that do not have a corresponding spawn egg.
     *
     * @param type the entity type
     * @return the Material of this EntityTypes spawn egg or null
     */
    @Nullable
    Material getSpawnEgg(@NotNull EntityType type);

    /**
     * Enchants the given item at the provided level.
     * <br>
     * If an item that is air is passed through an error is thrown.
     *
     * @param entity the entity to use as a source of randomness
     * @param item the item to enchant
     * @param level the level to use, which is the level in the enchantment table
     * @param allowTreasures allows treasure enchants, e.g. mending, if true.
     * @return a new ItemStack containing the result of the Enchantment
     * @deprecated use {@link #enchantWithLevels(ItemStack, int, boolean, java.util.Random)}. This method's implementation is poorly
     * designed and was originally broken.
     */
    @NotNull
    @Deprecated(since = "1.19.3") // Paper
    ItemStack enchantItem(@NotNull final Entity entity, @NotNull final ItemStack item, final int level, final boolean allowTreasures);

    /**
     * Enchants the given item at the provided level.
     * <br>
     * If an item that is air is passed through an error is thrown.
     *
     * @param world the world to use as a source of randomness
     * @param item the item to enchant
     * @param level the level to use, which is the level in the enchantment table
     * @param allowTreasures allow the treasure enchants, e.g. mending, if true.
     * @return a new ItemStack containing the result of the Enchantment
     * @deprecated use {@link #enchantWithLevels(ItemStack, int, boolean, java.util.Random)}. This method's implementation is poorly
     * designed and was originally broken.
     */
    @NotNull
    @Deprecated(since = "1.19.3") // Paper
    ItemStack enchantItem(@NotNull final World world, @NotNull final ItemStack item, final int level, final boolean allowTreasures);

    /**
     * Enchants the given item at the provided level.
     * <br>
     * If an item that is air is passed through an error is thrown.
     *
     * @param item the item to enchant
     * @param level the level to use, which is the level in the enchantment table
     * @param allowTreasures allow treasure enchantments, e.g. mending, if true.
     * @return a new ItemStack containing the result of the Enchantment
     * @deprecated use {@link #enchantWithLevels(ItemStack, int, boolean, java.util.Random)}. This method's implementation is poorly
     * designed and was originally broken.
     */
    @NotNull
    @Deprecated(since = "1.19.3") // Paper
    ItemStack enchantItem(@NotNull final ItemStack item, final int level, final boolean allowTreasures);

    /**
     * Creates a hover event for the given item.
     *
     * @param item The item
     * @return A hover event
     * @throws IllegalArgumentException if the {@link ItemStack#getAmount()} is not between 1 and 99
     */
    @NotNull
    net.kyori.adventure.text.event.HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowItem> asHoverEvent(final @NotNull ItemStack item, final @NotNull java.util.function.UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowItem> op);

    /**
     * Get the formatted display name of the {@link ItemStack}.
     *
     * @apiNote this component include a {@link net.kyori.adventure.text.event.HoverEvent item hover event}.
     * When used in chat, make sure to follow the ItemStack rules regarding amount, type, and other properties.
     * @param itemStack the {@link ItemStack}
     * @return display name of the {@link ItemStack}
     */
    @NotNull
    net.kyori.adventure.text.Component displayName(@NotNull ItemStack itemStack);

    // Paper start - add getI18NDisplayName
    /**
     * Gets the Display name as seen in the Client.
     * Currently, the server only supports the English language. To override this,
     * You must replace the language file embedded in the server jar.
     *
     * @param item Item to return Display name of
     * @return Display name of Item
     * @deprecated {@link ItemStack} implements {@link net.kyori.adventure.translation.Translatable}; use that and
     * {@link net.kyori.adventure.text.Component#translatable(net.kyori.adventure.translation.Translatable)} instead.
     */
    @Nullable
    @Deprecated(since = "1.18.1", forRemoval = true)
    String getI18NDisplayName(@Nullable ItemStack item);
    // Paper end - add getI18NDisplayName

    // Paper start - ensure server conversions API
    /**
     * Minecraft's updates are converting simple item stacks into more complex NBT oriented Item Stacks.
     *
     * Use this method to ensure any desired data conversions are processed.
     * The input itemstack will not be the same as the returned itemstack.
     *
     * @param item The item to process conversions on
     * @return A potentially Data-Converted-ItemStack
     */
    @NotNull
    ItemStack ensureServerConversions(@NotNull ItemStack item);
    // Paper end - ensure server conversions API

    // Paper start - bungee hover events
    /**
     * Creates a {@link net.md_5.bungee.api.chat.hover.content.Content} of that ItemStack for displaying.
     *
     * @param itemStack the itemstack
     * @return the {@link net.md_5.bungee.api.chat.hover.content.Content} of that ItemStack
     * @deprecated use {@link ItemStack#asHoverEvent()}
     */
    @NotNull
    @Deprecated // Paper
    net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(@NotNull ItemStack itemStack);

    /**
     * Creates a {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity} for displaying.
     * Uses the display name of the entity, if present.
     *
     * @param entity Entity to create the HoverEvent for
     * @return the {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity}
     * @deprecated use {@link org.bukkit.entity.Entity#asHoverEvent()}
     */
    @NotNull
    @Deprecated
    net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(@NotNull org.bukkit.entity.Entity entity);

    /**
     * Creates a {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity} for displaying.
     *
     * @param entity Entity to create the HoverEvent for
     * @param customName a custom name that should be displayed, if not passed entity name will be displayed
     * @return the {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity}
     * @deprecated use {@link org.bukkit.entity.Entity#asHoverEvent(java.util.function.UnaryOperator)}
     */
    @NotNull
    @Deprecated
    net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(@NotNull org.bukkit.entity.Entity entity, @Nullable String customName);

    /**
     * Creates a {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity} for displaying.
     *
     * @param entity Entity to create the HoverEvent for
     * @param customName a custom name that should be displayed, if not passed entity name will be displayed
     * @return the {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity}
     * @deprecated use {@link org.bukkit.entity.Entity#asHoverEvent(java.util.function.UnaryOperator)}
     */
    @NotNull
    @Deprecated
    net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(@NotNull org.bukkit.entity.Entity entity, @Nullable net.md_5.bungee.api.chat.BaseComponent customName);

    /**
     * Creates a {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity} for displaying.
     *
     * @param entity Entity to create the HoverEvent for
     * @param customName a custom name that should be displayed, if not passed entity name will be displayed
     * @return the {@link net.md_5.bungee.api.chat.hover.content.Content} of that {@link org.bukkit.entity.Entity}
     * @deprecated use {@link org.bukkit.entity.Entity#asHoverEvent(java.util.function.UnaryOperator)}
     */
    @NotNull
    @Deprecated
    net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(@NotNull org.bukkit.entity.Entity entity, @NotNull net.md_5.bungee.api.chat.BaseComponent[] customName);
    // Paper end - bungee hover events

    // Paper start - enchantWithLevels API
    /**
     * Randomly enchants a copy of the provided {@link ItemStack} using the given experience levels.
     *
     * <p>If the provided ItemStack is already enchanted, the existing enchants will be removed before enchanting.</p>
     *
     * <p>Enchantment tables use levels in the range {@code [1, 30]}.</p>
     *
     * @param itemStack ItemStack to enchant
     * @param levels levels to use for enchanting
     * @param allowTreasure whether to allow enchantments where {@link org.bukkit.enchantments.Enchantment#isTreasure()} returns true
     * @param random {@link java.util.Random} instance to use for enchanting
     * @return enchanted copy of the provided ItemStack
     * @throws IllegalArgumentException on bad arguments
     */
    @NotNull ItemStack enchantWithLevels(@NotNull ItemStack itemStack, int levels, boolean allowTreasure, @NotNull java.util.Random random);
    // Paper end - enchantWithLevels API
    // Paper start - enchantWithLevels with tag specification
    /**
     * Randomly enchants a copy of the provided {@link ItemStack} using the given experience levels.
     *
     * <p>If the provided ItemStack is already enchanted, the existing enchants will be removed before enchanting.</p>
     *
     * <p>Enchantment tables use levels in the range {@code [1, 30]}.</p>
     *
     * @param itemStack ItemStack to enchant
     * @param levels levels to use for enchanting
     * @param keySet registry key set defining the set of possible enchantments, e.g. {@link io.papermc.paper.registry.keys.tags.EnchantmentTagKeys#IN_ENCHANTING_TABLE}.
     * @param random {@link java.util.Random} instance to use for enchanting
     * @return enchanted copy of the provided ItemStack
     * @throws IllegalArgumentException on bad arguments
     */
    @NotNull ItemStack enchantWithLevels(@NotNull ItemStack itemStack, int levels, @NotNull io.papermc.paper.registry.set.RegistryKeySet<@NotNull Enchantment> keySet, @NotNull java.util.Random random);
    // Paper end - enchantWithLevels with tag specification
}
