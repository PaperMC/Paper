package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.entity.EntityType;

public enum CraftStatistic {
    // Start generate - CraftStatisticCustom
    // @GeneratedFrom 1.21.6-pre1
    ANIMALS_BRED(Stats.ANIMALS_BRED),
    AVIATE_ONE_CM(Stats.AVIATE_ONE_CM),
    BELL_RING(Stats.BELL_RING),
    BOAT_ONE_CM(Stats.BOAT_ONE_CM),
    ARMOR_CLEANED(Stats.CLEAN_ARMOR),
    BANNER_CLEANED(Stats.CLEAN_BANNER),
    CLEAN_SHULKER_BOX(Stats.CLEAN_SHULKER_BOX),
    CLIMB_ONE_CM(Stats.CLIMB_ONE_CM),
    CROUCH_ONE_CM(Stats.CROUCH_ONE_CM),
    DAMAGE_ABSORBED(Stats.DAMAGE_ABSORBED),
    DAMAGE_BLOCKED_BY_SHIELD(Stats.DAMAGE_BLOCKED_BY_SHIELD),
    DAMAGE_DEALT(Stats.DAMAGE_DEALT),
    DAMAGE_DEALT_ABSORBED(Stats.DAMAGE_DEALT_ABSORBED),
    DAMAGE_DEALT_RESISTED(Stats.DAMAGE_DEALT_RESISTED),
    DAMAGE_RESISTED(Stats.DAMAGE_RESISTED),
    DAMAGE_TAKEN(Stats.DAMAGE_TAKEN),
    DEATHS(Stats.DEATHS),
    DROP_COUNT(Stats.DROP),
    CAKE_SLICES_EATEN(Stats.EAT_CAKE_SLICE),
    ITEM_ENCHANTED(Stats.ENCHANT_ITEM),
    FALL_ONE_CM(Stats.FALL_ONE_CM),
    CAULDRON_FILLED(Stats.FILL_CAULDRON),
    FISH_CAUGHT(Stats.FISH_CAUGHT),
    FLY_ONE_CM(Stats.FLY_ONE_CM),
    HAPPY_GHAST_ONE_CM(Stats.HAPPY_GHAST_ONE_CM),
    HORSE_ONE_CM(Stats.HORSE_ONE_CM),
    DISPENSER_INSPECTED(Stats.INSPECT_DISPENSER),
    DROPPER_INSPECTED(Stats.INSPECT_DROPPER),
    HOPPER_INSPECTED(Stats.INSPECT_HOPPER),
    INTERACT_WITH_ANVIL(Stats.INTERACT_WITH_ANVIL),
    BEACON_INTERACTION(Stats.INTERACT_WITH_BEACON),
    INTERACT_WITH_BLAST_FURNACE(Stats.INTERACT_WITH_BLAST_FURNACE),
    BREWINGSTAND_INTERACTION(Stats.INTERACT_WITH_BREWINGSTAND),
    INTERACT_WITH_CAMPFIRE(Stats.INTERACT_WITH_CAMPFIRE),
    INTERACT_WITH_CARTOGRAPHY_TABLE(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE),
    CRAFTING_TABLE_INTERACTION(Stats.INTERACT_WITH_CRAFTING_TABLE),
    FURNACE_INTERACTION(Stats.INTERACT_WITH_FURNACE),
    INTERACT_WITH_GRINDSTONE(Stats.INTERACT_WITH_GRINDSTONE),
    INTERACT_WITH_LECTERN(Stats.INTERACT_WITH_LECTERN),
    INTERACT_WITH_LOOM(Stats.INTERACT_WITH_LOOM),
    INTERACT_WITH_SMITHING_TABLE(Stats.INTERACT_WITH_SMITHING_TABLE),
    INTERACT_WITH_SMOKER(Stats.INTERACT_WITH_SMOKER),
    INTERACT_WITH_STONECUTTER(Stats.INTERACT_WITH_STONECUTTER),
    JUMP(Stats.JUMP),
    LEAVE_GAME(Stats.LEAVE_GAME),
    MINECART_ONE_CM(Stats.MINECART_ONE_CM),
    MOB_KILLS(Stats.MOB_KILLS),
    OPEN_BARREL(Stats.OPEN_BARREL),
    CHEST_OPENED(Stats.OPEN_CHEST),
    ENDERCHEST_OPENED(Stats.OPEN_ENDERCHEST),
    SHULKER_BOX_OPENED(Stats.OPEN_SHULKER_BOX),
    PIG_ONE_CM(Stats.PIG_ONE_CM),
    NOTEBLOCK_PLAYED(Stats.PLAY_NOTEBLOCK),
    RECORD_PLAYED(Stats.PLAY_RECORD),
    PLAY_ONE_MINUTE(Stats.PLAY_TIME),
    PLAYER_KILLS(Stats.PLAYER_KILLS),
    FLOWER_POTTED(Stats.POT_FLOWER),
    RAID_TRIGGER(Stats.RAID_TRIGGER),
    RAID_WIN(Stats.RAID_WIN),
    SLEEP_IN_BED(Stats.SLEEP_IN_BED),
    SNEAK_TIME(Stats.CROUCH_TIME),
    SPRINT_ONE_CM(Stats.SPRINT_ONE_CM),
    STRIDER_ONE_CM(Stats.STRIDER_ONE_CM),
    SWIM_ONE_CM(Stats.SWIM_ONE_CM),
    TALKED_TO_VILLAGER(Stats.TALKED_TO_VILLAGER),
    TARGET_HIT(Stats.TARGET_HIT),
    TIME_SINCE_DEATH(Stats.TIME_SINCE_DEATH),
    TIME_SINCE_REST(Stats.TIME_SINCE_REST),
    TOTAL_WORLD_TIME(Stats.TOTAL_WORLD_TIME),
    TRADED_WITH_VILLAGER(Stats.TRADED_WITH_VILLAGER),
    TRAPPED_CHEST_TRIGGERED(Stats.TRIGGER_TRAPPED_CHEST),
    NOTEBLOCK_TUNED(Stats.TUNE_NOTEBLOCK),
    CAULDRON_USED(Stats.USE_CAULDRON),
    WALK_ON_WATER_ONE_CM(Stats.WALK_ON_WATER_ONE_CM),
    WALK_ONE_CM(Stats.WALK_ONE_CM),
    WALK_UNDER_WATER_ONE_CM(Stats.WALK_UNDER_WATER_ONE_CM),
    // End generate - CraftStatisticCustom
    // Start generate - CraftStatisticType
    // @GeneratedFrom 1.21.6-pre1
    BREAK_ITEM(ResourceLocation.withDefaultNamespace("broken")),
    CRAFT_ITEM(ResourceLocation.withDefaultNamespace("crafted")),
    DROP(ResourceLocation.withDefaultNamespace("dropped")),
    KILL_ENTITY(ResourceLocation.withDefaultNamespace("killed")),
    ENTITY_KILLED_BY(ResourceLocation.withDefaultNamespace("killed_by")),
    MINE_BLOCK(ResourceLocation.withDefaultNamespace("mined")),
    PICKUP(ResourceLocation.withDefaultNamespace("picked_up")),
    USE_ITEM(ResourceLocation.withDefaultNamespace("used"));
    // End generate - CraftStatisticType
    private final ResourceLocation minecraftKey;
    private final org.bukkit.Statistic bukkit;
    private static final BiMap<ResourceLocation, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<ResourceLocation, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.builder();
        for (CraftStatistic statistic : CraftStatistic.values()) {
            statisticBuilder.put(statistic.minecraftKey, statistic.bukkit);
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic(ResourceLocation minecraftKey) {
        this.minecraftKey = minecraftKey;

        this.bukkit = org.bukkit.Statistic.valueOf(this.name());
        Preconditions.checkState(this.bukkit != null, "Bukkit statistic %s does not exist", this.name());
    }

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.stats.Stat<?> statistic) {
        Preconditions.checkArgument(statistic != null, "NMS Statistic cannot be null");
        Registry statRegistry = statistic.getType().getRegistry();
        ResourceLocation nmsKey = BuiltInRegistries.STAT_TYPE.getKey(statistic.getType());

        if (statRegistry == BuiltInRegistries.CUSTOM_STAT) {
            nmsKey = (ResourceLocation) statistic.getValue();
        }

        return statistics.get(nmsKey);
    }

