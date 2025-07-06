package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.redstone.Redstone;
import org.bukkit.Sound;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asArgumentMinExclusive;
import static io.papermc.paper.registry.data.util.Checks.asArgumentRange;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperJukeboxSongRegistryEntry implements JukeboxSongRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable Holder<SoundEvent> soundEvent;
    protected @Nullable Component description;
    protected @Nullable Float lengthInSeconds;
    protected OptionalInt comparatorOutput = OptionalInt.empty();

    public PaperJukeboxSongRegistryEntry(final Conversions conversions, final @Nullable JukeboxSong internal) {
        this.conversions = conversions;

        if (internal == null) {
            return;
        }
        this.soundEvent = internal.soundEvent();
        this.description = internal.description();
        this.lengthInSeconds = internal.lengthInSeconds();
        this.comparatorOutput = OptionalInt.of(internal.comparatorOutput());
    }

    @Override
    public RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent() {
        final Holder<SoundEvent> current = asConfigured(this.soundEvent, "soundEvent");
        return PaperRegistryHolders.create(current, e -> new PaperSoundEventRegistryEntry(this.conversions, e));
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return this.conversions.asAdventure(asConfigured(this.description, "description"));
    }

    @Override
    public float lengthInSeconds() {
        return asConfigured(this.lengthInSeconds, "lengthInSeconds");
    }

    @Override
    public int comparatorOutput() {
        return asConfigured(this.comparatorOutput, "comparatorOutput");
    }

    public static final class PaperBuilder extends PaperJukeboxSongRegistryEntry implements JukeboxSongRegistryEntry.Builder, PaperRegistryBuilder<JukeboxSong, org.bukkit.JukeboxSong> {

        public PaperBuilder(final Conversions conversions, final @Nullable JukeboxSong internal) {
            super(conversions, internal);
        }

        @Override
        public JukeboxSongRegistryEntry.Builder soundEvent(final TypedKey<Sound> soundEvent) {
            this.soundEvent = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(soundEvent, "soundEvent")));
            return this;
        }

        @Override
        public JukeboxSongRegistryEntry.Builder soundEvent(final Consumer<RegistryBuilderFactory<Sound, ? extends SoundEventRegistryEntry.Builder>> soundEvent) {
            this.soundEvent = this.conversions.createHolderFromBuilder(RegistryKey.SOUND_EVENT, asArgument(soundEvent, "soundEvent"));
            return this;
        }

        @Override
        public Builder soundEvent(final RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent) {
            this.soundEvent = PaperRegistryHolders.convert(soundEvent, this.conversions);
            return this;
        }

        @Override
        public JukeboxSongRegistryEntry.Builder description(final net.kyori.adventure.text.Component description) {
            this.description = this.conversions.asVanilla(asArgument(description, "description"));
            return this;
        }

        @Override
        public JukeboxSongRegistryEntry.Builder lengthInSeconds(final @Positive float lengthInSeconds) {
            this.lengthInSeconds = asArgumentMinExclusive(lengthInSeconds, "lengthInSeconds", 0);
            return this;
        }

        @Override
        public JukeboxSongRegistryEntry.Builder comparatorOutput(final @Range(from = 0, to = 15) int comparatorOutput) {
            this.comparatorOutput = OptionalInt.of(asArgumentRange(comparatorOutput, "comparatorOutput", Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX));
            return this;
        }

        @Override
        public JukeboxSong build() {
            return new JukeboxSong(
                asConfigured(this.soundEvent, "soundEvent"),
                asConfigured(this.description, "description"),
                this.lengthInSeconds(),
                this.comparatorOutput()
            );
        }
    }
}
