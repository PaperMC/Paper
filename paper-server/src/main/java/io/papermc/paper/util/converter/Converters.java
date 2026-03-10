package io.papermc.paper.util.converter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.util.Handleable;

import static io.papermc.paper.util.MCUtil.transformUnmodifiable;

public final class Converters {

    public static <M extends Enum<M>, A extends Enum<A>> Converter<M, A> sameName(final Class<A> apiEnum, final Class<M> internalEnum) {
        return Converter.direct(
            value -> Enum.valueOf(apiEnum, value.name()),
            value -> Enum.valueOf(internalEnum, value.name())
        );
    }

    public static <M extends Enum<M>, A extends Enum<A>> Converter<M, A> sameOrder(final Class<A> apiEnum, final Class<M> internalEnum) {
        final A[] apiValues = apiEnum.getEnumConstants();
        final M[] internalValues = internalEnum.getEnumConstants();
        return Converter.direct(
            value -> apiValues[value.ordinal()],
            value -> internalValues[value.ordinal()]
        );
    }

    public static <M, A> Converter<Collection<? extends M>, Collection<? extends A>> collection(final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        return Converter.direct(
            collection -> transformUnmodifiable(collection, vanillaToApi),
            collection -> transformUnmodifiable(collection, apiToVanilla)
        );
    }

    public static <M, A> Converter<List<? extends M>, List<? extends A>> list(final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        return Converter.direct(
            list -> transformUnmodifiable(list, vanillaToApi),
            list -> transformUnmodifiable(list, apiToVanilla)
        );
    }

    public static <M, A> Converter<Set<? extends M>, Set<? extends A>> set(final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        return Converter.direct(
            set -> set.stream().map(vanillaToApi).collect(Collectors.toUnmodifiableSet()),
            set -> set.stream().map(apiToVanilla).collect(Collectors.toUnmodifiableSet())
        );
    }

    // make sure that no custom logic run in the specialized methods under implementation
    public static <M, A extends Keyed> Converter<Holder<M>, A> registryElement(final ResourceKey<? extends Registry<M>> registryKey) { // todo remove Keyed
        return Converter.direct(
            holder -> CraftRegistry.minecraftHolderToBukkit(holder, registryKey),
            CraftRegistry::bukkitToMinecraftHolder
        );
    }

    public static <M, A extends Handleable<M>> Converter<M, A> wrapper(final Function<M, A> vanillaToApi) {
        return Converter.direct(vanillaToApi, Handleable::getHandle);
    }

    public static <M extends Entity, A extends CraftEntity> Converter<M, A> entity(final Class<M> vanillaEntity, final Class<A> apiEntity) {
        return Converter.direct(
            entity -> apiEntity.cast(entity.getBukkitEntity()),
            entity -> vanillaEntity.cast(entity.getHandle())
        );
    }

    private static final Converter<?, ?> UNIMPLEMENTED = Converter.direct($ -> {
        throw new UnsupportedOperationException("Cannot convert an unimplemented type to an API value");
    }, $ -> {
        throw new UnsupportedOperationException("Cannot convert an API value to an unimplemented type");
    });

    private static final Converter<Unit, ?> UNVALUED = Converter.direct($ -> {
        throw new UnsupportedOperationException("Cannot convert the Unit type to an API value");
    }, $ -> Unit.INSTANCE);

    @SuppressWarnings("unchecked")
    public static <A, M> Converter<A, M> unimplemented() {
        return (Converter<A, M>) UNIMPLEMENTED;
    }

    @SuppressWarnings("unchecked")
    public static <A, M> Converter<A, M> unvalued() {
        return (Converter<A, M>) UNVALUED;
    }
}
