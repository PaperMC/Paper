package org.bukkit.entity;

import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a Creaking spawned from a creaking heart which will not persist.
 */
@ApiStatus.Experimental
@MinecraftExperimental(MinecraftExperimental.Requires.WINTER_DROP)
public interface CreakingTransient extends Creaking {

}
