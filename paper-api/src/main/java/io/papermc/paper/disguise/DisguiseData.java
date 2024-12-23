package io.papermc.paper.disguise;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the data used to disguise an entity as another.
 * Also supports disguising an entity as a player commonly known as `FakePlayer`.
 */
@NullMarked
public sealed interface DisguiseData permits DisguiseData.OriginalDisguise, EntityTypeDisguise, PlayerDisguise {

    /**
     * Creates an original disguise data that can be used to reset disguising.
     * <p>
     * The original instance is set by default when a new entity is spawned
     * and represents the state of no disguise should be made.
     * <p>
     * Same as {@link #reset()}
     *
     * @return an original disguise data
     */
    static DisguiseData original() {
        return reset();
    }

    /**
     * Creates a {@link PlayerDisguise.Builder} where you can configure certain properties of the fake player appearance.
     *
     *
     * @param playerProfile a already completed player profile that will be the fake players skin
     * @return a builder to configure certain attributes
     */
    static PlayerDisguise.Builder player(PlayerProfile playerProfile) {
        return new PlayerDisguise.Builder(playerProfile);
    }

    /**
     * Creates a {@link EntityTypeDisguise.Builder} to allow disguising your entity as the given {@link EntityType}.
     *
     *
     * @param entityType the entity type as which the entity should appear as.
     * @return an entity disguise
     */
    static EntityTypeDisguise.Builder entity(EntityType entityType) {
        return new EntityTypeDisguise.Builder(entityType);
    }

    /**
     * An alias for {@link #original()} to cover certain views on it.
     *
     * @see #original()
     *
     * @return an original disguise data
     */
    static OriginalDisguise reset() {
        return new OriginalDisguise();
    }

    record OriginalDisguise() implements DisguiseData{
        @ApiStatus.Internal
        public OriginalDisguise() {}
    }
}
