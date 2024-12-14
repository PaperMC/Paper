package org.bukkit.block;

/**
 * @since 1.16.1
 */
public interface Lidded {

    /**
     * Sets the block's animated state to open and prevents it from being closed
     * until {@link #close()} is called.
     */
    void open();

    /**
     * Sets the block's animated state to closed even if a player is currently
     * viewing this block.
     */
    void close();

    // Paper start - More Lidded Block API
    /**
     * Checks if the block's animation state.
     *
     * @return true if the block's animation state is set to open.
     * @since 1.16.5
     */
    boolean isOpen();
    // Paper end - More Lidded Block API
}
