package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.typewriter.util.ClassHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;

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

        return "%s<%s>".formatted(ClassHelper.retrieveFullNestedName(ItemType.Typed.class), ItemMeta.class.getSimpleName());
    }
}
