package org.bukkit.entity;

import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a Wind Charge.
 */
@MinecraftExperimental(Requires.UPDATE_1_21)
@ApiStatus.Experimental
public interface AbstractWindCharge extends Fireball {

    /**
     * Immediately explode this WindCharge.
     */
    public void explode();

}
