package io.papermc.generator.resources.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.papermc.generator.utils.SourceCodecs;
import io.papermc.typewriter.ClassNamed;

@Deprecated
public record ItemMetaData(ClassNamed api, String field) {

    public static final Codec<ItemMetaData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SourceCodecs.CLASS_NAMED.fieldOf("api").forGetter(ItemMetaData::api),
        SourceCodecs.IDENTIFIER.fieldOf("field").forGetter(ItemMetaData::field)
    ).apply(instance, ItemMetaData::new));
}
