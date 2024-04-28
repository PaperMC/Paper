package io.papermc.paper.datacomponent.item;

import java.util.Optional;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

public record PaperPotDecorations(
    net.minecraft.world.level.block.entity.PotDecorations impl
) implements PotDecorations, Handleable<net.minecraft.world.level.block.entity.PotDecorations> {

    @Override
    public @Nullable ItemType back() {
        return this.impl.back().map(CraftItemType::minecraftToBukkitNew).orElse(null);
    }

    @Override
    public @Nullable ItemType left() {
        return this.impl.left().map(CraftItemType::minecraftToBukkitNew).orElse(null);
    }

    @Override
    public @Nullable ItemType right() {
        return this.impl.right().map(CraftItemType::minecraftToBukkitNew).orElse(null);
    }

    @Override
    public @Nullable ItemType front() {
        return this.impl.front().map(CraftItemType::minecraftToBukkitNew).orElse(null);
    }

    @Override
    public net.minecraft.world.level.block.entity.PotDecorations getHandle() {
        return this.impl;
    }

    static final class BuilderImpl implements PotDecorations.Builder {

        private @Nullable ItemType back;
        private @Nullable ItemType left;
        private @Nullable ItemType right;
        private @Nullable ItemType front;

        @Override
        public PotDecorations.Builder back(final @Nullable ItemType back) {
            this.back = back;
            return this;
        }

        @Override
        public PotDecorations.Builder left(final @Nullable ItemType left) {
            this.left = left;
            return this;
        }

        @Override
        public PotDecorations.Builder right(final @Nullable ItemType right) {
            this.right = right;
            return this;
        }

        @Override
        public PotDecorations.Builder front(final @Nullable ItemType front) {
            this.front = front;
            return this;
        }

        @Override
        public PotDecorations build() {
            if (this.back == null && this.left == null && this.right == null && this.front == null) {
                return new PaperPotDecorations(net.minecraft.world.level.block.entity.PotDecorations.EMPTY);
            }

            return new PaperPotDecorations(new net.minecraft.world.level.block.entity.PotDecorations(
                Optional.ofNullable(this.back).map(CraftItemType::bukkitToMinecraftNew),
                Optional.ofNullable(this.left).map(CraftItemType::bukkitToMinecraftNew),
                Optional.ofNullable(this.right).map(CraftItemType::bukkitToMinecraftNew),
                Optional.ofNullable(this.front).map(CraftItemType::bukkitToMinecraftNew)
            ));
        }
    }
}
