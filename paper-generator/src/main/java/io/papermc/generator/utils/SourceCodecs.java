package io.papermc.generator.utils;

import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.squareup.javapoet.ClassName;
import io.papermc.typewriter.ClassNamed;
import java.util.Optional;
import java.util.function.Predicate;
import javax.lang.model.SourceVersion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public final class SourceCodecs {

    private SourceCodecs() {
    }

    public static final Codec<String> IDENTIFIER = Codec.STRING.validate(name -> {
        return SourceVersion.isIdentifier(name) && !SourceVersion.isKeyword(name) ? DataResult.success(name) : DataResult.error(() -> "Invalid identifier: '%s'".formatted(name));
    });

    public static final Codec<String> QUALIFIED_NAME = Codec.STRING.validate(name -> {
        return SourceVersion.isName(name) ? DataResult.success(name) : DataResult.error(() -> "Invalid qualified name: '%s'".formatted(name));
    });

    private static final Codec<String> BINARY_NAME = Codec.STRING.validate(name -> {
        return SourceVersion.isName(name.replace('$', '.')) ? DataResult.success(name) : DataResult.error(() -> "Invalid binary name: '%s'".formatted(name));
    });

    public static Codec<String> fieldNameCodec(Class<?> fieldHolder, Predicate<String> checker) {
        return IDENTIFIER.comapFlatMap(name -> {
            if (!checker.test(name)) {
                return DataResult.error(() -> "Unknown field '%s' in %s".formatted(name, fieldHolder.getSimpleName()));
            }

            return DataResult.success(name);
        }, name -> name);
    }

    public static Codec<String> fieldCodec(Class<?> fieldHolder, Predicate<String> checker) {
        String className = fieldHolder.getSimpleName();
        return QUALIFIED_NAME.comapFlatMap(name -> {
            if (!name.startsWith(className + ".")) {
                return DataResult.error(() -> "Invalid field '%s', field must belong to %s".formatted(name, className));
            }

            String fieldName = name.substring(className.length() + 1);
            if (!checker.test(fieldName)) {
                return DataResult.error(() -> "Unknown field '%s' in %s".formatted(fieldName, className));
            }

            return DataResult.success(name.substring(className.length() + 1));
        }, fieldName -> String.join(".", className, fieldName));
    }

    public static final Codec<Class<?>> CLASS = BINARY_NAME.comapFlatMap(name -> {
        try {
            return DataResult.success(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return DataResult.error(() -> "Class not found: %s".formatted(e.getMessage()));
        }
    }, Class::getName);

    public static <T> Codec<Class<? extends T>> classCodec(Class<T> baseClass) {
        return CLASS.comapFlatMap(klass -> {
            if (baseClass.isAssignableFrom(klass)) {
                return DataResult.success((Class<? extends T>) klass);
            }
            return DataResult.error(() -> "Class constraint failed: %s doesn't extends %s".formatted(klass, baseClass));
        }, klass -> klass);
    }

    public static <T> Codec<Class<? extends T>> classCodec(TypeToken<T> baseClass) {
        return CLASS.comapFlatMap(klass -> {
            if (baseClass.isSupertypeOf(klass)) {
                return DataResult.success((Class<? extends T>) klass);
            }
            return DataResult.error(() -> "Class constraint failed: %s doesn't extends %s".formatted(klass, baseClass));
        }, klass -> klass);
    }

    public static final Codec<ClassNamed> CLASS_NAMED = BINARY_NAME.xmap(name -> {
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return ClassNamed.of(name.substring(0, lastDotIndex), name.substring(lastDotIndex + 1));
        }

        return ClassNamed.of("", name);
    }, ClassNamed::binaryName);

    public static final Codec<ClassName> CLASS_NAME = CLASS_NAMED.xmap(
        io.papermc.generator.types.Types::typed, io.papermc.generator.rewriter.types.Types::typed
    );

    public static final Codec<ResourceKey<? extends Registry<?>>> REGISTRY_KEY = ResourceLocation.CODEC.xmap(ResourceKey::createRegistryKey, ResourceKey::location);

    public static <E> Codec<Either<TagKey<E>, Holder<E>>> elementOrTagCodec(ResourceKey<? extends Registry<E>> registryKey) {
        return Codec.either(RegistryAwareTagKeyCodec.hashedCodec(registryKey), RegistryFixedCodec.create(registryKey));
    }

    private record RegistryAwareTagKeyCodec<E>(Codec<TagKey<E>> delegate, ResourceKey<? extends Registry<E>> registryKey) implements Codec<TagKey<E>> {

        public static <E> RegistryAwareTagKeyCodec<E> hashedCodec(ResourceKey<? extends Registry<E>> registryKey) {
            return new RegistryAwareTagKeyCodec<>(TagKey.hashedCodec(registryKey), registryKey);
        }

        @Override
        public <T> DataResult<Pair<TagKey<E>, T>> decode(DynamicOps<T> ops, T input) {
            if (ops instanceof RegistryOps<T> registryOps) {
                Optional<HolderGetter<E>> getter = registryOps.getter(this.registryKey);
                if (getter.isPresent()) {
                    return this.delegate.decode(ops, input).flatMap(pair -> {
                        TagKey<E> result = pair.getFirst();
                        if (getter.get().get(result).isPresent()) {
                            return DataResult.success(pair);
                        }
                        return DataResult.error(() -> "Missing tag: '%s' in registry '%s'".formatted(result.location(), result.registry().location()));
                    });
                }
            }

            return DataResult.error(() -> "Can't access registry " + this.registryKey);
        }

        @Override
        public <T> DataResult<T> encode(TagKey<E> tagKey, DynamicOps<T> ops, T input) {
            return this.delegate.encode(tagKey, ops, input);
        }
    }
}
