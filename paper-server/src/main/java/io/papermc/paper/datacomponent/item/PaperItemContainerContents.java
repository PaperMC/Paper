package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.item.ItemStackTemplate;
import io.papermc.paper.item.PaperItemStackTemplate;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemStack;

public record PaperItemContainerContents(
    net.minecraft.world.item.component.ItemContainerContents impl
) implements ItemContainerContents, Handleable<net.minecraft.world.item.component.ItemContainerContents> {

    @Override
    public net.minecraft.world.item.component.ItemContainerContents getHandle() {
        return this.impl;
    }

    @Override
    public List<ItemStack> contents() {
        return MCUtil.transformUnmodifiable(this.impl.items, optional -> optional.map(CraftItemStack::asBukkitCopy).orElse(ItemStack.empty()));
    }

    @Override
    public List<Optional<ItemStackTemplate>> contentTemplates() {
        return MCUtil.transformUnmodifiable(this.impl.items, optional -> optional.map(PaperItemStackTemplate::new));
    }

    static final class BuilderImpl implements ItemContainerContents.Builder {

        private final List<Optional<net.minecraft.world.item.ItemStackTemplate>> items = new ObjectArrayList<>();

        private void checkSize(final int toAdd) {
            Preconditions.checkArgument(
                this.items.size() + toAdd <= net.minecraft.world.item.component.ItemContainerContents.MAX_SIZE,
                "Cannot have more than %s items, had %s",
                net.minecraft.world.item.component.ItemContainerContents.MAX_SIZE,
                this.items.size() + toAdd
            );
        }

        private static Optional<net.minecraft.world.item.ItemStackTemplate> toVanilla(final ItemStack stack) {
            Preconditions.checkArgument(stack != null, "item cannot be null");
            return stack.isEmpty() ? Optional.empty() : Optional.of(PaperItemStackTemplate.unwrap(stack.toTemplate()));
        }

        private static Optional<net.minecraft.world.item.ItemStackTemplate> toVanilla(final Optional<ItemStackTemplate> template) {
            Preconditions.checkArgument(template != null, "template cannot be null");
            return template.map(PaperItemStackTemplate::unwrap);
        }

        @Override
        public ItemContainerContents.Builder add(final ItemStack item) {
            this.checkSize(1);
            this.items.add(toVanilla(item));
            return this;
        }

        @Override
        public ItemContainerContents.Builder add(final Optional<ItemStackTemplate> template) {
            this.checkSize(1);
            this.items.add(toVanilla(template));
            return this;
        }

        @Override
        public ItemContainerContents.Builder addAll(final List<ItemStack> items) {
            this.checkSize(items.size());
            MCUtil.addAndConvert(this.items, items, BuilderImpl::toVanilla);
            return this;
        }

        @Override
        public ItemContainerContents.Builder addAllTemplates(final List<Optional<ItemStackTemplate>> templates) {
            this.checkSize(templates.size());
            MCUtil.addAndConvert(this.items, templates, BuilderImpl::toVanilla);
            return this;
        }

        @Override
        public ItemContainerContents build() {
            // trailing empty slots are dropped, same as vanilla does when creating the contents from item stacks
            int size = this.items.size();
            while (size > 0 && this.items.get(size - 1).isEmpty()) {
                size--;
            }
            if (size == 0) {
                return new PaperItemContainerContents(net.minecraft.world.item.component.ItemContainerContents.EMPTY);
            }
            return new PaperItemContainerContents(new net.minecraft.world.item.component.ItemContainerContents(List.copyOf(this.items.subList(0, size))));
        }
    }
}
