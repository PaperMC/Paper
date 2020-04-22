package com.destroystokyo.paper.entity.villager;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

/**
 * A reputation score for a player on a villager.
 */
@NullMarked
public final class Reputation {

    private final Map<ReputationType, Integer> reputation;

    public Reputation() {
        this(new EnumMap<>(ReputationType.class));
    }

    public Reputation(final Map<ReputationType, Integer> reputation) {
        Preconditions.checkNotNull(reputation, "reputation cannot be null");
        this.reputation = reputation;
    }

    /**
     * Gets the reputation value for a specific {@link ReputationType}.
     *
     * @param type The {@link ReputationType type} of reputation to get.
     * @return The value of the {@link ReputationType type}.
     */
    public int getReputation(final ReputationType type) {
        Preconditions.checkNotNull(type, "the reputation type cannot be null");
        return this.reputation.getOrDefault(type, 0);
    }

    /**
     * Sets the reputation value for a specific {@link ReputationType}.
     *
     * @param type The {@link ReputationType type} of reputation to set.
     * @param value The value of the {@link ReputationType type}.
     */
    public void setReputation(final ReputationType type, final int value) {
        Preconditions.checkNotNull(type, "the reputation type cannot be null");
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
}
