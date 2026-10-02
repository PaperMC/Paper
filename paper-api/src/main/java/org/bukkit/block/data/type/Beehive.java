package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

/// 'honey\_level' represents the amount of honey stored in the hive.
public interface Beehive extends Directional {

    /// Gets the value of the 'honey\_level' property.
    ///
    /// @return the 'honey\_level' value
    int getHoneyLevel();

    /// Sets the value of the 'honey\_level' property.
    ///
    /// @param honeyLevel the new 'honey\_level' value
    void setHoneyLevel(int honeyLevel);

    /// Gets the maximum allowed value of the 'honey\_level' property.
    ///
    /// @return the maximum 'honey\_level' value
    int getMaximumHoneyLevel();
}
