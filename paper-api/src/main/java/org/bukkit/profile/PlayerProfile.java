package org.bukkit.profile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Server;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A player profile.
 * <p>
 * A player profile always provides a unique id, a non-empty name, or both. Its
 * unique id and name are immutable, but other properties (such as its textures)
 * can be altered.
 * <p>
 * New profiles can be created via
 * {@link Server#createPlayerProfile(UUID, String)}.
 */
public interface PlayerProfile extends Cloneable, ConfigurationSerializable {

    /**
     * Gets the player's unique id.
     *
     * @return the player's unique id, or <code>null</code> if not available
     */
    @Nullable
    UUID getUniqueId();

    /**
     * Gets the player name.
     *
     * @return the player name, or <code>null</code> if not available
     */
    @Nullable
    String getName();

    /**
     * Gets the {@link PlayerTextures} of this profile.
     *
     * @return the textures, not <code>null</code>
     */
    @NotNull
    PlayerTextures getTextures();

    /**
     * Copies the given textures.
     *
     * @param textures the textures to copy, or <code>null</code> to clear the
     * textures
     */
    void setTextures(@Nullable PlayerTextures textures);

    /**
     * Checks whether this profile is complete.
     * <p>
     * A profile is currently considered complete if it has a name, a unique id,
     * and textures.
     *
     * @return <code>true</code> if this profile is complete
     */
    boolean isComplete();

    /**
     * Produces an updated player profile based on this profile.
     * <p>
     * This tries to produce a completed profile by filling in missing
     * properties (name, unique id, textures, etc.), and updates existing
     * properties (e.g. name, textures, etc.) to their official and up-to-date
     * values. This operation does not alter the current profile, but produces a
     * new updated {@link PlayerProfile}.
     * <p>
     * If no player exists for the unique id or name of this profile, this
     * operation yields a profile that is equal to the current profile, which
     * might not be complete.
     * <p>
     * This is an asynchronous operation: Updating the profile can result in an
     * outgoing connection in another thread in order to fetch the latest
     * profile properties. The returned {@link CompletableFuture} will be
     * completed once the updated profile is available. In order to not block
     * the server's main thread, you should not wait for the result of the
     * returned CompletableFuture on the server's main thread. Instead, if you
     * want to do something with the updated player profile on the server's main
     * thread once it is available, you could do something like this:
     * <pre>
     * profile.update().thenAcceptAsync(updatedProfile -> {
     *     // Do something with the updated profile:
     *     // ...
     * }, runnable -> Bukkit.getScheduler().runTask(plugin, runnable));
     * </pre>
     *
     * @return a completable future that gets completed with the updated
     * PlayerProfile once it is available
     */
    @NotNull
    CompletableFuture<PlayerProfile> update();

    @NotNull
    PlayerProfile clone();
}
