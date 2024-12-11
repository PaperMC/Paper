package io.papermc.paper.registry.data.util;

import com.google.common.base.Preconditions;
import com.mojang.serialization.JavaOps;
import io.papermc.paper.adventure.WrapperAwareSerializer;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.Nullable;

public class Conversions {

    private static @Nullable Conversions globalInstance;
    public static Conversions global() {
        if (globalInstance == null) {
            final RegistryAccess globalAccess = CraftRegistry.getMinecraftRegistry();
            Preconditions.checkState(globalAccess != null, "Global registry access is not available");
            globalInstance = new Conversions(new RegistryOps.RegistryInfoLookup() {
                @Override
                public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(final ResourceKey<? extends Registry<? extends T>> registryRef) {
                    final Registry<T> registry = globalAccess.lookupOrThrow(registryRef);
                    return Optional.of(
                        new RegistryOps.RegistryInfo<>(registry, registry, registry.registryLifecycle())
                    );
                }
            });
        }
        return globalInstance;
    }


    private final RegistryOps.RegistryInfoLookup lookup;
    private final WrapperAwareSerializer serializer;

    public Conversions(final RegistryOps.RegistryInfoLookup lookup) {
        this.lookup = lookup;
        this.serializer = new WrapperAwareSerializer(() -> RegistryOps.create(JavaOps.INSTANCE, lookup));
    }

    public RegistryOps.RegistryInfoLookup lookup() {
        return this.lookup;
    }

    @Contract("null -> null; !null -> !null")
    public net.minecraft.network.chat.@Nullable Component asVanilla(final @Nullable Component adventure) {
        if (adventure == null) return null;
        return this.serializer.serialize(adventure);
    }

    public Component asAdventure(final net.minecraft.network.chat.@Nullable Component vanilla) {
        return vanilla == null ? Component.empty() : this.serializer.deserialize(vanilla);
    }
}
