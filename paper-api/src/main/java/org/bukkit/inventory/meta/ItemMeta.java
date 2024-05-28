package org.bukkit.inventory.meta;

import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This type represents the storage mechanism for auxiliary item data.
 * <p>
 * An implementation will handle the creation and application for ItemMeta.
 * This class should not be implemented by a plugin in a live environment.
 */
public interface ItemMeta extends Cloneable, ConfigurationSerializable, PersistentDataHolder {

    /**
     * Checks for existence of a display name.
     *
     * @return true if this has a display name
     */
    boolean hasDisplayName();

    /**
     * Gets the display name that is set.
     * <p>
     * Plugins should check that hasDisplayName() returns <code>true</code>
     * before calling this method.
     *
     * @return the display name that is set
     */
    @NotNull
    String getDisplayName();

    /**
     * Sets the display name.
     *
     * @param name the name to set
     */
    void setDisplayName(@Nullable String name);

    /**
     * Checks for existence of an item name.
     * <br>
     * Item name differs from display name in that it is cannot be edited by an
     * anvil, is not styled with italics, and does not show labels.
     *
     * @return true if this has an item name
     */
    boolean hasItemName();

    /**
     * Gets the item name that is set.
     * <br>
     * Item name differs from display name in that it is cannot be edited by an
     * anvil, is not styled with italics, and does not show labels.
     * <p>
     * Plugins should check that hasItemName() returns <code>true</code> before
     * calling this method.
     *
     * @return the item name that is set
     */
    @NotNull
    String getItemName();

    /**
     * Sets the item name.
     * <br>
     * Item name differs from display name in that it is cannot be edited by an
     * anvil, is not styled with italics, and does not show labels.
     *
     * @param name the name to set
     */
    void setItemName(@Nullable String name);

    /**
     * Checks for existence of a localized name.
     *
     * @return true if this has a localized name
     * @deprecated meta no longer exists
     */
    @Deprecated(forRemoval = true)
    boolean hasLocalizedName();

    /**
     * Gets the localized display name that is set.
     * <p>
     * Plugins should check that hasLocalizedName() returns <code>true</code>
     * before calling this method.
     *
     * @return the localized name that is set
     * @deprecated meta no longer exists
     */
    @NotNull
    @Deprecated(forRemoval = true)
    String getLocalizedName();

    /**
     * Sets the localized name.
     *
     * @param name the name to set
     * @deprecated meta no longer exists
     */
    @Deprecated(forRemoval = true)
    void setLocalizedName(@Nullable String name);

    /**
     * Checks for existence of lore.
     *
     * @return true if this has lore
     */
    boolean hasLore();

    /**
     * Gets the lore that is set.
     * <p>
     * Plugins should check if hasLore() returns <code>true</code> before
     * calling this method.
     *
     * @return a list of lore that is set
     */
    @Nullable
    List<String> getLore();

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     */
    void setLore(@Nullable List<String> lore);

    /**
     * Checks for existence of custom model data.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     *
     * @return true if this has custom model data
     */
    boolean hasCustomModelData();

    /**
     * Gets the custom model data that is set.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     * <p>
     * Plugins should check that hasCustomModelData() returns <code>true</code>
     * before calling this method.
     *
     * @return the custom model data that is set
     */
    int getCustomModelData();

    /**
     * Sets the custom model data.
     * <p>
     * CustomModelData is an integer that may be associated client side with a
     * custom item model.
     *
     * @param data the data to set, or null to clear
     */
    void setCustomModelData(@Nullable Integer data);

    /**
     * Checks for the existence of any enchantments.
     *
     * @return true if an enchantment exists on this meta
     */
    boolean hasEnchants();

    /**
     * Checks for existence of the specified enchantment.
     *
     * @param ench enchantment to check
     * @return true if this enchantment exists for this meta
     */
    boolean hasEnchant(@NotNull Enchantment ench);

    /**
     * Checks for the level of the specified enchantment.
     *
     * @param ench enchantment to check
     * @return The level that the specified enchantment has, or 0 if none
     */
    int getEnchantLevel(@NotNull Enchantment ench);

    /**
     * Returns a copy the enchantments in this ItemMeta. <br>
     * Returns an empty map if none.
     *
     * @return An immutable copy of the enchantments
     */
    @NotNull
    Map<Enchantment, Integer> getEnchants();

