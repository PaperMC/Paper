package org.bukkit;

import com.google.common.collect.Multimap;
import io.papermc.paper.entity.EntitySerializationFlag;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

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
    // Paper start
    net.kyori.adventure.text.flattener.ComponentFlattener componentFlattener();
    @Deprecated(forRemoval = true) net.kyori.adventure.text.serializer.plain.PlainComponentSerializer plainComponentSerializer();
    @Deprecated(forRemoval = true) net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer plainTextSerializer();
    @Deprecated(forRemoval = true) net.kyori.adventure.text.serializer.gson.GsonComponentSerializer gsonComponentSerializer();
    @Deprecated(forRemoval = true) net.kyori.adventure.text.serializer.gson.GsonComponentSerializer colorDownsamplingGsonComponentSerializer();
    @Deprecated(forRemoval = true) net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer legacyComponentSerializer();
    net.kyori.adventure.text.Component resolveWithContext(net.kyori.adventure.text.Component component, org.bukkit.command.CommandSender context, org.bukkit.entity.Entity scoreboardSubject, boolean bypassPermissions) throws java.io.IOException;
    // Paper end

    Material toLegacy(Material material);

    Material fromLegacy(Material material);

    Material fromLegacy(MaterialData material);

    Material fromLegacy(MaterialData material, boolean itemPriority);

    BlockData fromLegacy(Material material, byte data);

    Material getMaterial(String material, int version);

    int getDataVersion();

    ItemStack modifyItemStack(ItemStack stack, String arguments);

    void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException;

    byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz);

    /**
     * Load an advancement represented by the specified string into the server.
     * The advancement format is governed by Minecraft and has no specified
     * layout.
     * <br>
     * It is currently a JSON object, as described by the <a href="https://minecraft.wiki/w/Advancements">Minecraft wiki</a>.
     * <br>
     * Loaded advancements will be stored and persisted across server restarts
     * and reloads.
     * <br>
     * Callers should be prepared for {@link Exception} to be thrown.
     *
     * @param key the unique advancement key
     * @param advancement representation of the advancement
     * @return the loaded advancement or null if an error occurred
     */
    Advancement loadAdvancement(NamespacedKey key, String advancement);

    /**
     * Delete an advancement which was loaded and saved by
     * {@link #loadAdvancement(org.bukkit.NamespacedKey, java.lang.String)}.
     * <br>
     * This method will only remove advancement from persistent storage. It
     * should be accompanied by a call to {@link Server#reloadData()} in order
     * to fully remove it from the running instance.
     *
     * @param key the unique advancement key
     * @return true if a file matching this key was found and deleted
     */
    boolean removeAdvancement(NamespacedKey key);

    @Deprecated(since = "1.21", forRemoval = true)
    Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot slot);

    @Deprecated(since = "1.21", forRemoval = true)
    CreativeCategory getCreativeCategory(Material material);

    @Deprecated(since = "1.21", forRemoval = true)
    String getBlockTranslationKey(Material material);

    @Deprecated(since = "1.21", forRemoval = true)
    String getItemTranslationKey(Material material);

    String getTranslationKey(EntityType entityType);

    String getTranslationKey(ItemStack itemStack);

    @Deprecated(since = "1.21.3", forRemoval = true)
    String getTranslationKey(Attribute attribute);

    // Paper - replace with better system

    /**
     * Do not use, method will get removed, and the plugin won't run
     *
     * @param key of the potion type
     * @return an internal potion data
     */
    @ApiStatus.Internal
    @Deprecated(since = "1.20.2", forRemoval = true)
    PotionType.InternalPotionData getInternalPotionData(NamespacedKey key);

    /**
     * Create a new {@link DamageSource.Builder}.
     *
     * @param damageType the {@link DamageType} to use
     * @return a {@link DamageSource.Builder}
     */
    @ApiStatus.Internal
    @NotNull
    DamageSource.Builder createDamageSourceBuilder(@NotNull DamageType damageType);

    @ApiStatus.Internal
    String get(Class<?> aClass, String value);

    @ApiStatus.Internal
    <B extends Keyed> B get(RegistryKey<B> registry, NamespacedKey key);

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
     * Called once by the version command on first use, then cached.
     */
    default com.destroystokyo.paper.util.VersionFetcher getVersionFetcher() {
        return new com.destroystokyo.paper.util.VersionFetcher.DummyVersionFetcher();
    }

    default @NotNull byte[] serializeItem(@NotNull ItemStack item) {
        return serializeItem(item, true);
    }

    @NotNull byte[] serializeItem(@NotNull ItemStack item, boolean compress);

    void serializeItemToNbt(@NotNull ItemStack item, @NotNull java.io.OutputStream output) throws java.io.IOException;

    @NotNull ItemStack deserializeItem(@NotNull byte[] data);

    @NotNull ItemStack deserializeItemFromNbt(@NotNull java.io.InputStream input) throws java.io.IOException;

    /**
     * Determines if the provided byte[] was compressed using GZip.<br>
     * This method does not mutate the input.
     * <p>
     * This method is intended for those who wish to manually handle NBT data emitted
     * from NBT serialization methods, such as {@link #serializeItem(ItemStack)}
     * <p>
     * NBT data always starts with 0A, which is the ID for an NBT Compound.
     * The next two bytes are a short denoting the length of the tag name.
     *
     * <p><b>NBT Format:</b>
     * <pre>
     * POS  DATA  DESCRIPTION
     * 0    0A    ID for NBT Compound
     * 1    ??    First byte of 2-byte string length {@link java.io.DataOutputStream#writeUTF(String)}
     * 2    ??    Second byte of 2-byte string length
     * </pre>
     *
     * Whereas GZip data always starts with the GZip header,
     * as defined by the GZip spec.
     * The first two bytes are always {@code 1F} and {@code 8B}.
     *
     * <p><b>GZip Format:</b>
     * <pre>
     * POS  DATA  DESCRIPTION
     * 0    1F    First GZip header byte
     * 1    8B    Second GZip header byte
     * 2    ??    Compression method
     * </pre>
     *
     * Using this difference, we can reliably determine if the data is compressed or
     * not by any of the NBT serialization methods provided by Paper.
     * <p>
     * This method will only reliably determine compression of raw NBT data
     * compressed by GZip.<br>
     * Don't use it with any other input.
     *
     * @param data the data to check
     * @return true if the data has a GZip header, false otherwise, including
     * if the {@code data.length} is below 2
     * @since 1.21.8
     */
    static boolean isGZipCompressedNbt(@NotNull byte[] data) {
        // if data length is below 2, it's always invalid data
        return data.length > 1
                && ((byte) 0x1F) == data[0]
                && ((byte) 0x8B) == data[1];
    }

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
     * Serializes the provided entity as GZip-compressed NBT.
     *
     * @param entity entity
     * @param compress true for compressed GZip output, false for raw NBT bytes.
     * @param serializationFlags serialization flags
     * @return serialized entity data
     * @throws IllegalArgumentException if couldn't serialize the entity
     * @see #deserializeEntity(byte[], World, boolean, boolean)
     * @since 1.21.8
     */
    byte @NotNull [] serializeEntity(@NotNull Entity entity, boolean compress, @NotNull EntitySerializationFlag... serializationFlags);

    /**
     * Serializes the provided entity as raw NBT to the provided OutputStream.
     *
     * @param entity entity
     * @param output the stream to write the data to
     * @param serializationFlags serialization flags
     * @throws IllegalArgumentException if it couldn't serialize the entity
     * @throws java.io.IOException if there was an IO problem
     * @see #deserializeEntityFromNbt(java.io.InputStream, World, boolean, boolean)
     * @since 1.21.8
     */
    void serializeEntityToNbt(@NotNull Entity entity, @NotNull java.io.OutputStream output, @NotNull EntitySerializationFlag... serializationFlags) throws java.io.IOException;

    /**
     * Deserializes the entity from NBT data.
     * <br>The entity's {@link java.util.UUID} as well as passengers will not be preserved.
     * <p>
     * If the data is compressed in the GZip format, it will be automatically decompressed.<br>
     * Such as the GZip-compressed data returned by {@link #serializeEntity(Entity, boolean, EntitySerializationFlag...)}
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
     * Deserializes the entity from NBT data.
     * <br>The entity's passengers will not be preserved.
     * <p>
     * If the data is compressed in the GZip format, it will be automatically decompressed.<br>
     * Such as the GZip-compressed data returned by {@link #serializeEntity(Entity, boolean, EntitySerializationFlag...)}
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
     * Deserializes the entity from NBT data.
     * <p>
     * If the data is compressed in the GZip format, it will be automatically decompressed.<br>
     * Such as the GZip-compressed data returned by {@link #serializeEntity(Entity, boolean, EntitySerializationFlag...)}
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
    @NotNull Entity deserializeEntity(byte @NotNull [] data, @NotNull World world, boolean preserveUUID, boolean preservePassengers);

    /**
     * Deserializes the entity from a stream of raw NBT data.
     *
     * @param input the InputStream of raw, uncompressed NBT data
     * @param world world
     * @param preserveUUID whether to preserve uuids of the entity and its passengers
     * @param preservePassengers whether to preserve passengers
     * @return deserialized entity
     * @throws IllegalArgumentException if invalid serialized entity data provided
     * @throws java.io.IOException if there was an IO problem
     * @see #serializeEntityToNbt(Entity, java.io.OutputStream, EntitySerializationFlag...)
     * @see Entity#spawnAt(Location, CreatureSpawnEvent.SpawnReason)
     * @since 1.21.8
     */
    @NotNull Entity deserializeEntityFromNbt(@NotNull java.io.InputStream input, @NotNull World world, boolean preserveUUID, boolean preservePassengers) throws java.io.IOException;

    /**
     * Creates and returns the next EntityId available.
     * <p>
     * Use this when sending custom packets, so that there are no collisions on the client or server.
     */
    public int nextEntityId();

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

    /**
     * Checks if an itemstack can be repaired with another itemstack.
     * Returns false if either argument's type is not an item ({@link Material#isItem()}).
     *
     * @param itemToBeRepaired the itemstack to be repaired
     * @param repairMaterial the repair material
     * @return true if valid repair, false if not
     */
    public boolean isValidRepairItemStack(@org.jetbrains.annotations.NotNull ItemStack itemToBeRepaired, @org.jetbrains.annotations.NotNull ItemStack repairMaterial);

    /**
     * Checks if the entity represented by the namespaced key has default attributes.
     *
     * @param entityKey the entity's key
     * @return true if it has default attributes
     */
    boolean hasDefaultEntityAttributes(@org.jetbrains.annotations.NotNull NamespacedKey entityKey);

    /**
     * Gets the default attributes for the entity represented by the namespaced key.
     *
     * @param entityKey the entity's key
     * @return an unmodifiable instance of Attributable for reading default attributes.
     * @throws IllegalArgumentException if the entity does not exist of have default attributes (use {@link #hasDefaultEntityAttributes(NamespacedKey)} first)
     */
    @org.jetbrains.annotations.NotNull org.bukkit.attribute.Attributable getDefaultEntityAttributes(@org.jetbrains.annotations.NotNull NamespacedKey entityKey);
    // Paper end

    // Paper start - namespaced key biome methods
    /**
     * Gets the {@link NamespacedKey} for the biome at the given location.
     *
     * @param accessor The {@link RegionAccessor} of the provided coordinates
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @deprecated custom biomes are properly supported in API now
     * @return the biome's {@link NamespacedKey}
     */
    @org.jetbrains.annotations.NotNull
    @Deprecated(since = "1.21.3", forRemoval = true)
    NamespacedKey getBiomeKey(RegionAccessor accessor, int x, int y, int z);

    /**
     * Sets the biome at the given location to a biome registered
     * to the given {@link NamespacedKey}. If no biome by the given
     * {@link NamespacedKey} exists, an {@link IllegalStateException}
     * will be thrown.
     *
     * @param accessor The {@link RegionAccessor} of the provided coordinates
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @param biomeKey Biome key
     * @deprecated custom biomes are properly supported in API now
     * @throws IllegalStateException if no biome by the given key is registered.
     */
    @Deprecated(since = "1.21.3", forRemoval = true)
    void setBiomeKey(RegionAccessor accessor, int x, int y, int z, NamespacedKey biomeKey);
    // Paper end - namespaced key biome methods

    String getStatisticCriteriaKey(@NotNull org.bukkit.Statistic statistic); // Paper - fix custom stats criteria creation

    // Paper start - spawn egg color visibility
    /**
     * Obtains the underlying color informating for a spawn egg of a given
     * entity type, or {@code null} if the entity passed does not have a spawn egg.
     * Spawn eggs have two colors - the background layer (0), and the
     * foreground layer (1)
     *
     * @param entityType the entity type to get the color for
     * @param layer the texture layer to get a color for
     * @return the color of the layer for the entity's spawn egg
     * @deprecated the color is no longer available to the server
     */
    @Deprecated(since = "1.21.4")
    @Nullable Color getSpawnEggLayerColor(EntityType entityType, int layer);
    // Paper end - spawn egg color visibility

    // Paper start - lifecycle event API
    /**
     * @hidden
     */
    @org.jetbrains.annotations.ApiStatus.Internal
    io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<org.bukkit.plugin.Plugin> createPluginLifecycleEventManager(final org.bukkit.plugin.java.JavaPlugin plugin, final java.util.function.BooleanSupplier registrationCheck);
    // Paper end - lifecycle event API

    @NotNull java.util.List<net.kyori.adventure.text.Component> computeTooltipLines(@NotNull ItemStack itemStack, @NotNull io.papermc.paper.inventory.tooltip.TooltipContext tooltipContext, @Nullable org.bukkit.entity.Player player); // Paper - expose itemstack tooltip lines

    ItemStack createEmptyStack(); // Paper - proxy ItemStack

    @NotNull Map<String, Object> serializeStack(ItemStack itemStack);

    @NotNull ItemStack deserializeStack(@NotNull Map<String, Object> args);
}
