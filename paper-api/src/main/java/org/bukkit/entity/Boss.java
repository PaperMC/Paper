package org.bukkit.entity;

import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the Boss Entity.
 *
 * @since 1.13.2
 */
public interface Boss extends Entity {

    /**
     * Returns the {@link BossBar} of the {@link Boss}
     *
     * @return the {@link BossBar} of the entity
     */
    @Nullable
    BossBar getBossBar();
}
