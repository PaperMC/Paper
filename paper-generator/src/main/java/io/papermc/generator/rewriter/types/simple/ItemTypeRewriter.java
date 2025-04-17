package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.utils.ItemMetaMapping;
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
        ClassNamed implName = null;
    mainLoop:
        for (Map.Entry<ClassNamed, List<ItemMetaMapping.ItemPredicate>> entry : ItemMetaMapping.PREDICATES.entrySet()) {
            for (ItemMetaMapping.ItemPredicate predicate : entry.getValue()) {
                if (predicate.test(reference)) {
                    implName = entry.getKey();
                    break mainLoop;
                }
            }
        }

        ClassNamed apiName = null;
        if (implName != null) {
            apiName = ItemMetaMapping.BRIDGE.get(implName).api();
        }
        return "%s<%s>".formatted(Types.ITEM_TYPE_TYPED.dottedNestedName(), apiName != null ? this.importCollector.getShortName(apiName) : Types.ITEM_META.simpleName());
    }
}
