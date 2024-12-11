package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.MCUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemStack;

public record PaperChargedProjectiles(
    net.minecraft.world.item.component.ChargedProjectiles impl
) implements ChargedProjectiles, Handleable<net.minecraft.world.item.component.ChargedProjectiles> {

    @Override
    public net.minecraft.world.item.component.ChargedProjectiles getHandle() {
        return this.impl;
    }

    @Override
    public List<ItemStack> projectiles() {
        return MCUtil.transformUnmodifiable(this.impl.getItems() /*makes copies internally*/, CraftItemStack::asCraftMirror);
    }

    static final class BuilderImpl implements ChargedProjectiles.Builder {

        private final List<net.minecraft.world.item.ItemStack> items = new ArrayList<>();

        @Override
        public ChargedProjectiles.Builder add(final ItemStack stack) {
            Preconditions.checkArgument(stack != null, "stack cannot be null");
            Preconditions.checkArgument(!stack.isEmpty(), "stack cannot be empty");
            this.items.add(CraftItemStack.asNMSCopy(stack));
            return this;
        }

        @Override
        public ChargedProjectiles.Builder addAll(final List<ItemStack> stacks) {
            stacks.forEach(this::add);
            return this;
        }

        @Override
        public ChargedProjectiles build() {
            if (this.items.isEmpty()) {
                return new PaperChargedProjectiles(net.minecraft.world.item.component.ChargedProjectiles.EMPTY);
            }
            return new PaperChargedProjectiles(net.minecraft.world.item.component.ChargedProjectiles.of(this.items));
        }
    }
}
