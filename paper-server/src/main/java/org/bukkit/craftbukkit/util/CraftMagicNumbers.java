package org.bukkit.craftbukkit.util;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.registry.RegistryKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Stream;
import io.papermc.paper.entity.EntitySerializationFlag;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
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
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.MemorySection;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.damage.CraftDamageSourceBuilder;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.legacy.CraftLegacy;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {
    public static final CraftMagicNumbers INSTANCE = new CraftMagicNumbers();
    public static final boolean DISABLE_OLD_API_SUPPORT = Boolean.getBoolean("paper.disableOldApiSupport"); // Paper

    private final Commodore commodore = new Commodore();

    private CraftMagicNumbers() {}

    @Override
    public net.kyori.adventure.text.flattener.ComponentFlattener componentFlattener() {
        return io.papermc.paper.adventure.PaperAdventure.FLATTENER;
    }

    @Override
    public net.kyori.adventure.text.serializer.gson.GsonComponentSerializer colorDownsamplingGsonComponentSerializer() {
        return net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.colorDownsamplingGson();
    }

    @Override
    public net.kyori.adventure.text.serializer.gson.GsonComponentSerializer gsonComponentSerializer() {
        return net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson();
    }

    @Override
    public net.kyori.adventure.text.serializer.plain.PlainComponentSerializer plainComponentSerializer() {
        return io.papermc.paper.adventure.PaperAdventure.PLAIN;
    }

    @Override
    public net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer plainTextSerializer() {
        return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText();
    }

    @Override
    public net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer legacyComponentSerializer() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection();
    }

    @Override
    public net.kyori.adventure.text.Component resolveWithContext(final net.kyori.adventure.text.Component component, final org.bukkit.command.CommandSender context, final org.bukkit.entity.Entity scoreboardSubject, final boolean bypassPermissions) throws IOException {
        return io.papermc.paper.adventure.PaperAdventure.resolveWithContext(component, context, scoreboardSubject, bypassPermissions);
    }

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

            ResourceLocation key = CraftNamespacedKey.toMinecraft(material.getKey());
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
        return CraftBlockData.fromData(CraftMagicNumbers.getBlock(material, data));
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

    /**
     * This string should be changed if the NMS mappings do.
     *
     * It has no meaning and should only be used as an equality check. Plugins
     * which are sensitive to the NMS mappings may read it and refuse to load if
     * it cannot be found or is different to the expected value.
     *
     * Remember: NMS is not supported API and may break at any time for any
     * reason irrespective of this. There is often supported API to do the same
     * thing as many common NMS usages. If not, you are encouraged to open a
     * feature and/or pull request for consideration, or use a well abstracted
     * third-party API such as ProtocolLib.
     *
     * @return string
     */
    public String getMappingsVersion() {
        return "7ecad754373a5fbc43d381d7450c53a5";
    }

    @Override
    public int getDataVersion() {
        return SharedConstants.getCurrentVersion().dataVersion().version();
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try {
            nmsStack.applyComponents(new ItemParser(Commands.createValidationContext(CraftRegistry.getMinecraftRegistry())).parse(new StringReader(arguments)).components());
        } catch (CommandSyntaxException ex) {
            com.mojang.logging.LogUtils.getClassLogger().error("Exception modifying ItemStack", new Throwable(ex)); // Paper - show stack trace
        }

        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));

        return stack;
    }

    private static File getBukkitDataPackFolder() {
        return new File(MinecraftServer.getServer().getWorldPath(LevelResource.DATAPACK_DIR).toFile(), "bukkit");
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        Preconditions.checkArgument(Bukkit.getAdvancement(key) == null, "Advancement %s already exists", key);
        ResourceLocation resourceKey = CraftNamespacedKey.toMinecraft(key);

        JsonElement jsonelement = JsonParser.parseString(advancement);
        final net.minecraft.resources.RegistryOps<JsonElement> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JsonOps.INSTANCE); // Paper - use RegistryOps
        final net.minecraft.advancements.Advancement nms = net.minecraft.advancements.Advancement.CODEC.parse(ops, jsonelement).getOrThrow(JsonParseException::new); // Paper - use RegistryOps
        if (nms != null) {
            final com.google.common.collect.ImmutableMap.Builder<ResourceLocation, AdvancementHolder> mapBuilder = com.google.common.collect.ImmutableMap.builder();
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
            // Make sure we still go through our reflection rewriting if needed
            return io.papermc.paper.pluginremap.reflect.ReflectionRemapper.processClass(clazz);
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
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot slot) {
        // Paper start - delegate to method on ItemType
        final org.bukkit.inventory.ItemType item = material.asItemType();
        Preconditions.checkArgument(item != null, material + " is not an item and does not have default attributes");
        return item.getDefaultAttributeModifiers(slot);
        // Paper end - delegate to method on ItemType
    }

    @Override
    public CreativeCategory getCreativeCategory(Material material) {
        return material.getCreativeCategory();
    }

    @Override
    public String getBlockTranslationKey(Material material) {
        return material.getBlockTranslationKey();
    }

    @Override
    public String getItemTranslationKey(Material material) {
        return material.getItemTranslationKey();
    }

    @Override
    public String getTranslationKey(EntityType entityType) {
        Preconditions.checkArgument(entityType.getName() != null, "Invalid name of EntityType %s for translation key", entityType);
        return net.minecraft.world.entity.EntityType.byString(entityType.getName()).map(net.minecraft.world.entity.EntityType::getDescriptionId).orElseThrow();
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        return nmsItemStack.getItem().getDescriptionId();
    }

    @Override
    public boolean isSupportedApiVersion(String apiVersion) {
        if (apiVersion == null) return false;
        final ApiVersion toCheck = ApiVersion.getOrCreateVersion(apiVersion);
        final ApiVersion minimumVersion = MinecraftServer.getServer().server.minimumAPI;

        return !toCheck.isNewerThan(ApiVersion.CURRENT) && !toCheck.isOlderThan(minimumVersion);
    }

    @Override
    public String getTranslationKey(final Attribute attribute) {
        return attribute.getTranslationKey();
    }

    @Override
    public PotionType.InternalPotionData getInternalPotionData(NamespacedKey namespacedKey) {
        Potion potionRegistry = CraftRegistry.getMinecraftRegistry(Registries.POTION)
                .getOptional(CraftNamespacedKey.toMinecraft(namespacedKey)).orElseThrow();

        return new CraftPotionType(namespacedKey, potionRegistry);
    }

    @Override
    public DamageSource.Builder createDamageSourceBuilder(DamageType damageType) {
        return new CraftDamageSourceBuilder(damageType);
    }

    @Override
    public String get(Class<?> aClass, String s) {
        if (aClass == Enchantment.class) {
            // We currently do not have any version-dependent remapping, so we can use current version
            return FieldRename.convertEnchantmentName(ApiVersion.CURRENT, s);
        }
        return s;
    }

    @Override
    public <B extends Keyed> B get(RegistryKey<B> registry, NamespacedKey namespacedKey) {
        // We currently do not have any version-dependent remapping, so we can use current version
        return CraftRegistry.get(registry, namespacedKey, ApiVersion.CURRENT);
    }

    @Override
    public com.destroystokyo.paper.util.VersionFetcher getVersionFetcher() {
        return new com.destroystokyo.paper.PaperVersionFetcher();
    }

    @Override
    public byte[] serializeItem(ItemStack item) {
        Preconditions.checkNotNull(item, "null cannot be serialized");
        Preconditions.checkArgument(item.getType() != Material.AIR, "air cannot be serialized");
        Preconditions.checkArgument(!item.isEmpty(), "Empty itemstack cannot be serialised");

        return serializeNbtToBytes(
            (CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
                MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE),
                CraftItemStack.unwrap(item)
            ).getOrThrow()
        );
    }

    @Override
    public ItemStack deserializeItem(byte[] data) {
        Preconditions.checkNotNull(data, "null cannot be deserialized");
        Preconditions.checkArgument(data.length > 0, "cannot deserialize nothing");

        CompoundTag compound = deserializeNbtFromBytes(data);
        return deserializeItem(compound);
    }

    private ItemStack deserializeItem(CompoundTag compound) {
        final int dataVersion = compound.getIntOr("DataVersion", 0);
        compound = PlatformHooks.get().convertNBT(References.ITEM_STACK, DataFixers.getDataFixer(), compound, dataVersion, this.getDataVersion()); // Paper - possibly use dataconverter
        if (compound.getStringOr("id", "minecraft:air").equals("minecraft:air")) {
            return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.EMPTY);
        }
        return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.CODEC.parse(
            MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE), compound
        ).getOrThrow());
    }

    @Override
    public @org.jetbrains.annotations.NotNull Map<String, Object> serializeStack(final ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return Map.of("id", "minecraft:air", SharedConstants.DATA_VERSION_TAG, this.getDataVersion(), "schema_version", 1);
        }
        final CompoundTag tag = (CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
            CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE),
            CraftItemStack.asNMSCopy(itemStack)
        ).getOrThrow();
        NbtUtils.addCurrentDataVersion(tag);

        final Map<String, Object> ret = new LinkedHashMap<>();
        tag.asCompound().get().forEach((key, value) -> {
            switch (key) {
                case "id" -> {
                    ret.put("id", value.asString().get());
                }
                case "count" -> {
                    ret.put("count", value.asInt().get());
                }
                case "components" -> {
                    final Map<String, Object> components = new LinkedHashMap<>();
                    value.asCompound().ifPresent((compoundTag) -> {
                        compoundTag.forEach((componentKey, componentTag) -> {
                            final String serializedComponent = componentTag.toString();
                            components.put(componentKey, serializedComponent);
                        });
                    });
                    ret.put("components", components);
                }
                case SharedConstants.DATA_VERSION_TAG -> {
                    ret.put(SharedConstants.DATA_VERSION_TAG, value.asInt().get());
                }
                default -> throw new IllegalStateException("Unexpected value: " + key);
            }
        });
        ret.put("schema_version", 1);
        return ret;
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
                        } else if (value instanceof MemorySection memory) {
                            componentMap = new HashMap<>();
                            for (final String memoryKey : memory.getKeys(false)) {
                                componentMap.put(memoryKey, memory.getString(memoryKey));
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

        return deserializeItem(tag);
    }

    @Override
    public com.google.gson.JsonObject serializeItemAsJson(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "Cannot serialize empty ItemStack");
        Preconditions.checkArgument(!itemStack.isEmpty(), "Cannot serialize empty ItemStack");

        net.minecraft.core.RegistryAccess.Frozen reg = net.minecraft.server.MinecraftServer.getServer().registryAccess();
        com.mojang.serialization.DynamicOps<com.google.gson.JsonElement> ops = reg.createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
        com.google.gson.JsonObject item;
        // Serialize as SNBT to preserve exact NBT types; vanilla codecs already can handle such deserialization.
        net.minecraft.world.item.component.CustomData.SERIALIZE_CUSTOM_AS_SNBT.set(true);
        try {
            item = net.minecraft.world.item.ItemStack.CODEC.encodeStart(ops, CraftItemStack.unwrap(itemStack)).getOrThrow().getAsJsonObject();
        } finally {
            net.minecraft.world.item.component.CustomData.SERIALIZE_CUSTOM_AS_SNBT.set(false);
        }
        item.addProperty("DataVersion", this.getDataVersion());
        return item;
    }

    @Override
    public ItemStack deserializeItemFromJson(com.google.gson.JsonObject data) throws IllegalArgumentException {
        Preconditions.checkNotNull(data, "null cannot be deserialized");

        final int dataVersion = data.get("DataVersion").getAsInt();
        final int currentVersion = org.bukkit.craftbukkit.util.CraftMagicNumbers.INSTANCE.getDataVersion();
        data = (com.google.gson.JsonObject) MinecraftServer.getServer().fixerUpper.update(References.ITEM_STACK, new Dynamic<>(com.mojang.serialization.JsonOps.INSTANCE, data), dataVersion, currentVersion).getValue();
        com.mojang.serialization.DynamicOps<com.google.gson.JsonElement> ops = MinecraftServer.getServer().registryAccess().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
        return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.CODEC.parse(ops, data).getOrThrow(IllegalArgumentException::new));
    }

    @Override
    public byte[] serializeEntity(org.bukkit.entity.Entity entity, EntitySerializationFlag... serializationFlags) {
        Preconditions.checkNotNull(entity, "null cannot be serialized");
        Preconditions.checkArgument(entity instanceof CraftEntity, "Only CraftEntities can be serialized");

        Set<EntitySerializationFlag> flags = Set.of(serializationFlags);
        final boolean serializePassangers = flags.contains(EntitySerializationFlag.PASSENGERS);
        final boolean forceSerialization = flags.contains(EntitySerializationFlag.FORCE);
        final boolean allowPlayerSerialization = flags.contains(EntitySerializationFlag.PLAYER);
        final boolean allowMiscSerialization = flags.contains(EntitySerializationFlag.MISC);
        final boolean includeNonSaveable = allowPlayerSerialization || allowMiscSerialization;

        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        (serializePassangers ? nmsEntity.getSelfAndPassengers() : Stream.of(nmsEntity)).forEach(e -> {
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
                    nmsEntity.getType().canSerialize() || allowMiscSerialization,
                    "Cannot serialize misc non-saveable entity %s(%s) without the MISC flag",
                    e.getType().toShortString(),
                    e.getStringUUID()
                );
            }
        });

        final TagValueOutput output = TagValueOutput.createDiscardingWithContext(nmsEntity.registryAccess());
        if (serializePassangers) {
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
        return serializeNbtToBytes(output.buildResult());
    }

    @Override
    public org.bukkit.entity.Entity deserializeEntity(byte[] data, World world, boolean preserveUUID, boolean preservePassengers) {
        Preconditions.checkNotNull(data, "null cannot be deserialized");
        Preconditions.checkArgument(data.length > 0, "Cannot deserialize empty data");

        CompoundTag compound = deserializeNbtFromBytes(data);
        int dataVersion = compound.getIntOr("DataVersion", 0);
        compound = PlatformHooks.get().convertNBT(References.ENTITY, MinecraftServer.getServer().fixerUpper, compound, dataVersion, this.getDataVersion()); // Paper - possibly use dataconverter
        if (!preservePassengers) {
            compound.remove("Passengers");
        }
        net.minecraft.world.entity.Entity nmsEntity = deserializeEntity(compound, ((CraftWorld) world).getHandle(), preserveUUID);
        return nmsEntity.getBukkitEntity();
    }

    private net.minecraft.world.entity.Entity deserializeEntity(CompoundTag compound, ServerLevel world, boolean preserveUUID) {
        if (!preserveUUID) {
            // Generate a new UUID, so we don't have to worry about deserializing the same entity twice
            compound.remove("UUID");
        }
        net.minecraft.world.entity.Entity nmsEntity = net.minecraft.world.entity.EntityType.create(
            TagValueInput.createDiscarding(world.registryAccess(), compound),
            world,
            net.minecraft.world.entity.EntitySpawnReason.LOAD
        ).orElseThrow(() -> new IllegalArgumentException("An ID was not found for the data. Did you downgrade?"));
        compound.getList("Passengers").ifPresent(passengers -> {
            for (final Tag tag : passengers) {
                if (!(tag instanceof final CompoundTag serializedPassenger)) {
                    continue;
                }
                final net.minecraft.world.entity.Entity passengerEntity = deserializeEntity(serializedPassenger, world, preserveUUID);
                passengerEntity.startRiding(nmsEntity, true);
            }
        });
        return nmsEntity;
    }

    private byte[] serializeNbtToBytes(CompoundTag compound) {
        compound.putInt("DataVersion", getDataVersion());
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        try {
            net.minecraft.nbt.NbtIo.writeCompressed(
                compound,
                outputStream
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return outputStream.toByteArray();
    }

    private CompoundTag deserializeNbtFromBytes(byte[] data) {
        CompoundTag compound;
        try {
            compound = net.minecraft.nbt.NbtIo.readCompressed(
                new java.io.ByteArrayInputStream(data), net.minecraft.nbt.NbtAccounter.unlimitedHeap()
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        int dataVersion = compound.getIntOr("DataVersion", 0);
        Preconditions.checkArgument(dataVersion <= getDataVersion(), "Newer version! Server downgrades are not supported!");
        return compound;
    }

    @Override
    public int nextEntityId() {
        return net.minecraft.world.entity.Entity.nextEntityId();
    }

    @Override
    public String getMainLevelName() {
        return ((net.minecraft.server.dedicated.DedicatedServer) net.minecraft.server.MinecraftServer.getServer()).getProperties().levelName;
    }

    @Override
    public int getProtocolVersion() {
        return net.minecraft.SharedConstants.getCurrentVersion().protocolVersion();
    }

    @Override
    public boolean isValidRepairItemStack(org.bukkit.inventory.ItemStack itemToBeRepaired, org.bukkit.inventory.ItemStack repairMaterial) {
        if (!itemToBeRepaired.getType().isItem() || !repairMaterial.getType().isItem()) {
            return false;
        }
        return CraftItemStack.unwrap(itemToBeRepaired).isValidRepairItem(CraftItemStack.unwrap(repairMaterial));
    }

    @Override
    public boolean hasDefaultEntityAttributes(NamespacedKey entityKey) {
        return net.minecraft.world.entity.ai.attributes.DefaultAttributes.hasSupplier(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(CraftNamespacedKey.toMinecraft(entityKey)));
    }

    @Override
    public org.bukkit.attribute.Attributable getDefaultEntityAttributes(NamespacedKey entityKey) {
        Preconditions.checkArgument(hasDefaultEntityAttributes(entityKey), entityKey + " doesn't have default attributes");
        var supplier = net.minecraft.world.entity.ai.attributes.DefaultAttributes.getSupplier((net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.LivingEntity>) net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(CraftNamespacedKey.toMinecraft(entityKey)));
        return new io.papermc.paper.attribute.UnmodifiableAttributeMap(supplier);
    }

    @Override
    public org.bukkit.NamespacedKey getBiomeKey(org.bukkit.RegionAccessor accessor, int x, int y, int z) {
        return accessor.getBiome(x, y, z).getKey();
    }

    @Override
    public void setBiomeKey(org.bukkit.RegionAccessor accessor, int x, int y, int z, org.bukkit.NamespacedKey biomeKey) {
        accessor.setBiome(x, y, z, org.bukkit.Registry.BIOME.getOrThrow(biomeKey));
    }

    @Override
    public String getStatisticCriteriaKey(org.bukkit.Statistic statistic) {
        if (statistic.getType() != org.bukkit.Statistic.Type.UNTYPED) return "minecraft.custom:minecraft." + statistic.getKey().getKey();
        return org.bukkit.craftbukkit.CraftStatistic.getNMSStatistic(statistic).getName();
    }

    @Override
    public List<net.kyori.adventure.text.Component> computeTooltipLines(final ItemStack itemStack, final io.papermc.paper.inventory.tooltip.TooltipContext tooltipContext, final org.bukkit.entity.Player player) {
        Preconditions.checkArgument(tooltipContext != null, "tooltipContext cannot be null");
        net.minecraft.world.item.TooltipFlag.Default flag = tooltipContext.isAdvanced() ? net.minecraft.world.item.TooltipFlag.ADVANCED : net.minecraft.world.item.TooltipFlag.NORMAL;
        if (tooltipContext.isCreative()) {
            flag = flag.asCreative();
        }
        final List<net.minecraft.network.chat.Component> lines = CraftItemStack.asNMSCopy(itemStack).getTooltipLines(
            net.minecraft.world.item.Item.TooltipContext.of(player == null ? CraftRegistry.getMinecraftRegistry() : ((org.bukkit.craftbukkit.entity.CraftPlayer) player).getHandle().level().registryAccess()),
            player == null ? null : ((org.bukkit.craftbukkit.entity.CraftPlayer) player).getHandle(), flag);
        return lines.stream().map(io.papermc.paper.adventure.PaperAdventure::asAdventure).toList();
    }
    // Paper end

    @Override
    public org.bukkit.Color getSpawnEggLayerColor(final EntityType entityType, final int layer) {
        final net.minecraft.world.entity.EntityType<?> nmsType = org.bukkit.craftbukkit.entity.CraftEntityType.bukkitToMinecraft(entityType);
        final net.minecraft.world.item.SpawnEggItem eggItem = net.minecraft.world.item.SpawnEggItem.byId(nmsType);
        if (eggItem != null) {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    @Override
    public io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager<org.bukkit.plugin.Plugin> createPluginLifecycleEventManager(final org.bukkit.plugin.java.JavaPlugin plugin, final java.util.function.BooleanSupplier registrationCheck) {
        return new io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEventManager<>(plugin, registrationCheck);
    }

    @Override
    public org.bukkit.inventory.ItemStack createEmptyStack() {
        return CraftItemStack.asCraftMirror(null);
    }
}
