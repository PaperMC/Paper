package io.papermc.paper.registry.data.variant;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.block.Biome;

record BiomeCheckImpl(RegistryKeySet<Biome> requiredBiomes) implements SpawnCondition.BiomeCheck {
}
