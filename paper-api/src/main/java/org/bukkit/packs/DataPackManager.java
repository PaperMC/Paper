package org.bukkit.packs;

import java.util.Collection;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manager of data packs.
 */
@ApiStatus.Experimental
public interface DataPackManager {

    /**
     * Return all the available {@link DataPack}s on the server.
     *
     * @return a Collection of {@link DataPack}
     */
    @NotNull
    public Collection<DataPack> getDataPacks();

    /**
     * Gets a {@link DataPack} by its key.
     *
     * @param dataPackKey the key of the {@link DataPack}
     * @return the {@link DataPack} or null if it does not exist
     */
    @Nullable
    public DataPack getDataPack(@NotNull NamespacedKey dataPackKey);

    /**
     * Return all the enabled {@link DataPack} in the World.
     *
     * @param world the world to search
     * @return a Collection of {@link DataPack}
     */
    @NotNull
    public Collection<DataPack> getEnabledDataPacks(@NotNull World world);

    /**
     * Return all the disabled {@link DataPack} in the World.
     *
     * @param world the world to search
     * @return a Collection of {@link DataPack}
     */
    @NotNull
    public Collection<DataPack> getDisabledDataPacks(@NotNull World world);

    /**
     * Gets if the Material is enabled for use by the features in World.
     *
     * @param material Material to check (needs to be an {@link Material#isItem()} or {@link Material#isBlock()})
     * @param world World to check
     * @return {@code True} if the Item/Block related to the material is enabled
     */
    public boolean isEnabledByFeature(@NotNull Material material, @NotNull World world);

    /**
     * Gets if the ItemType is enabled for use by the features in World.
     *
     * @param itemType ItemType to check
     * @param world World to check
     * @return {@code True} if the ItemType is enabled
     * @apiNote this method is not ready for public usage yet
     */
    @ApiStatus.Internal
    public boolean isEnabledByFeature(@NotNull ItemType itemType, @NotNull World world);

    /**
     * Gets if the BlockType is enabled for use by the features in World.
     *
     * @param blockType BlockType to check
     * @param world World to check
     * @return {@code True} if the BlockType is enabled
     * @apiNote this method is not ready for public usage yet
     */
    @ApiStatus.Internal
    public boolean isEnabledByFeature(@NotNull BlockType blockType, @NotNull World world);

    /**
     * Gets if the EntityType is enabled for use by the Features in World.
     *
     * @param entityType EntityType to check
     * @param world World to check
     * @return {@code True} if the type of entity is enabled
     */
    public boolean isEnabledByFeature(@NotNull EntityType entityType, @NotNull World world);
}
