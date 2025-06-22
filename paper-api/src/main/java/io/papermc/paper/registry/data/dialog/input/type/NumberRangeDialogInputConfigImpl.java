package io.papermc.paper.registry.data.dialog.input.type;

import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

record NumberRangeDialogInputConfigImpl(
    int width,
    Component label,
    String labelFormat,
    float start,
    float end,
    @Nullable Float initial,
    @Nullable Float step
) implements NumberRangeDialogInputConfig {

    static final class BuilderImpl implements NumberRangeDialogInputConfig.Builder {

        private final Component label;
        private final float start;
        private final float end;
        private int width = 200;
        private String labelFormat = "%s: %s";
        private @Nullable Float initial = null;
        private @Nullable Float step = null;

        BuilderImpl(final Component label, final float start, final float end) {
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
            this.step = step;
            return this;
        }

        @Override
        public NumberRangeDialogInputConfig build() {
            return new NumberRangeDialogInputConfigImpl(this.width, this.label, this.labelFormat, this.start, this.end, this.initial, this.step);
        }
    }
}
