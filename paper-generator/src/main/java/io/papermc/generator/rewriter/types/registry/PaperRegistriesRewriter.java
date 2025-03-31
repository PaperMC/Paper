package io.papermc.generator.rewriter.types.registry;

import com.google.common.base.CaseFormat;
import io.papermc.generator.registry.RegistryData;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.lang.constant.ConstantDescs;
import net.minecraft.core.registries.Registries;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperRegistriesRewriter extends SearchReplaceRewriter {

    private void appendEntry(String indent, StringBuilder builder, RegistryEntry<?> entry, boolean canBeDelayed, boolean apiOnly) {
        builder.append(indent);
        builder.append("start");
        builder.append('(');
        builder.append(Registries.class.getSimpleName()).append('.').append(entry.registryKeyField());
        builder.append(", ");
        builder.append(Types.REGISTRY_KEY.simpleName()).append('.').append(entry.registryKeyField());
        builder.append(").");

        RegistryData data = entry.data();
        if (apiOnly) {
            builder.append("apiOnly(");
            if (data.api().legacyEnum()) {
                builder.append(this.importCollector.getShortName(Types.PAPER_SIMPLE_REGISTRY)).append("::").append(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.registryKey().location().getPath()));
            } else {
                builder.append("() -> ");
                builder.append(Types.REGISTRY.canonicalName()).append('.').append(data.api().registryField().orElse(entry.registryKeyField()));
            }
            builder.append(')');
        } else {
            builder.append("craft(");
            builder.append(this.importCollector.getShortName(data.api().holders().orElse(data.api().klass()))).append(".class");
            builder.append(", ");

            builder.append(this.importCollector.getShortName(data.impl().klass())).append("::").append(data.impl().instanceMethod().equals(ConstantDescs.INIT_NAME) ? "new" : data.impl().instanceMethod());

            if (data.allowInline()) {
                builder.append(", ");
                builder.append(Boolean.TRUE.toString());
            }
            builder.append(')');

            data.serializationUpdaterField().ifPresent(field -> {
                builder.append(".serializationUpdater(").append(Types.FIELD_RENAME.simpleName()).append('.').append(field).append(")");
            });

            data.builder().ifPresentOrElse(b -> {
                builder.append(".writable(");
                builder.append(this.importCollector.getShortName(b.impl())).append("::new");
                builder.append(')');
            }, () -> builder.append(".build()"));
        }
        if (canBeDelayed && data.delayed()) {
            builder.append(".delayed()");
        }
        builder.append(',');
        builder.append('\n');
    }

    @Override
    public void insert(SearchMetadata metadata, StringBuilder builder) {
        builder.append(metadata.indent()).append("// built-in");
        builder.append('\n');

        for (RegistryEntry<?> entry : RegistryEntries.BUILT_IN) {
            appendEntry(metadata.indent(), builder, entry, false, false);
        }

        builder.append('\n');
        builder.append(metadata.indent()).append("// data-driven");
        builder.append('\n');

        for (RegistryEntry<?> entry : RegistryEntries.DATA_DRIVEN) {
            appendEntry(metadata.indent(), builder, entry, true, false);
        }

        builder.append('\n');
        builder.append(metadata.indent()).append("// api-only");
        builder.append('\n');

        for (RegistryEntry<?> entry : RegistryEntries.API_ONLY) {
            appendEntry(metadata.indent(), builder, entry, false, true);
        }

        builder.deleteCharAt(builder.length() - 2); // delete extra comma...
    }
}
