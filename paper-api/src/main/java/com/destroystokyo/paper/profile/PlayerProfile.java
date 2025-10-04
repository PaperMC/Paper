package com.destroystokyo.paper.profile;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.kyori.adventure.text.object.PlayerHeadObjectContents.property;

/**
 * Represents a players profile for the game, such as UUID, Name, and textures.
 */
@NullMarked
public interface PlayerProfile extends org.bukkit.profile.PlayerProfile, PlayerHeadObjectContents.SkinSource {

    /**
     * @return The players name, if set
     */
    @Override
    @Nullable
    String getName();

    /**
     * Sets this profiles Name
     *
     * @param name The new Name
     * @return The previous Name
     */
    @Deprecated(forRemoval = true, since = "1.18.1")
    String setName(@Nullable String name);

    /**
     * @return The players unique identifier, if set
     */
    @Nullable
    UUID getId();

    /**
     * Sets this profiles UUID
     *
     * @param uuid The new UUID
     * @return The previous UUID
     */
    @Deprecated(forRemoval = true, since = "1.18.1")
    @Nullable
    UUID setId(@Nullable UUID uuid);

    /**
     * Gets the {@link PlayerTextures} of this profile.
     * This will build a snapshot of the current texture data once
     * requested inside PlayerTextures.
     *
     * @return the textures
     */
    @Override
    PlayerTextures getTextures();

    /**
     * Copies the given textures.
     *
     * @param textures the textures to copy, or {@code null} to clear the
     * textures
     */
    @Override
    void setTextures(@Nullable PlayerTextures textures);

    /**
     * @return A Mutable set of this player's properties, such as textures.
     * Values specified here are subject to implementation details.
     */
    Set<ProfileProperty> getProperties();

    /**
     * Check if the Profile has the specified property
     *
     * @param property Property name to check
     * @return If the property is set
     */
    boolean hasProperty(@Nullable String property);

    /**
     * Sets a property. If the property already exists, the previous one will be replaced
     *
     * @param property Property to set.
     * @throws IllegalArgumentException if setting the property results in more than 16 properties
     */
    void setProperty(ProfileProperty property);

    /**
     * Sets multiple properties. If any of the set properties already exist, it will be replaced
     *
     * @param properties The properties to set
     * @throws IllegalArgumentException if the number of properties exceeds 16
     */
    void setProperties(Collection<ProfileProperty> properties);

    /**
     * Removes a specific property from this profile
     *
     * @param property The property to remove
     * @return If a property was removed
     */
    boolean removeProperty(@Nullable String property);

    /**
     * Removes a specific property from this profile
     *
     * @param property The property to remove
     * @return If a property was removed
     */
    default boolean removeProperty(final ProfileProperty property) {
        return this.removeProperty(property.getName());
    }

    /**
     * Removes all properties in the collection
     *
     * @param properties The properties to remove
     * @return If any property was removed
     */
    default boolean removeProperties(final Collection<ProfileProperty> properties) {
        boolean removed = false;
        for (final ProfileProperty property : properties) {
            if (this.removeProperty(property)) {
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Clears all properties on this profile
     */
    void clearProperties();

    /**
     * @return If the profile is now complete (has UUID and Name)
     */
    @Override
    boolean isComplete();

    /**
     * Like {@link #complete(boolean)} but will try only from cache, and not make network calls
     * Does not account for textures.
     *
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean completeFromCache();

    /**
     * Like {@link #complete(boolean)} but will try only from cache, and not make network calls
     * Does not account for textures.
     *
     * @param onlineMode Treat this as online mode or not
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean completeFromCache(boolean onlineMode);

    /**
     * Like {@link #complete(boolean)} but will try only from cache, and not make network calls
     * Does not account for textures.
     *
     * @param lookupUUID If only name is supplied, should we do a UUID lookup
     * @param onlineMode Treat this as online mode or not
     * @return If the profile is now complete (has UUID and Name)
     */
    boolean completeFromCache(boolean lookupUUID, boolean onlineMode);

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     * <p>
     * This will also complete textures. If you do not want to load textures, use {{@link #complete(boolean)}}
     *
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    default boolean complete() {
        return this.complete(true);
    }

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     * <p>
     * Optionally will also fill textures.
     * <p>
     * Online mode will be automatically determined
     *
     * @param textures controls if we should fill the profile with texture properties
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    boolean complete(boolean textures);

    /**
     * If this profile is not complete, then make the API call to complete it.
     * This is a blocking operation and should be done asynchronously.
     * <p>
     * Optionally will also fill textures.
     *
     * @param textures controls if we should fill the profile with texture properties
     * @param onlineMode Treat this server as online mode or not
     * @return If the profile is now complete (has UUID and Name) (if you get rate limited, this operation may fail)
     */
    boolean complete(boolean textures, boolean onlineMode);

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
     */
    @Override
    CompletableFuture<PlayerProfile> update();

    /**
     * Whether this Profile has textures associated to it
     *
     * @return If it has a textures property
     */
    default boolean hasTextures() {
        return this.hasProperty("textures");
    }

    /**
     * {@inheritDoc}
     *
     * @return the cloned player profile.
     */
    @Override
    PlayerProfile clone();

    @Override
    default void applySkinToPlayerHeadContents(final PlayerHeadObjectContents.Builder builder) {
        if (this.getProperties().isEmpty() && (this.getName() != null) != (this.getId() != null)) {
            if (this.getId() != null) {
                builder.id(this.getId());
            } else {
                builder.name(this.getName());
            }
            return;
        }
        builder.id(this.getId())
            .name(this.getName())
            .profileProperties(this.getProperties().stream().map(prop -> property(prop.getName(), prop.getValue(), prop.getSignature())).toList());
    }
}
