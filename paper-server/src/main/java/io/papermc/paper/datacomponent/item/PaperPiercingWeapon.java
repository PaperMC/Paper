package io.papermc.paper.datacomponent.item;


import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PiercingWeapon;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@org.jspecify.annotations.NullMarked
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

        private float minReach = 0.0F;    // CODEC default
        private float maxReach = 3.0F;    // CODEC default
        private float hitboxMargin = 0.3F; // CODEC default

        private boolean dealsKnockback = true; // CODEC default
        private boolean dismounts = false;     // CODEC default

        private @Nullable Key sound = null;    // optional
        private @Nullable Key hitSound = null; // optional

        @Override
        public PiercingWeapon.Builder minReach(final float minReach) {
            com.google.common.base.Preconditions.checkArgument(minReach >= 0.0F, "minReach must be >= 0");
            this.minReach = minReach;
            return this;
        }

        @Override
        public PiercingWeapon.Builder maxReach(final float maxReach) {
            com.google.common.base.Preconditions.checkArgument(maxReach >= 0.0F, "maxReach must be >= 0");
            this.maxReach = maxReach;
            return this;
        }

        @Override
        public PiercingWeapon.Builder hitboxMargin(final float hitboxMargin) {
            com.google.common.base.Preconditions.checkArgument(hitboxMargin >= 0.0F, "hitboxMargin must be >= 0");
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