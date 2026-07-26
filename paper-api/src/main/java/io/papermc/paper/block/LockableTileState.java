package io.papermc.paper.block;

import org.bukkit.Nameable;
import org.bukkit.block.Lockable;
import org.bukkit.block.TileState;

/**
 * Interface for block entities that are lockable.
 */
public interface LockableTileState extends TileState, Lockable, Nameable {
}
