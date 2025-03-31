package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.ApiStatus;

import static io.papermc.generator.rewriter.utils.Annotations.annotation;
import static io.papermc.generator.utils.Formatting.quoted;

public class FeatureFlagRewriter extends SearchReplaceRewriter {

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        Iterator<Map.Entry<ResourceLocation, FeatureFlag>> flagIterator = FeatureFlags.REGISTRY.names.entrySet().stream().sorted(Formatting.alphabeticKeyOrder(entry -> entry.getKey().getPath())).iterator();
        while (flagIterator.hasNext()) {
            Map.Entry<ResourceLocation, FeatureFlag> entry = flagIterator.next();
            ResourceLocation name = entry.getKey();
            if (FeatureFlags.isExperimental(FeatureFlagSet.of(entry.getValue()))) {
                builder.append(metadata.indent()).append(annotation(ApiStatus.Experimental.class, this.importCollector)).append('\n');
            }

            builder.append(metadata.indent());

            builder.append(Types.FEATURE_FLAG.simpleName()).append(' ').append(Formatting.formatKeyAsField(name.getPath()));
            builder.append(" = ");
            builder.append("create(%s)".formatted(quoted(name.getPath())));
            builder.append(';');

            builder.append('\n');
            if (flagIterator.hasNext()) {
                builder.append('\n');
            }
        }
    }
}
