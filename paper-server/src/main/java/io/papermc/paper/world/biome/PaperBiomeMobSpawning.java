package io.papermc.paper.world.biome;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.papermc.paper.util.MCUtil;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PaperBiomeMobSpawning implements BiomeMobSpawning {

    private static final Set<SpawnCategory> allSpawnCategories = Sets.immutableEnumSet(Arrays.asList(SpawnCategory.values()));

    private final MobSpawnSettings settings;

    public PaperBiomeMobSpawning(MobSpawnSettings settings) {
        this.settings = settings;
    }

    @Override
    public Map<SpawnCategory, List<MobData>> spawners() {
        return Maps.asMap(allSpawnCategories, category -> {
            WeightedList<MobSpawnSettings.SpawnerData> nms = this.settings.getMobs(CraftSpawnCategory.toNMS(category));
            return MCUtil.transformUnmodifiable(nms.unwrap(), PaperMobData::new);
        });
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
        public PaperMobData(Weighted<MobSpawnSettings.SpawnerData> data) {
            this(CraftEntityType.minecraftToBukkit(data.value().type()), data.weight(), data.value().minCount(), data.value().maxCount());
        }
    }

    record PaperSpawnCost(@Override double energyBudget, @Override double charge) implements BiomeMobSpawning.SpawnCost {
        public PaperSpawnCost(MobSpawnSettings.MobSpawnCost cost) {
            this(cost.energyBudget(), cost.charge());
        }
    }
}
