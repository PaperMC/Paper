package org.bukkit.entity;

import org.bukkit.boss.BossBar;

/**
 * Represents the Boss Entity.
 */
public interface Boss extends Entity {

    /**
     * Returns the {@link BossBar} of the {@link Boss}
     *
     * @return the {@link BossBar} of the entity
     */
    BossBar getBossBar();
}
