package io.papermc.paper.registry.data.variant;

import java.util.Optional;

record SpawnConditionPriorityImpl(Optional<SpawnCondition> condition, int priority) implements SpawnConditionPriority {
}
