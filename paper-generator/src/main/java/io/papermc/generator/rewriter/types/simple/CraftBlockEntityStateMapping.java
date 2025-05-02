package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.utils.BlockEntityMapping;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CraftBlockEntityStateMapping extends SearchReplaceRewriter {

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        BlockEntityMapping.MAPPING.entrySet().stream().sorted(Formatting.alphabeticKeyOrder(entry -> entry.getKey().location().getPath())).forEach(entry -> {
            builder.append(metadata.indent());
            builder.append("register(%s.%s, %s.class, %s::new);".formatted(
                BlockEntityType.class.getSimpleName(), Formatting.formatKeyAsField(entry.getKey().location().getPath()),
                entry.getValue(), entry.getValue()));
            builder.append('\n');
        });
    }
}
