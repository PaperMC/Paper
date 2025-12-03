package org.bukkit.craftbukkit;

import com.mojang.serialization.DataResult;
import io.papermc.paper.util.Holderable;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftGameRule<T> extends GameRule<T> implements Holderable<net.minecraft.world.level.gamerules.GameRule<T>> {

    public static final BiFunction<String, DataResult.Error<?>, IllegalArgumentException> INVALID_VALUE = (value, error) -> {
        return new IllegalArgumentException("Invalid value: %s (%s)".formatted(value, error.message()));
    };

    public static <T> GameRule<T> minecraftToBukkit(net.minecraft.world.level.gamerules.GameRule<T> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.GAME_RULE);
    }

    public static <T> net.minecraft.world.level.gamerules.GameRule<T> bukkitToMinecraft(GameRule<T> bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final Holder<net.minecraft.world.level.gamerules.GameRule<T>> holder;

    @SuppressWarnings("unchecked")
    public CraftGameRule(Holder<net.minecraft.world.level.gamerules.GameRule<?>> holder) {
        this.holder = (Holder) holder;
    }

    @SuppressWarnings("unchecked")
    public static <LEGACY, MODERN> GameRule<LEGACY> wrap(GameRule<MODERN> rule, Function<LEGACY, MODERN> fromLegacyToModern, Function<MODERN, LEGACY> toLegacyFromModern, Class<LEGACY> legacyClass) {
        return new LegacyGameRuleWrapper<>(((CraftGameRule) rule).getHolder(), fromLegacyToModern, toLegacyFromModern, legacyClass);
    }

    @Override
    public Holder<net.minecraft.world.level.gamerules.GameRule<T>> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return Holderable.super.implEquals(obj);
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }

    @Override
    public String getName() {
        return this.holder.getRegisteredName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getType() {
        return (Class<T>) switch (this.getHandle().gameRuleType()) {
            case INT -> Integer.class;
            case BOOL -> Boolean.class;
        };
    }

    @Override
    public String translationKey() {
        return this.getHandle().getDescriptionId();
    }

    public static class LegacyGameRuleWrapper<LEGACY, MODERN> extends CraftGameRule<LEGACY> {

        private final Class<LEGACY> typeOverride;
        private final Function<LEGACY, MODERN> fromLegacyToModern;
        private final Function<MODERN, LEGACY> toLegacyFromModern;

        public LegacyGameRuleWrapper(Holder<net.minecraft.world.level.gamerules.GameRule<?>> holder, Function<LEGACY, MODERN> fromLegacyToModern, Function<MODERN, LEGACY> toLegacyFromModern, Class<LEGACY> typeOverride) {
            super(holder);
            this.fromLegacyToModern = fromLegacyToModern;
            this.toLegacyFromModern = toLegacyFromModern;
            this.typeOverride = typeOverride;
        }

        public Function<LEGACY, MODERN> getFromLegacyToModern() {
            return this.fromLegacyToModern;
        }

        public Function<MODERN, LEGACY> getToLegacyFromModern() {
            return this.toLegacyFromModern;
        }

        @Override
        public Class<LEGACY> getType() {
            return this.typeOverride;
        }
    }
}
