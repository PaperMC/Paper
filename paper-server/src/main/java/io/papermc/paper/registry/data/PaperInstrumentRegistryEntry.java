package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Instrument;
import org.bukkit.MusicInstrument;
import org.bukkit.Sound;
import org.checkerframework.checker.index.qual.Positive;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asArgumentMinExclusive;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperInstrumentRegistryEntry implements InstrumentRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable Holder<SoundEvent> soundEvent;
    protected @Nullable Float useDuration;
    protected @Nullable Float range;
    protected @Nullable Component description;

    public PaperInstrumentRegistryEntry(final Conversions conversions, final @Nullable Instrument internal) {
        this.conversions = conversions;

        if (internal == null) {
            return;
        }
        this.soundEvent = internal.soundEvent();
        this.useDuration = internal.useDuration();
        this.range = internal.range();
        this.description = internal.description();
    }

    @Override
    public RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent() {
        final Holder<SoundEvent> current = asConfigured(this.soundEvent, "soundEvent");
        return PaperRegistryHolders.create(current, e -> new PaperSoundEventRegistryEntry(this.conversions, e));
    }

    @Override
    public @Positive float duration() {
        return asConfigured(this.useDuration, "useDuration");
    }

    @Override
    public @Positive float range() {
        return asConfigured(this.range, "range");
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return this.conversions.asAdventure(asConfigured(this.description, "description"));
    }

    public static final class PaperBuilder extends PaperInstrumentRegistryEntry implements Builder, PaperRegistryBuilder<Instrument, MusicInstrument> {

        public PaperBuilder(final Conversions conversions, final @Nullable Instrument internal) {
            super(conversions, internal);
        }

        @Override
        public Builder soundEvent(final TypedKey<Sound> soundEvent) {
            this.soundEvent = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(soundEvent, "soundEvent")));
            return this;
        }

        @Override
        public Builder soundEvent(final Consumer<RegistryBuilderFactory<Sound, ? extends SoundEventRegistryEntry.Builder>> soundEvent) {
            this.soundEvent = this.conversions.createHolderFromBuilder(RegistryKey.SOUND_EVENT, asArgument(soundEvent, "soundEvent"));
            return this;
        }

        @Override
        public Builder soundEvent(final RegistryHolder<Sound, SoundEventRegistryEntry> soundEvent) {
            this.soundEvent = PaperRegistryHolders.convert(soundEvent, this.conversions);
            return this;
        }

        @Override
        public Builder duration(final @Positive float duration) {
            this.useDuration = asArgumentMinExclusive(duration, "useDuration", 0);
            return this;
        }

        @Override
        public Builder range(final @Positive float range) {
            this.range = asArgumentMinExclusive(range, "range", 0);
            return this;
        }

        @Override
        public Builder description(final net.kyori.adventure.text.Component description) {
            this.description = this.conversions.asVanilla(asArgument(description, "description"));
            return this;
        }

        @Override
        public Instrument build() {
            return new Instrument(
                asConfigured(this.soundEvent, "soundEvent"),
                this.duration(),
                this.range(),
                asConfigured(this.description, "description")
            );
        }
    }
}
