/**
 * The root package of the Bukkit API, contains generalized API classes.
 *
 * Note: While the Bukkit API makes every effort to ensure stability, this is
 * not guaranteed, especially across major versions. In particular the following
 * is a (incomplete) list of things that are <b>not</b> API.
 * <ul>
 * <li>Implementing interfaces. The Bukkit API is designed to only be
 * implemented by server software. Unless a class/interface is obviously
 * designed for extension (eg {@link org.bukkit.scheduler.BukkitRunnable}), or
 * explicitly marked as such, it should not be implemented by plugins. Although
 * this can sometimes work, it is not guaranteed to do so and resulting bugs
 * will be disregarded.</li>
 * <li>Constructing inbuilt events. Although backwards compatibility is
 * attempted where possible, it is sometimes not possible to add new fields to
 * events without breaking existing constructors. To ensure that the API
 * continues to evolve, event constructors are therefore not plugin API.</li>
 * <li>Implementation classes. Concrete implementation classes packaged with
 * Bukkit (eg those beginning with Simple) are not API. You should access them
 * via their interfaces instead.</li>
 * </ul>
 */
package org.bukkit;
