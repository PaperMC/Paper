package com.destroystokyo.paper.entity.villager;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

/// A reputation score for a player on a villager.
@NullMarked
public final class Reputation {

    private final Map<ReputationType, Integer> reputation;

    public Reputation() {
        this(new EnumMap<>(ReputationType.class));
    }

    public Reputation(final Map<ReputationType, Integer> reputation) {
        Preconditions.checkArgument(reputation != null, "reputation cannot be null");
        this.reputation = reputation;
    }

    /// Gets the reputation value for a specific [ReputationType].
    ///
    /// @param type The [`type`][ReputationType] of reputation to get.
    /// @return The value of the [`type`][ReputationType].
    public int getReputation(final ReputationType type) {
        Preconditions.checkArgument(type != null, "type cannot be null");
        return this.reputation.getOrDefault(type, 0);
    }

    /// Sets the reputation value for a specific [ReputationType].
    ///
    /// @param type The [`type`][ReputationType] of reputation to set.
    /// @param value The value of the [`type`][ReputationType].
    public void setReputation(final ReputationType type, final int value) {
        Preconditions.checkArgument(type != null, "type cannot be null");
        this.reputation.put(type, value);
    }

    /// Gets if a reputation value is currently set for a specific [ReputationType].
    ///
    /// @param type The [`type`][ReputationType] to check
    /// @return If there is a value for this [`type`][ReputationType] set.
    public boolean hasReputationSet(final ReputationType type) {
        return this.reputation.containsKey(type);
    }
}
