package io.papermc.paper.configuration.type.fallback;

import com.google.common.base.Preconditions;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.math.NumberUtils;
import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public sealed abstract class FallbackValue permits FallbackValue.Int {

    private static final String DEFAULT_VALUE = "default";
    static final ContextKey<SpigotWorldConfig> SPIGOT_WORLD_CONFIG = new ContextKey<>("SpigotWorldConfig");
    static final ContextKey<Supplier<MinecraftServer>> MINECRAFT_SERVER = new ContextKey<>("MinecraftServer");

    private final Map<ContextKey<?>, Object> contextMap;

    protected FallbackValue(Map<ContextKey<?>, Object> contextMap) {
        for (ContextKey<?> contextKey : this.required()) {
            Preconditions.checkArgument(contextMap.containsKey(contextKey), contextMap + " is missing " + contextKey);
        }
        this.contextMap = contextMap;
    }

    protected abstract String serialize();

    protected abstract Set<ContextKey<?>> required();

    @SuppressWarnings("unchecked")
    protected <T> T get(ContextKey<T> contextKey) {
        return (T) Objects.requireNonNull(this.contextMap.get(contextKey), "Missing " + contextKey);
    }

    public non-sealed abstract static class Int extends FallbackValue {

        private final OptionalInt value;

        Int(Map<ContextKey<?>, Object> contextMap, OptionalInt value) {
            super(contextMap);
            if (value.isEmpty()) {
                this.value = value;
            } else {
                this.value = this.process(value.getAsInt());
            }
        }

        public int value() {
            return value.orElseGet(this::fallback);
        }

        @Override
        protected final String serialize() {
            return value.isPresent() ? String.valueOf(this.value.getAsInt()) : DEFAULT_VALUE;
        }

        protected OptionalInt process(int value) {
            return OptionalInt.of(value);
        }

        protected abstract int fallback();

        protected static OptionalInt fromObject(Object obj) throws SerializationException {
            if (obj instanceof OptionalInt optionalInt) {
                return optionalInt;
            } else if (obj instanceof String string) {
                if (DEFAULT_VALUE.equalsIgnoreCase(string)) {
                    return OptionalInt.empty();
                }
                if (NumberUtils.isParsable(string)) {
                    return OptionalInt.of(Integer.parseInt(string));
                }
            } else if (obj instanceof Integer num) {
                return OptionalInt.of(num);
            }
            throw new SerializationException(obj + " is not a integer or '" + DEFAULT_VALUE + "'");
        }
    }

    static class ContextKey<T> {

        private final String name;

        ContextKey(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        Map<ContextKey<?>, Object> singleton(T value) {
            return Map.of(this, value);
        }
    }
}
