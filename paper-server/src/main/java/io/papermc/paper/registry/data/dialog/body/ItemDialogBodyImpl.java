package io.papermc.paper.registry.data.dialog.body;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public record ItemDialogBodyImpl(
    ItemStack item, @Nullable PlainMessageDialogBody description, boolean showDecorations, boolean showTooltip, int width, int height
) implements ItemDialogBody {

    public static final class BuilderImpl implements ItemDialogBody.Builder {

        private final ItemStack item;
        private @Nullable PlainMessageDialogBody description;
        private boolean showDecorations = true;
        private boolean showTooltip = true;
        private int width = 16;
        private int height = 16;

        public BuilderImpl(final ItemStack item) {
            net.minecraft.world.item.ItemStack.validateStrict(CraftItemStack.unwrap(item)).getOrThrow();
            this.item = item;
        }

        @Override
        public ItemDialogBody.Builder description(final @Nullable PlainMessageDialogBody description) {
            this.description = description;
            return this;
        }

        @Override
        public ItemDialogBody.Builder showDecorations(final boolean showDecorations) {
            this.showDecorations = showDecorations;
            return this;
        }

        @Override
        public ItemDialogBody.Builder showTooltip(final boolean showTooltip) {
            this.showTooltip = showTooltip;
            return this;
        }

        @Override
        public ItemDialogBody.Builder width(final int width) {
            Preconditions.checkArgument(width >= 1 && width <= 256, "Width must be between 1 and 256");
            this.width = width;
            return this;
        }

        @Override
        public ItemDialogBody.Builder height(final int height) {
            Preconditions.checkArgument(height >= 1 && height <= 256, "Height must be between 1 and 256");
            this.height = height;
            return this;
        }

        @Override
        public ItemDialogBody build() {
            return new ItemDialogBodyImpl(this.item, this.description, this.showDecorations, this.showTooltip, this.width, this.height);
        }
    }
}
