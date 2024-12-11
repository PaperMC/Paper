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

        getOrEmpty(tag, CraftMetaBundle.ITEMS).ifPresent((bundle) -> {
            bundle.items().forEach((item) -> {
                ItemStack itemStack = CraftItemStack.asCraftMirror(item);

                if (!itemStack.getType().isAir()) { // SPIGOT-7174 - Avoid adding air
                    this.addItem(itemStack);
                }
            });
        });
    }

    CraftMetaBundle(Map<String, Object> map) {
        super(map);

        Iterable<?> items = SerializableMeta.getObject(Iterable.class, map, CraftMetaBundle.ITEMS.BUKKIT, true);
        if (items != null) {
            for (Object stack : items) {
                if (stack instanceof ItemStack itemStack && !itemStack.getType().isAir()) { // SPIGOT-7174 - Avoid adding air
                    this.addItem(itemStack);
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasItems()) {
            List<net.minecraft.world.item.ItemStack> list = new ArrayList<>();

            for (ItemStack item : this.items) {
                list.add(CraftItemStack.asNMSCopy(item));
            }

            tag.put(CraftMetaBundle.ITEMS, new BundleContents(list));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBundleEmpty();
    }

    boolean isBundleEmpty() {
        return !(this.hasItems());
    }

    @Override
    public boolean hasItems() {
        return this.items != null && !this.items.isEmpty();
    }

    @Override
    public List<ItemStack> getItems() {
        return (this.items == null) ? ImmutableList.of() : ImmutableList.copyOf(this.items);
    }

    @Override
    public void setItems(List<ItemStack> items) {
        this.items = null;

        if (items == null) {
            return;
        }

        for (ItemStack i : items) {
            this.addItem(i);
        }
    }

    @Override
    public void addItem(ItemStack item) {
        Preconditions.checkArgument(item != null && !item.getType().isAir(), "item is null or air");

        if (this.items == null) {
            this.items = new ArrayList<>();
        }

        this.items.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBundle) {
            CraftMetaBundle that = (CraftMetaBundle) meta;

            return (this.hasItems() ? that.hasItems() && this.items.equals(that.items) : !that.hasItems());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBundle || this.isBundleEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasItems()) {
            hash = 61 * hash + this.items.hashCode();
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

        if (this.hasItems()) {
            builder.put(CraftMetaBundle.ITEMS.BUKKIT, this.items);
        }

        return builder;
    }
}
