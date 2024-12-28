package io.papermc.paper.world.damagesource;

import net.kyori.adventure.translation.Translatable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a type of location from which the entity fell
 *
 * @since 1.21.4
 */
@NullMarked
@ApiStatus.Experimental
public sealed interface FallLocation extends Translatable permits FallLocationImpl {

    /**
     * Gets the fall location id
     *
     * @return the fall location id
     */
    String id();

    /**
     * Gets the translation key used for a fall death message
     * caused by falling from this location
     *
     * @return the translation key
     */
    @Override
    String translationKey();

    /**
     * The entity is not within a special fall location
     */
    FallLocation GENERIC = new FallLocationImpl("generic");
    /**
     * The entity is within the ladder
     */
    FallLocation LADDER = new FallLocationImpl("ladder");
    /**
     * The entity is in vines
     */
    FallLocation VINES = new FallLocationImpl("vines");
    /**
     * The entity is in weeping wines
     */
    FallLocation WEEPING_VINES = new FallLocationImpl("weeping_vines");
    /**
     * The entity is in twisting vines
     */
    FallLocation TWISTING_VINES = new FallLocationImpl("twisting_vines");
    /**
     * The entity is in scaffolding
     */
    FallLocation SCAFFOLDING = new FallLocationImpl("scaffolding");
    /**
     * The entity is within some other climable block
     */
    FallLocation OTHER_CLIMBABLE = new FallLocationImpl("other_climbable");
    /**
     * The entity is in water
     */
    FallLocation WATER = new FallLocationImpl("water");

}
