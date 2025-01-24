package io.papermc.paper.world.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

    public PaperBiomeMobSpawning(Biome biome) {
        this.settings = biome.getMobSettings();
    }

    @Override
    public Map<SpawnCategory, List<MobData>> spawners() {
        return this.settings.spawners.entrySet().stream().collect(ImmutableMap.toImmutableMap(
            entry -> CraftSpawnCategory.toBukkit(entry.getKey()),
            entry -> entry.getValue().unwrap().stream().map(PaperMobData::new).collect(ImmutableList.toImmutableList())
        ));
    }

    @Override
    public Map<EntityType, SpawnCost> entityCost() {
        return this.settings.mobSpawnCosts.entrySet().stream().collect(ImmutableMap.toImmutableMap(
            entry -> CraftEntityType.minecraftToBukkit(entry.getKey()),
            entry -> new PaperSpawnCost(entry.getValue())
        ));
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
