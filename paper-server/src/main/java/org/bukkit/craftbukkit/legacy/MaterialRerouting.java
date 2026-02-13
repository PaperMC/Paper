package org.bukkit.craftbukkit.legacy;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.RegionAccessor;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DecoratedPot;
import org.bukkit.block.Jukebox;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.legacy.reroute.InjectPluginVersion;
import org.bukkit.craftbukkit.legacy.reroute.RerouteStatic;
import org.bukkit.craftbukkit.tag.CraftBlockTag;
import org.bukkit.craftbukkit.tag.CraftItemTag;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Steerable;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.material.MaterialData;
import org.bukkit.scoreboard.Criteria;

public class MaterialRerouting {

    private static Material transformFromBlockType(Material blockType, ApiVersion version) {
        if (blockType == null) {
            return null;
        }

        if (version.isOlderThan(ApiVersion.FLATTENING)) {
            return CraftLegacy.toLegacyData(blockType, false).getItemType();
        }

        return blockType;
    }

    private static Material transformFromItemType(Material itemType, ApiVersion version) {
        if (itemType == null) {
            return null;
        }

        if (version.isOlderThan(ApiVersion.FLATTENING)) {
            return CraftLegacy.toLegacyData(itemType, true).getItemType();
        }

        return itemType;
    }

    private static Material transformToBlockType(Material material) {
        if (material == null) {
            return null;
        }

        if (material.isLegacy()) {
            return CraftLegacy.fromLegacy(new MaterialData(material), false);
        }

        return material;
    }

    private static Material transformToItemType(Material material) {
        if (material == null) {
            return null;
        }

        if (material.isLegacy()) {
            return CraftLegacy.fromLegacy(new MaterialData(material), true);
        }

        return material;
    }

    public static Material getMaterial(BlockData blockData, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(blockData.getMaterial(), version);
    }

    public static Material getPlacementMaterial(BlockData blockData, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(blockData.getPlacementMaterial(), version);
    }

    public static Material getType(Block block, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(block.getType(), version);
    }

    public static void setType(Block block, Material type) {
        block.setType(MaterialRerouting.transformToBlockType(type));
    }

    public static void setType(Block block, Material type, boolean applyPhysics) {
        block.setType(MaterialRerouting.transformToBlockType(type), applyPhysics);
    }

    public static Material getType(BlockState blockState, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(blockState.getType(), version);
    }

    public static void setType(BlockState blockState, Material type) {
        blockState.setType(MaterialRerouting.transformToBlockType(type));
    }

    public static void setSherd(DecoratedPot decoratedPot, DecoratedPot.Side side, Material sherd) {
        decoratedPot.setSherd(side, MaterialRerouting.transformToItemType(sherd));
    }

    public static Material getSherd(DecoratedPot decoratedPot, DecoratedPot.Side side, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(decoratedPot.getSherd(side), version);
    }

    public static Map<DecoratedPot.Side, Material> getSherds(DecoratedPot decoratedPot, @InjectPluginVersion ApiVersion version) {
        Map<DecoratedPot.Side, Material> result = new EnumMap<>(DecoratedPot.Side.class);
        for (Map.Entry<DecoratedPot.Side, Material> entry : decoratedPot.getSherds().entrySet()) {
            result.put(entry.getKey(), MaterialRerouting.transformFromItemType(entry.getValue(), version));
        }

        return result;
    }

    @Deprecated
    public static List<Material> getShards(DecoratedPot decoratedPot, @InjectPluginVersion ApiVersion version) {
        return decoratedPot.getSherds().values().stream().map(shard -> MaterialRerouting.transformFromItemType(shard, version)).toList();
    }

    public static void setPlaying(Jukebox jukebox, Material record) {
        jukebox.setPlaying(MaterialRerouting.transformToItemType(record));
    }

    public static Material getPlaying(Jukebox jukebox, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(jukebox.getPlaying(), version);
    }

    public static boolean includes(EnchantmentTarget enchantmentTarget, Material item) {
        return enchantmentTarget.includes(MaterialRerouting.transformToItemType(item));
    }

