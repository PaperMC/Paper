package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

record ActionButtonImpl(Component label, @Nullable Component tooltip, int width, @Nullable DialogAction action) implements ActionButton {

    static final class BuilderImpl implements ActionButton.Builder {

        private final Component label;
        private @Nullable Component tooltip;
        private int width = 150;
        private @Nullable DialogAction action;

        BuilderImpl(final Component label) {
            this.label = label;
        }

        @Override
        public Builder tooltip(final @Nullable Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @Override
        public Builder width(final int width) {
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
