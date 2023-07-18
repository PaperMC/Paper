package org.bukkit.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
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
    @Nullable
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
     * command. For example, "minecraft:diamond_sword{Enchantments:[{id:"minecraft:sharpness", lvl:3}]}"
     * would yield an ItemStack of {@link Material#DIAMOND_SWORD} with an {@link ItemMeta}
     * containing a level 3 {@link Enchantment#DAMAGE_ALL}
     * enchantment.
     *
     * @param input the item input string
     * @return the created ItemStack
     * @throws IllegalArgumentException if the input string was provided in an
     * invalid or unsupported format
     */
    @NotNull
    ItemStack createItemStack(@NotNull String input) throws IllegalArgumentException;

    /**
     * Apply a material change for an item meta. Do not use under any
     * circumstances.
     *
     * @param meta meta
     * @param material material
     * @return updated material
     * @throws IllegalArgumentException if bad material or data
     * @deprecated for internal use only
     */
    @Deprecated
    @NotNull
    Material updateMaterial(@NotNull final ItemMeta meta, @NotNull final Material material) throws IllegalArgumentException;

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
}
