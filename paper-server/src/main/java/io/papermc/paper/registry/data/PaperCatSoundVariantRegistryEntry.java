package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.feline.CatSoundVariant;
import org.bukkit.Sound;
import org.bukkit.entity.Cat;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCatSoundVariantRegistryEntry implements CatSoundVariantRegistryEntry {

    protected final Conversions conversions;
    protected CatSoundVariant.@Nullable CatSoundSet adultSounds;
    protected CatSoundVariant.@Nullable CatSoundSet babySounds;

    public PaperCatSoundVariantRegistryEntry(final Conversions conversions, final @Nullable CatSoundVariant internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.adultSounds = internal.adultSounds();
        this.babySounds = internal.babySounds();
    }

    @Override
    public SoundSet adultSounds() {
        return this.toApi(asConfigured(this.adultSounds, "adultSounds"));
    }

    @Override
    public SoundSet babySounds() {
        return this.toApi(asConfigured(this.babySounds, "babySounds"));
    }

    private SoundSet toApi(final CatSoundVariant.CatSoundSet soundSet) {
        return SoundSet.of(
            this.toApi(soundSet.ambientSound()),
            this.toApi(soundSet.strayAmbientSound()),
            this.toApi(soundSet.hissSound()),
            this.toApi(soundSet.hurtSound()),
            this.toApi(soundSet.deathSound()),
            this.toApi(soundSet.eatSound()),
            this.toApi(soundSet.begForFoodSound()),
            this.toApi(soundSet.purrSound()),
            this.toApi(soundSet.purreowSound())
        );
    }

    private RegistryHolder<Sound, SoundEventRegistryEntry> toApi(final Holder<SoundEvent> sound) {
        return PaperRegistryHolders.create(RegistryKey.SOUND_EVENT, sound, this.conversions);
    }

    public static final class PaperBuilder extends PaperCatSoundVariantRegistryEntry implements CatSoundVariantRegistryEntry.Builder, PaperRegistryBuilder<CatSoundVariant, Cat.SoundVariant> {

        public PaperBuilder(final Conversions conversions, final @Nullable CatSoundVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder adultSounds(final SoundSet adultSounds) {
            this.adultSounds = this.toVanilla(asArgument(adultSounds, "adultSounds"));
            return this;
        }

        @Override
        public Builder babySounds(final SoundSet babySounds) {
            this.babySounds = this.toVanilla(asArgument(babySounds, "babySounds"));
            return this;
        }

        private CatSoundVariant.CatSoundSet toVanilla(final SoundSet soundSet) {
            return new CatSoundVariant.CatSoundSet(
                this.toVanilla(asArgument(soundSet.ambientSound(), "ambientSound")),
                this.toVanilla(asArgument(soundSet.strayAmbientSound(), "strayAmbientSound")),
                this.toVanilla(asArgument(soundSet.hissSound(), "hissSound")),
                this.toVanilla(asArgument(soundSet.hurtSound(), "hurtSound")),
                this.toVanilla(asArgument(soundSet.deathSound(), "deathSound")),
                this.toVanilla(asArgument(soundSet.eatSound(), "eatSound")),
                this.toVanilla(asArgument(soundSet.begForFoodSound(), "begForFoodSound")),
                this.toVanilla(asArgument(soundSet.purrSound(), "purrSound")),
                this.toVanilla(asArgument(soundSet.purreowSound(), "purreowSound"))
            );
        }

        private Holder<SoundEvent> toVanilla(final RegistryHolder<Sound, SoundEventRegistryEntry> sound) {
            return PaperRegistryHolders.convert(sound, this.conversions);
        }

        @Override
        public CatSoundVariant build() {
            return new CatSoundVariant(
                asConfigured(this.adultSounds, "adultSounds"),
                asConfigured(this.babySounds, "babySounds")
            );
        }
    }
}
