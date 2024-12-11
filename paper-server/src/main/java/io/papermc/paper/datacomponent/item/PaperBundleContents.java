package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
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
        return MCUtil.transformUnmodifiable((List<net.minecraft.world.item.ItemStack>) this.impl.items(), CraftItemStack::asBukkitCopy);
    }

    static final class BuilderImpl implements BundleContents.Builder {

        private final List<net.minecraft.world.item.ItemStack> items = new ObjectArrayList<>();

        @Override
        public BundleContents.Builder add(final ItemStack stack) {
            Preconditions.checkArgument(stack != null, "stack cannot be null");
            Preconditions.checkArgument(!stack.isEmpty(), "stack cannot be empty");
            this.items.add(CraftItemStack.asNMSCopy(stack));
            return this;
        }

        @Override
        public BundleContents.Builder addAll(final List<ItemStack> stacks) {
            stacks.forEach(this::add);
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
