package io.papermc.paper.registry.data.variant;

import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.generator.structure.Structure;

record StructureCheckImpl(RegistryKeySet<Structure> requiredStructures) implements SpawnCondition.StructureCheck {
}
