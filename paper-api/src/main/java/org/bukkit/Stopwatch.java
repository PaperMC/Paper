package org.bukkit;

/**
 * Represents a stopwatch that measures real-world elapsed time, independent of Minecraft server ticks.<br>
 * Stopwatches use Java Virtual Machine's high-resolution time source to measure time. It can be accessed with the
 * {@link System#nanoTime()} method.
 */
public interface Stopwatch extends Keyed {

    /**
     * Returns the value, in milliseconds, of the Java Virtual Machine's high-resolution time source as it was at the
     * moment this object was created.
     *
     * @return Timestamp at creation
     */
    long creationTime();

    /**
     * Retrieves the accumulated time in milliseconds.
     *
     * @return Accumulated time
     */
    long accumulatedElapsedTime();

    /**
     * Calculates the elapsed time in milliseconds between the start of the stopwatch
     * and the specified timestamp.
     *
     * @param time A value, in milliseconds, of the Java Virtual Machine's high-resolution time source
     * @return The elapsed time in milliseconds since the stopwatch was started and the given timestamp.
     */
    long elapsedMilliseconds(long time);

    /**
     * Calculates the elapsed time in seconds between the start of the stopwatch
     * and the specified timestamp.
     *
     * @param time A value, in milliseconds, of the Java Virtual Machine's high-resolution time source
     * @return The elapsed time in seconds since the stopwatch was started and the given timestamp.
     */
    double elapsedSeconds(long time);

    /**
     * Restart the stopwatch.
     */
    void restart();

}
