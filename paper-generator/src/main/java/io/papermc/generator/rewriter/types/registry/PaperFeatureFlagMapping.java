package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import org.bukkit.FeatureFlag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperFeatureFlagMapping extends SearchReplaceRewriter {

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        Iterator<Identifier> flagIterator = FeatureFlags.REGISTRY.toNames(FeatureFlags.REGISTRY.allFlags()).stream().sorted(Formatting.alphabeticKeyOrder(Identifier::getPath)).iterator();

        while (flagIterator.hasNext()) {
            Identifier name = flagIterator.next();
            String keyedName = Formatting.formatKeyAsField(name.getPath());
            builder.append(metadata.indent());
            builder.append("%s.%s, %s.%s".formatted(FeatureFlag.class.getSimpleName(), keyedName, FeatureFlags.class.getSimpleName(), keyedName));
            if (flagIterator.hasNext()) {
                builder.append(',');
            }
            builder.append('\n');
        }
    }
}
