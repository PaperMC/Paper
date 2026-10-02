package org.bukkit.block.data.type;

import org.bukkit.block.data.Waterlogged;

/// 'can\_summon' indicates whether the sculk shrieker can summon the warden.
///
/// 'shrieking' indicated whether the sculk shrieker is shrieking or not.
public interface SculkShrieker extends Waterlogged {

    /// Gets the value of the 'can\_summon' property.
    ///
    /// @return the 'can\_summon' value
    boolean isCanSummon();

    /// Sets the value of the 'can\_summon' property.
    ///
    /// @param can_summon the new 'can\_summon' value
    void setCanSummon(boolean can_summon);

    /// Gets the value of the 'shrieking' property.
    ///
    /// @return the 'shrieking' value
    boolean isShrieking();

    /// Sets the value of the 'shrieking' property.
    ///
    /// @param shrieking the new 'shrieking' value
    void setShrieking(boolean shrieking);
}
