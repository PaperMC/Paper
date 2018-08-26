package org.bukkit.craftbukkit;

import net.minecraft.server.StatisticList;

import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.server.Block;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.IRegistry;
import net.minecraft.server.Item;
import net.minecraft.server.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public enum CraftStatistic {
    DAMAGE_DEALT(StatisticList.DAMAGE_DEALT),
    DAMAGE_TAKEN(StatisticList.DAMAGE_TAKEN),
    DEATHS(StatisticList.DEATHS),
    MOB_KILLS(StatisticList.MOB_KILLS),
    PLAYER_KILLS(StatisticList.PLAYER_KILLS),
    FISH_CAUGHT(StatisticList.FISH_CAUGHT),
    ANIMALS_BRED(StatisticList.ANIMALS_BRED),
    LEAVE_GAME(StatisticList.LEAVE_GAME),
    JUMP(StatisticList.JUMP),
    DROP_COUNT(StatisticList.DROP),
    DROP(new MinecraftKey("dropped")),
    PICKUP(new MinecraftKey("picked_up")),
    PLAY_ONE_MINUTE(StatisticList.PLAY_ONE_MINUTE),
    WALK_ONE_CM(StatisticList.WALK_ONE_CM),
    WALK_ON_WATER_ONE_CM(StatisticList.WALK_ON_WATER_ONE_CM),
    FALL_ONE_CM(StatisticList.FALL_ONE_CM),
    SNEAK_TIME(StatisticList.SNEAK_TIME),
    CLIMB_ONE_CM(StatisticList.CLIMB_ONE_CM),
    FLY_ONE_CM(StatisticList.FLY_ONE_CM),
    WALK_UNDER_WATER_ONE_CM(StatisticList.WALK_UNDER_WATER_ONE_CM),
    MINECART_ONE_CM(StatisticList.MINECART_ONE_CM),
    BOAT_ONE_CM(StatisticList.BOAT_ONE_CM),
    PIG_ONE_CM(StatisticList.PIG_ONE_CM),
    HORSE_ONE_CM(StatisticList.HORSE_ONE_CM),
    SPRINT_ONE_CM(StatisticList.SPRINT_ONE_CM),
    CROUCH_ONE_CM(StatisticList.CROUCH_ONE_CM),
    AVIATE_ONE_CM(StatisticList.AVIATE_ONE_CM),
    MINE_BLOCK(new MinecraftKey("mined")),
    USE_ITEM(new MinecraftKey("used")),
    BREAK_ITEM(new MinecraftKey("broken")),
    CRAFT_ITEM(new MinecraftKey("crafted")),
    KILL_ENTITY(new MinecraftKey("killed")),
    ENTITY_KILLED_BY(new MinecraftKey("killed_by")),
    TIME_SINCE_DEATH(StatisticList.TIME_SINCE_DEATH),
    TALKED_TO_VILLAGER(StatisticList.TALKED_TO_VILLAGER),
    TRADED_WITH_VILLAGER(StatisticList.TRADED_WITH_VILLAGER),
    CAKE_SLICES_EATEN(StatisticList.EAT_CAKE_SLICE),
    CAULDRON_FILLED(StatisticList.FILL_CAULDRON),
    CAULDRON_USED(StatisticList.USE_CAULDRON),
    ARMOR_CLEANED(StatisticList.CLEAN_ARMOR),
    BANNER_CLEANED(StatisticList.CLEAN_BANNER),
    BREWINGSTAND_INTERACTION(StatisticList.INTERACT_WITH_BREWINGSTAND),
    BEACON_INTERACTION(StatisticList.INTERACT_WITH_BEACON),
    DROPPER_INSPECTED(StatisticList.INSPECT_DROPPER),
    HOPPER_INSPECTED(StatisticList.INSPECT_HOPPER),
    DISPENSER_INSPECTED(StatisticList.INSPECT_DISPENSER),
    NOTEBLOCK_PLAYED(StatisticList.PLAY_NOTEBLOCK),
    NOTEBLOCK_TUNED(StatisticList.TUNE_NOTEBLOCK),
    FLOWER_POTTED(StatisticList.POT_FLOWER),
    TRAPPED_CHEST_TRIGGERED(StatisticList.TRIGGER_TRAPPED_CHEST),
    ENDERCHEST_OPENED(StatisticList.OPEN_ENDERCHEST),
    ITEM_ENCHANTED(StatisticList.ENCHANT_ITEM),
    RECORD_PLAYED(StatisticList.PLAY_RECORD),
    FURNACE_INTERACTION(StatisticList.INTERACT_WITH_FURNACE),
    CRAFTING_TABLE_INTERACTION(StatisticList.INTERACT_WITH_CRAFTING_TABLE),
    CHEST_OPENED(StatisticList.OPEN_CHEST),
    SLEEP_IN_BED(StatisticList.SLEEP_IN_BED),
    SHULKER_BOX_OPENED(StatisticList.OPEN_SHULKER_BOX),
    TIME_SINCE_REST(StatisticList.TIME_SINCE_REST),
    SWIM_ONE_CM(StatisticList.SWIM_ONE_CM),
    DAMAGE_DEALT_ABSORBED(StatisticList.DAMAGE_DEALT_ABSORBED),
    DAMAGE_DEALT_RESISTED(StatisticList.DAMAGE_DEALT_RESISTED),
    DAMAGE_BLOCKED_BY_SHIELD(StatisticList.DAMAGE_BLOCKED_BY_SHIELD),
    DAMAGE_ABSORBED(StatisticList.DAMAGE_ABSORBED),
    DAMAGE_RESISTED(StatisticList.DAMAGE_RESISTED),
    CLEAN_SHULKER_BOX(StatisticList.CLEAN_SHULKER_BOX);
    private final MinecraftKey minecraftKey;
    private final org.bukkit.Statistic bukkit;
    private static final BiMap<MinecraftKey, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<MinecraftKey, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.builder();
        for (CraftStatistic statistic : CraftStatistic.values()) {
            statisticBuilder.put(statistic.minecraftKey, statistic.bukkit);
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic(MinecraftKey minecraftKey) {
        this.minecraftKey = minecraftKey;

        this.bukkit = org.bukkit.Statistic.valueOf(this.name());
        Preconditions.checkState(bukkit != null, "Bukkit statistic %s does not exist", this.name());
    }

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.server.Statistic<?> statistic) {
        IRegistry statRegistry = statistic.a().a();
        MinecraftKey nmsKey = IRegistry.STATS.getKey(statistic.a());

        if (statRegistry == IRegistry.CUSTOM_STAT) {
            nmsKey = (MinecraftKey) statistic.b();
        }

        return statistics.get(nmsKey);
    }

    public static net.minecraft.server.Statistic getNMSStatistic(org.bukkit.Statistic bukkit) {
        Preconditions.checkArgument(bukkit.getType() == Statistic.Type.UNTYPED, "This method only accepts untyped statistics");

        net.minecraft.server.Statistic<MinecraftKey> nms = StatisticList.CUSTOM.b(statistics.inverse().get(bukkit));
        Preconditions.checkArgument(nms != null, "NMS Statistic %s does not exist", bukkit);

        return nms;
    }

    public static net.minecraft.server.Statistic getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatisticList.BLOCK_MINED.b(CraftMagicNumbers.getBlock(material));
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatisticList.ITEM_CRAFTED.b(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.USE_ITEM) {
                return StatisticList.ITEM_USED.b(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatisticList.ITEM_BROKEN.b(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.PICKUP) {
                return StatisticList.ITEM_PICKED_UP.b(CraftMagicNumbers.getItem(material));
            }
            if (stat == Statistic.DROP) {
                return StatisticList.ITEM_DROPPED.b(CraftMagicNumbers.getItem(material));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.server.Statistic getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        if (entity.getName() != null) {
            EntityTypes<?> nmsEntity = IRegistry.ENTITY_TYPE.get(new MinecraftKey(entity.getName()));

            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return net.minecraft.server.StatisticList.ENTITY_KILLED.b(nmsEntity);
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return net.minecraft.server.StatisticList.ENTITY_KILLED_BY.b(nmsEntity);
            }
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.server.Statistic<EntityTypes<?>> statistic) {
        MinecraftKey name = EntityTypes.getName(statistic.b());
        return EntityType.fromName(name.getKey());
    }

    public static Material getMaterialFromStatistic(net.minecraft.server.Statistic<?> statistic) {
        if (statistic.b() instanceof Item) {
            return CraftMagicNumbers.getMaterial((Item) statistic.b());
        }
        if (statistic.b() instanceof Block) {
            return CraftMagicNumbers.getMaterial((Block) statistic.b());
        }
        return null;
    }
}
