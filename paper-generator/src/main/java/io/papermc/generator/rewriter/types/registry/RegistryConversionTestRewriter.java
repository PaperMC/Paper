package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;

public class RegistryConversionTestRewriter extends SearchReplaceRewriter {

    @Override
    public void insert(SearchMetadata metadata, StringBuilder builder) {
        RegistryEntries.forEach(entry -> {
            if (entry.data().allowInline()) {
                builder.append(metadata.indent());
                builder.append("%s.%s,".formatted(Types.REGISTRY_KEY.simpleName(), entry.registryKeyField()));
                builder.append("\n");
            }
        });

        builder.deleteCharAt(builder.length() - 2); // delete extra comma...
    }
}
