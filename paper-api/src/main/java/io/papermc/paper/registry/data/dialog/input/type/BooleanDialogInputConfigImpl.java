package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;

record BooleanDialogInputConfigImpl(Component label, boolean initial, String onTrue, String onFalse) implements BooleanDialogInputConfig {

    static final class BuilderImpl implements BooleanDialogInputConfig.Builder {

        private final Component label;
        private boolean initial = false;
        private String onTrue = "true";
        private String onFalse = "false";

        BuilderImpl(final Component label) {
            this.label = label;
        }

        @Override
        public BooleanDialogInputConfig.Builder initial(final boolean initial) {
            this.initial = initial;
            return this;
        }

        @Override
        public BooleanDialogInputConfig.Builder onTrue(final String onTrue) {
            this.onTrue = onTrue;
            return this;
        }

        @Override
        public BooleanDialogInputConfig.Builder onFalse(final String onFalse) {
            this.onFalse = onFalse;
            return this;
        }

        @Override
        public BooleanDialogInputConfig build() {
            return new BooleanDialogInputConfigImpl(this.label, this.initial, this.onTrue, this.onFalse);
        }
    }
}
