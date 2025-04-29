package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import java.util.OptionalInt;

import static io.papermc.paper.registry.data.util.Checks.*;

public class PaperJukeboxSongRegistryEntry implements JukeboxSongRegistryEntry {

    protected @Nullable Holder<SoundEvent> soundEvent;
    protected @Nullable Component description;
    protected float lengthInSeconds;
    protected OptionalInt comparatorOutput = OptionalInt.empty();

    protected final Conversions conversions;

    public PaperJukeboxSongRegistryEntry(
        final Conversions conversions,
        final @Nullable JukeboxSong internal
        ) {

        this.conversions = conversions;

        if (internal == null) return;

        this.soundEvent = internal.soundEvent();
        this.description = internal.description();
        this.lengthInSeconds = internal.lengthInSeconds();
        this.comparatorOutput = OptionalInt.of(internal.comparatorOutput());
    }

    @Override
    public Key sound() {
        return PaperAdventure.asAdventure(this.soundEvent.value().location());
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return conversions.asAdventure(this.description);
    }

    @Override
    public float lengthInSeconds() {
        return this.lengthInSeconds;
    }

    @Override
    public @Range(from = 0, to = 15) int comparatorOutput() {
        return asConfigured(this.comparatorOutput, "comparatorOutput");
    }

    public static final class PaperBuilder extends PaperJukeboxSongRegistryEntry implements Builder, PaperRegistryBuilder<JukeboxSong, org.bukkit.JukeboxSong> {

        public PaperBuilder(final Conversions conversions, final @Nullable JukeboxSong internal) {
            super(conversions, internal);
        }

        @Override
        public Builder sound(final Key sound) {
            this.soundEvent = PaperAdventure.resolveSound(sound);
            return this;
        }

        @Override
        public Builder description(final net.kyori.adventure.text.Component description) {
            this.description = conversions.asVanilla(description);
            return this;
        }

        @Override
        public Builder lengthInSeconds(final float lengthInSeconds) {
            this.lengthInSeconds = lengthInSeconds;
            return this;
        }

        @Override
        public Builder comparatorOutput(final @Range(from = 0, to = 15) int comparatorOutput) {
            this.comparatorOutput = OptionalInt.of(asArgumentRange(comparatorOutput, "comparatorOutput", 0, 15));
            return this;
        }

        @Override
        public JukeboxSong build() {
            return new JukeboxSong(
                asConfigured(this.soundEvent, "soundEvent"),
                asConfigured(this.description, "description"),
                this.lengthInSeconds,
                this.comparatorOutput()
            );
        }
    }

}
