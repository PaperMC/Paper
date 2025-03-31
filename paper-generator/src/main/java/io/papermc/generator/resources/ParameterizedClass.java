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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import net.minecraft.util.ExtraCodecs;

public record ParameterizedClass(ClassNamed klass, List<ParameterizedClass> arguments) {
    public ParameterizedClass(ClassNamed value) {
        this(value, List.of());
    }

    private static Codec<ParameterizedClass> directCodec(Codec<ParameterizedClass> codec) {
        return RecordCodecBuilder.create(instance -> instance.group(
            SourceCodecs.CLASS_NAMED.fieldOf("class").forGetter(ParameterizedClass::klass),
            ExtraCodecs.compactListCodec(codec).optionalFieldOf("arguments", List.of()).forGetter(ParameterizedClass::arguments)
        ).apply(instance, ParameterizedClass::new));
    }

    public static final Codec<ParameterizedClass> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(ParameterizedClass::new, ParameterizedClass::klass);

    public static final Codec<ParameterizedClass> CODEC = Codec.recursive("ParameterizedClass", codec -> {
        return Codec.either(CLASS_ONLY_CODEC, directCodec(codec)).xmap(Either::unwrap, parameterizedClass -> {
            if (parameterizedClass.arguments().isEmpty()) {
                return Either.left(parameterizedClass);
            }
            return Either.right(parameterizedClass);
        });
    });

    public TypeName getType() {
        ClassName rawType = Types.typed(this.klass);
        if (!this.arguments.isEmpty()) {
            List<TypeName> convertedArgs = new ArrayList<>(this.arguments.size());
            for (ParameterizedClass argument : this.arguments) {
                convertedArgs.add(argument.getType());
            }

            return ParameterizedTypeName.get(rawType, convertedArgs.toArray(TypeName[]::new));
        }

        return rawType;
    }

    public void appendType(StringBuilder builder, Function<ClassNamed, String> imported) {
        builder.append(imported.apply(this.klass));
        if (!this.arguments.isEmpty()) {
            builder.append('<');
            for (ParameterizedClass argument : this.arguments) {
                argument.appendType(builder, imported);
            }
            builder.append('>');
        }
    }

    public static TypeName getAsWildcardType(ClassName rawType, List<ParameterizedClass> wildcardClasses) {
        if (!wildcardClasses.isEmpty()) {
            TypeName[] wildcards = new TypeName[wildcardClasses.size()];
            for (int i = 0; i < wildcardClasses.size(); i ++) {
                wildcards[i] = WildcardTypeName.subtypeOf(wildcardClasses.get(i).getType());
            }
            return ParameterizedTypeName.get(rawType, wildcards);
        }
        return rawType;
    }

    public static void appendAsWildcardType(StringBuilder builder, ClassNamed rawType, List<ParameterizedClass> wildcardClasses, Function<ClassNamed, String> imported) {
        builder.append(imported.apply(rawType));
        int size = wildcardClasses.size();
        if (size != 0) {
            builder.append('<');
            Iterator<ParameterizedClass> iterator = wildcardClasses.iterator();
            while (iterator.hasNext()) {
                ParameterizedClass wildcard = iterator.next();
                if (wildcard.arguments().isEmpty() && wildcard.klass().canonicalName().equals(Object.class.getCanonicalName())) {
                    builder.append('?');
                } else {
                    builder.append("? extends ");
                    wildcard.appendType(builder, imported);
                }
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
            builder.append('>');
        }
    }
}