    /**
     * Adds the specified enchantment to this item meta.
     *
     * @param ench Enchantment to add
     * @param level Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *     applied, ignoring the level limit
     * @return true if the item meta changed as a result of this call, false
     *     otherwise
     */
    boolean addEnchant(@NotNull Enchantment ench, int level, boolean ignoreLevelRestriction);

    /**
     * Removes the specified enchantment from this item meta.
     *
     * @param ench Enchantment to remove
     * @return true if the item meta changed as a result of this call, false
     *     otherwise
     */
    boolean removeEnchant(@NotNull Enchantment ench);

    /**
     * Removes all enchantments from this item meta.
     */
    void removeEnchantments();

    /**
     * Checks if the specified enchantment conflicts with any enchantments in
     * this ItemMeta.
     *
     * @param ench enchantment to test
     * @return true if the enchantment conflicts, false otherwise
     */
    boolean hasConflictingEnchant(@NotNull Enchantment ench);

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hideflags which shouldn't be rendered
     */
    void addItemFlags(@NotNull ItemFlag... itemFlags);

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hideflags which should be removed
     */
    void removeItemFlags(@NotNull ItemFlag... itemFlags);

    /**
     * Get current set itemFlags. The collection returned is unmodifiable.
     *
     * @return A set of all itemFlags set
     */
    @NotNull
    Set<ItemFlag> getItemFlags();

    /**
     * Check if the specified flag is present on this item.
     *
     * @param flag the flag to check
     * @return if it is present
     */
    boolean hasItemFlag(@NotNull ItemFlag flag);

    /**
     * Gets if this item has hide_tooltip set. An item with this set will not
     * show any tooltip whatsoever.
     *
     * @return hide_tooltip
     */
    boolean isHideTooltip();

    /**
     * Sets if this item has hide_tooltip set. An item with this set will not
     * show any tooltip whatsoever.
     *
     * @param hideTooltip new hide_tooltip
     */
    void setHideTooltip(boolean hideTooltip);

    /**
     * Return if the unbreakable tag is true. An unbreakable item will not lose
     * durability.
     *
     * @return true if the unbreakable tag is true
     */
    boolean isUnbreakable();

    /**
     * Sets the unbreakable tag. An unbreakable item will not lose durability.
     *
     * @param unbreakable true if set unbreakable
     */
    void setUnbreakable(boolean unbreakable);

    /**
     * Gets if an enchantment_glint_override is set.
     *
     * @return if an enchantment_glint_override is set
     */
    boolean hasEnchantmentGlintOverride();

    /**
     * Sets the enchantment_glint_override. If true, the item will glint, even
     * without enchantments; if false, the item will not glint, even with
     * enchantments.
     *
     * Plugins should check {@link #hasEnchantmentGlintOverride()} before
     * calling this method.
     *
     * @return enchantment_glint_override
     */
    @NotNull
    Boolean getEnchantmentGlintOverride();

    /**
     * Sets the enchantment_glint_override. If true, the item will glint, even
     * without enchantments; if false, the item will not glint, even with
     * enchantments. If null, the override will be cleared.
     *
     * @param override new enchantment_glint_override
     */
    void setEnchantmentGlintOverride(@Nullable Boolean override);

    /**
     * Checks if this item is fire_resistant. If true, it will not burn in fire
     * or lava.
     *
     * @return fire_resistant
     */
    boolean isFireResistant();

    /**
     * Sets if this item is fire_resistant. If true, it will not burn in fire
     * or lava.
     *
     * @param fireResistant fire_resistant
     */
    void setFireResistant(boolean fireResistant);

    /**
     * Gets if the max_stack_size is set.
     *
     * @return if a max_stack_size is set.
     */
    boolean hasMaxStackSize();

    /**
     * Gets the max_stack_size. This is the maximum amount which an item will
     * stack.
     *
     * @return max_stack_size
     */
    int getMaxStackSize();

    /**
     * Sets the max_stack_size. This is the maximum amount which an item will
     * stack.
     *
     * @param max max_stack_size, between 1 and 99 (inclusive)
     */
    void setMaxStackSize(@Nullable Integer max);

    /**
     * Gets if the rarity is set.
     *
     * @return rarity
     */
    boolean hasRarity();

    /**
     * Gets the item rarity.
     *
     * Plugins should check {@link #hasRarity()} before calling this method.
     *
     * @return rarity
     */
    @NotNull
    ItemRarity getRarity();

