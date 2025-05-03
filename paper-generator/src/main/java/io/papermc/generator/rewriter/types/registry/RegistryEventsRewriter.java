package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEventProvider;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import org.jspecify.annotations.NullMarked;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class RegistryEventsRewriter extends SearchReplaceRewriter {

    @Override
    public void insert(SearchMetadata metadata, StringBuilder builder) {
        RegistryEntries.forEach(entry -> {
            if (entry.apiRegistryBuilder() != null && entry.modificationApiSupport() != RegistryEntry.RegistryModificationApiSupport.NONE) {
                builder.append(metadata.indent());
                builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));
                builder.append(RegistryEventProvider.class.getSimpleName());
                builder.append("<").append(this.importCollector.getShortName(entry.apiClass())).append(", ").append(this.importCollector.getShortName(entry.apiRegistryBuilder())).append('>');
                builder.append(' ');
                builder.append(entry.registryKeyField());
                builder.append(" = ");
                builder.append("create(").append(RegistryKey.class.getSimpleName()).append('.').append(entry.registryKeyField()).append(");");
                builder.append('\n');
            }
        });
    }
}
