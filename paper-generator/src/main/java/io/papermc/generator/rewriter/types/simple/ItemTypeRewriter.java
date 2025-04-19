package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.utils.ItemMetaData;
import io.papermc.generator.utils.ItemPredicate;
import io.papermc.typewriter.ClassNamed;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.List;
import java.util.Map;

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

        // todo shortcut and remove order rule for CraftMetaColorableArmor <-> CraftMetaArmor / CraftMetaBanner|CraftMetaSkull <-> CraftMetaBlockState (create custom tag? or just inline?)
        ClassNamed implMetaName = null;
    mainLoop:
        for (Map.Entry<ClassNamed, List<ItemPredicate>> entry : ItemMetaData.PREDICATES.entrySet()) {
            for (ItemPredicate predicate : entry.getValue()) {
                if (predicate.test(reference)) {
                    implMetaName = entry.getKey();
                    break mainLoop;
                }
            }
        }

        ClassNamed metaName = null;
        if (implMetaName != null) {
            metaName = ItemMetaData.BRIDGE.get(implMetaName).api();
        }
        return "%s<%s>".formatted(Types.ITEM_TYPE_TYPED.dottedNestedName(), metaName != null ? this.importCollector.getShortName(metaName) : Types.ITEM_META.simpleName());
    }
}