    public static boolean isBreedItem(Animals animals, Material material) {
        return animals.isBreedItem(MaterialRerouting.transformToItemType(material));
    }

    public static Material getMaterial(Boat.Type type, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(type.getMaterial(), version);
    }

    @Deprecated
    public static Material getMaterial(FallingBlock fallingBlock, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(fallingBlock.getBlockData().getMaterial(), version);
    }

    public static boolean hasCooldown(HumanEntity humanEntity, Material material) {
        return humanEntity.hasCooldown(MaterialRerouting.transformToItemType(material));
    }

    public static int getCooldown(HumanEntity humanEntity, Material material) {
        return humanEntity.getCooldown(MaterialRerouting.transformToItemType(material));
    }

    public static void setCooldown(HumanEntity humanEntity, Material material, int ticks) {
        humanEntity.setCooldown(MaterialRerouting.transformToItemType(material), ticks);
    }

    public static List<Block> getLineOfSight(LivingEntity livingEntity, Set<Material> transparent, int maxDistance) {
        if (transparent == null) {
            return livingEntity.getLineOfSight(null, maxDistance);
        }

        return livingEntity.getLineOfSight(transparent.stream().map(MaterialRerouting::transformToBlockType).collect(Collectors.toSet()), maxDistance);
    }

    public static Block getTargetBlock(LivingEntity livingEntity, Set<Material> transparent, int maxDistance) {
        if (transparent == null) {
            return livingEntity.getTargetBlock(null, maxDistance);
        }

        return livingEntity.getTargetBlock(transparent.stream().map(MaterialRerouting::transformToBlockType).collect(Collectors.toSet()), maxDistance);
    }

    public static List<Block> getLastTwoTargetBlocks(LivingEntity livingEntity, Set<Material> transparent, int maxDistance) {
        if (transparent == null) {
            return livingEntity.getLastTwoTargetBlocks(null, maxDistance);
        }

        return livingEntity.getLastTwoTargetBlocks(transparent.stream().map(MaterialRerouting::transformToBlockType).collect(Collectors.toSet()), maxDistance);
    }

    public static boolean addBarterMaterial(Piglin piglin, Material material) {
        return piglin.addBarterMaterial(MaterialRerouting.transformToItemType(material));
    }

    public static boolean removeBarterMaterial(Piglin piglin, Material material) {
        return piglin.removeBarterMaterial(MaterialRerouting.transformToItemType(material));
    }

    public static boolean addMaterialOfInterest(Piglin piglin, Material material) {
        return piglin.addMaterialOfInterest(MaterialRerouting.transformToItemType(material));
    }

    public static boolean removeMaterialOfInterest(Piglin piglin, Material material) {
        return piglin.removeMaterialOfInterest(MaterialRerouting.transformToItemType(material));
    }

    public static Set<Material> getInterestList(Piglin piglin, @InjectPluginVersion ApiVersion version) {
        return piglin.getInterestList().stream().map(item -> MaterialRerouting.transformFromItemType(item, version)).collect(Collectors.toSet());
    }

    public static Set<Material> getBarterList(Piglin piglin, @InjectPluginVersion ApiVersion version) {
        return piglin.getBarterList().stream().map(item -> MaterialRerouting.transformFromItemType(item, version)).collect(Collectors.toSet());
    }

    @Deprecated
    public static void sendBlockChange(Player player, Location location, Material material, byte data) {
        player.sendBlockChange(location, CraftBlockData.fromData(CraftMagicNumbers.getBlock(material, data)));
    }

    public static Material getSteerMaterial(Steerable steerable, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(steerable.getSteerMaterial(), version);
    }

    public static Material getMaterial(BlockCanBuildEvent blockCanBuildEvent, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(blockCanBuildEvent.getMaterial(), version);
    }

    public static Material getChangedType(BlockPhysicsEvent blockPhysicsEvent, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(blockPhysicsEvent.getChangedType(), version);
    }

    public static Material getTo(EntityChangeBlockEvent entityChangeBlockEvent, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(entityChangeBlockEvent.getTo(), version);
    }

    public static Material getItemType(FurnaceExtractEvent furnaceExtractEvent, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(furnaceExtractEvent.getItemType(), version);
    }

