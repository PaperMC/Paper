package io.papermc.paper.registry.data.dialog.input;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

record NumberRangeDialogInputImpl(
    String key,
    int width,
    Component label,
    String labelFormat,
    float start,
    float end,
    @Nullable Float initial,
    @Nullable Float step
) implements NumberRangeDialogInput {

    static final class BuilderImpl implements NumberRangeDialogInput.Builder {

        private final String key;
        private final Component label;
        private final float start;
        private final float end;
        private int width = 200;
        private String labelFormat = "options.generic_value";
        private @Nullable Float initial = null;
        private @Nullable Float step = null;

        BuilderImpl(final String key, final Component label, final float start, final float end) {
            this.key = key;
            this.label = label;
            this.start = start;
            this.end = end;
        }

        @Override
        public BuilderImpl width(final int width) {
            this.width = width;
            return this;
        }

        @Override
        public BuilderImpl labelFormat(final String labelFormat) {
            this.labelFormat = labelFormat;
            return this;
        }

        @Override
        public BuilderImpl initial(final @Nullable Float initial) {
            this.initial = initial;
            return this;
        }

        @Override
        public BuilderImpl step(final @Nullable Float step) {
            Preconditions.checkArgument(step == null || step > 0, "step must be null or greater than 0");
            this.step = step;
            return this;
        }

        @Override
        public NumberRangeDialogInput build() {
            return new NumberRangeDialogInputImpl(this.key, this.width, this.label, this.labelFormat, this.start, this.end, this.initial, this.step);
        }
    }
}
