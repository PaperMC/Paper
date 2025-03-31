package io.papermc.generator.resources.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.squareup.javapoet.ClassName;
import io.papermc.generator.utils.SourceCodecs;

public record EntityClassData(ClassName name, boolean hasSpecifier) {

    public EntityClassData(ClassName name) {
        this(name, true);
    }

    public static final Codec<EntityClassData> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SourceCodecs.CLASS_NAME.fieldOf("name").forGetter(EntityClassData::name),
        Codec.BOOL.optionalFieldOf("has_specifier", true).forGetter(EntityClassData::hasSpecifier)
    ).apply(instance, EntityClassData::new));

    private static final Codec<EntityClassData> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAME.xmap(EntityClassData::new, EntityClassData::name);

    public static final Codec<EntityClassData> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, data -> {
        if (!data.hasSpecifier()) {
            return Either.right(data);
        }
        return Either.left(data);
    });
}
