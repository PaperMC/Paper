package com.destroystokyo.paper.entity.villager;

/**
 * A type of reputation gained with a {@link org.bukkit.entity.Villager Villager}.
 * <p>
 * All types but {@link #MAJOR_POSITIVE} are shared to other villagers.
 */
public enum ReputationType {
    /**
     * A gossip with a majorly negative effect. This is only gained through killing a nearby
     * villager.
     */
    MAJOR_NEGATIVE,

    /**
     * A gossip with a minor negative effect. This is only gained through damaging a villager.
     */
    MINOR_NEGATIVE,

    /**
     * A gossip with a minor positive effect. This is only gained through curing a zombie
     * villager.
     */
    MINOR_POSITIVE,

    /**
     * A gossip with a major positive effect. This is only gained through curing a zombie
     * villager.
     */
    MAJOR_POSITIVE,

    /**
     * A gossip with a minor positive effect. This is only gained through trading with a villager.
     */
    TRADING,
}
