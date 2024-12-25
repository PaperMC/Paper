package org.bukkit.block;

/**
 * @deprecated Incomplete api. Use {@link io.papermc.paper.block.Lidded} instead.
 */
@Deprecated // Paper - Deprecate Bukkit's Lidded API
public interface Lidded {

    /**
     * Sets the block's animated state to open and prevents it from being closed
     * until {@link #close()} is called.
     * @deprecated Use {@link io.papermc.paper.block.Lidded#setLidMode(io.papermc.paper.block.LidMode)}
     */
    @Deprecated
    void open();

    /**
     * Unsets a corresponding call to {@link #open()}.
     * @deprecated Misleading name. Use {@link io.papermc.paper.block.Lidded#setLidMode(io.papermc.paper.block.LidMode)}
     */
    @Deprecated
    void close();

    // Paper start - More Lidded Block API
    /**
     * Checks is the Lid is currently forced open.
     *
     * @return true if the block's animation state is force open.
     * @deprecated Misleading name. Use {@link io.papermc.paper.block.Lidded#getLidMode()} for the direct replacement, or {@link io.papermc.paper.block.Lidded#getEffectiveLidState()} to tell if the lid is visibly open to the player instead.
     */
    @Deprecated
    boolean isOpen();
    // Paper end - More Lidded Block API
}
