package io.papermc.paper.registry.data.dialog.input;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.data.util.Checks;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.functions.StringTemplate;
import net.minecraft.server.dialog.body.PlainMessage;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.requireArgumentMinInclusive;
import static io.papermc.paper.registry.data.util.Checks.requireArgumentPositive;
import static io.papermc.paper.registry.data.util.Checks.requireArgumentRange;

public record NumberRangeDialogInputImpl(
    String key,
    int width,
    Component label,
    String labelFormat,
    float start,
    float end,
    @Nullable Float initial,
    @Nullable Float step
) implements NumberRangeDialogInput {

    public static final class BuilderImpl implements NumberRangeDialogInput.Builder {

        private final String key;
        private final Component label;
        private final float start;
        private final float end;
        private int width = PlainMessage.DEFAULT_WIDTH;
        private String labelFormat = "options.generic_value";
        private @Nullable Float initial = null;
        private @Nullable Float step = null;

        public BuilderImpl(final String key, final Component label, final float start, final float end) {
            Preconditions.checkArgument(StringTemplate.isValidVariableName(key), "key must be a valid input name");
            this.key = key;
            this.label = label;
            this.start = start;
            this.end = end;
        }

        @Override
        public BuilderImpl width(final int width) {
            this.width = Checks.requireArgumentRange(width, "width", 1, 1024);
            return this;
        }

        @Override
        public BuilderImpl labelFormat(final String labelFormat) {
            this.labelFormat = labelFormat;
            return this;
        }

        @Override
        public BuilderImpl initial(final @Nullable Float initial) {
            this.initial = (initial == null ? null : requireArgumentMinInclusive(initial, "initial", this.start, this.end));
            return this;
        }

        @Override
        public BuilderImpl step(final @Nullable Float step) {
            this.step = (step == null ? null : requireArgumentPositive(step, "step"));
            return this;
        }

        @Override
        public NumberRangeDialogInput build() {
            return new NumberRangeDialogInputImpl(this.key, this.width, this.label, this.labelFormat, this.start, this.end, this.initial, this.step);
        }
    }
}
