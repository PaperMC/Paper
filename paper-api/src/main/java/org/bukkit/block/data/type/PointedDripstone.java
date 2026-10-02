package org.bukkit.block.data.type;

/// 'thickness' represents the speleothem thickness.
///
/// 'vertical\_direction' represents the speleothem orientation.
///
/// Some blocks may not be able to face in all directions, use
/// [#getVerticalDirections()] to get all possible directions for this
/// block.
///
/// @deprecated incorrect name as multiple type of speleothem exists now. Use [Speleothem]
@Deprecated(forRemoval = true, since = "26.2")
public interface PointedDripstone extends Speleothem {
}
