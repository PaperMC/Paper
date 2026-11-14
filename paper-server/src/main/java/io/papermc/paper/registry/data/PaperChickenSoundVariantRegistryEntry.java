package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.holder.PaperRegistryHolders;
import io.papermc.paper.registry.holder.RegistryHolder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperChickenSoundVariantRegistryEntry implements ChickenSoundVariantRegistryEntry {

    protected final Conversions conversions;
    protected ChickenSoundVariant.@Nullable ChickenSoundSet adultSounds;
    protected ChickenSoundVariant.@Nullable ChickenSoundSet babySounds;

    public PaperChickenSoundVariantRegistryEntry(final Conversions conversions, final @Nullable ChickenSoundVariant internal) {
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

    private SoundSet toApi(final ChickenSoundVariant.ChickenSoundSet soundSet) {
        return SoundSet.of(
            this.toApi(soundSet.ambientSound()),
            this.toApi(soundSet.hurtSound()),
            this.toApi(soundSet.deathSound()),
            this.toApi(soundSet.stepSound())
        );
    }

    private RegistryHolder<Sound, SoundEventRegistryEntry> toApi(final Holder<SoundEvent> sound) {
        return PaperRegistryHolders.create(RegistryKey.SOUND_EVENT, sound, this.conversions);
    }

    public static final class PaperBuilder extends PaperChickenSoundVariantRegistryEntry implements ChickenSoundVariantRegistryEntry.Builder, PaperRegistryBuilder<ChickenSoundVariant, Chicken.SoundVariant> {

        public PaperBuilder(final Conversions conversions, final @Nullable ChickenSoundVariant internal) {
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

        private ChickenSoundVariant.ChickenSoundSet toVanilla(final SoundSet soundSet) {
            return new ChickenSoundVariant.ChickenSoundSet(
                this.toVanilla(asArgument(soundSet.ambientSound(), "ambientSound")),
                this.toVanilla(asArgument(soundSet.hurtSound(), "hurtSound")),
                this.toVanilla(asArgument(soundSet.deathSound(), "deathSound")),
                this.toVanilla(asArgument(soundSet.stepSound(), "stepSound"))
            );
        }

        private Holder<SoundEvent> toVanilla(final RegistryHolder<Sound, SoundEventRegistryEntry> sound) {
            return PaperRegistryHolders.convert(sound, this.conversions);
        }

        @Override
        public ChickenSoundVariant build() {
            return new ChickenSoundVariant(
                asConfigured(this.adultSounds, "adultSounds"),
                asConfigured(this.babySounds, "babySounds")
            );
        }
    }
}
