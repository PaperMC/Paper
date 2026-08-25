package org.bukkit.event.world;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Event that is called when an organic structure attempts to grow (Sapling {@literal ->}
 * Tree), (Mushroom {@literal ->} Huge Mushroom), naturally or using bonemeal.
 */
public interface StructureGrowEvent extends WorldEvent, Cancellable {

    /**
     * Gets the location of the structure.
     *
     * @return Location of the structure
     */
    Location getLocation();

    /**
     * Gets the species type (birch, normal, pine, red mushroom, brown
     * mushroom)
     *
     * @return Structure species
     */
    TreeType getSpecies();

    /**
     * Checks if structure was grown using bonemeal.
     *
     * @return {@code true} if the structure was grown using bonemeal.
     */
    boolean isFromBonemeal();

    /**
     * Gets the player that created the structure.
     *
     * @return Player that created the structure, {@code null} if was not created
     *     manually
     */
    @Nullable Player getPlayer();

    /**
     * Gets a list of all blocks associated with the structure.
     *
     * @return list of all blocks associated with the structure.
     */
    List<BlockState> getBlocks();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
