package io.papermc.paper.configuration.type.fallback;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Supplier;

public class AutosavePeriod extends FallbackValue.Int {

    AutosavePeriod(Map<ContextKey<?>, Object> contextMap, Object value) throws SerializationException {
        super(contextMap, fromObject(value));
    }

    private AutosavePeriod(Map<ContextKey<?>, Object> contextMap) {
        super(contextMap, OptionalInt.empty());
    }

    @Override
    protected OptionalInt process(int value) {
        return Util.negToDef(value);
    }

    @Override
    protected Set<ContextKey<?>> required() {
        return Set.of(FallbackValue.MINECRAFT_SERVER);
    }

    @Override
    protected int fallback() {
        return this.get(FallbackValue.MINECRAFT_SERVER).get().autosavePeriod;
    }

    public static AutosavePeriod def() {
        return new AutosavePeriod(FallbackValue.MINECRAFT_SERVER.singleton(MinecraftServer::getServer));
    }
}
