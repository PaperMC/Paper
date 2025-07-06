package io.papermc.paper.registry.data.dialog.input;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.functions.StringTemplate;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.server.dialog.input.TextInput;
import org.jspecify.annotations.Nullable;

public record TextDialogInputImpl(
    String key,
    int width,
    Component label,
    boolean labelVisible,
    String initial,
    int maxLength,
    TextDialogInput.@Nullable MultilineOptions multiline
) implements TextDialogInput {

    public record MultilineOptionsImpl(@Nullable Integer maxLines, @Nullable Integer height) implements MultilineOptions {
        public MultilineOptionsImpl {
            Preconditions.checkArgument(maxLines == null || maxLines > 0, "maxLines must be null or greater than 0");
            Preconditions.checkArgument(height == null || (height >= 1 && height <= TextInput.MultilineOptions.MAX_HEIGHT), "height must be null or between 1 and 512");
        }
    }

    public static final class BuilderImpl implements TextDialogInput.Builder {

        private final String key;
        private int width = PlainMessage.DEFAULT_WIDTH;
        private final Component label;
        private boolean labelVisible = true;
        private String initial = "";
        private int maxLength = 32;
        private TextDialogInput.@Nullable MultilineOptions multiline = null;

        public BuilderImpl(final String key, final Component label) {
            Preconditions.checkArgument(StringTemplate.isValidVariableName(key), "key must be a valid input name");
            this.key = key;
            this.label = label;
        }

        @Override
        public TextDialogInput.Builder width(final int width) {
            Preconditions.checkArgument(width >= 1 && width <= 1024, "width must be between 1 and 1024");
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
            Preconditions.checkArgument(maxLength > 0, "maxLength must be greater than 0");
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
