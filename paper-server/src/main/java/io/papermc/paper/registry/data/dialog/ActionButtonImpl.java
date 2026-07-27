package io.papermc.paper.registry.data.dialog;

import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.text.Component;
import net.minecraft.server.dialog.CommonButtonData;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireRange;

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
        public Builder width(final @Range(from = 1, to = 1024) int width) {
            this.width = requireRange(width, "width", 1, 1024);
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
