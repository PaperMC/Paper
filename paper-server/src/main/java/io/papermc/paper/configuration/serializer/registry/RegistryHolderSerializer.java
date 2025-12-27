package io.papermc.paper.configuration.serializer.registry;

import com.google.common.base.Preconditions;
import io.leangen.geantyref.TypeFactory;
import io.leangen.geantyref.TypeToken;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.configurate.serialize.SerializationException;

public final class RegistryHolderSerializer<T> extends RegistryEntrySerializer<Holder<T>, T> {

    @SuppressWarnings("unchecked")
    public RegistryHolderSerializer(
        final TypeToken<T> typeToken,
        final RegistryAccess registryAccess,
        final ResourceKey<? extends Registry<T>> registryKey,
        final boolean omitMinecraftNamespace
    ) {
        super((TypeToken<Holder<T>>) TypeToken.get(TypeFactory.parameterizedClass(Holder.class, typeToken.getType())), registryAccess, registryKey, omitMinecraftNamespace);
    }

    public RegistryHolderSerializer(
        final Class<T> type,
        final RegistryAccess registryAccess,
        final ResourceKey<? extends Registry<T>> registryKey,
        final boolean omitMinecraftNamespace
    ) {
        this(TypeToken.get(type), registryAccess, registryKey, omitMinecraftNamespace);
        Preconditions.checkArgument(type.getTypeParameters().length == 0, "%s must have 0 type parameters", type);
    }

    @Override
    protected Holder<T> convertFromResourceKey(final ResourceKey<T> key) throws SerializationException {
        return this.registry().get(key).orElseThrow(() -> new SerializationException("Missing holder in " + this.registry().key() + " with key " + key));
    }

    @Override
    protected ResourceKey<T> convertToResourceKey(final Holder<T> value) {
        return value.unwrap().map(Function.identity(), r -> this.registry().getResourceKey(r).orElseThrow());
    }
}
