package io.papermc.paper.registry.data.dialog.input.type;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public record TextDialogInputConfigImpl(
    int width, Component label, boolean labelVisible, String initial, int maxLength, TextDialogInputConfig.@Nullable MultilineOptions multiline
) implements TextDialogInputConfig {

    static final class BuilderImpl implements TextDialogInputConfig.Builder {

        private int width = 200;
        private final Component label;
        private boolean labelVisible = true;
        private String initial = "";
        private int maxLength = 32;
        private TextDialogInputConfig.@Nullable MultilineOptions multiline = null;

        BuilderImpl(final Component label) {
            this.label = label;
        }

        @Override
        public TextDialogInputConfig.Builder width(final int width) {
            this.width = width;
            return this;
        }

        @Override
        public TextDialogInputConfig.Builder labelVisible(final boolean labelVisible) {
            this.labelVisible = labelVisible;
            return this;
        }

        @Override
        public TextDialogInputConfig.Builder initial(final String initial) {
            this.initial = initial;
            return this;
        }

        @Override
        public TextDialogInputConfig.Builder maxLength(final int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        @Override
        public TextDialogInputConfig.Builder multiline(final TextDialogInputConfig.@Nullable MultilineOptions multiline) {
            this.multiline = multiline;
            return this;
        }

        @Override
        public TextDialogInputConfig build() {
            Preconditions.checkState(this.initial.length() <= this.maxLength, "The initial value must be less than or equal to the maximum length.");
            return new TextDialogInputConfigImpl(this.width, this.label, this.labelVisible, this.initial, this.maxLength, this.multiline);
        }
    }
}
