package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.Main;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.registry.RegistryIdentifiable;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

import static io.papermc.generator.utils.Formatting.quoted;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@ApiStatus.Obsolete
public class RegistryTagRewriter<T> extends SearchReplaceRewriter implements RegistryIdentifiable<T> {

    private final ResourceKey<? extends Registry<T>> registryKey;

    public RegistryTagRewriter(ResourceKey<? extends Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        RegistryEntry<T> entry = RegistryEntries.byRegistryKey(this.registryKey);
        Iterator<? extends TagKey<T>> keyIterator = entry.registry().listTagIds().sorted(Formatting.TAG_ORDER).iterator();

        while (keyIterator.hasNext()) {
            TagKey<T> tagKey = keyIterator.next();

            String featureFlagName = Main.EXPERIMENTAL_TAGS.get(tagKey);
            if (featureFlagName != null) {
                Annotations.experimentalAnnotations(builder, metadata.indent(), this.importCollector, SingleFlagHolder.fromName(featureFlagName));
            }

            builder.append(metadata.indent());
            builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));

            builder.append("%s<%s>".formatted(Types.TAG.simpleName(), entry.data().api().klass().name().simpleName())).append(' ').append(this.rewriteFieldName(tagKey));
            builder.append(" = ");
            builder.append(this.rewriteFieldValue(tagKey));
            builder.append(';');

            builder.append('\n');
            if (keyIterator.hasNext()) {
                builder.append('\n');
            }
        }
    }

    protected String rewriteFieldName(TagKey<T> tagKey) {
        return Formatting.formatKeyAsField(tagKey.location().getPath());
    }

    protected String rewriteFieldValue(TagKey<T> tagKey) {
        return "%s(%s)".formatted(TagRewriter.FETCH_METHOD, quoted(tagKey.location().getPath()));
    }
}
