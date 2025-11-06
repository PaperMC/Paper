package org.bukkit.craftbukkit;

import io.papermc.paper.util.Holderable;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftGameRule<T> extends GameRule<T> implements Holderable<net.minecraft.world.level.gamerules.GameRule<T>> {

    public static GameRule<?> minecraftToBukkit(net.minecraft.world.level.gamerules.GameRule minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.GAME_RULE);
    }

    public static <T> net.minecraft.world.level.gamerules.GameRule<T> bukkitToMinecraft(GameRule<T> bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.level.gamerules.GameRule<?>> bukkitToMinecraftHolder(GameRule<?> bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    private final Holder<net.minecraft.world.level.gamerules.GameRule<T>> holder;

    @SuppressWarnings("unchecked")
    public CraftGameRule(Holder<net.minecraft.world.level.gamerules.GameRule<?>> holder) {
        this.holder = (Holder) holder;
    }

    public static <LEGACY, MODERN> GameRule<LEGACY> wrap(GameRule<MODERN> rule, @Nullable Function<LEGACY, MODERN> fromLegacyToModern, @Nullable Function<MODERN, LEGACY> toLegacyFromModern, Class<LEGACY> legacyClass) {
        return new LegacyGameRuleWrapper<>(bukkitToMinecraftHolder(rule), fromLegacyToModern, toLegacyFromModern, legacyClass);
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
    public @NotNull String getName() {
        return this.holder.getRegisteredName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Class<T> getType() {
        return (Class<T>) switch (this.holder.value().gameRuleType()) {
            case INT -> Integer.class;
            case BOOL -> Boolean.class;
        };
    }

    @Override
    public @NotNull String translationKey() {
        return this.holder.value().id();
    }

    public static class LegacyGameRuleWrapper<LEGACY, MODERN> extends CraftGameRule<LEGACY> {

        private final Class<LEGACY> typeOverride;
        private final @Nullable Function<LEGACY, MODERN> fromLegacyToModern;
        private final @Nullable Function<MODERN, LEGACY> toLegacyFromModern;

        public LegacyGameRuleWrapper(Holder<net.minecraft.world.level.gamerules.GameRule<?>> holder, @Nullable Function<LEGACY, MODERN> fromLegacyToModern, @Nullable Function<MODERN, LEGACY> toLegacyFromModern, Class<LEGACY> typeOverride) {
            super(holder);
            this.fromLegacyToModern = fromLegacyToModern;
            this.toLegacyFromModern = toLegacyFromModern;
            this.typeOverride = typeOverride;
        }


        public @Nullable Function<LEGACY, MODERN> getFromLegacyToModern() {
            return fromLegacyToModern;
        }

        public @Nullable Function<MODERN, LEGACY> getToLegacyFromModern() {
            return toLegacyFromModern;
        }

        @Override
        public @NotNull Class<LEGACY> getType() {
            return this.typeOverride;
        }
    }
}
