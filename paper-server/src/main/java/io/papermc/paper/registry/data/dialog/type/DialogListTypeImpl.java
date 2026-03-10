package io.papermc.paper.registry.data.dialog.type;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import net.minecraft.server.dialog.CommonButtonData;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requirePositive;
import static io.papermc.paper.util.BoundChecker.requireRange;

public record DialogListTypeImpl(
    RegistrySet<Dialog> dialogs,
    @Nullable ActionButton exitAction,
    int columns,
    int buttonWidth
) implements DialogListType {

    public static final class BuilderImpl implements DialogListType.Builder {

        private final RegistrySet<Dialog> dialogs;
        private @Nullable ActionButton exitAction;
        private int columns = 2;
        private int buttonWidth = CommonButtonData.DEFAULT_WIDTH;

        public BuilderImpl(final RegistrySet<Dialog> dialogs) {
            this.dialogs = dialogs;
        }

        @Override
        public DialogListType.Builder exitAction(final @Nullable ActionButton exitAction) {
            this.exitAction = exitAction;
            return this;
        }

        @Override
        public DialogListType.Builder columns(final @Positive int columns) {
            this.columns = requirePositive(columns, "columns");
            return this;
        }

        @Override
        public DialogListType.Builder buttonWidth(final @Range(from = 1, to = 1024) int buttonWidth) {
            this.buttonWidth = requireRange(buttonWidth, "buttonWidth", 1, 1024);
            return this;
        }

        @Override
        public DialogListType build() {
            return new DialogListTypeImpl(this.dialogs, this.exitAction, this.columns, this.buttonWidth);
        }
    }
}
