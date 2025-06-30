package io.papermc.paper.registry.data.dialog.body;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

record ItemDialogBodyImpl(
    ItemStack item, @Nullable PlainMessageDialogBody description, boolean showDecorations, boolean showTooltip, int width, int height
) implements ItemDialogBody {

    static final class BuilderImpl implements ItemDialogBody.Builder {

        private final ItemStack item;
        private @Nullable PlainMessageDialogBody description;
        private boolean showDecorations = true;
        private boolean showTooltip = true;
        private int width = 16;
        private int height = 16;

        BuilderImpl(final ItemStack item) {
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
            this.width = width;
            return this;
        }

        @Override
        public ItemDialogBody.Builder height(final int height) {
            this.height = height;
            return this;
        }

        @Override
        public ItemDialogBody build() {
            return new ItemDialogBodyImpl(this.item, this.description, this.showDecorations, this.showTooltip, this.width, this.height);
        }
    }
}
