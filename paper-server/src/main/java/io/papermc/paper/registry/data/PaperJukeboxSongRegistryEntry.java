package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.sound.SoundEventImpl;
import io.papermc.paper.registry.data.sound.SoundEventReferenceImpl;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import org.bukkit.craftbukkit.CraftSound;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperJukeboxSongRegistryEntry implements JukeboxSongRegistryEntry {

    protected Holder<SoundEvent> soundEvent;
    protected Component description;
    protected float lengthInSeconds;
    protected int comparatorOutput;

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
        this.comparatorOutput = internal.comparatorOutput();
    }

    @Override
    public io.papermc.paper.registry.data.sound.SoundEvent soundEvent() {
        return switch (soundEvent.kind()) {
            case DIRECT ->
                new SoundEventImpl(PaperAdventure.asAdventure(soundEvent.value().location()), soundEvent.value().fixedRange());
            case REFERENCE -> new SoundEventReferenceImpl(CraftSound.minecraftHolderToBukkit(soundEvent));
        };
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return this.conversions.asAdventure(this.description);
    }

    @Override
    public float lengthInSeconds() {
        return lengthInSeconds;
    }

    @Override
    public @Range(from = 0, to = 15) int comparatorOutput() {
        return comparatorOutput;
    }

    public static final class PaperBuilder extends PaperJukeboxSongRegistryEntry implements JukeboxSongRegistryEntry.Builder, PaperRegistryBuilder<JukeboxSong, org.bukkit.JukeboxSong> {

        public PaperBuilder(final Conversions conversions, final @Nullable JukeboxSong internal) {
            super(conversions, internal);
        }

        @Override
        public Builder soundEvent(final io.papermc.paper.registry.data.sound.SoundEvent soundEvent) {
            switch (soundEvent) {
                case SoundEventImpl direct ->
                    this.soundEvent = Holder.direct(new SoundEvent(PaperAdventure.asVanilla(direct.soundId()), direct.range()));
                case SoundEventReferenceImpl ref -> this.soundEvent = CraftSound.bukkitToMinecraftHolder(ref.sound());
                default -> throw new IllegalStateException("Invalid sound event");
            }
            return this;
        }

        @Override
        public Builder description(final net.kyori.adventure.text.Component description) {
            this.description = this.conversions.asVanilla(description);
            return this;
        }

        @Override
        public Builder lengthInSeconds(final float lengthInSeconds) {
            this.lengthInSeconds = lengthInSeconds;
            return this;
        }

        @Override
        public Builder comparatorOutput(@Range(from = 0, to = 15) final int comparatorOutput) {
            this.comparatorOutput = comparatorOutput;
            return this;
        }

        @Override
        public net.minecraft.world.item.JukeboxSong build() {
            return new net.minecraft.world.item.JukeboxSong(soundEvent, description, lengthInSeconds, comparatorOutput);
        }
    }
}
