package org.bukkit.block.data;

import org.jspecify.annotations.NullMarked;

/// 'side\_chain' represents the current side of this block.
@NullMarked
public interface SideChaining extends BlockData {

    /// Gets the value of the 'side\_chain' property.
    ///
    /// @return the 'side\_chain' value
    ChainPart getSideChain();

    /// Sets the value of the 'side\_chain' property.
    ///
    /// @param part the new 'side\_chain' value
    void setSideChain(ChainPart part);

    enum ChainPart {
        UNCONNECTED,
        RIGHT,
        CENTER,
        LEFT
    }
}
