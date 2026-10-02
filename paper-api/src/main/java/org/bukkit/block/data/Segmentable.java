package org.bukkit.block.data;

/// 'segment\_amount' represents the number of segment in this block.
public interface Segmentable extends BlockData {

    /// Gets the value of the 'segment\_amount' property.
    ///
    /// @return the 'segment\_amount' value
    int getSegmentAmount();

    /// Sets the value of the 'segment\_amount' property.
    ///
    /// @param segmentAmount the new 'segment\_amount' value
    void setSegmentAmount(int segmentAmount);

    /// Gets the minimum allowed value of the 'segment\_amount' property.
    ///
    /// @return the minimum 'segment\_amount' value
    int getMinimumSegmentAmount();

    /// Gets the maximum allowed value of the 'segment\_amount' property.
    ///
    /// @return the maximum 'segment\_amount' value
    int getMaximumSegmentAmount();
}
