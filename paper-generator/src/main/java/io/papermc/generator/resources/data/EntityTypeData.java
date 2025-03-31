package io.papermc.generator.resources.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.papermc.generator.utils.SourceCodecs;
import io.papermc.typewriter.ClassNamed;
import net.minecraft.util.ExtraCodecs;

public record EntityTypeData(ClassNamed api, int legacyId) {

    private static final int NO_LEGACY_ID = -1;

    public EntityTypeData(ClassNamed api) {
        this(api, NO_LEGACY_ID);
    }

    public static final Codec<EntityTypeData> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SourceCodecs.CLASS_NAMED.fieldOf("api").forGetter(EntityTypeData::api),
        ExtraCodecs.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("legacy_id", NO_LEGACY_ID).deprecated(13).forGetter(EntityTypeData::legacyId)
    ).apply(instance, EntityTypeData::new));

    private static final Codec<EntityTypeData> CLASS_ONLY_CODEC = SourceCodecs.CLASS_NAMED.xmap(EntityTypeData::new, EntityTypeData::api);

    public static final Codec<EntityTypeData> CODEC = Codec.either(CLASS_ONLY_CODEC, DIRECT_CODEC).xmap(Either::unwrap, data -> {
        if (data.legacyId() != NO_LEGACY_ID) {
            return Either.right(data);
        }
        return Either.left(data);
    });
}
