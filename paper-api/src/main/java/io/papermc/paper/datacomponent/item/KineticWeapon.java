package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface KineticWeapon {

    /**
     * Returns a new builder for creating a Kinetic Weapon.
     *
     * @return a builder instance
     */
    @Contract(value = "-> new", pure = true)
    static Builder kineticWeapon() {
        return ItemComponentTypesBridge.bridge().kineticWeapon();
    }

    /**
     * Creates a {@link Condition} object.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static Condition condition(final @NonNegative int maxDurationTicks, final float minSpeed, final float minRelativeSpeed) {
        return ItemComponentTypesBridge.bridge().kineticWeaponCondition(maxDurationTicks, minSpeed, minRelativeSpeed);
    }

    @Contract(pure = true)
    @NonNegative int contactCooldownTicks();

    @Contract(pure = true)
    @NonNegative int delayTicks();

    @Contract(pure = true)
    @Nullable Condition dismountConditions();

    @Contract(pure = true)
    @Nullable Condition knockbackConditions();

    @Contract(pure = true)
    @Nullable Condition damageConditions();

    float forwardMovement();

    float damageMultiplier();

    @Contract(pure = true)
    @Nullable Key sound();

    @Contract(pure = true)
    @Nullable Key hitSound();

    interface Condition {

        @Contract(pure = true)
        @NonNegative int maxDurationTicks();

        @Contract(pure = true)
        float minSpeed();

        @Contract(pure = true)
        float minRelativeSpeed();
    }

    /**
     * Builder for {@link KineticWeapon}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<KineticWeapon> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder contactCooldownTicks(@NonNegative int ticks);

        @Contract(value = "_ -> this", mutates = "this")
        Builder delayTicks(@NonNegative int ticks);

        @Contract(value = "_ -> this", mutates = "this")
        Builder dismountConditions(@Nullable Condition condition);

        @Contract(value = "_ -> this", mutates = "this")
        Builder knockbackConditions(@Nullable Condition condition);

        @Contract(value = "_ -> this", mutates = "this")
        Builder damageConditions(@Nullable Condition condition);

        @Contract(value = "_ -> this", mutates = "this")
        Builder forwardMovement(float forwardMovement);

        @Contract(value = "_ -> this", mutates = "this")
        Builder damageMultiplier(float damageMultiplier);

        @Contract(value = "_ -> this", mutates = "this")
        Builder sound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hitSound(@Nullable Key sound);
    }
}
