package org.bukkit.craftbukkit.util;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.AdvancementDataWorld;
import net.minecraft.server.Block;
import net.minecraft.server.ChatDeserializer;
import net.minecraft.server.DataConverterRegistry;
import net.minecraft.server.DataConverterTypes;
import net.minecraft.server.DynamicOpsNBT;
import net.minecraft.server.FluidType;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IRegistry;
import net.minecraft.server.Item;
import net.minecraft.server.LootDeserializationContext;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagString;
import net.minecraft.server.SavedFile;
import net.minecraft.server.SharedConstants;
import org.bukkit.Bukkit;
import org.bukkit.Fluid;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.legacy.CraftLegacy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

@SuppressWarnings("deprecation")
public final class CraftMagicNumbers implements UnsafeValues {
    public static final UnsafeValues INSTANCE = new CraftMagicNumbers();

    private CraftMagicNumbers() {}

    public static IBlockData getBlock(MaterialData material) {
        return getBlock(material.getItemType(), material.getData());
    }

    public static IBlockData getBlock(Material material, byte data) {
        return CraftLegacy.fromLegacyData(CraftLegacy.toLegacy(material), data);
    }

    public static MaterialData getMaterial(IBlockData data) {
        return CraftLegacy.toLegacy(getMaterial(data.getBlock())).getNewData(toLegacyData(data));
    }

    public static Item getItem(Material material, short data) {
        if (material.isLegacy()) {
            return CraftLegacy.fromLegacyData(CraftLegacy.toLegacy(material), data);
        }

        return getItem(material);
    }

    public static MaterialData getMaterialData(Item item) {
        return CraftLegacy.toLegacyData(getMaterial(item));
    }

    // ========================================================================
    private static final Map<Block, Material> BLOCK_MATERIAL = new HashMap<>();
    private static final Map<Item, Material> ITEM_MATERIAL = new HashMap<>();
    private static final Map<FluidType, Fluid> FLUID_MATERIAL = new HashMap<>();
    private static final Map<Material, Item> MATERIAL_ITEM = new HashMap<>();
    private static final Map<Material, Block> MATERIAL_BLOCK = new HashMap<>();
    private static final Map<Material, FluidType> MATERIAL_FLUID = new HashMap<>();

    static {
        for (Block block : IRegistry.BLOCK) {
            BLOCK_MATERIAL.put(block, Material.getMaterial(IRegistry.BLOCK.getKey(block).getKey().toUpperCase(Locale.ROOT)));
        }

        for (Item item : IRegistry.ITEM) {
            ITEM_MATERIAL.put(item, Material.getMaterial(IRegistry.ITEM.getKey(item).getKey().toUpperCase(Locale.ROOT)));
        }

        for (FluidType fluid : IRegistry.FLUID) {
            FLUID_MATERIAL.put(fluid, Registry.FLUID.get(CraftNamespacedKey.fromMinecraft(IRegistry.FLUID.getKey(fluid))));
        }

        for (Material material : Material.values()) {
            if (material.isLegacy()) {
                continue;
            }

            MinecraftKey key = key(material);
            IRegistry.ITEM.getOptional(key).ifPresent((item) -> {
                MATERIAL_ITEM.put(material, item);
            });
            IRegistry.BLOCK.getOptional(key).ifPresent((block) -> {
                MATERIAL_BLOCK.put(material, block);
            });
            IRegistry.FLUID.getOptional(key).ifPresent((fluid) -> {
                MATERIAL_FLUID.put(material, fluid);
            });
        }
    }

    public static Material getMaterial(Block block) {
        return BLOCK_MATERIAL.get(block);
    }

    public static Material getMaterial(Item item) {
        return ITEM_MATERIAL.getOrDefault(item, Material.AIR);
    }

    public static Fluid getFluid(FluidType fluid) {
        return FLUID_MATERIAL.get(fluid);
    }

    public static Item getItem(Material material) {
        if (material != null && material.isLegacy()) {
            material = CraftLegacy.fromLegacy(material);
        }

        return MATERIAL_ITEM.get(material);
    }

    public static Block getBlock(Material material) {
        if (material != null && material.isLegacy()) {
            material = CraftLegacy.fromLegacy(material);
        }

        return MATERIAL_BLOCK.get(material);
    }

    public static FluidType getFluid(Fluid fluid) {
        return MATERIAL_FLUID.get(fluid);
    }

    public static MinecraftKey key(Material mat) {
        return CraftNamespacedKey.toMinecraft(mat.getKey());
    }
    // ========================================================================

    public static byte toLegacyData(IBlockData data) {
        return CraftLegacy.toLegacyData(data);
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
        return CraftBlockData.fromData(getBlock(material, data));
    }

