package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.item.ItemStackTemplate;
import io.papermc.paper.item.PaperItemStackTemplate;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemStack;

public record PaperBundleContents(
    net.minecraft.world.item.component.BundleContents impl
) implements BundleContents, Handleable<net.minecraft.world.item.component.BundleContents> {

    @Override
    public net.minecraft.world.item.component.BundleContents getHandle() {
        return this.impl;
    }

    @Override
    public List<ItemStack> contents() {
        return MCUtil.transformUnmodifiable(this.impl.items(), CraftItemStack::asBukkitCopy);
    }

    @Override
    public List<ItemStackTemplate> contentTemplates() {
        return MCUtil.transformUnmodifiable(this.impl.items(), PaperItemStackTemplate::new);
    }

    static final class BuilderImpl implements BundleContents.Builder {

        private final List<net.minecraft.world.item.ItemStackTemplate> items = new ObjectArrayList<>();

        @Override
        public BundleContents.Builder add(final ItemStack item) {
            Preconditions.checkArgument(item != null, "item cannot be null");
            return this.add(item.toTemplate());
        }

        @Override
        public BundleContents.Builder add(final ItemStackTemplate template) {
            Preconditions.checkArgument(template != null, "template cannot be null");
            this.items.add(PaperItemStackTemplate.unwrap(template));
            return this;
        }

        @Override
        public BundleContents.Builder addAll(final List<ItemStack> items) {
            items.forEach(this::add);
            return this;
        }

        @Override
        public BundleContents.Builder addAllTemplates(final List<ItemStackTemplate> templates) {
            templates.forEach(this::add);
            return this;
        }

        @Override
        public BundleContents build() {
            if (this.items.isEmpty()) {
                return new PaperBundleContents(net.minecraft.world.item.component.BundleContents.EMPTY);
            }
            return new PaperBundleContents(new net.minecraft.world.item.component.BundleContents(new ObjectArrayList<>(this.items)));
        }
    }
}