    public static Material getBucket(PlayerBucketEvent playerBucketEvent, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(playerBucketEvent.getBucket(), version);
    }

    public static Material getMaterial(PlayerInteractEvent playerInteractEvent, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(playerInteractEvent.getMaterial(), version);
    }

    public static Material getMaterial(PlayerStatisticIncrementEvent playerStatisticIncrementEvent, @InjectPluginVersion ApiVersion version) {
        if (playerStatisticIncrementEvent.getStatistic().getType() == Statistic.Type.BLOCK) {
            return MaterialRerouting.transformFromBlockType(playerStatisticIncrementEvent.getMaterial(), version);
        } else if (playerStatisticIncrementEvent.getStatistic().getType() == Statistic.Type.ITEM) {
            return MaterialRerouting.transformFromItemType(playerStatisticIncrementEvent.getMaterial(), version);
        } else {
            // Theoretically this should be null, but just in case convert from block type
            // Can probably check if it is not null and print a warning, but for now it should be fine without the check.
            return MaterialRerouting.transformFromBlockType(playerStatisticIncrementEvent.getMaterial(), version);
        }
    }

    public static void setBlock(ChunkGenerator.ChunkData chunkData, int x, int y, int z, Material material) {
        chunkData.setBlock(x, y, z, MaterialRerouting.transformToBlockType(material));
    }

