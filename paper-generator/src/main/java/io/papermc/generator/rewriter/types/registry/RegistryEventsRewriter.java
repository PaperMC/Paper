package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.registry.RegistryData;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.Types;
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
            RegistryData data = entry.data();
            if (data.builder().isPresent()) {
                builder.append(metadata.indent());
                builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));
                builder.append(Types.REGISTRY_EVENT_PROVIDER.simpleName());
                builder.append("<").append(this.importCollector.getShortName(data.api().klass())).append(", ").append(this.importCollector.getShortName(data.builder().get().api())).append('>');
                builder.append(' ');
                builder.append(entry.registryKeyField());
                builder.append(" = ");
                builder.append("create(").append(Types.REGISTRY_KEY.simpleName()).append('.').append(entry.registryKeyField()).append(");");
                builder.append('\n');
            }
        });
    }
}
