package org.bukkit.block.data;

/**
 * 'segment_amount' represents the number of segment in this block.
 */
public interface Segmentable extends BlockData {

    /**
     * Gets the value of the 'segment_amount' property.
     *
     * @return the 'segment_amount' value
     */
    int getSegmentAmount();

    /**
     * Sets the value of the 'segment_amount' property.
     *
     * @param segmentAmount the new 'segment_amount' value
     */
    void setSegmentAmount(int segmentAmount);

    /**
     * Gets the minimum allowed value of the 'segment_amount' property.
     *
     * @return the minimum 'segment_amount' value
     */
    int getMinimumSegmentAmount();

    /**
     * Gets the maximum allowed value of the 'segment_amount' property.
     *
     * @return the maximum 'segment_amount' value
     */
    int getMaximumSegmentAmount();
}
