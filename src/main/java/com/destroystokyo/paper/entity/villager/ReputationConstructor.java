package com.destroystokyo.paper.entity.villager;
// Must have own package due to package-level constructor.

public final class ReputationConstructor {
    // Abuse the package-level constructor.
    public static Reputation construct(int[] values) {
        return new Reputation(values);
    }
}
