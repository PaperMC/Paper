package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

/// 'potent\_sulfur\_state' represents the environmental state of the block and whether it can form a geyser.
@NullMarked
public interface PotentSulfur extends BlockData {

    /// Gets the value of the 'potent\_sulfur\_state' property.
    ///
    /// @return the 'potent\_sulfur\_state' value
    State getPotentSulfurState();

    /// Sets the value of the 'potent\_sulfur\_state' property.
    ///
    /// @param state the new 'potent\_sulfur\_state' value
    void setPotentSulfurState(State state);

    /// The environmental state of the potent sulfur.
    enum State {
        DRY,
        WET,
        DORMANT,
        ERUPTING,
        CONTINUOUS;
    }
}