    public static void setRegion(ChunkGenerator.ChunkData chunkData, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
        chunkData.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, MaterialRerouting.transformToBlockType(material));
    }

    public static Material getType(ChunkGenerator.ChunkData chunkData, int x, int y, int z, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(chunkData.getType(x, y, z), version);
    }

    public static BlockData getBlockData(BlockDataMeta blockDataMeta, Material material) {
        return blockDataMeta.getBlockData(MaterialRerouting.transformToBlockType(material));
    }

    public static CookingRecipe<?> setInput(CookingRecipe<?> cookingRecipe, Material material) {
        return cookingRecipe.setInput(MaterialRerouting.transformToItemType(material));
    }

    public static FurnaceRecipe setInput(FurnaceRecipe furnaceRecipe, Material material) {
        return furnaceRecipe.setInput(MaterialRerouting.transformToItemType(material));
    }

    @Deprecated
    public static FurnaceRecipe setInput(FurnaceRecipe furnaceRecipe, Material material, int data) {
        return furnaceRecipe.setInput(CraftItemType.minecraftToBukkit(CraftMagicNumbers.getItem(material, (short) data)));
    }

    public static boolean contains(Inventory inventory, Material material) {
        return inventory.contains(MaterialRerouting.transformToItemType(material));
    }

    public static boolean contains(Inventory inventory, Material material, int amount) {
        return inventory.contains(MaterialRerouting.transformToItemType(material), amount);
    }

    public static HashMap<Integer, ? extends ItemStack> all(Inventory inventory, Material material) {
        return inventory.all(MaterialRerouting.transformToItemType(material));
    }

    public static int first(Inventory inventory, Material material) {
        return inventory.first(MaterialRerouting.transformToItemType(material));
    }

    public static void remove(Inventory inventory, Material material) {
        inventory.remove(MaterialRerouting.transformToItemType(material));
    }

    public static ItemMeta getItemMeta(ItemFactory itemFactory, Material material) {
        return itemFactory.getItemMeta(MaterialRerouting.transformToItemType(material));
    }

    public static boolean isApplicable(ItemFactory itemFactory, ItemMeta itemMeta, Material material) {
        return itemFactory.isApplicable(itemMeta, MaterialRerouting.transformToItemType(material));
    }

    public static ItemMeta asMetaFor(ItemFactory itemFactory, ItemMeta itemMeta, Material material) {
        return itemFactory.asMetaFor(itemMeta, MaterialRerouting.transformToItemType(material));
    }

    public static Material getSpawnEgg(ItemFactory itemFactory, EntityType entityType, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(itemFactory.getSpawnEgg(entityType), version);
    }

    public static Material getType(ItemStack itemStack, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromItemType(itemStack.getType(), version);
    }

    public static void setType(ItemStack itemStack, Material material) {
        itemStack.setType(MaterialRerouting.transformToItemType(material));
    }

    public static List<Material> getChoices(RecipeChoice.MaterialChoice materialChoice, @InjectPluginVersion ApiVersion version) {
        return materialChoice.getChoices().stream().map(m -> MaterialRerouting.transformFromItemType(m, version)).toList();
    }

    public static ShapedRecipe setIngredient(ShapedRecipe shapedRecipe, char key, Material material) {
        return shapedRecipe.setIngredient(key, MaterialRerouting.transformToItemType(material));
    }

    @Deprecated
    public static ShapedRecipe setIngredient(ShapedRecipe shapedRecipe, char key, Material material, int raw) {
        return shapedRecipe.setIngredient(key, CraftItemType.minecraftToBukkit(CraftMagicNumbers.getItem(material, (short) raw)));
    }

    public static ShapelessRecipe addIngredient(ShapelessRecipe shapelessRecipe, Material material) {
        return shapelessRecipe.addIngredient(MaterialRerouting.transformToItemType(material));
    }

    @Deprecated
    public static ShapelessRecipe addIngredient(ShapelessRecipe shapelessRecipe, Material material, int raw) {
        return shapelessRecipe.addIngredient(CraftItemType.minecraftToBukkit(CraftMagicNumbers.getItem(material, (short) raw)));
    }

    public static ShapelessRecipe addIngredient(ShapelessRecipe shapelessRecipe, int count, Material material) {
        return shapelessRecipe.addIngredient(count, MaterialRerouting.transformToItemType(material));
    }

    @Deprecated
    public static ShapelessRecipe addIngredient(ShapelessRecipe shapelessRecipe, int count, Material material, int raw) {
        return shapelessRecipe.addIngredient(count, CraftItemType.minecraftToBukkit(CraftMagicNumbers.getItem(material, (short) raw)));
    }

    public static ShapelessRecipe removeIngredient(ShapelessRecipe shapelessRecipe, Material material) {
        return shapelessRecipe.removeIngredient(MaterialRerouting.transformToItemType(material));
    }

    public static ShapelessRecipe removeIngredient(ShapelessRecipe shapelessRecipe, int count, Material material) {
        return shapelessRecipe.removeIngredient(count, MaterialRerouting.transformToItemType(material));
    }

    @Deprecated
    public static ShapelessRecipe removeIngredient(ShapelessRecipe shapelessRecipe, Material material, int raw) {
        return shapelessRecipe.removeIngredient(CraftItemType.minecraftToBukkit(CraftMagicNumbers.getItem(material, (short) raw)));
    }

    @Deprecated
    public static ShapelessRecipe removeIngredient(ShapelessRecipe shapelessRecipe, int count, Material material, int raw) {
        return shapelessRecipe.removeIngredient(count, CraftItemType.minecraftToBukkit(CraftMagicNumbers.getItem(material, (short) raw)));
    }

    public static StonecuttingRecipe setInput(StonecuttingRecipe stonecuttingRecipe, Material material) {
        return stonecuttingRecipe.setInput(MaterialRerouting.transformToItemType(material));
    }

    @RerouteStatic("org/bukkit/scoreboard/Criteria")
    public static Criteria statistic(Statistic statistic, Material material) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            return Criteria.statistic(statistic, MaterialRerouting.transformToBlockType(material));
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            return Criteria.statistic(statistic, MaterialRerouting.transformToItemType(material));
        } else {
            // This is not allowed, the method will throw an error
            return Criteria.statistic(statistic, MaterialRerouting.transformToBlockType(material));
        }
    }

    @RerouteStatic("org/bukkit/Bukkit")
    public static BlockData createBlockData(Material material) {
        return Bukkit.createBlockData(MaterialRerouting.transformToBlockType(material));
    }

    @RerouteStatic("org/bukkit/Bukkit")
    public static BlockData createBlockData(Material material, Consumer<? super BlockData> consumer) {
        return Bukkit.createBlockData(MaterialRerouting.transformToBlockType(material), consumer);
    }

    @RerouteStatic("org/bukkit/Bukkit")
    public static BlockData createBlockData(Material material, String data) {
        return Bukkit.createBlockData(MaterialRerouting.transformToBlockType(material), data);
    }

    public static Material getBlockType(ChunkSnapshot chunkSnapshot, int x, int y, int z, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(chunkSnapshot.getBlockType(x, y, z), version);
    }

    public static void incrementStatistic(OfflinePlayer offlinePlayer, Statistic statistic, Material material) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            offlinePlayer.incrementStatistic(statistic, MaterialRerouting.transformToBlockType(material));
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            offlinePlayer.incrementStatistic(statistic, MaterialRerouting.transformToItemType(material));
        } else {
            // This is not allowed, the method will throw an error
            offlinePlayer.incrementStatistic(statistic, MaterialRerouting.transformToBlockType(material));
        }
    }

    public static void decrementStatistic(OfflinePlayer offlinePlayer, Statistic statistic, Material material) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            offlinePlayer.decrementStatistic(statistic, MaterialRerouting.transformToBlockType(material));
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            offlinePlayer.decrementStatistic(statistic, MaterialRerouting.transformToItemType(material));
        } else {
            // This is not allowed, the method will throw an error
            offlinePlayer.decrementStatistic(statistic, MaterialRerouting.transformToBlockType(material));
        }
    }

    public static int getStatistic(OfflinePlayer offlinePlayer, Statistic statistic, Material material) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            return offlinePlayer.getStatistic(statistic, MaterialRerouting.transformToBlockType(material));
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            return offlinePlayer.getStatistic(statistic, MaterialRerouting.transformToItemType(material));
        } else {
            // This is not allowed, the method will throw an error
            return offlinePlayer.getStatistic(statistic, MaterialRerouting.transformToBlockType(material));
        }
    }

    public static void incrementStatistic(OfflinePlayer offlinePlayer, Statistic statistic, Material material, int amount) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            offlinePlayer.incrementStatistic(statistic, MaterialRerouting.transformToBlockType(material), amount);
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            offlinePlayer.incrementStatistic(statistic, MaterialRerouting.transformToItemType(material), amount);
        } else {
            // This is not allowed, the method will throw an error
            offlinePlayer.incrementStatistic(statistic, MaterialRerouting.transformToBlockType(material), amount);
        }
    }

    public static void decrementStatistic(OfflinePlayer offlinePlayer, Statistic statistic, Material material, int amount) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            offlinePlayer.decrementStatistic(statistic, MaterialRerouting.transformToBlockType(material), amount);
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            offlinePlayer.decrementStatistic(statistic, MaterialRerouting.transformToItemType(material), amount);
        } else {
            // This is not allowed, the method will throw an error
            offlinePlayer.decrementStatistic(statistic, MaterialRerouting.transformToBlockType(material), amount);
        }
    }

    public static void setStatistic(OfflinePlayer offlinePlayer, Statistic statistic, Material material, int newValue) {
        if (statistic.getType() == Statistic.Type.BLOCK) {
            offlinePlayer.setStatistic(statistic, MaterialRerouting.transformToBlockType(material), newValue);
        } else if (statistic.getType() == Statistic.Type.ITEM) {
            offlinePlayer.setStatistic(statistic, MaterialRerouting.transformToItemType(material), newValue);
        } else {
            // This is not allowed, the method will throw an error
            offlinePlayer.setStatistic(statistic, MaterialRerouting.transformToBlockType(material), newValue);
        }
    }

    public static Material getType(RegionAccessor regionAccessor, Location location, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(regionAccessor.getType(location), version);
    }

    public static Material getType(RegionAccessor regionAccessor, int x, int y, int z, @InjectPluginVersion ApiVersion version) {
        return MaterialRerouting.transformFromBlockType(regionAccessor.getType(x, y, z), version);
    }

    public static void setType(RegionAccessor regionAccessor, Location location, Material material) {
        regionAccessor.setType(location, MaterialRerouting.transformToBlockType(material));
    }

    public static void setType(RegionAccessor regionAccessor, int x, int y, int z, Material material) {
        regionAccessor.setType(x, y, z, MaterialRerouting.transformToBlockType(material));
    }

    public static BlockData createBlockData(Server server, Material material) {
        return server.createBlockData(MaterialRerouting.transformToBlockType(material));
    }

    public static BlockData createBlockData(Server server, Material material, Consumer<? super BlockData> consumer) {
        return server.createBlockData(MaterialRerouting.transformToBlockType(material), consumer);
    }

    public static BlockData createBlockData(Server server, Material material, String data) {
        return server.createBlockData(MaterialRerouting.transformToBlockType(material), data);
    }

    public static <T extends Keyed> boolean isTagged(Tag<T> tag, T item) {
        if (tag instanceof CraftBlockTag) {
            return tag.isTagged((T) MaterialRerouting.transformToBlockType((Material) item));
        } else if (tag instanceof CraftItemTag) {
            return tag.isTagged((T) MaterialRerouting.transformToItemType((Material) item));
        }

        return tag.isTagged(item);
    }

    public static <T extends Keyed> Set<T> getValues(Tag<T> tag, @InjectPluginVersion ApiVersion version) {
        Set<T> values = tag.getValues();
        if (values.isEmpty()) {
            return values;
        }

        if (tag instanceof CraftBlockTag) {
            return values.stream().map(val -> (Material) val).map(val -> MaterialRerouting.transformFromBlockType(val, version)).map(val -> (T) val).collect(Collectors.toSet());
        } else if (tag instanceof CraftItemTag) {
            return values.stream().map(val -> (Material) val).map(val -> MaterialRerouting.transformFromItemType(val, version)).map(val -> (T) val).collect(Collectors.toSet());
        }

        return values;
    }

    @Deprecated
    public static FallingBlock spawnFallingBlock(World world, Location location, Material material, byte data) {
        return world.spawnFallingBlock(location, CraftBlockData.fromData(CraftMagicNumbers.getBlock(material, data)));
    }

    public static ToolComponent.ToolRule addRule(ToolComponent toolComponent, Material block, Float speed, Boolean correctForDrops) {
        return toolComponent.addRule(MaterialRerouting.transformToBlockType(block), speed, correctForDrops);
    }

    public static ToolComponent.ToolRule addRule(ToolComponent toolComponent, Collection<Material> blocks, Float speed, Boolean correctForDrops) {
        return toolComponent.addRule(blocks.stream().map(MaterialRerouting::transformToBlockType).collect(Collectors.toList()), speed, correctForDrops);
    }

    public static Collection<Material> getBlocks(ToolComponent.ToolRule toolRule, @InjectPluginVersion ApiVersion version) {
        return toolRule.getBlocks().stream().map(val -> MaterialRerouting.transformFromBlockType(val, version)).toList();
    }

    public static void setBlocks(ToolComponent.ToolRule toolRule, Material block) {
        toolRule.setBlocks(MaterialRerouting.transformToBlockType(block));
    }

    public static void setBlocks(ToolComponent.ToolRule toolRule, Collection<Material> blocks) {
        toolRule.setBlocks(blocks.stream().map(MaterialRerouting::transformToBlockType).toList());
    }

    // Paper start - register paper API specific material consumers in rerouting
    // A lot of these methods do *not* run through MaterialRerouting to avoid the overhead of a system that
    // currently is an effective noop.
    // The only downside is that upstream moved the handling of legacy materials into transformFromXType methods.
    // As such, methods introduced prior to 1.13 need to run through the transformation to make sure legacy material
    // constants still work.

    // Utility method for constructing a set from an existing one after mapping each element.
    private static <I, O> Set<O> mapSet(final Set<I> input, final java.util.function.Function<I,O> mapper) {
        final Set<O> output = new it.unimi.dsi.fastutil.objects.ObjectOpenHashSet<>(input.size());
        for (final I i : input) {
            output.add(mapper.apply(i));
        }
        return output;
    }

    // Method added post-1.13, noop (https://github.com/PaperMC/Paper/pull/4965)
    public static org.bukkit.Material getMinecartMaterial(org.bukkit.entity.Minecart minecart, @InjectPluginVersion ApiVersion version) {
        return minecart.getMinecartMaterial();
    }

    // Method added post-1.13, noop (https://github.com/PaperMC/Paper/pull/4965)
    public static Material getBoatMaterial(Boat boat, @InjectPluginVersion ApiVersion version) {
        return boat.getBoatMaterial();
    }

    // Method added post-1.13, noop (https://github.com/PaperMC/Paper/pull/3807)
    public static Material getType(io.papermc.paper.event.player.PlayerItemCooldownEvent event, @InjectPluginVersion ApiVersion version) {
        return event.getType();
    }

    // Method added post-1.13, noop (https://github.com/PaperMC/Paper/pull/3850)
    public static Collection<Material> getInfiniburn(World world, @InjectPluginVersion ApiVersion version) {
        return world.getInfiniburn();
    }

    // Method added pre-1.13, needs legacy rerouting (https://github.com/PaperMC/Paper/commit/3438e96192)
    public static Set<Material> getTypes(
        final com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType slotType,
        @InjectPluginVersion final ApiVersion apiVersion
    ) {
        if (apiVersion.isNewerThanOrSameAs(ApiVersion.FLATTENING)) return slotType.getTypes();
        else return mapSet(slotType.getTypes(), MaterialRerouting::transformToItemType); // Needed as pre-flattening is handled by transformToItemType
    }

    // Method added pre-1.13, needs legacy rerouting (https://github.com/PaperMC/Paper/commit/3438e96192)
    @RerouteStatic("com/destroystokyo/paper/event/player/PlayerArmorChangeEvent$SlotType")
    public static com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType getByMaterial(
        final Material material
    ) {
        return com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType.getByMaterial(MaterialRerouting.transformToItemType(material));
    }

    // Method added pre-1.13, needs legacy rerouting (https://github.com/PaperMC/Paper/commit/3438e96192)
    @RerouteStatic("com/destroystokyo/paper/event/player/PlayerArmorChangeEvent$SlotType")
    public static boolean isEquipable(final Material material) {
        return com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType.isEquipable(MaterialRerouting.transformToItemType(material));
    }

    // Method added post 1.13, no-op (https://github.com/PaperMC/Paper/pull/1244)
    public static Material getMaterial(final com.destroystokyo.paper.event.block.AnvilDamagedEvent.DamageState damageState) {
        return damageState.getMaterial();
    }

    // Method added post 1.13, no-op (https://github.com/PaperMC/Paper/pull/1244)
    @RerouteStatic("com/destroystokyo/paper/event/block/AnvilDamagedEvent$DamageState")
    public static com.destroystokyo.paper.event.block.AnvilDamagedEvent.DamageState getState(
        final Material material
    ) {
        return com.destroystokyo.paper.event.block.AnvilDamagedEvent.DamageState.getState(material);
    }

    // Method added post 1.13, no-op (https://github.com/PaperMC/Paper/pull/10290)
    public static ItemStack withType(final ItemStack itemStack, final Material material) {
        return itemStack.withType(material);
    }
    // Paper end - register paper API specific material consumers in rerouting

    // Paper start - methods added post 1.13, no-op
    @RerouteStatic("org/bukkit/inventory/ItemStack")
    public static ItemStack of(final Material material) {
        return ItemStack.of(material);
    }

    @RerouteStatic("org/bukkit/inventory/ItemStack")
    public static ItemStack of(final Material material, final int amount) {
        return ItemStack.of(material, amount);
    }
    // Paper end

    // Paper start - methods added post 1.13, no-op (https://github.com/PaperMC/Paper/pull/1015)
    public static Set<Material> getCanDestroy(final ItemMeta meta) {
        return meta.getCanDestroy();
    }

    public static void setCanDestroy(final ItemMeta meta, final Set<Material> materials) {
        meta.setCanDestroy(materials);
    }

    public static Set<Material> getCanPlaceOn(final ItemMeta meta) {
        return meta.getCanPlaceOn();
    }

    public static void setCanPlaceOn(final ItemMeta meta, final Set<Material> materials) {
        meta.setCanPlaceOn(materials);
    }
    // Paper end
}
