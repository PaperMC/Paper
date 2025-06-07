package io.papermc.paper.registry.data.dialog.input;

import net.kyori.adventure.text.Component;

record BooleanDialogInputImpl(String key, Component label, boolean initial, String onTrue, String onFalse) implements BooleanDialogInput {

    static final class BuilderImpl implements BooleanDialogInput.Builder {

        private final String key;
        private final Component label;
        private boolean initial = false;
        private String onTrue = "true";
        private String onFalse = "false";

        BuilderImpl(final String key, final Component label) {
            this.key = key;
            this.label = label;
        }

        @Override
        public BooleanDialogInput.Builder initial(final boolean initial) {
            this.initial = initial;
            return this;
        }

        @Override
        public BooleanDialogInput.Builder onTrue(final String onTrue) {
            this.onTrue = onTrue;
            return this;
        }

        @Override
        public BooleanDialogInput.Builder onFalse(final String onFalse) {
            this.onFalse = onFalse;
            return this;
        }

        @Override
        public BooleanDialogInput build() {
            return new BooleanDialogInputImpl(this.key, this.label, this.initial, this.onTrue, this.onFalse);
        }
    }
}
