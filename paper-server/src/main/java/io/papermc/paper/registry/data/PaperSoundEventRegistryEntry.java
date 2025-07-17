package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.Sound;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

/**
 * Not actually used for modifying {@link net.minecraft.core.registries.Registries#SOUND_EVENT}
 * but for creating direct holders for other registries and direct {@link org.bukkit.craftbukkit.CraftSound}s.
 */
public class PaperSoundEventRegistryEntry implements SoundEventRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable ResourceLocation location;
    protected @Nullable Float fixedRange;

    public PaperSoundEventRegistryEntry(final Conversions conversions, final @Nullable SoundEvent soundEvent) {
        this.conversions = conversions;
        if (soundEvent == null) {
            return;
        }

        this.location = soundEvent.location();
        this.fixedRange = soundEvent.fixedRange().orElse(null);
    }

    @Override
    public Key location() {
        return PaperAdventure.asAdventure(asConfigured(this.location, "location"));
    }

    @Override
    public @Nullable Float fixedRange() {
        return this.fixedRange;
    }

    public static final class PaperBuilder extends PaperSoundEventRegistryEntry implements SoundEventRegistryEntry.Builder, PaperRegistryBuilder<SoundEvent, Sound> {

        public PaperBuilder(final Conversions conversions, final @Nullable SoundEvent soundEvent) {
            super(conversions, soundEvent);
        }

        @Override
        public SoundEventRegistryEntry.Builder location(final Key location) {
            this.location = PaperAdventure.asVanilla(asArgument(location, "location"));
            return this;
        }

        @Override
        public SoundEventRegistryEntry.Builder fixedRange(final @Nullable Float fixedRange) {
            this.fixedRange = fixedRange;
            return this;
        }

        @Override
        public SoundEvent build() {
            return new SoundEvent(asConfigured(this.location, "location"), Optional.ofNullable(this.fixedRange));
        }
    }
}
