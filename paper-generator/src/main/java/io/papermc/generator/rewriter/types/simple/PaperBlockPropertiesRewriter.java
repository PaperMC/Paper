package io.papermc.generator.rewriter.types.simple;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.Formatting;
import io.papermc.paper.block.property.BlockProperties;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class PaperBlockPropertiesRewriter extends SearchReplaceRewriter {

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        Multimap<String, Property<?>> nameToProperties = BlockStateMapping.FALLBACK_GENERIC_FIELDS.entrySet().stream().collect(
            Multimaps.toMultimap(entry -> entry.getKey().getName(), Map.Entry::getKey,
                HashMultimap::create)
        );

        for (Map.Entry<Property<?>, Field> entry : BlockStateMapping.FALLBACK_GENERIC_FIELDS.entrySet()
            .stream().sorted(Formatting.alphabeticKeyOrder(entry -> entry.getValue().getName())).toList()) {
            if (nameToProperties.get(entry.getKey().getName()).size() <= 1) {
                continue;
            }

            builder.append(metadata.indent()).append("register(%1$s.%3$s, %2$s.%3$s);".formatted(
                this.importCollector.getShortName(BlockStateProperties.class),
                BlockProperties.class.getSimpleName(),
                entry.getValue().getName()
            ));
            builder.append('\n');
        }
    }
}
