package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Weapon {

    /**
     * Returns a new builder for creating a Weapon.
     *
     * @return a builder instance.
     */
    static Builder weapon() {
        return ItemComponentTypesBridge.bridge().weapon();
    }

    /**
     * Amount of durability to remove each time the weapon is used to attack.
     *
     * @return durability
     */
    int itemDamagePerAttack();

    /**
     * The number of seconds that blocking is disabled.
     *
     * @return seconds
     */
    float disableBlockingForSeconds();

    /**
     * Builder for {@link Weapon}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<Weapon> {

        /**
         * Controls the amount of durability to remove each time the weapon is used to attack.
         *
         * @param damage durability to remove
         * @return the builder for chaining
         * @see #itemDamagePerAttack()
         */
        Builder itemDamagePerAttack(int damage);

        /**
         * Sets the disable blocking duration (in seconds).
         *
         * @param seconds the duration in seconds.
         * @return the builder for chaining.
         */
        Builder disableBlockingForSeconds(float seconds);
    }
}
