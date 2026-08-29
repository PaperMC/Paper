package io.papermc.paper.particle;

import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;

public interface ParticleType extends Keyed { // todo migrate on top of existing api?

    @SuppressWarnings("unused")
    @ApiStatus.NonExtendable
    interface Valued<T> extends ParticleType {

    }

    @ApiStatus.NonExtendable
    interface NonValued extends ParticleType {

    }
}