    @Override
    public Material getMaterial(String material, int version) {
        Preconditions.checkArgument(material != null, "material == null");
        Preconditions.checkArgument(version <= this.getDataVersion(), "Newer version! Server downgrades are not supported!");

        // Fastpath up to date materials
        if (version == this.getDataVersion()) {
            return Material.getMaterial(material);
        }

        Dynamic<NBTBase> name = new Dynamic<>(DynamicOpsNBT.a, NBTTagString.a("minecraft:" + material.toLowerCase(Locale.ROOT)));
        Dynamic<NBTBase> converted = DataConverterRegistry.a().update(DataConverterTypes.ITEM_NAME, name, version, this.getDataVersion());

        if (name.equals(converted)) {
            converted = DataConverterRegistry.a().update(DataConverterTypes.BLOCK_NAME, name, version, this.getDataVersion());
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
        return "da85101b34b252659e3ddf10c0c57cc9";
    }

    @Override
    public int getDataVersion() {
        return SharedConstants.getGameVersion().getWorldVersion();
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        net.minecraft.server.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        try {
            nmsStack.setTag((NBTTagCompound) MojangsonParser.parse(arguments));
        } catch (CommandSyntaxException ex) {
            Logger.getLogger(CraftMagicNumbers.class.getName()).log(Level.SEVERE, null, ex);
        }

        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));

        return stack;
    }

    private static File getBukkitDataPackFolder() {
        return new File(MinecraftServer.getServer().a(SavedFile.DATAPACKS).toFile(), "bukkit");
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        if (Bukkit.getAdvancement(key) != null) {
            throw new IllegalArgumentException("Advancement " + key + " already exists.");
        }
        MinecraftKey minecraftkey = CraftNamespacedKey.toMinecraft(key);

        JsonElement jsonelement = AdvancementDataWorld.DESERIALIZER.fromJson(advancement, JsonElement.class);
        JsonObject jsonobject = ChatDeserializer.m(jsonelement, "advancement");
        net.minecraft.server.Advancement.SerializedAdvancement nms = net.minecraft.server.Advancement.SerializedAdvancement.a(jsonobject, new LootDeserializationContext(minecraftkey, MinecraftServer.getServer().getLootPredicateManager()));
        if (nms != null) {
            MinecraftServer.getServer().getAdvancementData().REGISTRY.a(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
            Advancement bukkit = Bukkit.getAdvancement(key);

            if (bukkit != null) {
                File file = new File(getBukkitDataPackFolder(), "data" + File.separator + key.getNamespace() + File.separator + "advancements" + File.separator + key.getKey() + ".json");
                file.getParentFile().mkdirs();

                try {
                    Files.write(advancement, file, Charsets.UTF_8);
                } catch (IOException ex) {
                    Bukkit.getLogger().log(Level.SEVERE, "Error saving advancement " + key, ex);
                }

                MinecraftServer.getServer().getPlayerList().reload();

                return bukkit;
            }
        }

        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        File file = new File(getBukkitDataPackFolder(), "data" + File.separator + key.getNamespace() + File.separator + "advancements" + File.separator + key.getKey() + ".json");
        return file.delete();
    }

    private static final List<String> SUPPORTED_API = Arrays.asList("1.13", "1.14", "1.15", "1.16");

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        String minimumVersion = MinecraftServer.getServer().server.minimumAPI;
        int minimumIndex = SUPPORTED_API.indexOf(minimumVersion);

        if (pdf.getAPIVersion() != null) {
            int pluginIndex = SUPPORTED_API.indexOf(pdf.getAPIVersion());

            if (pluginIndex == -1) {
                throw new InvalidPluginException("Unsupported API version " + pdf.getAPIVersion());
            }

            if (pluginIndex < minimumIndex) {
                throw new InvalidPluginException("Plugin API version " + pdf.getAPIVersion() + " is lower than the minimum allowed version. Please update or replace it.");
            }
        } else {
            if (minimumIndex == -1) {
                CraftLegacy.init();
                Bukkit.getLogger().log(Level.WARNING, "Legacy plugin " + pdf.getFullName() + " does not specify an api-version.");
            } else {
                throw new InvalidPluginException("Plugin API version " + pdf.getAPIVersion() + " is lower than the minimum allowed version. Please update or replace it.");
            }
        }
    }

    public static boolean isLegacy(PluginDescriptionFile pdf) {
        return pdf.getAPIVersion() == null;
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        try {
            clazz = Commodore.convert(clazz, !isLegacy(pdf));
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Fatal error trying to convert " + pdf.getFullName() + ":" + path, ex);
        }

        return clazz;
    }

    /**
     * This helper class represents the different NBT Tags.
     * <p>
     * These should match NBTBase#getTypeId
     */
    public static class NBT {

        public static final int TAG_END = 0;
        public static final int TAG_BYTE = 1;
        public static final int TAG_SHORT = 2;
        public static final int TAG_INT = 3;
        public static final int TAG_LONG = 4;
        public static final int TAG_FLOAT = 5;
        public static final int TAG_DOUBLE = 6;
        public static final int TAG_BYTE_ARRAY = 7;
        public static final int TAG_STRING = 8;
        public static final int TAG_LIST = 9;
        public static final int TAG_COMPOUND = 10;
        public static final int TAG_INT_ARRAY = 11;
        public static final int TAG_ANY_NUMBER = 99;
    }
}
