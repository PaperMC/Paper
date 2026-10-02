package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;

/// 'in\_wall' indicates if the fence gate is attached to a wall, and if true the
/// texture is lowered by a small amount to blend in better.
public interface Gate extends Directional, Openable, Powerable {

    /// Gets the value of the 'in\_wall' property.
    ///
    /// @return the 'in\_wall' value
    boolean isInWall();

    /// Sets the value of the 'in\_wall' property.
    ///
    /// @param inWall the new 'in\_wall' value
    void setInWall(boolean inWall);
}
