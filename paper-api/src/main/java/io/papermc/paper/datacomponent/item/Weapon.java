package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;

public interface Weapon {

    /**
     * Returns a new builder for creating a Weapon.
     *
     * @return a builder instance.
     */
    static Builder builder() {
        // TODO
    }

    /**
     * The damage that the weapon deals per attack.
     */
    int itemDamagePerAttack();

    /**
     * The number of seconds that blocking is disabled.
     */
    float disableBlockingForSeconds();

    /**
     * Builder for Weapon.
     */
    interface Builder extends DataComponentBuilder<Weapon> {
        /**
         * Sets the damage per attack.
         *
         * @param damage the damage value.
         * @return the builder for chaining.
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