package io.papermc.paper.datacomponent.item;

import java.util.Optional;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
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
    public boolean dealsKnockback() {
        return this.impl.dealsKnockback();
    }

    @Override
    public boolean dismounts() {
        return this.impl.dismounts();
    }

    @Override
    public @Nullable Sound sound() {
        return this.impl.sound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public @Nullable Sound hitSound() {
        return this.impl.hitSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    static final class BuilderImpl implements PiercingWeapon.Builder {

        private boolean dealsKnockback = true;
        private boolean dismounts = false;

        private @Nullable Sound sound = null;
        private @Nullable Sound hitSound = null;

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
        public PiercingWeapon.Builder sound(final @Nullable Sound sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public PiercingWeapon.Builder hitSound(final @Nullable Sound sound) {
            this.hitSound = sound;
            return this;
        }

        @Override
        public PiercingWeapon build() {
            return new PaperPiercingWeapon(
                new net.minecraft.world.item.component.PiercingWeapon(
                    this.dealsKnockback,
                    this.dismounts,
                    Optional.ofNullable(this.sound).map(CraftSound::bukkitToMinecraftHolder),
                    Optional.ofNullable(this.hitSound).map(CraftSound::bukkitToMinecraftHolder)
                )
            );
        }
    }
}
