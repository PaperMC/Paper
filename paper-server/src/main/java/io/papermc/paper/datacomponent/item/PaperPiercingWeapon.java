package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.Nullable;

public record PaperPiercingWeapon(
    net.minecraft.world.item.component.PiercingWeapon impl
) implements PiercingWeapon, Handleable<net.minecraft.world.item.component.PiercingWeapon> {

    @Override
    public net.minecraft.world.item.component.PiercingWeapon getHandle() {
        return this.impl;
    }

    @Override
    public float minReach() {
        return this.impl.minReach();
    }

    @Override
    public float maxReach() {
        return this.impl.maxReach();
    }

    @Override
    public float hitboxMargin() {
        return this.impl.hitboxMargin();
    }

    @Override
    public boolean dealsKnockback() {
        return this.impl.dealsKnockback();
    }

    @Override
    public boolean dismounts() {
        return this.impl.dismounts();
    }

    @Override
    public @Nullable Key sound() {
        return this.impl.sound()
            .map(Holder::value)
            .map(SoundEvent::location)
            .map(PaperAdventure::asAdventure)
            .orElse(null);
    }

    @Override
    public @Nullable Key hitSound() {
        return this.impl.hitSound()
            .map(Holder::value)
            .map(SoundEvent::location)
            .map(PaperAdventure::asAdventure)
            .orElse(null);
    }

    static final class BuilderImpl implements PiercingWeapon.Builder {

        private float minReach = 0.0F;
        private float maxReach = 3.0F;
        private float hitboxMargin = 0.3F;

        private boolean dealsKnockback = true;
        private boolean dismounts = false;

        private @Nullable Key sound = null;
        private @Nullable Key hitSound = null;

        @Override
        public PiercingWeapon.Builder minReach(final float minReach) {
            Preconditions.checkArgument(minReach >= 0.0F && minReach <= 128.0F, "minReach must be in range [0,128] was %s", minReach);
            this.minReach = minReach;
            return this;
        }

        @Override
        public PiercingWeapon.Builder maxReach(final float maxReach) {
            Preconditions.checkArgument(maxReach >= 0.0F && maxReach <= 128.0F, "maxReach must be in range [0,128] was %s", maxReach);
            this.maxReach = maxReach;
            return this;
        }

        @Override
        public PiercingWeapon.Builder hitboxMargin(final float hitboxMargin) {
            Preconditions.checkArgument(hitboxMargin >= 0.0F && hitboxMargin <= 1.0F, "hitboxMargin must be in range [0,1] was %s", hitboxMargin);
            this.hitboxMargin = hitboxMargin;
            return this;
        }

        @Override
        public PiercingWeapon.Builder dealsKnockback(final boolean dealsKnockback) {
            this.dealsKnockback = dealsKnockback;
            return this;
        }

        @Override
        public PiercingWeapon.Builder dismounts(final boolean dismounts) {
            this.dismounts = dismounts;
            return this;
        }

        @Override
        public PiercingWeapon.Builder sound(final @Nullable Key sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public PiercingWeapon.Builder hitSound(final @Nullable Key sound) {
            this.hitSound = sound;
            return this;
        }

        @Override
        public PiercingWeapon build() {
            return new PaperPiercingWeapon(
                new net.minecraft.world.item.component.PiercingWeapon(
                    this.minReach,
                    this.maxReach,
                    this.hitboxMargin,
                    this.dealsKnockback,
                    this.dismounts,
                    Optional.ofNullable(this.sound).map(PaperAdventure::resolveSound),
                    Optional.ofNullable(this.hitSound).map(PaperAdventure::resolveSound)
                )
            );
        }
    }
}
