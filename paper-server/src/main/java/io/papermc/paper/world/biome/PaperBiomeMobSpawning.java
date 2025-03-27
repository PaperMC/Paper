package io.papermc.paper.world.biome;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.util.MCUtil;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;
import java.util.List;
import java.util.Map;

public class PaperBiomeMobSpawning implements BiomeMobSpawning {

    private final MobSpawnSettings settings;
    private final ImmutableMap<SpawnCategory, List<MobData>> spawners;

    public PaperBiomeMobSpawning(Biome biome) {
        this.settings = biome.getMobSettings();
        ImmutableMap.Builder<SpawnCategory, List<MobData>> spawners = ImmutableMap.builder();
        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            List<MobSpawnSettings.SpawnerData> nms = this.settings.getMobs(CraftSpawnCategory.toNMS(spawnCategory)).unwrap();
            spawners.put(spawnCategory, MCUtil.transformUnmodifiable(nms, PaperMobData::new));
        }
        this.spawners = spawners.build();
    }

    @Override
    public Map<SpawnCategory, List<MobData>> spawners() {
        return this.spawners;
    }

    @Override
    public Map<EntityType, SpawnCost> entityCost() {
        ImmutableMap.Builder<EntityType, SpawnCost> builder = ImmutableMap.builder();
        this.settings.mobSpawnCosts.forEach((entityType, mobSpawnCost) -> {
            EntityType type = CraftEntityType.minecraftToBukkit(entityType);
            builder.put(type, new PaperSpawnCost(mobSpawnCost));
        });
        return builder.build();
    }

    record PaperMobData(@Override EntityType type, @Override int weight, @Override int minCount, @Override int maxCount) implements BiomeMobSpawning.MobData {
        public PaperMobData(MobSpawnSettings.SpawnerData data) {
            this(CraftEntityType.minecraftToBukkit(data.type), data.getWeight().asInt(), data.minCount, data.maxCount);
        }
    }

    record PaperSpawnCost(@Override double energyBudget, @Override double charge) implements BiomeMobSpawning.SpawnCost {
        public PaperSpawnCost(MobSpawnSettings.MobSpawnCost cost) {
            this(cost.energyBudget(), cost.charge());
        }
    }
}