    /**
     * Sets the item rarity.
     *
     * @param rarity new rarity
     */
    void setRarity(@Nullable ItemRarity rarity);

    /**
     * Checks if the food is set.
     *
     * @return if a food is set
     */
    boolean hasFood();

    /**
     * Gets the food set on this item, or creates an empty food instance.
     * <p>
     * The returned component is a snapshot of its current state and does not
     * reflect a live view of what is on an item. After changing any value on
     * this component, it must be set with {@link #setFood(FoodComponent)} to
     * apply the changes.
     *
     * @return food
     */
    @NotNull
    FoodComponent getFood();

    /**
     * Sets the item food.
     *
     * @param food new food
     */
    void setFood(@Nullable FoodComponent food);

    /**
     * Checks if the tool is set.
     *
     * @return if a tool is set
     */
    boolean hasTool();

    /**
     * Gets the tool set on this item, or creates an empty tool instance.
     * <p>
     * The returned component is a snapshot of its current state and does not
     * reflect a live view of what is on an item. After changing any value on
     * this component, it must be set with {@link #setTool(ToolComponent)} to
     * apply the changes.
     *
     * @return tool
     */
    @NotNull
    ToolComponent getTool();

    /**
     * Sets the item tool.
     *
     * @param tool new tool
     */
    void setTool(@Nullable ToolComponent tool);

    /**
     * Checks for the existence of any AttributeModifiers.
     *
     * @return true if any AttributeModifiers exist
     */
    boolean hasAttributeModifiers();

    /**
     * Return an immutable copy of all Attributes and
     * their modifiers in this ItemMeta.<br>
     * Returns null if none exist.
     *
     * @return an immutable {@link Multimap} of Attributes
     *         and their AttributeModifiers, or null if none exist
     */
    @Nullable
    Multimap<Attribute, AttributeModifier> getAttributeModifiers();

