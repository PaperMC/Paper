package org.bukkit.entity;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a Wind Charge.
 */
@ApiStatus.Experimental
public interface AbstractWindCharge extends Fireball {

    /**
     * Immediately explode this WindCharge.
     */
    public void explode();

}
