package org.bukkit.entity;

import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.Nullable;

/// Represents the Boss Entity.
public interface Boss extends Entity {

    /// Returns the [BossBar] of the [Boss]
    ///
    /// @return the [BossBar] of the entity
    @Nullable
    BossBar getBossBar();
}
