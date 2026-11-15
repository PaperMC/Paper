package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.cow.CowSoundVariant;
import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCowSoundVariantRegistryEntry implements CowSoundVariantRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable Holder<SoundEvent> ambientSound;
    protected @Nullable Holder<SoundEvent> hurtSound;
    protected @Nullable Holder<SoundEvent> deathSound;
    protected @Nullable Holder<SoundEvent> stepSound;

    public PaperCowSoundVariantRegistryEntry(final Conversions conversions, final @Nullable CowSoundVariant internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.ambientSound = internal.ambientSound();
        this.hurtSound = internal.hurtSound();
        this.deathSound = internal.deathSound();
        this.stepSound = internal.stepSound();
    }

    @Override
    public RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound() {
        return PaperRegistryHolders.create(RegistryKey.SOUND_EVENT, asConfigured(this.ambientSound, "ambientSound"), this.conversions);
    }

    @Override
    public RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound() {
        return PaperRegistryHolders.create(RegistryKey.SOUND_EVENT, asConfigured(this.hurtSound, "hurtSound"), this.conversions);
    }

    @Override
    public RegistryHolder<Sound, SoundEventRegistryEntry> deathSound() {
        return PaperRegistryHolders.create(RegistryKey.SOUND_EVENT, asConfigured(this.deathSound, "deathSound"), this.conversions);
    }

    @Override
    public RegistryHolder<Sound, SoundEventRegistryEntry> stepSound() {
        return PaperRegistryHolders.create(RegistryKey.SOUND_EVENT, asConfigured(this.stepSound, "stepSound"), this.conversions);
    }

    public static final class PaperBuilder extends PaperCowSoundVariantRegistryEntry implements CowSoundVariantRegistryEntry.Builder, PaperRegistryBuilder<CowSoundVariant, Cow.SoundVariant> {

        public PaperBuilder(final Conversions conversions, final @Nullable CowSoundVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder ambientSound(final TypedKey<Sound> ambientSound) {
            this.ambientSound = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(ambientSound, "ambientSound")));
            return this;
        }

        @Override
        public Builder ambientSound(final RegistryHolder<Sound, SoundEventRegistryEntry> ambientSound) {
            this.ambientSound = PaperRegistryHolders.convert(asArgument(ambientSound, "ambientSound"), this.conversions);
            return this;
        }

        @Override
        public Builder hurtSound(final TypedKey<Sound> hurtSound) {
            this.hurtSound = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(hurtSound, "hurtSound")));
            return this;
        }

        @Override
        public Builder hurtSound(final RegistryHolder<Sound, SoundEventRegistryEntry> hurtSound) {
            this.hurtSound = PaperRegistryHolders.convert(asArgument(hurtSound, "hurtSound"), this.conversions);
            return this;
        }

        @Override
        public Builder deathSound(final TypedKey<Sound> deathSound) {
            this.deathSound = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(deathSound, "deathSound")));
            return this;
        }

        @Override
        public Builder deathSound(final RegistryHolder<Sound, SoundEventRegistryEntry> deathSound) {
            this.deathSound = PaperRegistryHolders.convert(asArgument(deathSound, "deathSound"), this.conversions);
            return this;
        }

        @Override
        public Builder stepSound(final TypedKey<Sound> stepSound) {
            this.stepSound = this.conversions.getReferenceHolder(PaperRegistries.toNms(asArgument(stepSound, "stepSound")));
            return this;
        }

        @Override
        public Builder stepSound(final RegistryHolder<Sound, SoundEventRegistryEntry> stepSound) {
            this.stepSound = PaperRegistryHolders.convert(asArgument(stepSound, "stepSound"), this.conversions);
            return this;
        }

        @Override
        public CowSoundVariant build() {
            return new CowSoundVariant(
                asConfigured(this.ambientSound, "ambientSound"),
                asConfigured(this.hurtSound, "hurtSound"),
                asConfigured(this.deathSound, "deathSound"),
                asConfigured(this.stepSound, "stepSound")
            );
        }
    }
}
