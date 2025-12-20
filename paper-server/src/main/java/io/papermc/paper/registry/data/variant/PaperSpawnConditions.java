package io.papermc.paper.registry.data.variant;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.util.MCUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.MoonBrightnessCheck;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.entity.variant.StructureCheck;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperSpawnConditions {

    private PaperSpawnConditions() {
    }

    public static SpawnCondition fromNms(final net.minecraft.world.entity.variant.SpawnCondition spawnCondition) {
        return switch (spawnCondition) {
            case BiomeCheck(final HolderSet<Biome> requiredBiomes) -> new BiomeCheckImpl(PaperRegistrySets.convertToApi(RegistryKey.BIOME, requiredBiomes));
            case MoonBrightnessCheck(final MinMaxBounds.Doubles bounds) -> new MoonBrightnessCheckImpl(MCUtil.toRange(bounds));
            case StructureCheck(final HolderSet<Structure> requiredStructures) ->
                new StructureCheckImpl(PaperRegistrySets.convertToApi(RegistryKey.STRUCTURE, requiredStructures));
            default -> throw new IllegalArgumentException("Unknown spawn condition type: " + spawnCondition);
        };
    }

    public static net.minecraft.world.entity.variant.SpawnCondition fromApi(final SpawnCondition spawnCondition, final Conversions conversions) {
        return switch (spawnCondition) {
            case BiomeCheckImpl(final RegistryKeySet<org.bukkit.block.Biome> requiredBiomes) ->
                new BiomeCheck(PaperRegistrySets.convertToNms(Registries.BIOME, conversions.lookup(), requiredBiomes));
            case MoonBrightnessCheckImpl(final Range<Double> range) -> new MoonBrightnessCheck(MCUtil.toBounds(range));
            case StructureCheckImpl(final RegistryKeySet<org.bukkit.generator.structure.Structure> requiredStructures) ->
                new StructureCheck(PaperRegistrySets.convertToNms(Registries.STRUCTURE, conversions.lookup(), requiredStructures));
        };
    }

    public static List<SpawnConditionPriority> fromNms(final SpawnPrioritySelectors selectors) {
        if (selectors.selectors().isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Lists.transform(
            selectors.selectors(), input -> {
                return input.condition()
                    .map(cond -> SpawnConditionPriority.create(PaperSpawnConditions.fromNms(cond), input.priority()))
                    .orElseGet(() -> SpawnConditionPriority.alwaysTrue(input.priority()));
            }
        ));
    }

    public static SpawnPrioritySelectors fromApi(final List<SpawnConditionPriority> conditions, final Conversions conversions) {
        if (conditions.isEmpty()) {
            return SpawnPrioritySelectors.EMPTY;
        }
        final List<PriorityProvider.Selector<SpawnContext, net.minecraft.world.entity.variant.SpawnCondition>> selectors = new ArrayList<>(conditions.size());
        for (final SpawnConditionPriority condition : conditions) {
            final Optional<net.minecraft.world.entity.variant.SpawnCondition> nmsCondition = condition.condition().map(c -> PaperSpawnConditions.fromApi(c, conversions));
            selectors.add(new PriorityProvider.Selector<>(nmsCondition, condition.priority()));
        }
        return new SpawnPrioritySelectors(List.copyOf(selectors));
    }

}
