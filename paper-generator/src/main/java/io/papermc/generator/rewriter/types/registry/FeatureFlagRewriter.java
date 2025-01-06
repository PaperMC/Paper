package io.papermc.generator.rewriter.types.registry;

import com.mojang.logging.LogUtils;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.SourceFile;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

import static io.papermc.generator.rewriter.utils.Annotations.annotation;
import static io.papermc.generator.utils.Formatting.quoted;

@NullMarked
public class FeatureFlagRewriter extends SearchReplaceRewriter {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public boolean registerFor(SourceFile file) {
        try {
            org.bukkit.FeatureFlag.class.getDeclaredMethod("create", String.class);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Fetch method not found, skipping the rewriter for feature flag", e);
            return false;
        }

        return super.registerFor(file);
    }

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

            builder.append(org.bukkit.FeatureFlag.class.getSimpleName()).append(' ').append(Formatting.formatKeyAsField(name.getPath()));
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
