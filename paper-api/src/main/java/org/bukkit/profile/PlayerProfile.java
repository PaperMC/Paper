package org.bukkit.profile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Server;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// A player profile.
///
/// A player profile always provides a unique id, a non-empty name, or both. Its
/// unique id and name are immutable, but other properties (such as its textures)
/// can be altered.
///
/// New profiles can be created via
/// [Server#createPlayerProfile(UUID, String)].
/// @deprecated see [com.destroystokyo.paper.profile.PlayerProfile]
@Deprecated(since = "1.18.1") // Paper
public interface PlayerProfile extends Cloneable, ConfigurationSerializable {

    /// Gets the player's unique id.
    ///
    /// @return the player's unique id, or `null` if not available
    @Nullable
    @Deprecated(since = "1.18.1") // Paper
    UUID getUniqueId();

    /// Gets the player name.
    ///
    /// @return the player name, or `null` if not available
    @Nullable
    String getName();

    /// Gets the [PlayerTextures] of this profile.
    ///
    /// @return the textures
    @NotNull
    PlayerTextures getTextures();

    /// Copies the given textures.
    ///
    /// @param textures the textures to copy, or `null` to clear the
    /// textures
    void setTextures(@Nullable PlayerTextures textures);

    /// Checks whether this profile is complete.
    ///
    /// A profile is currently considered complete if it has a name, a unique id,
    /// and textures.
    ///
    /// @return `true` if this profile is complete
    boolean isComplete();

    /// Produces an updated player profile based on this profile.
    ///
    /// This tries to produce a completed profile by filling in missing
    /// properties (name, unique id, textures, etc.), and updates existing
    /// properties (e.g. name, textures, etc.) to their official and up-to-date
    /// values. This operation does not alter the current profile, but produces a
    /// new updated [PlayerProfile].
    ///
    /// If no player exists for the unique id or name of this profile, this
    /// operation yields a profile that is equal to the current profile, which
    /// might not be complete.
    ///
    /// This is an asynchronous operation: Updating the profile can result in an
    /// outgoing connection in another thread in order to fetch the latest
    /// profile properties. The returned [CompletableFuture] will be
    /// completed once the updated profile is available. In order to not block
    /// the server's main thread, you should not wait for the result of the
    /// returned CompletableFuture on the server's main thread. Instead, if you
    /// want to do something with the updated player profile on the server's main
    /// thread once it is available, you could do something like this:
    ///
    /// <pre>
    /// profile.update().thenAcceptAsync(updatedProfile -&gt; {
    ///     // Do something with the updated profile:
    ///     // ...
    /// }, runnable -&gt; Bukkit.getScheduler().runTask(plugin, runnable));
    /// </pre>
    ///
    /// @return a completable future that gets completed with the updated
    /// PlayerProfile once it is available
    @NotNull
    CompletableFuture<? extends PlayerProfile> update(); // Paper

    @NotNull
    PlayerProfile clone();
}
