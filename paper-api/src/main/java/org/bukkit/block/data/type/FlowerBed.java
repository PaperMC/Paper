package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

/// 'flower\_amount' represents the number of flowers.
public interface FlowerBed extends Directional {

    /// Gets the value of the 'flower\_amount' property.
    ///
    /// @return the 'flower\_amount' value
    int getFlowerAmount();

    /// Sets the value of the 'flower\_amount' property.
    ///
    /// @param flowerAmount the new 'flower\_amount' value
    void setFlowerAmount(int flowerAmount);

    // Paper start
    /// Gets the minimum allowed value of the 'flower\_amount' property.
    ///
    /// @return the minimum 'flower\_amount' value
    int getMinimumFlowerAmount();
    // Paper end

    /// Gets the maximum allowed value of the 'flower\_amount' property.
    ///
    /// @return the maximum 'flower\_amount' value
    int getMaximumFlowerAmount();
}
