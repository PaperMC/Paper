package io.papermc.paper.block;

import org.bukkit.Nameable;
import org.bukkit.block.Lockable;
import org.bukkit.block.TileState;

/**
 * Interface for tile entities that are lockable.
 *
 * @since 1.19.3
 */
public interface LockableTileState extends TileState, Lockable, Nameable {
}
