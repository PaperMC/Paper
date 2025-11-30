package io.papermc.paper.world.attribute;

import io.papermc.paper.math.Position;
import io.papermc.paper.util.FloatSupplier;
import java.util.function.LongSupplier;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public record PaperEnvironmentalAttributeContext(
    @Nullable Long time,
    @Nullable Position position,
    @Nullable Float rainLevel,
    @Nullable Float thunderLevel
) implements EnvironmentalAttributeContext {

    public static final PaperEnvironmentalAttributeContext EMPTY = new PaperEnvironmentalAttributeContext(null, null, null, null);
    public static final ThreadLocal<@NonNull PaperEnvironmentalAttributeContext> CURRENT_CONTEXT = ThreadLocal.withInitial(() -> PaperEnvironmentalAttributeContext.EMPTY);

    public long time(LongSupplier fallbackSupplier) {
        return this.time != null ? this.time : fallbackSupplier.getAsLong();
    }

    public float rainLevel(FloatSupplier fallbackSupplier) {
        return this.rainLevel != null ? this.rainLevel : fallbackSupplier.getAsFloat();
    }

    public float thunderLevel(FloatSupplier fallbackSupplier) {
        return this.thunderLevel != null ? this.thunderLevel : fallbackSupplier.getAsFloat();
    }

    public static class PaperBuilder implements EnvironmentalAttributeContext.Builder {

        private @Nullable Long time;
        private @Nullable Position position;
        private @Nullable Float rainLevel;
        private @Nullable Float thunderLevel;

        @Override
        public Builder time(@Nullable final Long time) {
            this.time = time;
            return this;
        }

        @Override
        public Builder position(@Nullable final Position position) {
            this.position = position;
            return this;
        }

        @Override
        public Builder rainLevel(@Nullable final Float rainLevel) {
            this.rainLevel = rainLevel;
            return this;
        }

        @Override
        public Builder raining(final boolean raining) {
            return this.rainLevel(raining ? 1.0F : 0.0F);
        }

        @Override
        public Builder thunderLevel(@Nullable final Float thunderLevel) {
            this.thunderLevel = thunderLevel;
            return this;
        }

        @Override
        public Builder thundering(final boolean thundering) {
            return this.thunderLevel(thundering ? 1.0F : 0.0F);
        }

        @Override
        public EnvironmentalAttributeContext build() {
            if (this.time == null && this.position == null && this.rainLevel == null && this.thunderLevel == null) {
                return PaperEnvironmentalAttributeContext.EMPTY;
            }

            return new PaperEnvironmentalAttributeContext(this.time, this.position, this.rainLevel, this.thunderLevel);
        }
    }
}
