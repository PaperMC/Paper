package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.predicate.ItemPredicate;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.typewriter.ClassNamed;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@Deprecated // bad generic
public class ItemTypeRewriter extends RegistryFieldRewriter<Item> {

    public ItemTypeRewriter() {
        super(Registries.ITEM, "getItemType");
    }

    @Override
    protected String rewriteFieldType(Holder.Reference<Item> reference) {
        if (reference.value().equals(Items.AIR)) {
            return super.rewriteFieldType(reference);
        }

        ClassNamed implMetaName = null;
    mainLoop:
        for (Map.Entry<ClassNamed, List<ItemPredicate>> entry : DataFileLoader.get(DataFiles.ITEM_META_PREDICATES).entrySet()) {
            for (ItemPredicate predicate : entry.getValue()) {
                if (predicate.matches(reference)) {
                    implMetaName = entry.getKey();
                    break mainLoop;
                }
            }
        }

        ClassNamed metaName = null;
        if (implMetaName != null) {
            metaName = DataFileLoader.get(DataFiles.ITEM_META_BRIDGE).get(implMetaName).api();
        }
        return "%s<%s>".formatted(Types.ITEM_TYPE_TYPED.dottedNestedName(), metaName != null ? this.importCollector.getShortName(metaName) : Types.ITEM_META.simpleName());
    }
}
