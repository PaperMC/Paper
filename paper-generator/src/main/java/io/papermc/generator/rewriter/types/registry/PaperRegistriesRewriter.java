package io.papermc.generator.rewriter.types.registry;

import com.google.common.base.CaseFormat;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.lang.constant.ConstantDescs;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperRegistriesRewriter extends SearchReplaceRewriter {


    private void appendEntry(String indent, StringBuilder builder, RegistryEntry<?> entry, boolean canBeDelayed, boolean apiOnly) {
        builder.append(indent);
        builder.append("start");
        builder.append('(');
        builder.append(Registries.class.getSimpleName()).append('.').append(entry.registryKeyField());
        builder.append(", ");
        builder.append(RegistryKey.class.getSimpleName()).append('.').append(entry.registryKeyField());
        builder.append(").");
        if (apiOnly) {
            builder.append("apiOnly(");
            if (entry.apiClass().isEnum()) {
                builder.append(this.importCollector.getShortName(Types.PAPER_SIMPLE_REGISTRY)).append("::").append(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.registryKey().location().getPath()));
            } else {
                builder.append("() -> ");
                builder.append(Registry.class.getCanonicalName()).append('.').append(entry.apiRegistryField().orElse(entry.registryKeyField()));
            }
            builder.append(')');
        } else {
            builder.append("craft(");
            builder.append(this.importCollector.getShortName(entry.preloadClass())).append(".class");
            builder.append(", ");

            builder.append(this.importCollector.getShortName(this.getImplClassName(entry))).append("::").append(entry.apiAccessName().equals(ConstantDescs.INIT_NAME) ? "new" : entry.apiAccessName());

            if (entry.canAllowDirect()) {
                builder.append(", ");
                builder.append(Boolean.TRUE.toString());
            }
            builder.append(')');

            if (entry.fieldRename() != null) {
                builder.append(".serializationUpdater(").append(Types.FIELD_RENAME.simpleName()).append('.').append(entry.fieldRename()).append(")");
            }

            if (entry.apiRegistryBuilderImpl() != null && entry.modificationApiSupport() != null) {
                switch (entry.modificationApiSupport()) {
                    case WRITABLE -> builder.append(".writable(");
                    case ADDABLE -> builder.append(".addable(");
                    case MODIFIABLE -> builder.append(".modifiable(");
                    case NONE -> builder.append(".create(");
                }
                builder.append(this.importCollector.getShortName(this.classNamedView.findFirst(entry.apiRegistryBuilderImpl()).resolve(this.classResolver))).append("::new");
                if (entry.modificationApiSupport() == RegistryEntry.RegistryModificationApiSupport.NONE) {
                    builder.append(", ");
                    builder.append(Types.REGISTRY_MODIFICATION_API_SUPPORT.dottedNestedName()).append(".NONE");
                }
                builder.append(')');
            } else {
                builder.append(".build()");
            }
        }
        if (canBeDelayed && entry.isDelayed()) {
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

    private ClassNamed getImplClassName(RegistryEntry<?> entry) {
        try (Stream<ClassNamed> stream = this.classNamedView.find(entry.implClass())) {
            return stream.map(klass -> klass.resolve(this.classResolver))
                .filter(klass -> Keyed.class.isAssignableFrom(klass.knownClass())) // todo check handleable/holderable once keyed is gone
                .findFirst().orElseThrow();
        }
    }
}
