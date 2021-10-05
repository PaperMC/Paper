package org.bukkit.structure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureManager {

    /**
     * Gets the currently registered structures.
     * <p>
     * These are the currently loaded structures that the StructureManager is
     * aware of. When a structure block refers to a structure, these structures
     * are checked first. If the specified structure is not found among the
     * currently registered structures, the StructureManager may dynamically
     * read the structure from the primary world folder, DataPacks, or the
     * server's own resources. Structures can be registered via {@link
     * #registerStructure(NamespacedKey, Structure)}
     *
     * @return an unmodifiable shallow copy of the currently registered
     * structures
     */
    @NotNull
    Map<NamespacedKey, Structure> getStructures();

    /**
     * Gets a registered Structure.
     *
     * @param structureKey The key for which to get the structure
     * @return The structure that belongs to the structureKey or
     * <code>null</code> if there is none registered for that key.
     */
    @Nullable
    Structure getStructure(@NotNull NamespacedKey structureKey);

    /**
     * Registers the given structure. See {@link #getStructures()}.
     *
     * @param structureKey The key for which to register the structure
     * @param structure The structure to register
     * @return The structure for the specified key, or <code>null</code> if the
     * structure could not be found.
     */
    @Nullable
    Structure registerStructure(@NotNull NamespacedKey structureKey, @NotNull Structure structure);

    /**
     * Unregisters a structure. Unregisters the specified structure. If the
     * structure still exists in the primary world folder, a DataPack, or is
     * part of the server's own resources, it may be loaded and registered again
     * when it is requested by a plugin or the server itself.
     *
     * @param structureKey The key for which to save the structure for
     * @return The structure that was registered for that key or
     * <code>null</code> if there was none
     */
    @Nullable
    Structure unregisterStructure(@NotNull NamespacedKey structureKey);

    /**
     * Loads a structure for the specified key and optionally {@link
     * #registerStructure(NamespacedKey, Structure) registers} it.
     * <p>
     * This will first check the already loaded {@link #getStructures()
     * registered structures}, and otherwise load the structure from the primary
     * world folder, DataPacks, and the server's own resources (in this order).
     * <p>
     * When loading the structure from the primary world folder, the given key
     * is translated to a file as specified by
     * {@link #getStructureFile(NamespacedKey)}.
     *
     * @param structureKey The key for which to load the structure
     * @param register <code>true</code> to register the loaded structure.
     * @return The structure, or <code>null</code> if no structure was found for
     * the specified key
     */
    @Nullable
    Structure loadStructure(@NotNull NamespacedKey structureKey, boolean register);

    /**
     * Loads the structure for the specified key and automatically registers it.
     * See {@link #loadStructure(NamespacedKey, boolean)}.
     *
     * @param structureKey The key for which to load the structure
     * @return The structure for the specified key, or <code>null</code> if the
     * structure could not be found.
     */
    @Nullable
    Structure loadStructure(@NotNull NamespacedKey structureKey);

    /**
     * Saves the currently {@link #getStructures() registered structure} for the
     * specified {@link NamespacedKey key} to the primary world folder as
     * specified by {#getStructureFile(NamespacedKey}.
     *
     * @param structureKey The key for which to save the structure for
     */
    void saveStructure(@NotNull NamespacedKey structureKey);

    /**
     * Saves a structure with a given key to the primary world folder.
     *
     * @param structureKey The key for which to save the structure for
     * @param structure The structure to save for this structureKey
     */
    void saveStructure(@NotNull NamespacedKey structureKey, @NotNull Structure structure) throws IOException;

    /**
     * Unregisters the specified structure and deletes its {@link
     * #getStructureFile(NamespacedKey) structure file} from the primary world
     * folder. Note that this method cannot be used to delete vanilla Minecraft
     * structures, or structures from DataPacks. Unregistering these structures
     * will however work fine.
     *
     * @param structureKey The key of the structure to remove
     * @throws IOException If the file could not be removed for some reason.
     */
    void deleteStructure(@NotNull NamespacedKey structureKey) throws IOException;

    /**
     * Deletes the {@link #getStructureFile(NamespacedKey) structure file} for
     * the specified structure from the primary world folder. Note that this
     * method cannot be used to delete vanilla Minecraft structures, or
     * structures from DataPacks. Unregistering these structures will however
     * work fine.
     *
     * @param structureKey The key of the structure to remove
     * @param unregister Whether to also unregister the specified structure if
     * it is currently loaded.
     * @throws IOException If the file could not be removed for some reason.
     */
    void deleteStructure(@NotNull NamespacedKey structureKey, boolean unregister) throws IOException;

    /**
     * Gets the location where a structure file would exist in the primary world
     * directory based on the NamespacedKey using the format
     * world/generated/{NAMESPACE}/structures/{KEY}.nbt. This method will always
     * return a file, even if none exists at the moment.
     *
     * @param structureKey The key to build the filepath from.
     * @return The location where a file with this key would be.
     */
    @NotNull
    File getStructureFile(@NotNull NamespacedKey structureKey);

    /**
     * Reads a Structure from disk.
     *
     * @param file The file of the structure
     * @return The read structure
     * @throws IOException when the given file can not be read from
     */
    @NotNull
    Structure loadStructure(@NotNull File file) throws IOException;

    /**
     * Reads a Structure from a stream.
     *
     * @param inputStream The file of the structure
     * @return The read Structure
     */
    @NotNull
    Structure loadStructure(@NotNull InputStream inputStream) throws IOException;

    /**
     * Save a structure to a file. This will overwrite a file if it already
     * exists.
     *
     * @param file the target to save to.
     * @param structure the Structure to save.
     * @throws IOException when the given file can not be written to.
     */
    void saveStructure(@NotNull File file, @NotNull Structure structure) throws IOException;

    /**
     * Save a structure to a stream.
     *
     * @param outputStream the stream to write to.
     * @param structure the Structure to save.
     * @throws IOException when the given file can not be written to.
     */
    void saveStructure(@NotNull OutputStream outputStream, @NotNull Structure structure) throws IOException;

    /**
     * Creates a new empty structure.
     *
     * @return an empty structure.
     */
    @NotNull
    Structure createStructure();

    /**
     * Creates a copy of this structure.
     *
     * @param structure The structure to copy
     * @return a copy of the structure
     */
    @NotNull
    Structure copy(@NotNull Structure structure);
}
