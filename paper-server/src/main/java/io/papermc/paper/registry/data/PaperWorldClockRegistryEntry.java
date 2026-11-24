package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.clock.WorldClock;
import org.jspecify.annotations.Nullable;

public class PaperWorldClockRegistryEntry implements WorldClockRegistryEntry {

    public PaperWorldClockRegistryEntry(final Conversions ignoredConversions, final @Nullable WorldClock ignoredInternal) {
    }

    public static final class PaperBuilder extends PaperWorldClockRegistryEntry implements WorldClockRegistryEntry.Builder, PaperRegistryBuilder<WorldClock, io.papermc.paper.world.WorldClock> {

        public PaperBuilder(final Conversions conversions, final @Nullable WorldClock internal) {
            super(conversions, internal);
        }

        @Override
        public WorldClock build() {
            return new WorldClock();
        }
    }
}
