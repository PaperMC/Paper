package org.bukkit.entity;

import org.jetbrains.annotations.Contract;

/// Represents a Zombified piglin.
public interface PigZombie extends Zombie {

    /// Get the zombified piglin's current anger level.
    ///
    /// @return The anger level.
    int getAnger();

    /// Set the zombified piglin's current anger level.
    ///
    /// @param level The anger level. Higher levels of anger take longer to
    ///     wear off.
    void setAnger(int level);

    /// Shorthand; sets to either 0 or the default level.
    ///
    /// @param angry Whether the piglin should be angry.
    void setAngry(boolean angry);

    /// Shorthand; gets whether the piglin is angry.
    ///
    /// @return True if the piglin is angry, otherwise false.
    boolean isAngry();

    /// **Not applicable to this entity**
    ///
    /// @return `false`
    @Override
    @Contract("-> false")
    public boolean isConverting();

    /// **Not applicable to this entity**
    @Override
    @Contract("-> fail")
    public int getConversionTime();

    /// **Not applicable to this entity**
    ///
    /// @param time unused
    @Override
    @Contract("_ -> fail")
    public void setConversionTime(int time);
}
