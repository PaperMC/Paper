package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Powerable;

/// 'has\_book' is a quick flag to check whether this lectern has a book inside
/// it.
public interface Lectern extends Directional, Powerable {

    /// Gets the value of the 'has\_book' property.
    ///
    /// @return the 'has\_book' value
    boolean hasBook();

    /// Sets the value of the 'has\_book' property.
    ///
    /// @param hasBook the new 'has\_book' value
    void setHasBook(boolean hasBook);
}
