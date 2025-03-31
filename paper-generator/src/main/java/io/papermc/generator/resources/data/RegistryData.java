package io.papermc.generator.resources.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.squareup.javapoet.TypeName;
import io.papermc.generator.resources.ParameterizedClass;
import io.papermc.generator.types.Types;
import io.papermc.generator.utils.SourceCodecs;
import io.papermc.typewriter.ClassNamed;
import java.lang.constant.ConstantDescs;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

public record RegistryData(
    Api api,
    Impl impl,
    Optional<Builder> builder,
    Optional<String> serializationUpdaterField,
    boolean allowInline
) {

    public static final Codec<RegistryData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Api.CODEC.fieldOf("api").forGetter(RegistryData::api),
        Impl.CODEC.fieldOf("impl").forGetter(RegistryData::impl),
        Builder.CODEC.optionalFieldOf("builder").forGetter(RegistryData::builder),
        SourceCodecs.IDENTIFIER.optionalFieldOf("serialization_updater_field").forGetter(RegistryData::serializationUpdaterField),
        Codec.BOOL.optionalFieldOf("allow_inline", false).forGetter(RegistryData::allowInline)
    ).apply(instance, RegistryData::new));

    public record Api(Class klass, Optional<HolderClass> holderClass, boolean keyClassNameRelate, Optional<String> registryField) {
        public Api(ClassNamed klass) {
            this(new Class(klass), Optional.of(new HolderClass(klass)), false, Optional.empty());
        }

        public static final Codec<Api> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Class.CODEC.fieldOf("class").forGetter(Api::klass),
            HolderClass.CODEC.optionalFieldOf("holder_class").forGetter(Api::holderClass),
            Codec.BOOL.optionalFieldOf("key_class_name_relate", false).forGetter(Api::keyClassNameRelate),
            SourceCodecs.IDENTIFIER.optionalFieldOf("registry_field").forGetter(Api::registryField)
        ).apply(instance, Api::new));

        public static final Codec<Api> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(Api::new, api -> api.klass().name());

        public static final Codec<Api> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, api -> {
            if ((api.holderClass().isEmpty() ||
                (api.holderClass().get().name().isEmpty() || api.klass().name().equals(api.holderClass().get().name().get()) && api.holderClass().get().isInterface())) &&
                !api.klass().legacyEnum() && api.klass().wildcards().isEmpty() &&
                !api.keyClassNameRelate() && api.registryField().isEmpty()) {
                return Either.left(api);
            }
            return Either.right(api);
        });

        public record Class(ClassNamed name, List<ParameterizedClass> wildcards, boolean legacyEnum) {
            public Class(ClassNamed name) {
                this(name, List.of(), false);
            }

            public static final Codec<Class> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SourceCodecs.CLASS_NAMED.fieldOf("name").forGetter(Class::name),
                ExtraCodecs.compactListCodec(ParameterizedClass.CODEC).optionalFieldOf("wildcards", List.of()).forGetter(Class::wildcards),
                Codec.BOOL.optionalFieldOf("legacy_enum", false).deprecated(8).forGetter(Class::legacyEnum)
            ).apply(instance, Class::new));

            public static final Codec<Class> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(Class::new, Class::name);

            public static final Codec<Class> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, api -> {
                if (!api.legacyEnum() && api.wildcards().isEmpty()) {
                    return Either.left(api);
                }
                return Either.right(api);
            });

            public TypeName getType() {
                return ParameterizedClass.getAsWildcardType(Types.typed(this.name), this.wildcards);
            }

            public void appendType(StringBuilder builder, Function<ClassNamed, String> imported) {
                ParameterizedClass.appendAsWildcardType(builder, this.name, this.wildcards, imported);
            }
        }

        public record HolderClass(Optional<ClassNamed> name, boolean isInterface) {
            public HolderClass(ClassNamed name) {
                this(Optional.of(name), true);
            }

            public static final Codec<HolderClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SourceCodecs.CLASS_NAMED.optionalFieldOf("name").forGetter(HolderClass::name),
                Codec.BOOL.optionalFieldOf("is_interface", true).forGetter(HolderClass::isInterface)
            ).apply(instance, HolderClass::new));
        }
    }

    public record Impl(ClassNamed klass, String instanceMethod, boolean delayed) {
        public Impl(ClassNamed klass) {
            this(klass, ConstantDescs.INIT_NAME, false);
        }

        public static final Codec<Impl> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SourceCodecs.CLASS_NAMED.fieldOf("class").forGetter(Impl::klass),
            SourceCodecs.IDENTIFIER.optionalFieldOf("instance_method", ConstantDescs.INIT_NAME).forGetter(Impl::instanceMethod),
            Codec.BOOL.optionalFieldOf("delayed", false).deprecated(21).forGetter(Impl::delayed)
        ).apply(instance, Impl::new));

        public static final Codec<Impl> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(Impl::new, Impl::klass);

        public static final Codec<Impl> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, impl -> {
            if (impl.instanceMethod().equals(ConstantDescs.INIT_NAME) && !impl.delayed()) {
                return Either.left(impl);
            }
            return Either.right(impl);
        });
    }

    public record Builder(ClassNamed api, ClassNamed impl, List<ParameterizedClass> wildcards, RegisterCapability capability) {

        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SourceCodecs.CLASS_NAMED.fieldOf("api").forGetter(Builder::api),
            SourceCodecs.CLASS_NAMED.fieldOf("impl").forGetter(Builder::impl),
            ExtraCodecs.compactListCodec(ParameterizedClass.CODEC).optionalFieldOf("wildcards", List.of()).forGetter(Builder::wildcards),
            RegisterCapability.CODEC.optionalFieldOf("capability", RegisterCapability.WRITABLE).forGetter(Builder::capability)
        ).apply(instance, Builder::new));

        public TypeName getApiType() {
            return ParameterizedClass.getAsWildcardType(Types.typed(this.api), this.wildcards);
        }

        public void appendApiType(StringBuilder builder, Function<ClassNamed, String> imported) {
            ParameterizedClass.appendAsWildcardType(builder, this.api, this.wildcards, imported);
        }

        public enum RegisterCapability implements StringRepresentable {
            NONE("none"),
            ADDABLE("addable"),
            MODIFIABLE("modifiable"),
            WRITABLE("writable");

            private final String name;
            static final Codec<RegisterCapability> CODEC = StringRepresentable.fromEnum(RegisterCapability::values);

            RegisterCapability(String name) {
                this.name = name;
            }

            public boolean canAdd() {
                return this != MODIFIABLE && this != NONE;
            }

            @Override
            public String getSerializedName() {
                return this.name;
            }
        }
    }
}
