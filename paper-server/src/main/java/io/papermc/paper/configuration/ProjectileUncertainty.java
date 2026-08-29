package io.papermc.paper.configuration;

import io.papermc.paper.configuration.type.number.DoubleOr;
import net.minecraft.world.entity.EntityType;

public final class ProjectileUncertainty {

    private ProjectileUncertainty() {
    }

    public static float resolve(final EntityType<?> type, final float vanillaUncertainty) {
        final DoubleOr.Default configured = GlobalConfiguration.get().projectiles.uncertainty.get(type);
        if (configured == null) {
            return vanillaUncertainty;
        }
        return (float) configured.or(vanillaUncertainty);
    }
}
