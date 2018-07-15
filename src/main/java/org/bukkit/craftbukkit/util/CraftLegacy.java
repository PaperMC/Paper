package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.Dynamic;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.Block;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.DataConverterFlattenData;
import net.minecraft.server.DataConverterMaterialId;
import net.minecraft.server.DataConverterRegistry;
import net.minecraft.server.DataConverterTypes;
import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.DynamicOpsNBT;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

/**
 * This class may seem unnecessarily slow and complicated/repetitive however it
 * is able to handle a lot more edge cases and invertible transformations (many
 * of which are not immediately obvious) than any other alternative. If you do
 * make changes to this class please make sure to contribute them back
 * https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse so
 * that all may benefit.
 *
 * @deprecated legacy use only
 */
@Deprecated
public class CraftLegacy {

    private static final Map<EntityType, Material> SPAWN_EGGS = new HashMap<>();
    private static final Set<String> whitelistedStates = new HashSet<>(Arrays.asList("explode", "check_decay", "decayable"));
    private static final Map<MaterialData, Item> materialToItem = new HashMap<>();
    private static final Map<Item, MaterialData> itemToMaterial = new HashMap<>();
    private static final Map<MaterialData, IBlockData> materialToData = new HashMap<>();
    private static final Map<IBlockData, MaterialData> dataToMaterial = new HashMap<>();
    private static final Map<MaterialData, Block> materialToBlock = new HashMap<>();
    private static final Map<Block, MaterialData> blockToMaterial = new HashMap<>();

    public static Material toLegacy(Material material) {
        if (material == null || material.isLegacy()) {
            return material;
        }

        return toLegacyData(material).getItemType();
    }

    public static MaterialData toLegacyData(Material material) {
        Preconditions.checkArgument(!material.isLegacy(), "toLegacy on legacy Material");
        MaterialData mappedData;

        if (material.isBlock()) {
            Block block = CraftMagicNumbers.getBlock(material);
            IBlockData blockData = block.getBlockData();

            // Try exact match first
            mappedData = dataToMaterial.get(blockData);
            // Fallback to any block
            if (mappedData == null) {
                mappedData = blockToMaterial.get(block);
                // Fallback to matching item
                if (mappedData == null) {
                    mappedData = itemToMaterial.get(block.getItem());
                }
            }
        } else {
            Item item = CraftMagicNumbers.getItem(material);
            mappedData = itemToMaterial.get(item);
        }

        return (mappedData == null) ? new MaterialData(Material.LEGACY_AIR) : mappedData;
    }

    public static IBlockData fromLegacyData(Material material, Block block, byte data) {
        Preconditions.checkArgument(material.isLegacy(), "fromLegacyData on modern Material");

        MaterialData materialData = new MaterialData(material, data);

        // Try exact match first
        IBlockData converted = materialToData.get(materialData);
        if (converted != null) {
            return converted;
        }

        // Fallback to any block
        Block convertedBlock = materialToBlock.get(materialData);
        if (convertedBlock != null) {
            return convertedBlock.getBlockData();
        }

        // Return existing block
        return block.getBlockData();
    }

    public static Item fromLegacyData(Material material, Item item, short data) {
        Preconditions.checkArgument(material.isLegacy(), "fromLegacyData on modern Material. Did you forget to define api-version: 1.13 in your plugin.yml?");

        MaterialData materialData = new MaterialData(material, (byte) data);

        if (material.isBlock()) {
            // Try exact match first
            IBlockData converted = materialToData.get(materialData);
            if (converted != null) {
                return converted.getBlock().getItem();
            }

            // Fallback to any block
            Block convertedBlock = materialToBlock.get(materialData);
            if (convertedBlock != null) {
                return convertedBlock.getItem();
            }
        }

        // Fallback to matching item
        Item convertedItem = materialToItem.get(materialData);
        if (convertedItem != null) {
            return convertedItem;
        }

        // Return existing item
        return item;
    }

    public static byte toLegacyData(IBlockData blockData) {
        MaterialData mappedData;

        // Try exact match first
        mappedData = dataToMaterial.get(blockData);
        // Fallback to any block
        if (mappedData == null) {
            mappedData = blockToMaterial.get(blockData.getBlock());
        }

        return (mappedData == null) ? 0 : mappedData.getData();
    }

