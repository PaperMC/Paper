package io.papermc.paper.world.attribute;

import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.math.Position;
import org.bukkit.WeatherType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EnvironmentalAttributeContext {

    @Nullable Long time();

    @Nullable Position position();

    @Nullable Float rainLevel();

    @Nullable Float thunderLevel();

    static Builder builder() {
        return InternalAPIBridge.get().environmentalAttributeContextBuilder();
    }

    @ApiStatus.NonExtendable
    interface Builder {

        Builder time(@Nullable Long time);

        Builder position(@Nullable Position position);

        Builder rainLevel(@Nullable Float rainLevel);

        Builder raining(boolean raining);

        Builder thunderLevel(@Nullable Float thunderLevel);

        Builder thundering(boolean thundering);

        EnvironmentalAttributeContext build();

    }
}
