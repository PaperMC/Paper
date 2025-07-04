package io.papermc.paper.registry.data.dialog.type;

import com.google.common.base.Preconditions;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import net.minecraft.server.dialog.CommonButtonData;
import org.jspecify.annotations.Nullable;

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
        public DialogListType.Builder columns(final int columns) {
            Preconditions.checkArgument(columns > 0, "columns must be greater than 0");
            this.columns = columns;
            return this;
        }

        @Override
        public DialogListType.Builder buttonWidth(final int buttonWidth) {
            Preconditions.checkArgument(buttonWidth >= 1 && buttonWidth <= 1024, "buttonWidth must be between 1 and 1024");
            this.buttonWidth = buttonWidth;
            return this;
        }

        @Override
        public DialogListType build() {
            return new DialogListTypeImpl(this.dialogs, this.exitAction, this.columns, this.buttonWidth);
        }
    }
}
