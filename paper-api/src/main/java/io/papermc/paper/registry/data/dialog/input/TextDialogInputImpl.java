package io.papermc.paper.registry.data.dialog.input;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

record TextDialogInputImpl(
    String key,
    int width,
    Component label,
    boolean labelVisible,
    String initial,
    int maxLength,
    TextDialogInput.@Nullable MultilineOptions multiline
) implements TextDialogInput {

    static final class BuilderImpl implements TextDialogInput.Builder {

        private final String key;
        private int width = 200;
        private final Component label;
        private boolean labelVisible = true;
        private String initial = "";
        private int maxLength = 32;
        private TextDialogInput.@Nullable MultilineOptions multiline = null;

        BuilderImpl(final String key, final Component label) {
            this.key = key;
            this.label = label;
        }

        @Override
        public TextDialogInput.Builder width(final int width) {
            this.width = width;
            return this;
        }

        @Override
        public TextDialogInput.Builder labelVisible(final boolean labelVisible) {
            this.labelVisible = labelVisible;
            return this;
        }

        @Override
        public TextDialogInput.Builder initial(final String initial) {
            this.initial = initial;
            return this;
        }

        @Override
        public TextDialogInput.Builder maxLength(final int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        @Override
        public TextDialogInput.Builder multiline(final TextDialogInput.@Nullable MultilineOptions multiline) {
            this.multiline = multiline;
            return this;
        }

        @Override
        public TextDialogInput build() {
            Preconditions.checkState(this.initial.length() <= this.maxLength, "The initial value must be less than or equal to the maximum length.");
            return new TextDialogInputImpl(this.key, this.width, this.label, this.labelVisible, this.initial, this.maxLength, this.multiline);
        }
    }
}
