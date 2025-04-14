package io.papermc.paper.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Holderable<M> extends Handleable<M> {

    Holder<M> getHolder();

    @Override
    default M getHandle() {
        return this.getHolder().value();
    }

    static <T extends org.bukkit.Keyed, M> @Nullable T fromBukkitSerializationObject(final Object deserialized, final Codec<M> directCodec, final RegistryKey<T> registryKey) { // TODO remove Keyed
        final Registry<T> registry = RegistryAccess.registryAccess().getRegistry(registryKey);
        return switch (deserialized) {
            case @Subst("key:value") final String string -> {
                if (!(Key.parseable(string))) {
                    yield null;
                }
                yield registry.get(Key.key(string));
            }
            case JsonObjectWrapper(final JsonObject element) -> {
                if (!(registry instanceof final CraftRegistry<?, ?> craftRegistry) || !craftRegistry.supportsDirectHolders()) {
                    throw new IllegalArgumentException("Cannot deserialize direct holders for " + registry);
                }
                final RegistryOps<JsonElement> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JsonOps.INSTANCE);
                final M holder = directCodec.decode(ops, element).getOrThrow().getFirst();
                yield ((CraftRegistry<T, M>) registry).createBukkit(Holder.direct(holder));
            }
            default -> throw new IllegalArgumentException("Cannot deserialize " + deserialized);
        };
    }

    default Object toBukkitSerializationObject(final Codec<? super M> directCodec) {
        return switch (this.getHolder()) {
            case final Holder.Direct<M> direct -> {
                final RegistryOps<JsonElement> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(JsonOps.INSTANCE);
                yield new JsonObjectWrapper(directCodec.encodeStart(ops, direct.value()).getOrThrow().getAsJsonObject());
            }
            case final Holder.Reference<M> reference -> reference.key().location().toString();
            default -> throw new IllegalArgumentException("Cannot serialize " + this.getHolder());
        };
    }

    /**
     * All implementations should use this as their hashCode implementation
     */
    default int implHashCode() {
        return this.getHolder().hashCode();
    }

    /**
     * All implementations should use this as their equals implementation
     */
    default boolean implEquals(final @Nullable Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        final Holderable<?> that = (Holderable<?>) o;
        return this.getHolder().equals(that.getHolder());
    }

    default String implToString() {
        return "%s{holder=%s}".formatted(this.getClass().getSimpleName(), this.getHolder().toString());
    }

    default @Nullable NamespacedKey getKeyOrNull() {
        return this.getHolder().unwrapKey().map(MCUtil::fromResourceKey).orElse(null);
    }

    default NamespacedKey getKey() {
        return MCUtil.fromResourceKey(this.getHolder().unwrapKey().orElseThrow(() -> new IllegalStateException("Cannot get key for this registry item, because it is not registered.")));
    }
}
