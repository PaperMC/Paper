package io.papermc.paper.event.world;

import io.papermc.paper.math.Position;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEventNew;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.Nullable;

/**
 * Called <b>before</b> a set of configured structures is located.
 * This happens when:
 * <ul>
 *     <li>The /locate command is used.<br></li>
 *     <li>An Eye of Ender is used.</li>
 *     <li>An Explorer/Treasure Map is activated.</li>
 *     <li>A dolphin swims to a treasure location.</li>
 *     <li>A trade is done with a villager for a map.</li>
 *     <li>{@link World#locateNearestStructure(Location, StructureType, int, boolean)} is invoked.</li>
 *     <li>{@link World#locateNearestStructure(Location, Structure, int, boolean)} is invoked.</li>
 * </ul>
 */
public interface StructuresLocateEvent extends WorldEventNew, Cancellable {

    /**
     * Gets the {@link Location} from which the search is to be conducted.
     *
     * @return {@link Location} where search begins
     */
    Location getOrigin();

    /**
     * Gets the {@link Location} and {@link Structure} set as the result, if it was defined.
     * <p>
     * Returns {@code null} if it has not been set by {@link StructuresLocateEvent#setResult(Result)}.
     * Since this event fires <i>before</i> the search is done, the actual result is unknown at this point.
     *
     * @return The result location and structure, if it has been set. {@code null} if it has not.
     * @see World#locateNearestStructure(Location, StructureType, int, boolean)
     */
    @Nullable Result getResult();

    /**
     * Sets the result {@link Location} and {@link Structure}. This causes the search to be
     * skipped, and the result object passed here to be used as the result.
     *
     * @param result the {@link Location} and {@link Structure} of the search.
     */
    void setResult(@Nullable Result result);

    /**
     * Gets an unmodifiable list of Structures that are valid targets for the search.
     *
     * @return an unmodifiable list of Structures
     */
    @UnmodifiableView List<Structure> getStructures();

    /**
     * Sets the list of Structures that are valid targets for the search.
     *
     * @param structures a list of Structures targets
     */
    void setStructures(List<Structure> structures);

    /**
     * Gets the search radius in which to attempt locating the structure.
     * <p>
     * This radius may not always be obeyed during the structure search!
     *
     * @return the search radius (in chunks)
     */
    int getRadius();

    /**
     * Sets the search radius in which to attempt locating the structure.
     * <p>
     * This radius may not always be obeyed during the structure search!
     *
     * @param radius the search radius (in chunks)
     */
    void setRadius(int radius);

    /**
     * Gets whether to search exclusively for unexplored structures.
     * <p>
     * As with the search radius, this value is not always obeyed.
     *
     * @return Whether to search for only unexplored structures.
     */
    boolean shouldFindUnexplored();

    /**
     * Sets whether to search exclusively for unexplored structures.
     * <p>
     * As with the search radius, this value is not always obeyed.
     *
     * @param findUnexplored Whether to search for only unexplored structures.
     */
    void setFindUnexplored(boolean findUnexplored);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * Result for {@link StructuresLocateEvent}.
     */
    record Result(Position pos, Structure structure) { // todo this is not really nice
    }
}
