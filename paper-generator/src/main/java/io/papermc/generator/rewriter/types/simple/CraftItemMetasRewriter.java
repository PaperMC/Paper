package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.predicate.ItemPredicate;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.context.IndentUnit;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

@Deprecated
public class CraftItemMetasRewriter extends SearchReplaceRewriter {

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        IndentUnit indentUnit = this.indentUnit();
        ClassNamed itemType = RegistryEntries.byRegistryKey(Registries.ITEM).data().api().klass().name();
        for (Map.Entry<ClassNamed, List<ItemPredicate>> entry : DataFileLoader.get(DataFiles.ITEM_META_PREDICATES).entrySet()) {
            String field = DataFileLoader.get(DataFiles.ITEM_META_BRIDGE).get(entry.getKey()).field();
            Iterator<ItemPredicate> predicateIterator = entry.getValue().iterator();

            builder.append(metadata.indent());
            builder.append("if (");
            boolean beginning = true;
            while (predicateIterator.hasNext()) {
                ItemPredicate predicate = predicateIterator.next();
                if (!beginning) {
                    builder.append(metadata.indent()).append(indentUnit);
                }
                switch (predicate) {
                    case ItemPredicate.IsElementPredicate isElementPredicate:
                        isElementPredicate.value()
                            .ifLeft(tagKey -> {
                                // flatten tag since they can change at runtime with plugins/data-packs
                                Iterator<Holder<Item>> tagValues = BuiltInRegistries.ITEM.getTagOrEmpty(tagKey).iterator();
                                while (tagValues.hasNext()) {
                                    appendElementEquality(builder, itemType, tagValues.next().unwrapKey().orElseThrow().location());
                                    if (tagValues.hasNext()) {
                                        builder.append(" ||").append("\n");
                                        builder.append(metadata.indent()).append(indentUnit);
                                    }
                                }
                            })
                            .ifRight(element -> appendElementEquality(builder, itemType, element.unwrapKey().orElseThrow().location()));
                        break;
                    case ItemPredicate.IsClassPredicate isClassPredicate: {
                        String itemLikeName = isClassPredicate.againstBlock() ? "blockHandle" : "itemHandle";
                        if (itemLikeName.equals("itemHandle")) { // itemHandle is never null
                            builder.append("%s.getClass().equals(%s.class)".formatted(itemLikeName, this.importCollector.getShortName(isClassPredicate.value())));
                        } else {
                            builder.append("(%1$s != null && %1$s.getClass().equals(%2$s.class))".formatted(itemLikeName, this.importCollector.getShortName(isClassPredicate.value())));
                        }
                        break;
                    }
                    case ItemPredicate.InstanceOfPredicate instanceOfPredicate: {
                        String itemLikeName = instanceOfPredicate.againstBlock() ? "blockHandle" : "itemHandle";
                        builder.append("%s instanceof %s".formatted(itemLikeName, this.importCollector.getShortName(instanceOfPredicate.value())));
                        break;
                    }
                }
                if (predicateIterator.hasNext()) {
                    builder.append(" ||").append("\n");
                }
                beginning = false;
            }

            builder.append(") {").append("\n");
            {
                builder.append(metadata.indent()).append(indentUnit);
                builder.append("return %s.asType(%s);".formatted(Types.CRAFT_ITEM_METAS.simpleName(), field));
                builder.append("\n");
            }
            builder.append(metadata.indent());
            builder.append("}").append("\n");
        }
    }

    private static void appendElementEquality(StringBuilder builder, ClassNamed itemType, ResourceLocation id) {
        builder.append("itemType == %s.%s".formatted(itemType.simpleName(), Formatting.formatKeyAsField(id.getPath())));
    }
}
