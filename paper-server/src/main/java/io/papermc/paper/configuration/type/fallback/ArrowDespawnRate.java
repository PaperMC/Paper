package io.papermc.paper.configuration.type.fallback;

import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

public class ArrowDespawnRate extends FallbackValue.Int {

    ArrowDespawnRate(Map<ContextKey<?>, Object> context, Object value) throws SerializationException {
        super(context, fromObject(value));
    }

    private ArrowDespawnRate(Map<ContextKey<?>, Object> context) {
        super(context, OptionalInt.empty());
    }

    @Override
    protected OptionalInt process(int value) {
        return Util.negToDef(value);
    }

    @Override
    public Set<ContextKey<?>> required() {
        return Set.of(FallbackValue.SPIGOT_WORLD_CONFIG);
    }

    @Override
    protected int fallback() {
        return this.get(FallbackValue.SPIGOT_WORLD_CONFIG).arrowDespawnRate;
    }

    public static ArrowDespawnRate def(SpigotWorldConfig spigotConfig) {
        return new ArrowDespawnRate(FallbackValue.SPIGOT_WORLD_CONFIG.singleton(spigotConfig));
    }
}
