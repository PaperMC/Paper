package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.jspecify.annotations.NullMarked;

/// 'thickness' represents the speleothem thickness.
///
/// 'vertical\_direction' represents the speleothem orientation.
///
/// Some blocks may not be able to face in all directions, use
/// [#getVerticalDirections()] to get all possible directions for this
/// block.
@NullMarked
public interface Speleothem extends Waterlogged {

    /// Gets the value of the 'vertical\_direction' property.
    ///
    /// @return the 'vertical\_direction' value
    BlockFace getVerticalDirection();

    /// Sets the value of the 'vertical\_direction' property.
    ///
    /// @param direction the new 'vertical\_direction' value
    void setVerticalDirection(BlockFace direction);

    /// Gets the faces which are applicable to this block.
    ///
    /// @return the allowed 'vertical\_direction' values
    Set<BlockFace> getVerticalDirections();

    /// Gets the value of the 'thickness' property.
    ///
    /// @return the 'thickness' value
    Thickness getThickness();

    /// Sets the value of the 'thickness' property.
    ///
    /// @param thickness the new 'thickness' value
    void setThickness(Thickness thickness);

    /// Represents the thickness of the speleothem, corresponding to its position
    /// within a multi-block speleothem formation.
    enum Thickness {
        /// Extended tip.
        TIP_MERGE,
        /// Just the tip.
        TIP,
        /// Top section.
        FRUSTUM,
        /// Middle section.
        MIDDLE,
        /// Base.
        BASE;
    }
}
