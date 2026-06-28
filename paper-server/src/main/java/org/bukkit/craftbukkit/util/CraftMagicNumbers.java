package org.bukkit.craftbukkit.util;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.adventure.AdventureCodecs;
import io.papermc.paper.entity.EntitySerializationFlag;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.MCUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Stream;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.legacy.CraftLegacy;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;
import org.slf4j.Logger;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CraftMagicNumbers INSTANCE = new CraftMagicNumbers();
    public static final boolean DISABLE_OLD_API_SUPPORT = Boolean.getBoolean("paper.disableOldApiSupport"); // Paper

    private final Commodore commodore = new Commodore();

    private CraftMagicNumbers() {}

    public static BlockState getBlock(MaterialData material) {
        return CraftMagicNumbers.getBlock(material.getItemType(), material.getData());
    }

    public static BlockState getBlock(Material material, byte data) {
        return CraftLegacy.fromLegacyData(CraftLegacy.toLegacy(material), data);
    }

    public static MaterialData getMaterial(BlockState data) {
        return CraftLegacy.toLegacy(CraftMagicNumbers.getMaterial(data.getBlock())).getNewData(CraftMagicNumbers.toLegacyData(data));
    }

    public static Item getItem(Material material, short data) {
        if (material.isLegacy()) {
            return CraftLegacy.fromLegacyData(CraftLegacy.toLegacy(material), data);
        }

        return CraftMagicNumbers.getItem(material);
    }

    public static MaterialData getMaterialData(Item item) {
        return CraftLegacy.toLegacyData(CraftMagicNumbers.getMaterial(item));
    }

    // ========================================================================
    private static final Map<Block, Material> BLOCK_MATERIAL = new HashMap<>();
    private static final Map<Item, Material> ITEM_MATERIAL = new HashMap<>();
    private static final Map<Material, Item> MATERIAL_ITEM = new HashMap<>();
    private static final Map<Material, Block> MATERIAL_BLOCK = new HashMap<>();

    static {
        for (Block block : BuiltInRegistries.BLOCK) {
            BLOCK_MATERIAL.put(block, Material.getMaterial(BuiltInRegistries.BLOCK.getKey(block).getPath().toUpperCase(Locale.ROOT)));
        }

        for (Item item : BuiltInRegistries.ITEM) {
            ITEM_MATERIAL.put(item, Material.getMaterial(BuiltInRegistries.ITEM.getKey(item).getPath().toUpperCase(Locale.ROOT)));
        }

        for (Material material : Material.values()) {
            if (material.isLegacy()) {
                continue;
            }

            Identifier key = CraftNamespacedKey.toMinecraft(material.getKey());
            BuiltInRegistries.ITEM.getOptional(key).ifPresent((item) -> {
                CraftMagicNumbers.MATERIAL_ITEM.put(material, item);
            });
            BuiltInRegistries.BLOCK.getOptional(key).ifPresent((block) -> {
                CraftMagicNumbers.MATERIAL_BLOCK.put(material, block);
            });
        }
    }

    public static Material getMaterial(Block block) {
        return CraftMagicNumbers.BLOCK_MATERIAL.get(block);
    }

    public static Material getMaterial(Item item) {
        return CraftMagicNumbers.ITEM_MATERIAL.getOrDefault(item, Material.AIR);
    }

    public static Item getItem(Material material) {
        if (material != null && material.isLegacy()) {
            material = CraftLegacy.fromLegacy(material);
        }

        return CraftMagicNumbers.MATERIAL_ITEM.get(material);
    }

    public static Block getBlock(Material material) {
        if (material != null && material.isLegacy()) {
            material = CraftLegacy.fromLegacy(material);
        }

        return CraftMagicNumbers.MATERIAL_BLOCK.get(material);
    }
    // ========================================================================

    public static byte toLegacyData(BlockState data) {
        return CraftLegacy.toLegacyData(data);
    }

    public Commodore getCommodore() {
        return this.commodore;
    }

    @Override
    public Material toLegacy(Material material) {
        return CraftLegacy.toLegacy(material);
    }

    @Override
    public Material fromLegacy(Material material) {
        return CraftLegacy.fromLegacy(material);
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        return CraftLegacy.fromLegacy(material);
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        return CraftLegacy.fromLegacy(material, itemPriority);
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        return CraftMagicNumbers.getBlock(material, data).asBlockData();
    }

    @Override
    public Material getMaterial(String material, int version) {
        Preconditions.checkArgument(material != null, "material == null");
        Preconditions.checkArgument(version <= this.getDataVersion(), "Newer version! Server downgrades are not supported!");

        // Fastpath up to date materials
        if (version == this.getDataVersion()) {
            return Material.getMaterial(material);
        }

        Dynamic<Tag> name = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf("minecraft:" + material.toLowerCase(Locale.ROOT)));
        Dynamic<Tag> converted = DataFixers.getDataFixer().update(References.ITEM_NAME, name, version, this.getDataVersion());

        if (name.equals(converted)) {
            converted = DataFixers.getDataFixer().update(References.BLOCK_NAME, name, version, this.getDataVersion());
        }

        return Material.matchMaterial(converted.asString(""));
    }

    @Override
    public int getDataVersion() {
        return SharedConstants.getCurrentVersion().dataVersion().version();
    }

    @Override
    public ItemStack modifyItemStack(ItemStack item, String components) {
        if (item.isEmpty()) {
            return item;
        }

        net.minecraft.world.item.ItemStack stack = CraftItemStack.unwrap(item); // mutation is expected as old behavior
        try {
            StringReader reader = new StringReader(item.getType().key().asString() + components);
            stack.applyComponents(new ItemParser(CraftRegistry.getMinecraftRegistry()).parse(reader).components());
            if (reader.canRead()) {
                throw new IllegalArgumentException("Trailing input found when parsing components: " + reader.getRemaining());
            }
        } catch (CommandSyntaxException ex) {
            throw new IllegalArgumentException("Could not parse components: " + components, ex);
        }
        return item;
    }

    private static File getBukkitDataPackFolder() {
        return new File(MinecraftServer.getServer().getWorldPath(LevelResource.DATAPACK_DIR).toFile(), "bukkit");
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        Preconditions.checkArgument(Bukkit.getAdvancement(key) == null, "Advancement %s already exists", key);
        Identifier resourceKey = CraftNamespacedKey.toMinecraft(key);

        JsonElement jsonelement = JsonParser.parseString(advancement);
        final net.minecraft.resources.RegistryOps<JsonElement> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JsonOps.INSTANCE); // Paper - use RegistryOps
        final net.minecraft.advancements.Advancement nms = net.minecraft.advancements.Advancement.CODEC.parse(ops, jsonelement).getOrThrow(JsonParseException::new); // Paper - use RegistryOps
        if (nms != null) {
            final com.google.common.collect.ImmutableMap.Builder<Identifier, AdvancementHolder> mapBuilder = com.google.common.collect.ImmutableMap.builder();
            mapBuilder.putAll(MinecraftServer.getServer().getAdvancements().advancements);

            final AdvancementHolder holder = new AdvancementHolder(resourceKey, nms);
            mapBuilder.put(resourceKey, holder);

            MinecraftServer.getServer().getAdvancements().advancements = mapBuilder.build();
            final net.minecraft.advancements.AdvancementTree tree = MinecraftServer.getServer().getAdvancements().tree();
            tree.addAll(java.util.List.of(holder));

            // recalculate advancement position
            final net.minecraft.advancements.AdvancementNode node = tree.get(resourceKey);
            if (node != null) {
                final net.minecraft.advancements.AdvancementNode root = node.root();
                if (root.holder().value().display().isPresent()) {
                    net.minecraft.advancements.TreeNodePosition.run(root);
                }
            }

            Advancement bukkit = Bukkit.getAdvancement(key);

            if (bukkit != null) {
                File file = new File(CraftMagicNumbers.getBukkitDataPackFolder(), "data" + File.separator + key.getNamespace() + File.separator + "advancements" + File.separator + key.getKey() + ".json");
                file.getParentFile().mkdirs();

                try {
                    Files.write(advancement, file, StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error saving advancement " + key, ex);
                }

                MinecraftServer.getServer().getPlayerList().getPlayers().forEach(player -> {
                    player.getAdvancements().reload(MinecraftServer.getServer().getAdvancements());
                    player.getAdvancements().flushDirty(player, false);
                });

                return bukkit;
            }
        }

        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        File file = new File(CraftMagicNumbers.getBukkitDataPackFolder(), "data" + File.separator + key.getNamespace() + File.separator + "advancements" + File.separator + key.getKey() + ".json");
        return file.delete();
    }

    @Override
    public void checkSupported(PluginDescriptionFile descriptionFile) throws InvalidPluginException {
        ApiVersion toCheck = ApiVersion.getOrCreateVersion(descriptionFile.getAPIVersion());
        ApiVersion minimumVersion = MinecraftServer.getServer().server.minimumAPI;

        if (toCheck.isNewerThan(ApiVersion.CURRENT)) {
            // Newer than supported
            throw new InvalidPluginException("Unsupported API version " + descriptionFile.getAPIVersion());
        }

        if (toCheck.isOlderThan(minimumVersion)) {
            // Older than supported
            throw new InvalidPluginException("Plugin API version " + descriptionFile.getAPIVersion() + " is lower than the minimum allowed version. Please update or replace it.");
        }

        if (!DISABLE_OLD_API_SUPPORT && toCheck.isOlderThan(ApiVersion.FLATTENING)) { // Paper
            CraftLegacy.init();
        }

        if (toCheck == ApiVersion.NONE) {
            Bukkit.getLogger().log(Level.WARNING, "Legacy plugin " + descriptionFile.getFullName() + " does not specify an api-version.");
        }
    }

    public static boolean isLegacy(PluginDescriptionFile pdf) {
        return pdf.getAPIVersion() == null;
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        // Paper start
        if (DISABLE_OLD_API_SUPPORT) {
            return clazz;
        }
        // Paper end
        try {
            clazz = this.commodore.convert(clazz, pdf.getName(), ApiVersion.getOrCreateVersion(pdf.getAPIVersion()), ((CraftServer) Bukkit.getServer()).activeCompatibilities);
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Fatal error trying to convert " + pdf.getFullName() + ":" + path, ex);
        }

        return clazz;
    }

    @Override
    public boolean isSupportedApiVersion(String apiVersion) {
        if (apiVersion == null) return false;
        final ApiVersion toCheck = ApiVersion.getOrCreateVersion(apiVersion);
        final ApiVersion minimumVersion = MinecraftServer.getServer().server.minimumAPI;

        return !toCheck.isNewerThan(ApiVersion.CURRENT) && !toCheck.isOlderThan(minimumVersion);
    }

    @Override
    public PotionType.InternalPotionData getInternalPotionData(NamespacedKey namespacedKey) {
        Potion potionRegistry = CraftRegistry.getMinecraftRegistry(Registries.POTION)
                .getOptional(CraftNamespacedKey.toMinecraft(namespacedKey)).orElseThrow();

        return new CraftPotionType(namespacedKey, potionRegistry);
    }

    @Override
    public String get(Class<?> elementClass, String value) {
        if (elementClass == Enchantment.class) {
            // We currently do not have any version-dependent remapping, so we can use current version
            return FieldRename.convertEnchantmentName(ApiVersion.CURRENT, value);
        }
        return value;
    }

    @Override
    public <B extends Keyed> B get(RegistryKey<B> registry, NamespacedKey namespacedKey) {
        // We currently do not have any version-dependent remapping, so we can use current version
        return CraftRegistry.get(registry, namespacedKey, ApiVersion.CURRENT);
    }

    private static final TagParser<Tag> SNBT_REGISTRY_UNAWARE_PARSER = TagParser.create(NbtOps.INSTANCE);
    @Override
    public @org.jetbrains.annotations.NotNull ItemStack deserializeStack(@org.jetbrains.annotations.NotNull final Map<String, Object> args) {
        final int version = args.getOrDefault("schema_version", 1) instanceof Number val ? val.intValue() : -1;

        final CompoundTag tag = new CompoundTag();
        args.forEach((key, value) -> {
            switch (key) {
                case "id" -> {
                    tag.putString("id", (String) value);
                }
                case "count" -> {
                    tag.putInt("count", ((Number) value).intValue());
                }
                case "components" -> {
                    if (version == 1) {
                        Map<String, String> componentMap;
                        if (value instanceof Map) {
                            componentMap = (Map<String, String>) value;
                        } else if (value instanceof MemorySection section) {
                            componentMap = new HashMap<>();
                            for (final String sectionKey : section.getKeys(false)) {
                                componentMap.put(sectionKey, section.getString(sectionKey));
                            }
                        } else {
                            throw new IllegalArgumentException("components must be a Map");
                        }
                        final CompoundTag componentsTag = new CompoundTag();
                        componentMap.forEach((componentKey, componentString) -> {
                            final Tag componentTag;
                            try {
                                componentTag = SNBT_REGISTRY_UNAWARE_PARSER.parseFully(componentString);
                            } catch (final CommandSyntaxException e) {
                                throw new RuntimeException("Error parsing item stack data components", e);
                            }
                            componentsTag.put(componentKey, componentTag);
                        });
                        tag.put("components", componentsTag);

                    } else {
                        throw new IllegalStateException("Unexpected version: " + version);
                    }
                }
                case SharedConstants.DATA_VERSION_TAG -> {
                    tag.putInt(SharedConstants.DATA_VERSION_TAG, ((Number) value).intValue());
                }
                case "==", "schema_version" -> {
                    // Ignore
                }
                default -> throw new IllegalStateException("Unexpected value: " + key);
            }
        });

        return MCUtil.deserializeItem(tag);
    }

    @Override
    public com.google.gson.JsonObject serializeItemAsJson(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "Cannot serialize empty ItemStack");
        Preconditions.checkArgument(!itemStack.isEmpty(), "Cannot serialize empty ItemStack");

        net.minecraft.core.RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
        com.mojang.serialization.DynamicOps<com.google.gson.JsonElement> ops = registryAccess.createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
        com.google.gson.JsonObject item;
        // Serialize as SNBT to preserve exact NBT types; vanilla codecs already can handle such deserialization.
        net.minecraft.world.item.component.CustomData.SERIALIZE_CUSTOM_AS_SNBT.set(true);
        try {
            item = net.minecraft.world.item.ItemStack.CODEC.encodeStart(ops, CraftItemStack.unwrap(itemStack)).getOrThrow().getAsJsonObject();
        } finally {
            net.minecraft.world.item.component.CustomData.SERIALIZE_CUSTOM_AS_SNBT.set(false);
        }
        item.addProperty(SharedConstants.DATA_VERSION_TAG, this.getDataVersion());
        return item;
    }

    @Override
    public ItemStack deserializeItemFromJson(com.google.gson.JsonObject data) throws IllegalArgumentException {
        Preconditions.checkNotNull(data, "null cannot be deserialized");

        final int dataVersion = data.get(SharedConstants.DATA_VERSION_TAG).getAsInt();
        final int currentVersion = this.getDataVersion();
        data = (com.google.gson.JsonObject) MinecraftServer.getServer().getFixerUpper().update(References.ITEM_STACK, new Dynamic<>(com.mojang.serialization.JsonOps.INSTANCE, data), dataVersion, currentVersion).getValue();
        com.mojang.serialization.DynamicOps<com.google.gson.JsonElement> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
        return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.CODEC.parse(ops, data).getOrThrow(IllegalArgumentException::new));
    }

    @Override
    public byte[] serializeEntity(org.bukkit.entity.Entity entity, EntitySerializationFlag... serializationFlags) {
        Preconditions.checkNotNull(entity, "null cannot be serialized");
        Preconditions.checkArgument(entity instanceof CraftEntity, "Only CraftEntities can be serialized");

        Set<EntitySerializationFlag> flags = Set.of(serializationFlags);
        final boolean serializePassengers = flags.contains(EntitySerializationFlag.PASSENGERS);
        final boolean forceSerialization = flags.contains(EntitySerializationFlag.FORCE);
        final boolean allowPlayerSerialization = flags.contains(EntitySerializationFlag.PLAYER);
        final boolean allowMiscSerialization = flags.contains(EntitySerializationFlag.MISC);
        final boolean includeNonSaveable = allowPlayerSerialization || allowMiscSerialization;

        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        (serializePassengers ? nmsEntity.getSelfAndPassengers() : Stream.of(nmsEntity)).forEach(e -> {
            // Ensure force flag is not needed
            Preconditions.checkArgument(
                (e.getBukkitEntity().isValid() && e.getBukkitEntity().isPersistent()) || forceSerialization,
                "Cannot serialize invalid or non-persistent entity %s(%s) without the FORCE flag",
                e.getType().toShortString(),
                e.getStringUUID()
            );

            if (e instanceof Player) {
                // Ensure player flag is not needed
                Preconditions.checkArgument(
                    allowPlayerSerialization,
                    "Cannot serialize player(%s) without the PLAYER flag",
                    e.getStringUUID()
                );
            } else {
                // Ensure misc flag is not needed
                Preconditions.checkArgument(
                    e.getType().canSerialize() || allowMiscSerialization,
                    "Cannot serialize misc non-saveable entity %s(%s) without the MISC flag",
                    e.getType().toShortString(),
                    e.getStringUUID()
                );
            }
        });

        try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
            () -> "serialiseEntity@" + entity.getUniqueId(), LOGGER
        )) {
            final TagValueOutput output = TagValueOutput.createWithContext(problemReporter, nmsEntity.registryAccess());
            if (serializePassengers) {
                if (!nmsEntity.saveAsPassenger(output, true, includeNonSaveable, forceSerialization)) {
                    throw new IllegalArgumentException("Couldn't serialize entity");
                }
            } else {
                List<net.minecraft.world.entity.Entity> pass = new ArrayList<>(nmsEntity.getPassengers());
                nmsEntity.passengers = com.google.common.collect.ImmutableList.of();
                boolean serialized = nmsEntity.saveAsPassenger(output, true, includeNonSaveable, forceSerialization);
                nmsEntity.passengers = com.google.common.collect.ImmutableList.copyOf(pass);
                if (!serialized) {
                    throw new IllegalArgumentException("Couldn't serialize entity");
                }
            }
            return MCUtil.serializeTagToBytes(output.buildResult());
        }
    }

    @Override
    public org.bukkit.entity.Entity deserializeEntity(byte[] data, World world, boolean preserveUUID, boolean preservePassengers) {
        Preconditions.checkNotNull(data, "null cannot be deserialized");
        Preconditions.checkArgument(data.length > 0, "Cannot deserialize empty data");

        CompoundTag tag = MCUtil.deserializeTagFromBytes(data);
        int dataVersion = NbtUtils.getDataVersion(tag, 0);
        Preconditions.checkArgument(dataVersion <= Bukkit.getUnsafe().getDataVersion(), "Newer version! Server downgrades are not supported!");
        tag = PlatformHooks.get().convertNBT(References.ENTITY, MinecraftServer.getServer().getFixerUpper(), tag, dataVersion, this.getDataVersion()); // Paper - possibly use dataconverter
        if (!preservePassengers) {
            tag.remove(Entity.TAG_PASSENGERS);
        }
        net.minecraft.world.entity.Entity nmsEntity = deserializeEntity(tag, ((CraftWorld) world).getHandle(), preserveUUID);
        return nmsEntity.getBukkitEntity();
    }

    private net.minecraft.world.entity.Entity deserializeEntity(CompoundTag tag, ServerLevel world, boolean preserveUUID) {
        if (!preserveUUID) {
            // Generate a new UUID, so we don't have to worry about deserializing the same entity twice
            tag.remove(Entity.TAG_UUID);
        }

        final net.minecraft.world.entity.Entity nmsEntity;
        try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
            () -> "deserialiseEntity", LOGGER
        )) {
            nmsEntity = net.minecraft.world.entity.EntityType.create(
                TagValueInput.create(problemReporter, world.registryAccess(), tag),
                world,
                new EntitySpawnRequest(EntitySpawnReason.LOAD, false)
            ).orElseThrow(() -> new IllegalArgumentException("An ID was not found for the data. Did you downgrade?"));
        }

        tag.getList(Entity.TAG_PASSENGERS).ifPresent(passengers -> {
            for (final Tag passenger : passengers) {
                if (!(passenger instanceof final CompoundTag serializedPassenger)) {
                    continue;
                }
                final net.minecraft.world.entity.Entity passengerEntity = deserializeEntity(serializedPassenger, world, preserveUUID);
                passengerEntity.startRiding(nmsEntity, true, true);
            }
        });
        return nmsEntity;
    }

    @Override
    public int nextEntityId(final World world) {
        return ((CraftWorld) world).getHandle().getNextEntityId();
    }

    @Override
    public String getMainLevelName() {
        return ((net.minecraft.server.dedicated.DedicatedServer) net.minecraft.server.MinecraftServer.getServer()).getProperties().levelName;
    }

    @Override
    public int getProtocolVersion() {
        return net.minecraft.SharedConstants.getCurrentVersion().protocolVersion();
    }
    // Paper end

    @Override
    public ItemStack deserializeItemHover(final HoverEvent.ShowItem itemHover) {
        final RegistryOps<Object> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JavaOps.INSTANCE);
        final Object encoded = AdventureCodecs.SHOW_ITEM_CODEC.codec()
            .encodeStart(ops, HoverEvent.showItem(itemHover)).getOrThrow(IllegalStateException::new);

        return CraftItemStack.asBukkitCopy(net.minecraft.network.chat.HoverEvent.ShowItem.CODEC.codec()
            .parse(ops, encoded).getOrThrow(IllegalStateException::new)
            .item());
    }
}
