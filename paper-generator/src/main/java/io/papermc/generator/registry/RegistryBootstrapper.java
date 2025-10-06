package io.papermc.generator.registry;

import com.squareup.javapoet.FieldSpec;
import io.papermc.generator.rewriter.registration.PatternSourceSetRewriter;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.PaperRegistriesRewriter;
import io.papermc.generator.rewriter.types.registry.RegistryEventsRewriter;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.types.registry.GeneratedKeyType;
import io.papermc.generator.types.registry.GeneratedReferenceType;
import io.papermc.generator.types.registry.GeneratedTagKeyType;
import io.papermc.paper.registry.event.RegistryEvents;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RegistryBootstrapper {

    private static final String PAPER_REGISTRY_PACKAGE = "io.papermc.paper.registry";
    private static final String PAPER_REFERENCES_PACKAGE = "io.papermc.paper.generator.references";

    public static void bootstrapApi(List<SourceGenerator> generators) {
        // typed/tag keys
        RegistryEntries.forEach(entry -> {
            generators.add(new GeneratedKeyType<>(PAPER_REGISTRY_PACKAGE + ".keys", entry));
            if (entry.registry().listTags().findAny().isPresent()) {
                generators.add(new GeneratedTagKeyType(entry, PAPER_REGISTRY_PACKAGE + ".keys.tags"));
            }
        });

        // todo remove once entity type and potion are both a registry
        generators.add(new GeneratedTagKeyType(RegistryEntries.byRegistryKey(Registries.ENTITY_TYPE), PAPER_REGISTRY_PACKAGE + ".keys.tags"));
        generators.add(new GeneratedTagKeyType(RegistryEntries.byRegistryKey(Registries.POTION), PAPER_REGISTRY_PACKAGE + ".keys.tags"));
    }

    public static void bootstrapServer(List<SourceGenerator> generators) {
        // references
        generators.add(new GeneratedReferenceType.NonFiltered<>(PAPER_REFERENCES_PACKAGE, RegistryEntries.byRegistryKey(Registries.ENTITY_TYPE)));
        generators.add(new GeneratedReferenceType.NonFiltered<>(PAPER_REFERENCES_PACKAGE, RegistryEntries.byRegistryKey(Registries.ITEM)));
        generators.add(new GeneratedReferenceType.NonFiltered<>(PAPER_REFERENCES_PACKAGE, RegistryEntries.byRegistryKey(Registries.BLOCK)));
        generators.add(new GeneratedReferenceType<>(PAPER_REFERENCES_PACKAGE, "BlockItemType", RegistryEntries.byRegistryKey(Registries.ITEM)) {

            @Override
            public boolean isDisplayed(Holder.Reference<Item> reference) {
                return reference.value() instanceof BlockItem;
            }

            @Override
            protected MethodReference createMethod() {
                return new MethodReference.Identified(BlockItemId.class, "create") {
                    @Override
                    public void callFrom(FieldSpec.Builder fieldBuilder, Holder.Reference<?> item) {
                        BlockItem blockItem = (BlockItem) item.value();
                        Holder.Reference<Block> block = (Holder.Reference<Block>) BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock());
                        Identifier blockName = block.key().identifier();
                        Identifier itemName = item.key().identifier();
                        if (blockName.equals(itemName)) {
                            super.callFrom(fieldBuilder, block); // swap
                        } else {
                            fieldBuilder.initializer("%s($S, $S)".formatted(this.name), blockName.getPath(), itemName.getPath());
                        }
                    }
                };
            }
        });
    }

    public static void bootstrap(PatternSourceSetRewriter apiSourceSet, PatternSourceSetRewriter serverSourceSet) {
        bootstrapApi(apiSourceSet);
        bootstrapServer(serverSourceSet);
    }

    public static void bootstrapApi(PatternSourceSetRewriter sourceSet) {
        sourceSet.register("RegistryEvents", RegistryEvents.class, new RegistryEventsRewriter());
    }

    public static void bootstrapServer(PatternSourceSetRewriter sourceSet) {
        sourceSet.register("RegistryDefinitions", Types.PAPER_REGISTRIES, new PaperRegistriesRewriter());
    }
}
