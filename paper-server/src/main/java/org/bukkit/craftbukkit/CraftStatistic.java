package org.bukkit.craftbukkit;

import net.minecraft.server.EntityTypes;
import net.minecraft.server.EntityTypes.MonsterEggInfo;
import net.minecraft.server.StatisticList;

import org.bukkit.Statistic;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.google.common.base.CaseFormat;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.MinecraftKey;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftStatistic {
    private static final BiMap<String, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<String, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.<String, org.bukkit.Statistic>builder();
        for (Statistic statistic : Statistic.values()) {
            if (statistic == Statistic.PLAY_ONE_TICK) {
                statisticBuilder.put("stat.playOneMinute", statistic);
            } else {
                statisticBuilder.put("stat." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, statistic.name()), statistic);
            }
        }

        statistics = statisticBuilder.build();
    }

    private CraftStatistic() {}

    public static org.bukkit.Statistic getBukkitStatistic(net.minecraft.server.Statistic statistic) {
        return getBukkitStatisticByName(statistic.name);
    }

    public static org.bukkit.Statistic getBukkitStatisticByName(String name) {
        if (name.startsWith("stat.killEntity.")) {
            name = "stat.killEntity";
        }
        if (name.startsWith("stat.entityKilledBy.")) {
            name = "stat.entityKilledBy";
        }
        if (name.startsWith("stat.breakItem.")) {
            name = "stat.breakItem";
        }
        if (name.startsWith("stat.useItem.")) {
            name = "stat.useItem";
        }
        if (name.startsWith("stat.mineBlock.")) {
            name = "stat.mineBlock";
        }
        if (name.startsWith("stat.craftItem.")) {
            name = "stat.craftItem";
        }
        if (name.startsWith("stat.drop.")) {
            name = "stat.drop";
        }
        if (name.startsWith("stat.pickup.")) {
            name = "stat.pickup";
        }
        return statistics.get(name);
    }

    public static net.minecraft.server.Statistic getNMSStatistic(org.bukkit.Statistic statistic) {
        return StatisticList.getStatistic(statistics.inverse().get(statistic));
    }

    public static net.minecraft.server.Statistic getMaterialStatistic(org.bukkit.Statistic stat, Material material) {
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return StatisticList.a(CraftMagicNumbers.getBlock(material)); // PAIL: getMineBlockStatistic
            }
            if (stat == Statistic.CRAFT_ITEM) {
                return StatisticList.a(CraftMagicNumbers.getItem(material)); // PAIL: getCraftItemStatistic
            }
            if (stat == Statistic.USE_ITEM) {
                return StatisticList.b(CraftMagicNumbers.getItem(material)); // PAIL: getUseItemStatistic
            }
            if (stat == Statistic.BREAK_ITEM) {
                return StatisticList.c(CraftMagicNumbers.getItem(material)); // PAIL: getBreakItemStatistic
            }
            if (stat == Statistic.PICKUP) {
                return StatisticList.d(CraftMagicNumbers.getItem(material)); // PAIL: getPickupStatistic
            }
            if (stat == Statistic.DROP) {
                return StatisticList.e(CraftMagicNumbers.getItem(material)); // PAIL: getDropItemStatistic
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    public static net.minecraft.server.Statistic getEntityStatistic(org.bukkit.Statistic stat, EntityType entity) {
        MonsterEggInfo monsteregginfo = (MonsterEggInfo) EntityTypes.eggInfo.get(new MinecraftKey(entity.getName()));

        if (monsteregginfo != null) {
            if (stat == org.bukkit.Statistic.KILL_ENTITY) {
                return monsteregginfo.killEntityStatistic;
            }
            if (stat == org.bukkit.Statistic.ENTITY_KILLED_BY) {
                return monsteregginfo.killedByEntityStatistic;
            }
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
