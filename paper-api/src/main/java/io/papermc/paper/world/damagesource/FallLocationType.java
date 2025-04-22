package io.papermc.paper.world.damagesource;

import net.kyori.adventure.translation.Translatable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a type of location from which the entity fell.
 */
@NullMarked
@ApiStatus.Experimental
public sealed interface FallLocationType extends Translatable permits FallLocationTypeImpl {

    /**
     * Gets the fall location id.
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
     * The entity was not within a special fall location.
     */
    FallLocationType GENERIC = new FallLocationTypeImpl("generic");
    /**
     * The entity was within the ladder.
     */
    FallLocationType LADDER = new FallLocationTypeImpl("ladder");
    /**
     * The entity was in vines.
     */
    FallLocationType VINES = new FallLocationTypeImpl("vines");
    /**
     * The entity was in weeping wines.
     */
    FallLocationType WEEPING_VINES = new FallLocationTypeImpl("weeping_vines");
    /**
     * The entity was in twisting vines.
     */
    FallLocationType TWISTING_VINES = new FallLocationTypeImpl("twisting_vines");
    /**
     * The entity was in scaffolding.
     */
    FallLocationType SCAFFOLDING = new FallLocationTypeImpl("scaffolding");
    /**
     * The entity was within some other climbable block.
     */
    FallLocationType OTHER_CLIMBABLE = new FallLocationTypeImpl("other_climbable");
    /**
     * The entity was in water.
     */
    FallLocationType WATER = new FallLocationTypeImpl("water");

}
