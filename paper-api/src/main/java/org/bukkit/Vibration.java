package org.bukkit;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a vibration from a Skulk sensor.
 */
public class Vibration {

    private final Location origin;
    private final Destination destination;
    private final int arrivalTime;

    // Paper start
    public Vibration(@NotNull Destination destination, @NotNull int arrivalTime) {
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.origin = new Location(null, 0, 0, 0); // Dummy origin because getter expects null
    }

    @Deprecated(forRemoval = true) // Paper end
    public Vibration(@NotNull Location origin, @NotNull Destination destination, int arrivalTime) {
        this.origin = origin;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
    }

    /**
     * Get the origin of the vibration.
     *
     * @deprecated unused as of 1.19
     * @return origin
     */
    @NotNull
    @Deprecated(forRemoval = true) // Paper
    public Location getOrigin() {
        return origin;
    }

    /**
     * Get the vibration destination.
     *
     * @return destination
     */
    @NotNull
    public Destination getDestination() {
        return destination;
    }

    /**
     * Get the vibration arrival time in ticks.
     *
     * @return arrival time
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    public interface Destination {

        public static class EntityDestination implements Destination {

            private final Entity entity;

            public EntityDestination(@NotNull Entity entity) {
                this.entity = entity;
            }

            @NotNull
            public Entity getEntity() {
                return entity;
            }
        }

        public static class BlockDestination implements Destination {

            private final Location block;

            public BlockDestination(@NotNull Location block) {
                this.block = block.clone();
            }

            public BlockDestination(@NotNull Block block) {
                this(block.getLocation());
            }

            @NotNull
            public Location getLocation() {
                return block.clone();
            }

            @NotNull
            public Block getBlock() {
                return block.getBlock();
            }
        }
    }
}
