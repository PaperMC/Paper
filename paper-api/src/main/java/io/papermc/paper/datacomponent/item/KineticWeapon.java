package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface KineticWeapon {

    /**
     * Returns a new builder for creating a Kinetic Weapon.
     *
     * @return a builder instance.
     */
    @Contract(value = "-> new", pure = true)
    static Builder kineticWeapon() {
        return ItemComponentTypesBridge.bridge().kineticWeapon();
    }

    /**
     * Creates a {@link Condition} object.
     */
    @Contract(value = "_,_,_ -> new", pure = true)
    static Condition condition(final int maxDurationTicks, final float minSpeed, final float minRelativeSpeed) {
        return ItemComponentTypesBridge.bridge().kineticWeaponCondition(maxDurationTicks, minSpeed, minRelativeSpeed);
    }

    @Contract(pure = true)
    float minReach();

    @Contract(pure = true)
    float maxReach();

    @Contract(pure = true)
    float hitboxMargin();

    @Contract(pure = true)
    int contactCooldownTicks();

    @Contract(pure = true)
    int delayTicks();

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
        int maxDurationTicks();
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
        Builder minReach(float minReach);

        @Contract(value = "_ -> this", mutates = "this")
        Builder maxReach(float maxReach);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hitboxMargin(float hitboxMargin);

        @Contract(value = "_ -> this", mutates = "this")
        Builder contactCooldownTicks(int ticks);

        @Contract(value = "_ -> this", mutates = "this")
        Builder delayTicks(int ticks);

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
        Builder sound(Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder hitSound(Key sound);

    }
}