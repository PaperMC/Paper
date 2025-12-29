package org.bukkit;

/**
 * Represents a stopwatch that measures real-world elapsed time, independent of Minecraft server ticks.
 */
public interface Stopwatch extends Keyed {

    /**
     * Retrieves the creation time of the stopwatch.
     *
     * @return The timestamp in milliseconds since the UNIX epoch when the stopwatch was created.
     */
    long creationTime();

    /**
     * Calculates the elapsed time in milliseconds between the start of the stopwatch
     * and the specified timestamp.
     *
     * @param time The timestamp in milliseconds since the UNIX epoch.
     * @return The elapsed time in milliseconds since the stopwatch was started and the given timestamp.
     */
    long elapsedMilliseconds(long time);

    /**
     * Calculates the elapsed time in seconds between the start of the stopwatch
     * and the specified timestamp.
     *
     * @param time The timestamp in milliseconds since the UNIX epoch.
     * @return The elapsed time in seconds since the stopwatch was started and the given timestamp.
     */
    double elapsedSeconds(long time);

    /**
     * Restart the stopwatch.
     */
    void restart();

}