    /**
     * Return an immutable copy of all {@link Attribute}s and their
     * {@link AttributeModifier}s for a given {@link EquipmentSlot}.<br>
     * Any {@link AttributeModifier} that does have have a given
     * {@link EquipmentSlot} will be returned. This is because
     * AttributeModifiers without a slot are active in any slot.<br>
     * If there are no attributes set for the given slot, an empty map
     * will be returned.
     *
     * @param slot the {@link EquipmentSlot} to check
     * @return the immutable {@link Multimap} with the
     *         respective Attributes and modifiers, or an empty map
     *         if no attributes are set.
     */
    @NotNull
    Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot);

    /**
     * Return an immutable copy of all {@link AttributeModifier}s
     * for a given {@link Attribute}
     *
     * @param attribute the {@link Attribute}
     * @return an immutable collection of {@link AttributeModifier}s
     *          or null if no AttributeModifiers exist for the Attribute.
     * @throws NullPointerException if Attribute is null
     */
    @Nullable
    Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute);

    /**
     * Add an Attribute and it's Modifier.
     * AttributeModifiers can now support {@link EquipmentSlot}s.
     * If not set, the {@link AttributeModifier} will be active in ALL slots.
     * <br>
     * Two {@link AttributeModifier}s that have the same {@link java.util.UUID}
     * cannot exist on the same Attribute.
     *
     * @param attribute the {@link Attribute} to modify
     * @param modifier the {@link AttributeModifier} specifying the modification
     * @return true if the Attribute and AttributeModifier were
     *         successfully added
     * @throws NullPointerException if Attribute is null
     * @throws NullPointerException if AttributeModifier is null
     * @throws IllegalArgumentException if AttributeModifier already exists
     */
    boolean addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier);

    /**
     * Set all {@link Attribute}s and their {@link AttributeModifier}s.
     * To clear all currently set Attributes and AttributeModifiers use
     * null or an empty Multimap.
     * If not null nor empty, this will filter all entries that are not-null
     * and add them to the ItemStack.
     *
     * @param attributeModifiers the new Multimap containing the Attributes
     *                           and their AttributeModifiers
     */
    void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers);

    /**
     * Remove all {@link AttributeModifier}s associated with the given
     * {@link Attribute}.
     * This will return false if nothing was removed.
     *
     * @param attribute attribute to remove
     * @return  true if all modifiers were removed from a given
     *                  Attribute. Returns false if no attributes were
     *                  removed.
     * @throws NullPointerException if Attribute is null
     */
    boolean removeAttributeModifier(@NotNull Attribute attribute);

    /**
     * Remove all {@link Attribute}s and {@link AttributeModifier}s for a
     * given {@link EquipmentSlot}.<br>
     * If the given {@link EquipmentSlot} is null, this will remove all
     * {@link AttributeModifier}s that do not have an EquipmentSlot set.
     *
     * @param slot the {@link EquipmentSlot} to clear all Attributes and
     *             their modifiers for
     * @return true if all modifiers were removed that match the given
     *         EquipmentSlot.
     */
    boolean removeAttributeModifier(@NotNull EquipmentSlot slot);

    /**
     * Remove a specific {@link Attribute} and {@link AttributeModifier}.
     * AttributeModifiers are matched according to their {@link java.util.UUID}.
     *
     * @param attribute the {@link Attribute} to remove
     * @param modifier the {@link AttributeModifier} to remove
     * @return if any attribute modifiers were remove
     *
     * @throws NullPointerException if the Attribute is null
     * @throws NullPointerException if the AttributeModifier is null
     *
     * @see AttributeModifier#getUniqueId()
     */
    boolean removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier);

    /**
     * Get this ItemMeta as an NBT string. If this ItemMeta does not have any
     * NBT, then {@code "{}"} will be returned.
     * <p>
     * This string should <strong>NEVER</strong> be relied upon as a serializable value. If
     * serialization is desired, the {@link ConfigurationSerializable} API should be used
     * instead.
     *
     * @return the NBT string
     */
    @NotNull
    String getAsString();

    /**
     * Get this ItemMeta as a component-compliant string. If this ItemMeta does
     * not contain any components, then {@code "[]"} will be returned.
     * <p>
     * The result of this method should yield a string representing the components
     * altered by this ItemMeta instance. When passed to {@link ItemFactory#createItemStack(String)}
     * with a prepended item type, it will create an ItemStack that has an ItemMeta
     * matching this ItemMeta instance exactly. Note that this method returns <strong>
     * ONLY</strong> the components and cannot be passed to createItemStack() alone.
     * An example may look something like this:
     * <pre>
     * ItemStack itemStack = // ... an item stack obtained from somewhere
     * ItemMeta itemMeta = itemStack.getItemMeta();
     *
     * String components = itemMeta.getAsComponentString(); // example: "[minecraft:damage=53]"
     * String itemTypeKey = itemStack.getType().getKey().toString(); // example: "minecraft:diamond_sword"
     * String itemAsString = itemTypeKey + components; // results in: "minecraft:diamond_sword[minecraft:damage=53]"
     *
     * ItemStack recreatedItemStack = Bukkit.getItemFactory().createItemStack(itemAsString);
     * assert itemStack.isSimilar(recreatedItemStack); // Should be true*
     * </pre>
     * <p>
     * *Components not represented or explicitly overridden by this ItemMeta instance
     * will not be included in the resulting string and therefore may result in ItemStacks
     * that do not match <em>exactly</em>. For example, if {@link #setDisplayName(String)}
     * is not set, then the custom name component will not be included. Or if this ItemMeta
     * is a PotionMeta, it will not include any components related to lodestone compasses,
     * banners, or books, etc., only components modifiable by a PotionMeta instance.
     * <p>
     * This string should <strong>NEVER</strong> be relied upon as a serializable value. If
     * serialization is desired, the {@link ConfigurationSerializable} API should be used
     * instead.
     *
     * @return the component-compliant string
     */
    @NotNull
    String getAsComponentString();

    /**
     * Returns a public custom tag container capable of storing tags on the
     * item.
     *
     * Those tags will be sent to the client with all of their content, so the
     * client is capable of reading them. This will result in the player seeing
     * a NBT Tag notification on the item.
     *
     * These tags can also be modified by the client once in creative mode
     *
     * @return the custom tag container
     * @deprecated this API part has been replaced by the {@link PersistentDataHolder} API.
     * Please use {@link PersistentDataHolder#getPersistentDataContainer()} instead of this.
     */
    @NotNull
    @Deprecated
    CustomItemTagContainer getCustomTagContainer();

    /**
     * Internal use only! Do not use under any circumstances!
     *
     * @param version version
     * @apiNote  internal use only
     */
    @ApiStatus.Internal
    void setVersion(int version);

    @SuppressWarnings("javadoc")
    @NotNull
    ItemMeta clone();
}
