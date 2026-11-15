package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.item.ItemStackTemplate;
import io.papermc.paper.item.PaperItemStackTemplate;
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
        return MCUtil.transformUnmodifiable(this.impl.items(), CraftItemStack::asBukkitCopy);
    }

    @Override
    public List<ItemStackTemplate> projectileTemplates() {
        return MCUtil.transformUnmodifiable(this.impl.items(), PaperItemStackTemplate::new);
    }

    static final class BuilderImpl implements ChargedProjectiles.Builder {

        private final List<net.minecraft.world.item.ItemStackTemplate> items = new ArrayList<>();

        @Override
        public ChargedProjectiles.Builder add(final ItemStack item) {
            Preconditions.checkArgument(item != null, "item cannot be null");
            return this.add(item.toTemplate());
        }

        @Override
        public ChargedProjectiles.Builder add(final ItemStackTemplate template) {
            Preconditions.checkArgument(template != null, "template cannot be null");
            this.items.add(PaperItemStackTemplate.unwrap(template));
            return this;
        }

        @Override
        public ChargedProjectiles.Builder addAll(final List<ItemStack> items) {
            items.forEach(this::add);
            return this;
        }

        @Override
        public ChargedProjectiles.Builder addAllTemplates(final List<ItemStackTemplate> templates) {
            templates.forEach(this::add);
            return this;
        }

        @Override
        public ChargedProjectiles build() {
            if (this.items.isEmpty()) {
                return new PaperChargedProjectiles(net.minecraft.world.item.component.ChargedProjectiles.EMPTY);
            }
            return new PaperChargedProjectiles(new net.minecraft.world.item.component.ChargedProjectiles(List.copyOf(this.items)));
        }
    }
}
