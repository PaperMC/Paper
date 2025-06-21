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
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemType;

@Deprecated(forRemoval = true)
public final class CraftStatistic {
    private static final BiMap<ResourceLocation, org.bukkit.Statistic> statistics;

    static {
        ImmutableBiMap.Builder<ResourceLocation, org.bukkit.Statistic> statisticBuilder = ImmutableBiMap.builder();
        for (Statistic statistic : Statistic.values()) {
            statisticBuilder.put(CraftNamespacedKey.toMinecraft(statistic.getKey()), statistic);
        }

        statistics = statisticBuilder.build();
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

    private static net.minecraft.stats.Stat getBlockTypeStatistic(org.bukkit.Statistic stat, BlockType blockType) {
        Preconditions.checkArgument(blockType != null, "BlockType cannot be null");
        try {
            if (stat == Statistic.MINE_BLOCK) {
                return Stats.BLOCK_MINED.get(CraftBlockType.bukkitToMinecraftNew(blockType));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }

    private static net.minecraft.stats.Stat getItemTypeStatistic(org.bukkit.Statistic stat, ItemType itemType) {
        Preconditions.checkArgument(itemType != null, "ItemType cannot be null");
        try {
            if (stat == Statistic.CRAFT_ITEM) {
                return Stats.ITEM_CRAFTED.get(CraftItemType.bukkitToMinecraftNew(itemType));
            }
            if (stat == Statistic.USE_ITEM) {
                return Stats.ITEM_USED.get(CraftItemType.bukkitToMinecraftNew(itemType));
            }
            if (stat == Statistic.BREAK_ITEM) {
                return Stats.ITEM_BROKEN.get(CraftItemType.bukkitToMinecraftNew(itemType));
            }
            if (stat == Statistic.PICKUP) {
                return Stats.ITEM_PICKED_UP.get(CraftItemType.bukkitToMinecraftNew(itemType));
            }
            if (stat == Statistic.DROP) {
                return Stats.ITEM_DROPPED.get(CraftItemType.bukkitToMinecraftNew(itemType));
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
        net.minecraft.stats.Stat nmsStatistic;
        if (statistic.getType() == Type.BLOCK) {
            Preconditions.checkArgument(material.isBlock(), "Material %s must be a block", material);
            BlockType blockType = material.asBlockType();
            nmsStatistic = CraftStatistic.getBlockTypeStatistic(statistic, blockType);
        } else if (statistic.getType() == Type.ITEM) {
            Preconditions.checkArgument(material.isItem(), "Material %s must be an item", material);
            ItemType itemType = material.asItemType();
            nmsStatistic = CraftStatistic.getItemTypeStatistic(statistic, itemType);
        } else {
            throw new IllegalArgumentException("This statistic does not take a Material parameter");
        }
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
        net.minecraft.stats.Stat nmsStatistic;
        if (statistic.getType() == Type.BLOCK) {
            Preconditions.checkArgument(material.isBlock(), "Material %s must be a block", material);
            BlockType blockType = material.asBlockType();
            nmsStatistic = CraftStatistic.getBlockTypeStatistic(statistic, blockType);
        } else if (statistic.getType() == Type.ITEM) {
            Preconditions.checkArgument(material.isItem(), "Material %s must be an item", material);
            ItemType itemType = material.asItemType();
            nmsStatistic = CraftStatistic.getItemTypeStatistic(statistic, itemType);
        } else {
            throw new IllegalArgumentException("This statistic does not take a Material parameter");
        }
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
