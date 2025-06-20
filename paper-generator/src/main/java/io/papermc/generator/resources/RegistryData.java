package io.papermc.generator.resources;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.WildcardTypeName;
import io.papermc.generator.types.Types;
import io.papermc.generator.utils.SourceCodecs;
import io.papermc.typewriter.ClassNamed;
import java.lang.constant.ConstantDescs;
import java.util.Iterator;
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

    public record Api(Class klass, Optional<ClassNamed> holders, boolean keyClassNameRelate, Optional<String> registryField) {
        public Api(ClassNamed klass) {
            this(new Class(klass), Optional.of(klass), false, Optional.empty());
        }

        public static final Codec<Api> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Class.CODEC.fieldOf("class").forGetter(Api::klass),
            SourceCodecs.CLASS_NAMED.optionalFieldOf("holders").forGetter(Api::holders),
            Codec.BOOL.optionalFieldOf("key_class_name_relate", false).forGetter(Api::keyClassNameRelate),
            SourceCodecs.IDENTIFIER.optionalFieldOf("registry_field").forGetter(Api::registryField)
        ).apply(instance, Api::new));

        public static final Codec<Api> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(Api::new, api -> api.klass().name());

        public static final Codec<Api> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, api -> {
            if ((api.holders().isEmpty() || api.klass().name().equals(api.holders().get())) &&
                !api.keyClassNameRelate() && api.registryField().isEmpty()) {
                return Either.left(api);
            }
            return Either.right(api);
        });

        public record Class(Type type, ClassNamed name, List<ParameterizedClass> wildcards) {
            public Class(ClassNamed name) {
                this(Type.INTERFACE, name, List.of());
            }

            public static final Codec<Class> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Type.CODEC.optionalFieldOf("type", Type.INTERFACE).forGetter(Class::type),
                SourceCodecs.CLASS_NAMED.fieldOf("name").forGetter(Class::name),
                ExtraCodecs.compactListCodec(ParameterizedClass.CODEC).optionalFieldOf("wildcards", List.of()).forGetter(Class::wildcards)
            ).apply(instance, Class::new));

            public static final Codec<Class> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(Class::new, Class::name);

            public static final Codec<Class> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, api -> {
                if (api.type() == Type.INTERFACE && api.wildcards().isEmpty()) {
                    return Either.left(api);
                }
                return Either.right(api);
            });

            public TypeName getType() {
                ClassName rawType = Types.typed(this.name);
                if (!this.wildcards.isEmpty()) {
                    TypeName[] wildcards = new TypeName[this.wildcards.size()];
                    for (int i = 0; i < this.wildcards.size(); i ++) {
                        wildcards[i] = WildcardTypeName.subtypeOf(this.wildcards.get(i).getType());
                    }
                    return ParameterizedTypeName.get(rawType, wildcards);
                }
                return rawType;
            }

            public void appendType(StringBuilder builder, Function<ClassNamed, String> imported) {
                builder.append(imported.apply(this.name));
                int size = this.wildcards.size();
                if (size != 0) {
                    builder.append("<");
                    Iterator<ParameterizedClass> iterator = this.wildcards.iterator();
                    while (iterator.hasNext()) {
                        ParameterizedClass wildcard = iterator.next();
                        if (wildcard.arguments().isEmpty() && wildcard.klass().canonicalName().equals(Object.class.getCanonicalName())) {
                            builder.append("?");
                        } else {
                            builder.append("? extends ");
                            wildcard.appendType(builder, imported);
                        }
                        if (iterator.hasNext()) {
                            builder.append(", ");
                        }
                    }
                    builder.append(">");
                }
            }

            public enum Type implements StringRepresentable {
                INTERFACE("interface"),
                CLASS("class"),
                @Deprecated(since = "1.8")
                ENUM("enum");

                private final String name;
                static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

                Type(String name) {
                    this.name = name;
                }

                @Override
                public String getSerializedName() {
                    return this.name;
                }
            }
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

    public record Builder(ClassNamed api, ClassNamed impl, RegisterCapability capability) {

        public static final Codec<Builder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SourceCodecs.CLASS_NAMED.fieldOf("api").forGetter(Builder::api),
            SourceCodecs.CLASS_NAMED.fieldOf("impl").forGetter(Builder::impl),
            RegisterCapability.CODEC.optionalFieldOf("capability", RegisterCapability.WRITABLE).forGetter(Builder::capability)
        ).apply(instance, Builder::new));

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
