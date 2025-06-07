package io.papermc.paper.registry.data.dialog.specialty;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.set.RegistrySet;
import org.jspecify.annotations.Nullable;

record DialogListSpecialtyImpl(
    RegistrySet<Dialog> dialogs,
    @Nullable ActionButton exitAction,
    int columns,
    int buttonWidth
) implements DialogListSpecialty {

    static final class BuilderImpl implements DialogListSpecialty.Builder {

        private final RegistrySet<Dialog> dialogs;
        private @Nullable ActionButton exitAction;
        private int columns = 2;
        private int buttonWidth = 150;

        BuilderImpl(final RegistrySet<Dialog> dialogs) {
            this.dialogs = dialogs;
        }

        @Override
        public DialogListSpecialty.Builder exitAction(final @Nullable ActionButton exitAction) {
            this.exitAction = exitAction;
            return this;
        }

        @Override
        public DialogListSpecialty.Builder columns(final int columns) {
            this.columns = columns;
            return this;
        }

        @Override
        public DialogListSpecialty.Builder buttonWidth(final int buttonWidth) {
            this.buttonWidth = buttonWidth;
            return this;
        }

        @Override
        public DialogListSpecialty build() {
            return new DialogListSpecialtyImpl(this.dialogs, this.exitAction, this.columns, this.buttonWidth);
        }
    }
}
