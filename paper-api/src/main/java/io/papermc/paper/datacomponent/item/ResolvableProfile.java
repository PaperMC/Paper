package io.papermc.paper.datacomponent.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds player profile data that can be resolved to a {@link PlayerProfile}.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#PROFILE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ResolvableProfile {

    @Contract(value = "_ -> new", pure = true)
    static ResolvableProfile resolvableProfile(final PlayerProfile profile) {
        return ItemComponentTypesBridge.bridge().resolvableProfile(profile);
    }

    @Contract(value = "-> new", pure = true)
    static ResolvableProfile.Builder resolvableProfile() {
        return ItemComponentTypesBridge.bridge().resolvableProfile();
    }

    @Contract(pure = true)
    @Nullable UUID uuid();

    @Contract(pure = true)
    @Nullable String name();

    @Contract(pure = true)
    @Unmodifiable Collection<ProfileProperty> properties();

    /**
     * Produces an updated player profile based on this.
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
     * profile.resolve().thenAcceptAsync(updatedProfile -> {
     *     // Do something with the updated profile:
     *     // ...
     * }, runnable -> Bukkit.getScheduler().runTask(plugin, runnable));
     * </pre>
     */
    @Contract(pure = true)
    CompletableFuture<PlayerProfile> resolve();

    /**
     * Builder for {@link ResolvableProfile}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<ResolvableProfile> {

        /**
         * Sets the name for this profile. Must be 16-or-less
         * characters and not contain invalid characters.
         *
         * @param name the name
         * @return the builder for chaining
         * @see #name()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder name(@Pattern("^[!-~]{0,16}$") @Nullable String name);

        /**
         * Sets the UUID for this profile.
         *
         * @param uuid the UUID
         * @return the builder for chaining
         * @see #uuid()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder uuid(@Nullable UUID uuid);

        /**
         * Adds a property to this profile.
         *
         * @param property the property
         * @return the builder for chaining
         * @see #properties()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addProperty(ProfileProperty property);

        /**
         * Adds properties to this profile.
         *
         * @param properties the properties
         * @return the builder for chaining
         * @see #properties()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addProperties(Collection<ProfileProperty> properties);
    }
}
