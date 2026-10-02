package org.bukkit.entity;

/// Piglin / Piglin Brute.
public interface PiglinAbstract extends Monster, Ageable {

    /// Gets whether the piglin is immune to zombification.
    ///
    /// @return Whether the piglin is immune to zombification
    public boolean isImmuneToZombification();

    /// Sets whether the piglin is immune to zombification.
    ///
    /// @param flag Whether the piglin is immune to zombification
    public void setImmuneToZombification(boolean flag);

    /// Gets the amount of ticks until this entity will be converted to a
    /// Zombified Piglin.
    /// When this reaches 300, the entity will be converted.
    ///
    /// @return conversion time
    /// @throws IllegalStateException if [#isConverting()] is false.
    public int getConversionTime();

    /// Sets the conversion counter value. The counter is incremented
    /// every tick the method [#isConverting()] returns true. Setting
    /// this value will not start the conversion if the [PiglinAbstract] is
    /// not in a valid environment ([org.bukkit.World#isPiglinSafe])
    /// to convert, is immune to zombification ([#isImmuneToZombification()])
    /// or has no AI ([#hasAI]).
    /// When this reaches 300, the entity will be converted. To stop the
    /// conversion use [#setImmuneToZombification(boolean)].
    ///
    /// @param time new conversion counter
    public void setConversionTime(int time);

    /// Get if this entity is in the process of converting to a Zombified Piglin.
    ///
    /// @return conversion status
    boolean isConverting();

    /// Gets whether the piglin is a baby
    ///
    /// @return Whether the piglin is a baby
    /// @deprecated see [Ageable#isAdult()]
    @Deprecated(since = "1.16.2")
    public boolean isBaby();

    /// Sets whether the piglin is a baby
    ///
    /// @param baby Whether the piglin is a baby
    /// @deprecated see [Ageable#setBaby()] and [Ageable#setAdult()]
    @Deprecated(since = "1.16.2")
    public void setBaby(boolean baby);
}
