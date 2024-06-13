package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.OptionalInt;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgumentMin;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperGameEventRegistryEntry implements GameEventRegistryEntry {

    protected OptionalInt range = OptionalInt.empty();

    public PaperGameEventRegistryEntry(
        final Conversions ignoredConversions,
        final @Nullable GameEvent internal
    ) {
        if (internal == null) return;

        this.range = OptionalInt.of(internal.notificationRadius());
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int range() {
        return asConfigured(this.range, "range");
    }

    public static final class PaperBuilder extends PaperGameEventRegistryEntry implements GameEventRegistryEntry.Builder,
        PaperRegistryBuilder<GameEvent, org.bukkit.GameEvent> {

        public PaperBuilder(
            final Conversions conversions,
            final @Nullable GameEvent internal
        ) {
            super(conversions, internal);
        }

        @Override
        public GameEventRegistryEntry.Builder range(final @Range(from = 0, to = Integer.MAX_VALUE) int range) {
            this.range = OptionalInt.of(asArgumentMin(range, "range", 0));
            return this;
        }

        @Override
        public GameEvent build() {
            return new GameEvent(this.range());
        }
    }
}
