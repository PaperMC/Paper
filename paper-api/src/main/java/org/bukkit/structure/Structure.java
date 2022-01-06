package org.bukkit.structure;

import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.RegionAccessor;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a structure.
 * <p>
 * A structure is a mutable template of captured blocks and entities that can be
 * copied back into the world. The {@link StructureManager}, retrieved via
 * {@link org.bukkit.Server#getStructureManager()}, allows you to create new
 * structures, load existing structures, and save structures.
 * <p>
 * In order for a structure to be usable by structure blocks, it needs to be
 * null {@link StructureManager#registerStructure(org.bukkit.NamespacedKey, Structure)
 * registered} with the {@link StructureManager}, or located in the primary
 * world folder, a DataPack, or the server's own default resources, so that the
 * StructureManager can find it.
 */
public interface Structure extends PersistentDataHolder {

    /**
     * Gets the current size of the structure.
     * <p>
     * The size of the structure may not be fixed.
     *
     * @return A new vector that represents the size of the structure along each
     * axis.
     */
    @NotNull
    BlockVector getSize();

    /**
     * Gets a list of available block palettes.
     *
     * @return a list of available variants of this structure.
     */
    @NotNull
    List<Palette> getPalettes();

    /**
     * Gets the number of palettes in this structure.
     *
     * @return The number of palettes in this structure
     */
    int getPaletteCount();

    /**
     * Gets a list of entities that have been included in the Structure.
     *
     * The entity positions are offsets relative to the structure's position
     * that is provided once the structure is placed into the world.
     *
     * @return a list of Entities included in the Structure.
     */
    @NotNull
    List<Entity> getEntities();

    /**
     * Gets the number of entities in this structure.
     *
     * @return The number of entities in this structure
     */
    int getEntityCount();

    /**
     * Place a structure in the world.
     *
     * @param location The location to place the structure at.
     * @param includeEntities If the entities present in the structure should be
     * spawned.
     * @param structureRotation The rotation of the structure.
     * @param mirror The mirror settings of the structure.
     * @param palette The palette index of the structure to use, starting at
     * {@code 0}, or {@code -1} to pick a random palette.
     * @param integrity Determines how damaged the building should look by
     * randomly skipping blocks to place. This value can range from 0 to 1. With
     * 0 removing all blocks and 1 spawning the structure in pristine condition.
     * @param random The randomizer used for setting the structure's
     * {@link org.bukkit.loot.LootTable}s and integrity.
     */
    void place(@NotNull Location location, boolean includeEntities, @NotNull StructureRotation structureRotation, @NotNull Mirror mirror, int palette, float integrity, @NotNull Random random);

    /**
     * Place a structure in the world.
     *
     * @param regionAccessor The world to place the structure in.
     * @param location The location to place the structure at.
     * @param includeEntities If the entities present in the structure should be
     * spawned.
     * @param structureRotation The rotation of the structure.
     * @param mirror The mirror settings of the structure.
     * @param palette The palette index of the structure to use, starting at
     * {@code 0}, or {@code -1} to pick a random palette.
     * @param integrity Determines how damaged the building should look by
     * randomly skipping blocks to place. This value can range from 0 to 1. With
     * 0 removing all blocks and 1 spawning the structure in pristine condition.
     * @param random The randomizer used for setting the structure's
     * {@link org.bukkit.loot.LootTable}s and integrity.
     */
    void place(@NotNull RegionAccessor regionAccessor, @NotNull BlockVector location, boolean includeEntities, @NotNull StructureRotation structureRotation, @NotNull Mirror mirror, int palette, float integrity, @NotNull Random random);

    /**
     * Fills the structure from an area in a world. The origin and size will be
     * calculated automatically from the two corners provided.
     * <p>
     * Be careful as this will override the current data of the structure.
     * <p>
     * Be aware that this method allows for creating structures larger than the
     * 48x48x48 size that Minecraft's Structure blocks support. Any structures
     * saved this way can not be loaded by using a structure block. Using the
     * API however will still work.
     *
     * @param corner1 A corner of the structure.
     * @param corner2 The corner opposite from corner1.
     * @param includeEntities true if entities should be included in the saved
     * structure.
     */
    void fill(@NotNull Location corner1, @NotNull Location corner2, boolean includeEntities);

    /**
     * Fills the Structure from an area in a world, starting at the specified
     * origin and extending in each axis according to the specified size vector.
     * <p>
     * Be careful as this will override the current data of the structure.
     * <p>
     * Be aware that this method allows for saving structures larger than the
     * 48x48x48 size that Minecraft's Structure blocks support. Any structures
     * saved this way can not be loaded by using a structure block. Using the
     * API however will still work.
     *
     * @param origin The origin of the structure.
     * @param size The size of the structure, must be at least 1x1x1.
     * @param includeEntities true if entities should be included in the saved
     * structure.
     * @throws IllegalArgumentException Thrown if size is smaller than 1x1x1
     */
    void fill(@NotNull Location origin, @NotNull BlockVector size, boolean includeEntities);
}
