package io.papermc.paper.datacomponent.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.bukkit.profile.PlayerTextures;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds player profile data that can be resolved to a {@link PlayerProfile}.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#PROFILE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ResolvableProfile extends PlayerHeadObjectContents.SkinSource {

    @Contract(value = "_ -> new", pure = true)
    static ResolvableProfile resolvableProfile(final PlayerProfile profile) {
        return ItemComponentTypesBridge.bridge().resolvableProfile(profile);
    }

    @Contract(value = "-> new", pure = true)
    static ResolvableProfile.Builder resolvableProfile() {
        return ItemComponentTypesBridge.bridge().resolvableProfile();
    }

    @Contract(pure = true)
    @Nullable
    UUID uuid();

    @Contract(pure = true)
    @Nullable
    String name();

    @Contract(pure = true)
    @Unmodifiable
    Collection<ProfileProperty> properties();

    /**
     * Whether this resolvable profile was marked as dynamic.
     * <p>
     * A dynamic resolvable profile is required to either have a non-null {@link #uuid()} or {@link #name()}
     * and will be resolved by the client via a network call.
     * A dynamic profile will also not include any properties.
     *
     * @return {@code true} if this profile is marked as dynamic, {@code false} otherwise.
     */
    @Contract(pure = true)
    boolean dynamic();

    /**
     * Produces an updated player profile based on this {@link ResolvableProfile}, mirroring client behavior.
     *
     * <ul>
     *     <li>
     *         For {@link #dynamic()} profiles:
     *
     *         <p>This tries to produce a completed profile by filling in missing
     *         properties (name, unique id, textures, etc.), and updates existing
     *         properties (e.g. name) to their official and up-to-date
     *         values. This operation does not alter the current profile, but produces a
     *         new updated {@link PlayerProfile}.</p>
     *
     *         <p>If no player exists for the unique id or name of this profile, this
     *         operation yields a profile that is equal to the current profile, which
     *         might not be complete.</p>
     *     </li>
     *     <li>
     *         For static (non-{@link #dynamic()}) profiles:
     *         <p>Completes with a profile containing all of {@link #properties()} exactly,
     *         and null {@link #name()}/{@link #uuid()} replaced by filler values.</p>
     *     </li>
     * </ul>
     *
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
     * Gets the skin patch used by the client for rendering. Overrides any values
     * resolved from the profile.
     *
     * @return the skin patch
     */
    @Contract(pure = true)
    SkinPatch skinPatch();

    /**
     * Override rendering options for a {@link ResolvableProfile}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface SkinPatch {

        /**
         * Returns the empty skin patch that does not override anything.
         *
         * @return the empty skin patch
         */
        static SkinPatch empty() {
            final class Holder {
                static final SkinPatch INSTANCE = ItemComponentTypesBridge.bridge().emptySkinPatch();
            }
            return Holder.INSTANCE;
        }

        /**
         * Creates a new builder for a skin patch.
         *
         * @return new skin patch builder
         */
        @Contract(value = "-> new", pure = true)
        static SkinPatchBuilder skinPatch() {
            return ItemComponentTypesBridge.bridge().skinPatch();
        }

        /**
         * Gets the body texture key.
         *
         * @return the body texture key, or {@code null} if not set
         */
        @Nullable Key body();

        /**
         * Gets the cape texture key.
         *
         * @return the cape texture key, or {@code null} if not set
         */
        @Nullable Key cape();

        /**
         * Gets the elytra texture key.
         *
         * @return the elytra texture key, or {@code null} if not set
         */
        @Nullable Key elytra();

        /**
         * Gets the skin model.
         *
         * @return the skin model, or {@code null} if not set
         */
        PlayerTextures.@Nullable SkinModel model();

        /**
         * Returns if this skin patch does not override any values.
         *
         * @return {@code true} if this skin patch is empty
         */
        default boolean isEmpty() {
            return this.body() == null && this.cape() == null && this.elytra() == null && this.model() == null;
        }
    }

    /**
     * Builder for {@link SkinPatch}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface SkinPatchBuilder extends DataComponentBuilder<SkinPatch> {
        /**
         * Sets the body texture key.
         *
         * @param body the body texture key, or {@code null} to unset it
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        SkinPatchBuilder body(@Nullable Key body);

        /**
         * Sets the cape texture key.
         *
         * @param cape the cape texture key, or {@code null} to unset it
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        SkinPatchBuilder cape(@Nullable Key cape);

        /**
         * Sets the elytra texture key.
         *
         * @param elytra the elytra texture key, or {@code null} to unset it
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        SkinPatchBuilder elytra(@Nullable Key elytra);

        /**
         * Sets the skin model.
         *
         * @param model the skin model, or {@code null} to unset it
         * @return the builder for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        SkinPatchBuilder model(PlayerTextures.@Nullable SkinModel model);
    }

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

        /**
         * Sets the skin patch for this profile.
         *
         * @param patch the skin patch
         * @return the builder for chaining
         * @see #skinPatch()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder skinPatch(SkinPatch patch);

        /**
         * Configures the skin patch for this profile.
         *
         * @param configure the configuration consumer
         * @return the builder for chaining
         * @see #skinPatch()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder skinPatch(Consumer<SkinPatchBuilder> configure);
    }
}
