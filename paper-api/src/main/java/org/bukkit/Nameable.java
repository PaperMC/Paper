package org.bukkit;

import org.jetbrains.annotations.Nullable;

/// Represents a block, entity, or other object that may receive a custom name.
public interface Nameable {

    /// Gets the custom name.
    ///
    /// This value has no effect on players, they will always use their real name.
    ///
    /// @return the custom name
    net.kyori.adventure.text.@Nullable Component customName();

    /// Sets the custom name.
    ///
    /// This name will be used in death messages and can be sent to the client as a nameplate over the mob.
    ///
    /// Setting the name to `null` will clear it.
    ///
    /// This value has no effect on players, they will always use their real name.
    ///
    /// @param customName the custom name to set
    void customName(final net.kyori.adventure.text.@Nullable Component customName);

    /// Gets the custom name on a mob or block. If there is no name this method
    /// will return null.
    ///
    /// This value has no effect on players, they will always use their real
    /// name.
    ///
    /// @deprecated in favour of [#customName()]
    /// @return name of the mob/block or null
    @Deprecated // Paper
    @Nullable
    public String getCustomName();

    /// Sets a custom name on a mob or block. This name will be used in death
    /// messages and can be sent to the client as a nameplate over the mob.
    ///
    /// Setting the name to null or an empty string will clear it.
    ///
    /// This value has no effect on players, they will always use their real
    /// name.
    ///
    /// @deprecated in favour of [#customName(net.kyori.adventure.text.Component)]
    /// @param name the name to set
    @Deprecated // Paper
    public void setCustomName(@Nullable String name);
}
