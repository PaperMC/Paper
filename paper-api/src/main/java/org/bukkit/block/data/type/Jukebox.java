package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/// 'has\_record' is a quick flag to check whether this jukebox has a record
/// inside it.
public interface Jukebox extends BlockData {

    /// Gets the value of the 'has\_record' property.
    ///
    /// @return the 'has\_record' value
    boolean hasRecord();

    /// Sets the value of the 'has\_record' property.
    ///
    /// @param hasRecord the new 'has\_record' value
    void setHasRecord(boolean hasRecord);
}
