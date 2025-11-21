package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.Nullable;

public record PaperKineticWeapon(
    net.minecraft.world.item.component.KineticWeapon impl
) implements KineticWeapon, Handleable<net.minecraft.world.item.component.KineticWeapon> {

    private static net.minecraft.world.item.component.KineticWeapon.Condition toNms(final KineticWeapon.Condition api) {
        if (api instanceof PaperKineticWeaponCondition(
            net.minecraft.world.item.component.KineticWeapon.Condition cond
        )) {
            return cond;
        } else {
            throw new UnsupportedOperationException("Could not decode " + api);
        }
    }

    @Override
    public net.minecraft.world.item.component.KineticWeapon getHandle() {
        return this.impl;
    }

    @Override
    public int contactCooldownTicks() {
        return this.impl.contactCooldownTicks();
    }

    @Override
    public int delayTicks() {
        return this.impl.delayTicks();
    }

    @Override
    public KineticWeapon.@Nullable Condition dismountConditions() {
        return this.impl.dismountConditions().map(PaperKineticWeaponCondition::new)
            .orElse(null);
    }

    @Override
    public KineticWeapon.@Nullable Condition knockbackConditions() {
        return this.impl.knockbackConditions().map(PaperKineticWeaponCondition::new)
            .orElse(null);
    }

    @Override
    public KineticWeapon.@Nullable Condition damageConditions() {
        return this.impl.damageConditions().map(PaperKineticWeaponCondition::new)
            .orElse(null);
    }

    @Override
    public float forwardMovement() {
        return this.impl.forwardMovement();
    }

    @Override
    public float damageMultiplier() {
        return this.impl.damageMultiplier();
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

    public record PaperKineticWeaponCondition(
            net.minecraft.world.item.component.KineticWeapon.Condition impl
    ) implements KineticWeapon.Condition, Handleable<net.minecraft.world.item.component.KineticWeapon.Condition> {

        @Override
        public net.minecraft.world.item.component.KineticWeapon.Condition getHandle() {
            return this.impl;
        }

        @Override
        public int maxDurationTicks() {
            return this.impl.maxDurationTicks();
        }

        @Override
        public float minSpeed() {
            return this.impl.minSpeed();
        }

        @Override
        public float minRelativeSpeed() {
            return this.impl.minRelativeSpeed();
        }
    }

    static final class BuilderImpl implements KineticWeapon.Builder {

        private int contactCooldownTicks = 10;
        private int delayTicks = 0;

        private net.minecraft.world.item.component.KineticWeapon.@Nullable Condition dismountConditions;
        private net.minecraft.world.item.component.KineticWeapon.@Nullable Condition knockbackConditions;
        private net.minecraft.world.item.component.KineticWeapon.@Nullable Condition damageConditions;

        private @Nullable Key sound = null;
        private @Nullable Key hitSound = null;

        private float damageMultiplier = 1;
        private float forwardMovement = 0.0F;

        @Override
        public KineticWeapon.Builder contactCooldownTicks(final int ticks) {
            Preconditions.checkArgument(ticks >= 0, "contactCooldownTicks must be non-negative");
            this.contactCooldownTicks = ticks;
            return this;
        }

        @Override
        public KineticWeapon.Builder delayTicks(final int ticks) {
            Preconditions.checkArgument(ticks >= 0, "delayTicks must be non-negative");
            this.delayTicks = ticks;
            return this;
        }

        @Override
        public KineticWeapon.Builder dismountConditions(final KineticWeapon.@Nullable Condition condition) {
            this.dismountConditions = condition == null ? null : toNms(condition);
            return this;
        }

        @Override
        public KineticWeapon.Builder knockbackConditions(final KineticWeapon.@Nullable Condition condition) {
            this.knockbackConditions = condition == null ? null : toNms(condition);
            return this;
        }

        @Override
        public KineticWeapon.Builder damageConditions(final KineticWeapon.@Nullable Condition condition) {
            this.damageConditions = condition == null ? null : toNms(condition);
            return this;
        }

        @Override
        public Builder damageMultiplier(float damageMultiplier) {
            this.damageMultiplier = damageMultiplier;
            return this;
        }

        @Override
        public Builder forwardMovement(float forwardMovement) {
            this.forwardMovement = forwardMovement;
            return this;
        }

        @Override
        public KineticWeapon.Builder sound(final @Nullable Key sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public KineticWeapon.Builder hitSound(final @Nullable Key sound) {
            this.hitSound = sound;
            return this;
        }

        @Override
        public KineticWeapon build() {
            return new PaperKineticWeapon(
                new net.minecraft.world.item.component.KineticWeapon(
                    this.contactCooldownTicks,
                    this.delayTicks,
                    Optional.ofNullable(this.dismountConditions),
                    Optional.ofNullable(this.knockbackConditions),
                    Optional.ofNullable(this.damageConditions),
                    this.forwardMovement,
                    this.damageMultiplier,
                    Optional.ofNullable(this.sound).map(PaperAdventure::resolveSound),
                    Optional.ofNullable(this.hitSound).map(PaperAdventure::resolveSound)
                )
            );
        }
    }
}
