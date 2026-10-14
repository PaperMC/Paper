package io.papermc.paper.registry.data.dialog.input;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.functions.StringTemplate;

public record BooleanDialogInputImpl(String key, Component label, boolean initial, String onTrue, String onFalse) implements BooleanDialogInput {

    public static final class BuilderImpl implements BooleanDialogInput.Builder {

        private final String key;
        private final Component label;
        private boolean initial = false;
        private String onTrue = "true";
        private String onFalse = "false";

        public BuilderImpl(final String key, final Component label) {
            Preconditions.checkArgument(StringTemplate.isValidVariableName(key), "key must be a valid input name");
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
