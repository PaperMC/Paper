package org.bukkit;

import io.papermc.paper.entity.EntitySerializationFlag;
import io.papermc.paper.registry.RegistryKey;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface provides value conversions that may be specific to a
 * runtime, or have arbitrary meaning (read: magic values).
 * <p>
 * Their existence and behavior is not guaranteed across future versions. They
 * may be poorly named, throw exceptions, have misleading parameters, or any
 * other bad programming practice.
 */
@Deprecated(since = "1.7.2")
public interface UnsafeValues {

    Material toLegacy(Material material);

    Material fromLegacy(Material material);

    Material fromLegacy(MaterialData material);

    Material fromLegacy(MaterialData material, boolean itemPriority);

    BlockData fromLegacy(Material material, byte data);

    Material getMaterial(String material, int version);

    int getDataVersion();

    ItemStack modifyItemStack(ItemStack item, String components);

    void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException;

    byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz);

    /**
     * Load an advancement represented by the specified string into the server.
     * The advancement format is governed by Minecraft and has no specified layout.
     * <p>
     * It is currently a JSON object, as described by the <a href="https://minecraft.wiki/w/Advancement_definition">Minecraft wiki</a>.
     * <p>
     * Loaded advancements will be stored and persisted across server restarts
     * and reloads.
     *
     * @param key the unique advancement key
     * @param advancement representation of the advancement
     * @return the loaded advancement or {@code null} if an error occurred
     */
    default @Nullable Advancement loadAdvancement(final NamespacedKey key, @Language("json") final String advancement) {
        return this.loadAdvancement(key, advancement, true);
    }

    /**
     * Load an advancement represented by the specified string into the server.
     * The advancement format is governed by Minecraft and has no specified layout.
     * <p>
     * It is currently a JSON object, as described by the <a href="https://minecraft.wiki/w/Advancement_definition">Minecraft wiki</a>.
     * <p>
     * Loaded advancements will only be stored and persisted across server restarts
     * and reloads, if the {@code persist} parameter is set to {@code true}.
     *
     * @param key the unique advancement key
     * @param advancement representation of the advancement
     * @param persist whether to store this advancement in the bukkit datapack for persistence
     * @return the loaded advancement or {@code null} if an error occurred
     */
    @Nullable Advancement loadAdvancement(Key key, @Language("json") String advancement, boolean persist);

    /**
     * Load multiple advancements represented by the specified strings into the server.
     * The advancement format is governed by Minecraft and has no specified layout.
     * <p>
     * It is currently a JSON object, as described by the <a href="https://minecraft.wiki/w/Advancement_definition">Minecraft wiki</a>.
     * <p>
     * Loaded advancements will only be stored and persisted across server restarts
     * and reloads, if the {@code persist} parameter is set to true.
     * <p>
     * Callers should be prepared for {@link Exception} to be thrown.
     *
     * @param advancements the advancements to register. The key is the unique advancement key and the value is the advancement's JSON representation
     * @param persist whether to store this advancement in the bukkit datapack for persistence
     * @return list of all successfully loaded advancements
     */
    List<Advancement> loadAdvancements(Map<Key, String> advancements, boolean persist);

    /**
     * Delete an advancement which was loaded and saved by
     * {@link #loadAdvancement(Key, String, boolean)} or {@link #loadAdvancements(Map, boolean)}.
     * <p>
     * This method will only remove advancement from persistent storage. It
     * should be accompanied by a call to {@link Server#reloadData()} in order
     * to fully remove it from the running instance.
     *
     * @param key the unique advancement key
     * @return true if a file matching this key was found and deleted
     */
    boolean removeAdvancement(NamespacedKey key);

    /**
     * Do not use, method will get removed, and the plugin won't run
     *
     * @param key of the potion type
     * @return an internal potion data
     */
    @ApiStatus.Internal
    @Deprecated(since = "1.20.2", forRemoval = true)
    PotionType.InternalPotionData getInternalPotionData(NamespacedKey key);

    @ApiStatus.Internal
    String get(Class<?> elementClass, String value);

    @ApiStatus.Internal
    @Nullable <B extends Keyed> B get(RegistryKey<B> registry, NamespacedKey key);

    // Paper start
    @Deprecated(forRemoval = true)
    boolean isSupportedApiVersion(String apiVersion);

    @Deprecated(forRemoval = true)
    static boolean isLegacyPlugin(org.bukkit.plugin.Plugin plugin) {
        return !Bukkit.getUnsafe().isSupportedApiVersion(plugin.getDescription().getAPIVersion());
    }
    // Paper end

    // Paper start
    /**
     * Serializes this itemstack to json format.
     * It is safe for data migrations as it will use the built-in data converter instead of bukkit's
     * dangerous serialization system.
     * <p>
     * The emitted json object's format will inherently change across versions and hence should not be used for
     * non-development purposes like plugin configurations or end-user input.
     *
     * @return json object representing this item.
     * @see #deserializeItemFromJson(com.google.gson.JsonObject)
     * @throws IllegalArgumentException if the passed itemstack is {@link ItemStack#empty()}.
     */
    @NotNull
    com.google.gson.JsonObject serializeItemAsJson(@NotNull ItemStack itemStack);

    /**
     * Creates an itemstack from a json object.
     * <p>
     * This method expects a json object in the format emitted by {@link #serializeItemAsJson(ItemStack)}.
     * <p>
     * The emitted json object's format will inherently change across versions and hence should not be used for
     * non-development purposes like plugin configurations or end-user input.
     *
     * @param data object representing an item in Json format
     * @return the deserialize item stack, migrated to the latest data version if needed.
     * @throws IllegalArgumentException if the json object is not a valid item
     * @see #serializeItemAsJson(ItemStack)
     */
    @NotNull ItemStack deserializeItemFromJson(@NotNull com.google.gson.JsonObject data) throws IllegalArgumentException;

    /**
     * Serializes the provided entity as GZip-compressed NBT.
     *
     * @param entity entity
     * @return serialized entity data
     * @apiNote For high-frequency use, consider {@link #serializeEntity(Entity, boolean, EntitySerializationFlag...)}
     * to avoid compression overhead.
     * @see #serializeEntity(Entity, EntitySerializationFlag...)
     * @see #deserializeEntity(byte[], World, boolean, boolean)
     * @throws IllegalArgumentException if couldn't serialize the entity
     * @since 1.17.1
     */
    default byte @NotNull [] serializeEntity(@NotNull Entity entity) {
        return serializeEntity(entity, new EntitySerializationFlag[0]);
    }

    /**
     * Serializes the provided entity as GZip-compressed NBT.
     *
     * @param entity entity
     * @param serializationFlags serialization flags
     * @return serialized entity data
     * @apiNote For high-frequency use, consider {@link #serializeEntity(Entity, boolean, EntitySerializationFlag...)}
     * to avoid compression overhead.
     * @throws IllegalArgumentException if couldn't serialize the entity
     * @see #deserializeEntity(byte[], World, boolean, boolean)
     * @since 1.21.4
     */
    default byte @NotNull [] serializeEntity(@NotNull Entity entity, @NotNull EntitySerializationFlag... serializationFlags) {
        return serializeEntity(entity, true, serializationFlags);
    }

    /**
     * Serializes the provided entity as optionally GZip-compressed NBT.
     *
     * @param entity entity
     * @param compress true for compressed GZip output, false for uncompressed output.
     * @param serializationFlags serialization flags
     * @return serialized entity data
     * @throws IllegalArgumentException if couldn't serialize the entity
     * @see #deserializeEntity(byte[], World, boolean, boolean)
     * @since 26.2
     */
    byte @NotNull [] serializeEntity(@NotNull Entity entity, boolean compress, @NotNull EntitySerializationFlag... serializationFlags);

    /**
     * Serializes the provided entity as uncompressed NBT to the provided OutputStream.<br>
     * The provided stream is passed as-is, it is the caller's responsibility to handle buffering.
     *
     * @param entity entity
     * @param output the stream to write the data to
     * @param serializationFlags serialization flags
     * @throws IllegalArgumentException if it couldn't serialize the entity
     * @throws java.io.IOException if there was an IO problem
     * @see #deserializeEntity(java.io.InputStream, World, boolean, boolean)
     * @since 26.2
     */
    void serializeEntity(@NotNull Entity entity, @NotNull java.io.OutputStream output, @NotNull EntitySerializationFlag... serializationFlags) throws java.io.IOException;

    /**
     * Deserializes the entity from GZip-compressed NBT data.
     * <br>The entity's {@link java.util.UUID} as well as passengers will not be preserved.
     *
     * @param data serialized entity data
     * @param world world
     * @return deserialized entity
     * @throws IllegalArgumentException if invalid serialized entity data provided
     * @see #deserializeEntity(byte[], World, boolean, boolean)
     * @see #serializeEntity(Entity, EntitySerializationFlag...)
     * @see Entity#spawnAt(Location, CreatureSpawnEvent.SpawnReason)
     * @since 1.17.1
     */
    default @NotNull Entity deserializeEntity(byte @NotNull [] data, @NotNull World world) {
        return deserializeEntity(data, world, false);
    }

    /**
     * Deserializes the entity from GZip-compressed NBT data.
     * <br>The entity's passengers will not be preserved.
     *
     * @param data serialized entity data
     * @param world world
     * @param preserveUUID whether to preserve the entity's uuid
     * @return deserialized entity
     * @throws IllegalArgumentException if invalid serialized entity data provided
     * @see #deserializeEntity(byte[], World, boolean, boolean)
     * @see #serializeEntity(Entity, EntitySerializationFlag...)
     * @see Entity#spawnAt(Location, CreatureSpawnEvent.SpawnReason)
     * @since 1.17.1
     */
    default @NotNull Entity deserializeEntity(byte @NotNull [] data, @NotNull World world, boolean preserveUUID) {
        return deserializeEntity(data, world, preserveUUID, false);
    }

    /**
     * Deserializes the entity from GZip-compressed NBT data.
     *
     * @param data serialized entity data
     * @param world world
     * @param preserveUUID whether to preserve uuids of the entity and its passengers
     * @param preservePassengers whether to preserve passengers
     * @return deserialized entity
     * @throws IllegalArgumentException if invalid serialized entity data provided
     * @see #serializeEntity(Entity, EntitySerializationFlag...)
     * @see Entity#spawnAt(Location, CreatureSpawnEvent.SpawnReason)
     * @since 1.21.4
     */
    default @NotNull Entity deserializeEntity(byte @NotNull [] data, @NotNull World world, boolean preserveUUID, boolean preservePassengers) {
        return deserializeEntity(data, true, world, preserveUUID, preservePassengers);
    }

    /**
     * Deserializes the entity from NBT data.
     *
     * @param data serialized entity data
     * @param decompress if the input needs to be decompressed. See {@link #serializeEntity(Entity, boolean, EntitySerializationFlag...)}
     * @param world world
     * @param preserveUUID whether to preserve uuids of the entity and its passengers
     * @param preservePassengers whether to preserve passengers
     * @return deserialized entity
     * @throws IllegalArgumentException if invalid serialized entity data provided
     * @see #serializeEntity(Entity, EntitySerializationFlag...)
     * @see Entity#spawnAt(Location, CreatureSpawnEvent.SpawnReason)
     * @since 26.2
     */
    @NotNull Entity deserializeEntity(byte @NotNull [] data, boolean decompress, @NotNull World world, boolean preserveUUID, boolean preservePassengers);

    /**
     * Deserializes the entity from a stream of uncompressed NBT data.<br>
     * The provided stream is passed as-is, it is the caller's responsibility to handle buffering.
     *
     * @param input the InputStream of raw, uncompressed NBT data
     * @param world world
     * @param preserveUUID whether to preserve uuids of the entity and its passengers
     * @param preservePassengers whether to preserve passengers
     * @return deserialized entity
     * @throws IllegalArgumentException if invalid serialized entity data provided
     * @throws java.io.IOException if there was an IO problem
     * @see #serializeEntity(Entity, java.io.OutputStream, EntitySerializationFlag...)
     * @see Entity#spawnAt(Location, CreatureSpawnEvent.SpawnReason)
     * @since 26.2
     */
    @NotNull Entity deserializeEntity(@NotNull java.io.InputStream input, @NotNull World world, boolean preserveUUID, boolean preservePassengers) throws java.io.IOException;

    /**
     * Creates and returns the next EntityId available.
     * <p>
     * Use this when sending custom packets, so that there are no collisions on the client or server.
     */
    int nextEntityId(final World world);

    /**
     * Just don't use it.
     */
    @org.jetbrains.annotations.NotNull String getMainLevelName();

    /**
     * Returns the server's protocol version.
     *
     * @return the server's protocol version
     */
    int getProtocolVersion();
    // Paper end

    @NotNull ItemStack deserializeStack(@NotNull Map<String, Object> args);

    /**
     * Deserializes a {@link HoverEvent.ShowItem} hover event value into an {@code ItemStack}.
     *
     * @param itemHover the hover to deserialize
     * @return the deserialized {@code ItemStack}
     */
    @NotNull ItemStack deserializeItemHover(HoverEvent.@NotNull ShowItem itemHover);

}
