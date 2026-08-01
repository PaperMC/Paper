package com.destroystokyo.paper.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface VersionFetcher {

    /**
     * Amount of time to cache results for in milliseconds
     * <p>
     * Negative values will never cache.
     *
     * @return cache time
     */
    long getCacheTime();

    /**
     * Gets the version message to cache and show to command senders.
     *
     * @return the message to show when requesting a version
     * @apiNote This method may involve a web request which will block the executing thread
     */
    Component getVersionMessage();

    /**
     * Gets the version message to cache and show to command senders.
     *
     * @param serverVersion the current version of the server (will match {@link Bukkit#getVersion()})
     * @return the message to show when requesting a version
     * @apiNote This method may involve a web request which will block the current thread
     * @see #getVersionMessage()
     * @deprecated {@code serverVersion} is not required
     */
    @Deprecated
    default Component getVersionMessage(String serverVersion) {
        return getVersionMessage();
    }

    /**
     * @hidden
     */
    @ApiStatus.Internal
    class DummyVersionFetcher implements VersionFetcher {

        @Override
        public long getCacheTime() {
            return -1;
        }

        @Override
        public Component getVersionMessage() {
            Bukkit.getLogger().warning("Version provider has not been set, cannot check for updates!");
            Bukkit.getLogger().info("Override the default implementation of org.bukkit.UnsafeValues#getVersionFetcher()");
            new Throwable().printStackTrace();
            return Component.text("Unable to check for updates. No version provider set.", NamedTextColor.RED);
        }
    }
}