    public static net.minecraft.stats.Stat getNMSStatistic(org.bukkit.Statistic bukkit) {
        Preconditions.checkArgument(bukkit.getType() == Statistic.Type.UNTYPED, "This method only accepts untyped statistics");

        net.minecraft.stats.Stat<ResourceLocation> nms = Stats.CUSTOM.get(statistics.inverse().get(bukkit));
        Preconditions.checkArgument(nms != null, "NMS Statistic %s does not exist", bukkit);

        return nms;
    }

    public static net.minecraft.stats.Stat getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return Stats.BLOCK_MINED.get(CraftBlockType.bukkitToMinecraft(material));
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return Stats.ITEM_CRAFTED.get(CraftItemType.bukkitToMinecraft(material));
            }
            if (stat == Statistic.USE_ITEM) {
                return Stats.ITEM_USED.get(CraftItemType.bukkitToMinecraft(material));
            }
            if (stat == Statistic.BREAK_ITEM) {
                return Stats.ITEM_BROKEN.get(CraftItemType.bukkitToMinecraft(material));
            }
            if (stat == Statistic.PICKUP) {
                return Stats.ITEM_PICKED_UP.get(CraftItemType.bukkitToMinecraft(material));
            }
            if (stat == Statistic.DROP) {
                return Stats.ITEM_DROPPED.get(CraftItemType.bukkitToMinecraft(material));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.stats.Stat getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        Preconditions.checkArgument(entity != null, "EntityType cannot be null");
        if (entity.getName() != null) {
            net.minecraft.world.entity.EntityType<?> nmsEntity = CraftEntityType.bukkitToMinecraft(entity);

            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return net.minecraft.stats.Stats.ENTITY_KILLED.get(nmsEntity);
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return net.minecraft.stats.Stats.ENTITY_KILLED_BY.get(nmsEntity);
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.stats.Stat<net.minecraft.world.entity.EntityType<?>> statistic) {
        Preconditions.checkArgument(statistic != null, "NMS Statistic cannot be null");
        return CraftEntityType.minecraftToBukkit(statistic.getValue());
    }

    public static Material getMaterialFromStatistic(net.minecraft.stats.Stat<?> statistic) {
        if (statistic.getValue() instanceof Item statisticItemValue) {
            return CraftItemType.minecraftToBukkit(statisticItemValue);
        }
        if (statistic.getValue() instanceof Block statisticBlockValue) {
            return CraftBlockType.minecraftToBukkit(statisticBlockValue);
        }
        return null;
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, ServerPlayer player) {
        incrementStatistic(manager, statistic, 1, player);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, ServerPlayer player) {
        decrementStatistic(manager, statistic, 1, player);
    }

    public static int getStatistic(ServerStatsCounter manager, Statistic statistic) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(statistic.getType() == Type.UNTYPED, "Must supply additional parameter for this statistic");
        return manager.getValue(CraftStatistic.getNMSStatistic(statistic));
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, int amount, ServerPlayer player) {
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, getStatistic(manager, statistic) + amount, player);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, int amount, ServerPlayer player) {
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, getStatistic(manager, statistic) - amount, player);
    }

    public static void setStatistic(ServerStatsCounter manager, Statistic statistic, int newValue, ServerPlayer player) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(statistic.getType() == Type.UNTYPED, "Must supply additional parameter for this statistic");
        Preconditions.checkArgument(newValue >= 0, "Value must be greater than or equal to 0");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getNMSStatistic(statistic);
        manager.setValue(null, nmsStatistic, newValue);

        // Update scoreboards
        if (player != null) {
            player.level().getCraftServer().getScoreboardManager().forAllObjectives(nmsStatistic, player, score -> {
                score.set(newValue);
            });
        }
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material, ServerPlayer player) {
        incrementStatistic(manager, statistic, material, 1, player);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material, ServerPlayer player) {
        decrementStatistic(manager, statistic, material, 1, player);
    }

    public static int getStatistic(ServerStatsCounter manager, Statistic statistic, Material material) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Preconditions.checkArgument(nmsStatistic != null, "The supplied Material %s does not have a corresponding statistic", material);
        return manager.getValue(nmsStatistic);
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material, int amount, ServerPlayer player) {
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, material, getStatistic(manager, statistic, material) + amount, player);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, Material material, int amount, ServerPlayer player) {
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, material, getStatistic(manager, statistic, material) - amount, player);
    }

    public static void setStatistic(ServerStatsCounter manager, Statistic statistic, Material material, int newValue, ServerPlayer player) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(newValue >= 0, "Value must be greater than or equal to 0");
        Preconditions.checkArgument(statistic.getType() == Type.BLOCK || statistic.getType() == Type.ITEM, "This statistic does not take a Material parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getMaterialStatistic(statistic, material);
        Preconditions.checkArgument(nmsStatistic != null, "The supplied Material %s does not have a corresponding statistic", material);
        manager.setValue(null, nmsStatistic, newValue);

        // Update scoreboards
        if (player != null) {
            player.level().getCraftServer().getScoreboardManager().forAllObjectives(nmsStatistic, player, score -> {
                score.set(newValue);
            });
        }
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, ServerPlayer player) {
        incrementStatistic(manager, statistic, entityType, 1, player);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, ServerPlayer player) {
        decrementStatistic(manager, statistic, entityType, 1, player);
    }

    public static int getStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        Preconditions.checkArgument(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Preconditions.checkArgument(nmsStatistic != null, "The supplied EntityType %s does not have a corresponding statistic", entityType);
        return manager.getValue(nmsStatistic);
    }

    public static void incrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, int amount, ServerPlayer player) {
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, entityType, getStatistic(manager, statistic, entityType) + amount, player);
    }

    public static void decrementStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, int amount, ServerPlayer player) {
        Preconditions.checkArgument(amount > 0, "Amount must be greater than 0");
        setStatistic(manager, statistic, entityType, getStatistic(manager, statistic, entityType) - amount, player);
    }

    public static void setStatistic(ServerStatsCounter manager, Statistic statistic, EntityType entityType, int newValue, ServerPlayer player) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        Preconditions.checkArgument(newValue >= 0, "Value must be greater than or equal to 0");
        Preconditions.checkArgument(statistic.getType() == Type.ENTITY, "This statistic does not take an EntityType parameter");
        net.minecraft.stats.Stat nmsStatistic = CraftStatistic.getEntityStatistic(statistic, entityType);
        Preconditions.checkArgument(nmsStatistic != null, "The supplied EntityType %s does not have a corresponding statistic", entityType);
        manager.setValue(null, nmsStatistic, newValue);

        // Update scoreboards
        if (player != null) {
            player.level().getCraftServer().getScoreboardManager().forAllObjectives(nmsStatistic, player, score -> {
                score.set(newValue);
            });
        }
    }
}
