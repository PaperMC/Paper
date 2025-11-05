package io.papermc.paper.registry.data.variant;

import java.util.Optional;

/**
 * Represents a priority for a spawn condition, which can be used to determine the order in which conditions are evaluated.
 * This interface allows for the creation of conditions with associated priorities, where a higher priority indicates
 * that the condition should be evaluated earlier than those with lower priorities.
 */
public sealed interface SpawnConditionPriority permits SpawnConditionPriorityImpl {

    /**
     * Creates a condition priority that is always true.
     *
     * @param priority  the priority of the condition
     * @return an always true condition with the specified priority
     */
    static SpawnConditionPriority alwaysTrue(final int priority) {
        return new SpawnConditionPriorityImpl(Optional.empty(), priority);
    }

    /**
     * Creates a condition priority with the specified condition and priority.
     *
     * @param condition the condition to associate with this priority
     * @param priority  the priority of the condition
     * @return a new SpawnConditionPriority instance with the specified condition and priority
     */
    static SpawnConditionPriority create(final SpawnCondition condition, final int priority) {
        return new SpawnConditionPriorityImpl(Optional.of(condition), priority);
    }

    /**
     * Gets the condition associated with this priority.
     *
     * @return an optional containing the condition if it exists, otherwise an empty, always-true condition
     */
    Optional<SpawnCondition> condition();

    /**
     * Gets the priority of this condition.
     *
     * @return the priority of this condition
     */
    int priority();
}