    public static Material fromLegacy(Material material) {
        return fromLegacy(new MaterialData(material));
    }

    public static Material fromLegacy(MaterialData materialData) {
        Material material = materialData.getItemType();
        if (material == null || !material.isLegacy()) {
            return material;
        }

        Material mappedData = null;

        if (material.isBlock()) {
            // Try exact match first
            IBlockData iblock = materialToData.get(materialData);
            if (iblock != null) {
                mappedData = CraftMagicNumbers.getMaterial(iblock.getBlock());
            }

            // Fallback to any block
            if (mappedData == null) {
                Block block = materialToBlock.get(materialData);
                if (block != null) {
                    mappedData = CraftMagicNumbers.getMaterial(block);
                }
            }
        }

        // Fallback to matching item
        if (mappedData == null) {
            Item item = materialToItem.get(materialData);
            if (item != null) {
                mappedData = CraftMagicNumbers.getMaterial(item);
            }
        }

        return (mappedData == null) ? Material.AIR : mappedData;
    }

    public static Material[] values() {
        Material[] values = Material.values();
        return Arrays.copyOfRange(values, Material.LEGACY_AIR.ordinal(), values.length);
    }

    public static Material valueOf(String name) {
        return (name.startsWith(Material.LEGACY_PREFIX)) ? Material.valueOf(name) : Material.valueOf(Material.LEGACY_PREFIX + name);
    }

    public static Material getMaterial(String name) {
        return (name.startsWith(Material.LEGACY_PREFIX)) ? Material.getMaterial(name) : Material.getMaterial(Material.LEGACY_PREFIX + name);
    }

    public static Material matchMaterial(String name) {
        return (name.startsWith(Material.LEGACY_PREFIX)) ? Material.matchMaterial(name) : Material.matchMaterial(Material.LEGACY_PREFIX + name);
    }

    public static int ordinal(Material material) {
        Preconditions.checkArgument(material.isLegacy(), "ordinal on modern Material");

        return material.ordinal() - Material.LEGACY_AIR.ordinal();
    }

    public static String name(Material material) {
        return material.name().substring(Material.LEGACY_PREFIX.length());
    }

    public static String toString(Material material) {
        return name(material);
    }

    public static Material[] modern_values() {
        Material[] values = Material.values();
        return Arrays.copyOfRange(values, 0, Material.LEGACY_AIR.ordinal());
    }

    public static int modern_ordinal(Material material) {
        if (material.isLegacy()) {
            // SPIGOT-4002: Fix for eclipse compiler manually compiling in default statements to lookupswitch
            throw new NoSuchFieldError("Legacy field ordinal: " + material);
        }

        return material.ordinal();
    }

