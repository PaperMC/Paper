package org.bukkit.block.data.type;

/**
 * 'thickness' represents the speleothem thickness.
 * <br>
 * 'vertical_direction' represents the speleothem orientation.
 * <br>
 * Some blocks may not be able to face in all directions, use
 * {@link #getVerticalDirections()} to get all possible directions for this
 * block.
 *
 * @deprecated incorrect name as multiple type of speleothem exists now. Use {@link Speleothem}
 */
@Deprecated(forRemoval = true, since = "26.2")
public interface PointedDripstone extends Speleothem {
}
