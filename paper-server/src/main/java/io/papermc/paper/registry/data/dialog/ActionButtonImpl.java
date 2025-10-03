package io.papermc.paper.registry.data.dialog;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.CommonButtonData;
import org.jspecify.annotations.Nullable;

public record ActionButtonImpl(Component label, @Nullable Component tooltip, int width, @Nullable DialogAction action) implements ActionButton {

    public static final class BuilderImpl implements ActionButton.Builder {

        private final Component label;
        private @Nullable Component tooltip;
        private int width = CommonButtonData.DEFAULT_WIDTH;
        private @Nullable DialogAction action;

        public BuilderImpl(final Component label) {
            this.label = label;
        }

        @Override
        public Builder tooltip(final @Nullable Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @Override
        public Builder width(final int width) {
            Preconditions.checkArgument(width >= 1 && width <= 1024, "Width must be between 1 and 1024");
            this.width = width;
            return this;
        }

        @Override
        public Builder action(final @Nullable DialogAction action) {
            this.action = action;
            return this;
        }

        @Override
        public ActionButton build() {
            return new ActionButtonImpl(this.label, this.tooltip, this.width, this.action);
        }
    }
}
