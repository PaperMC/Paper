package com.destroystokyo.paper.entity.villager;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A reputation score for a player on a villager.
 */
public final class Reputation {

    private final Map<ReputationType, Integer> reputation;

    /**
     * Creates an empty reputation.
     */
    public Reputation() {
        this(new EnumMap<>(ReputationType.class));
    }

    /**
     * Creates a reputation backed by the given map, so changes to the reputation are made to the map.
     *
     * @param reputation the reputation values by type
     */
    public Reputation(final Map<ReputationType, Integer> reputation) {
        Preconditions.checkArgument(reputation != null, "reputation cannot be null");
        this.reputation = reputation;
    }

    /**
     * Gets the reputation value for a specific {@link ReputationType}.
     *
     * @param type The {@link ReputationType type} of reputation to get.
     * @return The value of the {@link ReputationType type}.
     */
    public int getReputation(final ReputationType type) {
        Preconditions.checkArgument(type != null, "type cannot be null");
        return this.reputation.getOrDefault(type, 0);
    }

    /**
     * Sets the reputation value for a specific {@link ReputationType}.
     *
     * @param type  The {@link ReputationType type} of reputation to set.
     * @param value The value of the {@link ReputationType type}.
     */
    public void setReputation(final ReputationType type, final int value) {
        Preconditions.checkArgument(type != null, "type cannot be null");
        this.reputation.put(type, value);
    }

    /**
     * Gets if a reputation value is currently set for a specific {@link ReputationType}.
     *
     * @param type The {@link ReputationType type} to check
     * @return If there is a value for this {@link ReputationType type} set.
     */
    public boolean hasReputationSet(final ReputationType type) {
        return this.reputation.containsKey(type);
    }

    /**
     * Gets a set of all {@link ReputationType types} that have a reputation value set.
     *
     * @return A set of {@link ReputationType types}.
     */
    public @Unmodifiable Set<ReputationType> getReputationsSet() {
        return Set.copyOf(this.reputation.keySet());
    }
}
