package org.bukkit.craftbukkit;

import net.minecraft.server.EntityTypes;
import net.minecraft.server.EntityTypes.MonsterEggInfo;
import net.minecraft.server.StatisticList;

import org.bukkit.Achievement;
import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.MinecraftKey;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;
    private static final BiMap<String, org.bukkit.Achievement> achievements;

    static {
        ImmutableMap<String, org.bukkit.Achievement> specialCases = ImmutableMap.<String, org.bukkit.Achievement> builder()
                .put("achievement.buildWorkBench", Achievement.BUILD_WORKBENCH)
                .put("achievement.diamonds", Achievement.GET_DIAMONDS)
                .put("achievement.portal", Achievement.NETHER_PORTAL)
                .put("achievement.ghast", Achievement.GHAST_RETURN)
                .put("achievement.theEnd", Achievement.END_PORTAL)
                .put("achievement.theEnd2", Achievement.THE_END)
                .put("achievement.blazeRod", Achievement.GET_BLAZE_ROD)
                .put("achievement.potion", Achievement.BREW_POTION)
                .build();
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        ImmutableBiMap.Builder<String, org.bukkit.Achievement> achievementBuilder = ImmutableBiMap.<String, org.bukkit.Achievement>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }
        for (Achievement achievement : Achievement.values()) {
            if (specialCases.values().contains(achievement)) {
                continue;
            }
            achievementBuilder.put("achievement." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, achievement.name()), achievement);
        }

        achievementBuilder.putAll(specialCases);

        statistics = statisticBuilder.build();
        achievements = achievementBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Achievement getBukkitAchievement(net.minecraft.server.Achievement achievement) {
        return getBukkitAchievementByName(achievement.name);
    }

    public static org.bukkit.Achievement getBukkitAchievementByName(String name) {
        return achievements.get(name);
    }

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.server.Statistic statistic) {
        return getBukkitStatisticByName(statistic.name);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem")) {
            name = "stat.craftItem";
        }
        return statistics.get(name);
    }

    public static net.minecraft.server.Statistic getNMSStatistic(org.bukkit.Statistic statistic) {
        return StatisticList.getStatistic(statistics.inverse().get(statistic));
    }

    public static net.minecraft.server.Achievement getNMSAchievement(org.bukkit.Achievement achievement) {
        return (net.minecraft.server.Achievement) StatisticList.getStatistic(achievements.inverse().get(achievement));
    }

    public static net.minecraft.server.Statistic getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatisticList.MINE_BLOCK_COUNT[material.getId()];
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatisticList.CRAFT_BLOCK_COUNT[material.getId()];
            }
            if (stat == Statistic.USE_ITEM) {
                return StatisticList.USE_ITEM_COUNT[material.getId()];
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatisticList.BREAK_ITEM_COUNT[material.getId()];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.server.Statistic getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        MonsterEggInfo monsteregginfo = (MonsterEggInfo) EntityTypes.eggInfo.get(Integer.valueOf(entity.getTypeId()));

        if (monsteregginfo != null) {
            return monsteregginfo.killEntityStatistic;
        }
        return null;
    }

    public static EntityType getEntityTypeFromStatistic(net.minecraft.server.Statistic statistic) {
        String statisticString = statistic.name;
        return EntityType.fromName(statisticString.substring(statisticString.lastIndexOf(".") + 1));
    }

    public static Material getMaterialFromStatistic(net.minecraft.server.Statistic statistic) {
        String statisticString = statistic.name;
        String val = statisticString.substring(statisticString.lastIndexOf(".") + 1);
        Item item = (Item) Item.REGISTRY.get(new MinecraftKey(val));
        if (item != null) {
            return Material.getMaterial(Item.getId(item));
        }
        Block block = (Block) Block.REGISTRY.get(new MinecraftKey(val));
        if (block != null) {
            return Material.getMaterial(Block.getId(block));
        }
        try {
            return Material.getMaterial(Integer.parseInt(val));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
