package io.papermc.paper.registry.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.world.WorldClock;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.timeline.Timeline;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperTimelineRegistryEntry implements TimelineRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable Timeline internal;
    protected @Nullable Holder<net.minecraft.world.clock.WorldClock> clock;
    protected @Nullable Integer periodTicks;
    protected final Map<Key, TimeMarker> timeMarkers = new LinkedHashMap<>();

    public PaperTimelineRegistryEntry(final Conversions conversions, final @Nullable Timeline internal) {
        this.conversions = conversions;
        this.internal = internal;

        if (internal == null) {
            return;
        }
        this.clock = internal.clock();
        this.periodTicks = internal.periodTicks().orElse(null);
        internal.registerTimeMarkers((key, marker) -> this.timeMarkers.put(PaperAdventure.asAdventure(key.identifier()), TimeMarker.of(marker.ticks(), marker.showInCommands())));
    }

    @Override
    public TypedKey<WorldClock> clock() {
        return PaperRegistries.fromNms(asConfigured(this.clock, "clock").unwrapKey().orElseThrow());
    }

    @Override
    public OptionalInt periodTicks() {
        return this.periodTicks != null ? OptionalInt.of(this.periodTicks) : OptionalInt.empty();
    }

    @Override
    public Map<Key, TimeMarker> timeMarkers() {
        return Map.copyOf(this.timeMarkers);
    }

    public static final class PaperBuilder extends PaperTimelineRegistryEntry implements TimelineRegistryEntry.Builder, PaperRegistryBuilder<Timeline, io.papermc.paper.world.Timeline> {

        public PaperBuilder(final Conversions conversions, final @Nullable Timeline internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clock(final TypedKey<WorldClock> clock) {
            this.clock = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(clock, "clock")));
            return this;
        }

        @Override
        public Builder periodTicks(final @Range(from = 1, to = Integer.MAX_VALUE) int periodTicks) {
            Preconditions.checkArgument(periodTicks >= 1, "periodTicks must be at least 1, was %s", periodTicks);
            this.periodTicks = periodTicks;
            return this;
        }

        @Override
        public Builder clearPeriodTicks() {
            this.periodTicks = null;
            return this;
        }

        @Override
        public Builder addTimeMarker(final Key key, final @Range(from = 0, to = Integer.MAX_VALUE) int ticks, final boolean showInCommands) {
            Preconditions.checkArgument(ticks >= 0, "ticks must be at least 0, was %s", ticks);
            this.timeMarkers.put(asArgument(key, "key"), TimeMarker.of(ticks, showInCommands));
            return this;
        }

        @Override
        public Timeline build() {
            final Timeline.Builder builder = Timeline.builder(asConfigured(this.clock, "clock"));
            if (this.periodTicks != null) {
                builder.setPeriodTicks(this.periodTicks);
            }
            if (this.internal != null) {
                builder.addTracks(this.internal.tracks);
            }
            this.timeMarkers.forEach((key, marker) -> builder.addTimeMarker(ResourceKey.create(net.minecraft.world.clock.ClockTimeMarkers.ROOT_ID, PaperAdventure.asVanilla(key)), marker.ticks(), marker.showInCommands()));
            return builder.build();
        }
    }
}
