package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BundleContents;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBundle extends CraftMetaItem implements BundleMeta {

    static final ItemMetaKeyType<BundleContents> ITEMS = new ItemMetaKeyType<>(DataComponents.BUNDLE_CONTENTS, "items");
    //
    private List<ItemStack> items;

    CraftMetaBundle(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaBundle)) {
            return;
        }

        CraftMetaBundle bundle = (CraftMetaBundle) meta;

        if (bundle.hasItems()) {
            this.items = new ArrayList<>(bundle.items);
        }
    }

    CraftMetaBundle(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, ITEMS).ifPresent((bundle) -> {
            bundle.items().forEach((item) -> {
                ItemStack itemStack = CraftItemStack.asCraftMirror(item);

                if (!itemStack.getType().isAir()) { // SPIGOT-7174 - Avoid adding air
                    addItem(itemStack);
                }
            });
        });
    }

    CraftMetaBundle(Map<String, Object> map) {
        super(map);

        Iterable<?> items = SerializableMeta.getObject(Iterable.class, map, ITEMS.BUKKIT, true);
        if (items != null) {
            for (Object stack : items) {
                if (stack instanceof ItemStack itemStack && !itemStack.getType().isAir()) { // SPIGOT-7174 - Avoid adding air
                    addItem(itemStack);
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (hasItems()) {
            List<net.minecraft.world.item.ItemStack> list = new ArrayList<>();

            for (ItemStack item : items) {
                list.add(CraftItemStack.asNMSCopy(item));
            }

            tag.put(ITEMS, new BundleContents(list));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.BUNDLE;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBundleEmpty();
    }

    boolean isBundleEmpty() {
        return !(hasItems());
    }

    @Override
    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    @Override
    public List<ItemStack> getItems() {
        return (items == null) ? ImmutableList.of() : ImmutableList.copyOf(items);
    }

    @Override
    public void setItems(List<ItemStack> items) {
        this.items = null;

        if (items == null) {
            return;
        }

        for (ItemStack i : items) {
            addItem(i);
        }
    }

    @Override
    public void addItem(ItemStack item) {
        Preconditions.checkArgument(item != null && !item.getType().isAir(), "item is null or air");

        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBundle) {
            CraftMetaBundle that = (CraftMetaBundle) meta;

            return (hasItems() ? that.hasItems() && this.items.equals(that.items) : !that.hasItems());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBundle || isBundleEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasItems()) {
            hash = 61 * hash + items.hashCode();
        }

        return original != hash ? CraftMetaBundle.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaBundle clone() {
        return (CraftMetaBundle) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasItems()) {
            builder.put(ITEMS.BUKKIT, items);
        }

        return builder;
    }
}
