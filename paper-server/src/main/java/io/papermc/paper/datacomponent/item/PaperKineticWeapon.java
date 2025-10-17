package io.papermc.paper.datacomponent.item;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@org.jspecify.annotations.NullMarked
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
    public int contactCooldownTicks() {
        return this.impl.contactCooldownTicks();
    }

    @Override
    public int delayTicks() {
        return this.impl.delayTicks();
    }

    @Override
    public @Nullable KineticWeapon.Condition dismountConditions() {
        return this.impl.dismountConditions().map(PaperKineticWeaponCondition::new)
                .orElse(null);
    }

    @Override
    public @Nullable KineticWeapon.Condition knockbackConditions() {
        return this.impl.knockbackConditions().map(PaperKineticWeaponCondition::new)
                .orElse(null);
    }

    @Override
    public @Nullable KineticWeapon.Condition damageConditions() {
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

        private float minReach = 0.0F;
        private float maxReach = 3.0F;
        private float hitboxMargin = 0.3F;
        private int contactCooldownTicks = 10;
        private int delayTicks = 0;

        private @Nullable net.minecraft.world.item.component.KineticWeapon.Condition dismountConditions;
        private @Nullable net.minecraft.world.item.component.KineticWeapon.Condition knockbackConditions;
        private @Nullable net.minecraft.world.item.component.KineticWeapon.Condition damageConditions;

        private @Nullable Key sound = null;
        private @Nullable Key hitSound = null;

        private float damageMultiplier = 1;
        private float forwardMovement = 0.0F;

        @Override
        public KineticWeapon.Builder minReach(final float minReach) {
            com.google.common.base.Preconditions.checkArgument(minReach >= 0.0F, "minReach must be >= 0");
            this.minReach = minReach;
            return this;
        }

        @Override
        public KineticWeapon.Builder maxReach(final float maxReach) {
            com.google.common.base.Preconditions.checkArgument(maxReach >= 0.0F, "maxReach must be >= 0");
            this.maxReach = maxReach;
            return this;
        }

        @Override
        public KineticWeapon.Builder hitboxMargin(final float hitboxMargin) {
            com.google.common.base.Preconditions.checkArgument(hitboxMargin >= 0.0F, "hitboxMargin must be >= 0");
            this.hitboxMargin = hitboxMargin;
            return this;
        }

        @Override
        public KineticWeapon.Builder contactCooldownTicks(final int ticks) {
            com.google.common.base.Preconditions.checkArgument(ticks >= 0, "contactCooldownTicks must be >= 0");
            this.contactCooldownTicks = ticks;
            return this;
        }

        @Override
        public KineticWeapon.Builder delayTicks(final int ticks) {
            com.google.common.base.Preconditions.checkArgument(ticks >= 0, "delayTicks must be >= 0");
            this.delayTicks = ticks;
            return this;
        }

        @Override
        public KineticWeapon.Builder dismountConditions(@Nullable final KineticWeapon.Condition condition) {
            this.dismountConditions = toNms(condition);
            return this;
        }

        @Override
        public KineticWeapon.Builder knockbackConditions(@Nullable final KineticWeapon.Condition condition) {
            this.knockbackConditions = toNms(condition);
            return this;
        }

        @Override
        public KineticWeapon.Builder damageConditions(@Nullable final KineticWeapon.Condition condition) {
            this.damageConditions = toNms(condition);
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
                            this.minReach,
                            this.maxReach,
                            this.hitboxMargin,
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