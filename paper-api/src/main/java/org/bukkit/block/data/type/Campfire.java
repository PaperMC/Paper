package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Waterlogged;

/// 'signal\_fire' denotes whether the fire is extra smokey due to having a hay
/// bale placed beneath it.
public interface Campfire extends Directional, Lightable, Waterlogged {

    /// Gets the value of the 'signal\_fire' property.
    ///
    /// @return the 'signal\_fire' value
    boolean isSignalFire();

    /// Sets the value of the 'signal\_fire' property.
    ///
    /// @param signalFire the new 'signal\_fire' value
    void setSignalFire(boolean signalFire);
}