    static {
        SPAWN_EGGS.put(EntityType.BAT, Material.BAT_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.BLAZE, Material.BLAZE_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.CAVE_SPIDER, Material.CAVE_SPIDER_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.COW, Material.COW_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.CREEPER, Material.CREEPER_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.ENDERMAN, Material.ENDERMAN_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.ENDERMITE, Material.ENDERMITE_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.GHAST, Material.GHAST_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.GUARDIAN, Material.GUARDIAN_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.HORSE, Material.HORSE_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.MAGMA_CUBE, Material.MAGMA_CUBE_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.MUSHROOM_COW, Material.MOOSHROOM_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.OCELOT, Material.OCELOT_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.PIG, Material.PIG_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.RABBIT, Material.RABBIT_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SHEEP, Material.SHEEP_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SHULKER, Material.SHULKER_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SILVERFISH, Material.SILVERFISH_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SKELETON, Material.SKELETON_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SLIME, Material.SLIME_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SPIDER, Material.SPIDER_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.SQUID, Material.SQUID_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.VILLAGER, Material.VILLAGER_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.WITCH, Material.WITCH_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.WOLF, Material.WOLF_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.PIG_ZOMBIE, Material.ZOMBIE_PIGMAN_SPAWN_EGG);
        SPAWN_EGGS.put(EntityType.ZOMBIE, Material.ZOMBIE_SPAWN_EGG);

        DispenserRegistry.c();

        for (Material material : Material.values()) {
            if (!material.isLegacy()) {
                continue;
            }

            // Handle blocks
            if (material.isBlock()) {
                for (byte data = 0; data < 16; data++) {
                    MaterialData matData = new MaterialData(material, data);
                    Dynamic blockTag = DataConverterFlattenData.b(material.getId() << 4 | data);
                    // TODO: better skull conversion, chests
                    if (blockTag.getString("Name").contains("%%FILTER_ME%%")) {
                        continue;
                    }

                    String name = blockTag.getString("Name");
                    // TODO: need to fix
                    if (name.equals("minecraft:portal")) {
                        name = "minecraft:nether_portal";
                    }

                    Block block = Block.REGISTRY.get(new MinecraftKey(name));
                    IBlockData blockData = block.getBlockData();
                    BlockStateList states = block.getStates();

                    Optional<Dynamic> propMap = blockTag.get("Properties");
                    if (propMap.isPresent()) {
                        NBTTagCompound properties = (NBTTagCompound) propMap.get().getValue();
                        for (String dataKey : properties.getKeys()) {
                            IBlockState state = states.a(dataKey);

                            if (state == null) {
                                if (whitelistedStates.contains(dataKey)) {
                                    continue;
                                }
                                throw new IllegalStateException("No state for " + dataKey);
                            }

                            Preconditions.checkState(!properties.getString(dataKey).isEmpty(), "Empty data string");
                            Optional opt = state.b(properties.getString(dataKey));

                            blockData = blockData.set(state, (Comparable) opt.get());
                        }
                    }

                    if (block == Blocks.AIR) {
                        continue;
                    }

                    materialToData.put(matData, blockData);
                    if (!dataToMaterial.containsKey(blockData)) {
                        dataToMaterial.put(blockData, matData);
                    }

                    materialToBlock.put(matData, block);
                    if (!blockToMaterial.containsKey(block)) {
                        blockToMaterial.put(block, matData);
                    }
                }
            }

            // Handle items (and second fallback for blocks)
            int maxData = material.getMaxDurability() == 0 ? 16 : 1;
            // Manually do oldold spawn eggs
            if (material == Material.LEGACY_MONSTER_EGG) {
                maxData = 121; // Vilager + 1
            }

            for (byte data = 0; data < maxData; data++) {
                // Manually skip invalid oldold spawn
                if (material == Material.LEGACY_MONSTER_EGG /*&& data != 0 && EntityType.fromId(data) == null*/) { // Mojang broke 18w19b
                    continue;
                }
                // Skip non item stacks for now (18w19b)
                if (DataConverterMaterialId.a(material.getId()) == null) {
                    continue;
                }

                MaterialData matData = new MaterialData(material, data);

                NBTTagCompound stack = new NBTTagCompound();
                stack.setInt("id", material.getId());
                stack.setShort("Damage", data);

                Dynamic<NBTBase> converted = DataConverterRegistry.a().update(DataConverterTypes.ITEM_STACK, new Dynamic<NBTBase>(DynamicOpsNBT.a, stack), -1, CraftMagicNumbers.DATA_VERSION);

                String newId = converted.getString("id");
                // Recover spawn eggs with invalid data
                if (newId.equals("minecraft:spawn_egg")) {
                    newId = "minecraft:pig_spawn_egg";
                }

                // Preconditions.checkState(newId.contains("minecraft:"), "Unknown new material for " + matData);
                Item newMaterial = Item.REGISTRY.get(new MinecraftKey(newId));

                materialToItem.put(matData, newMaterial);
                if (!itemToMaterial.containsKey(newMaterial)) {
                    itemToMaterial.put(newMaterial, matData);
                }
            }

            for (Map.Entry<EntityType, Material> entry : SPAWN_EGGS.entrySet()) {
                MaterialData matData = new MaterialData(Material.LEGACY_MONSTER_EGG, (byte) entry.getKey().getTypeId());
                Item newMaterial = CraftMagicNumbers.getItem(entry.getValue());

                materialToItem.put(matData, newMaterial);
                itemToMaterial.put(newMaterial, matData);
            }
        }
    }
}
