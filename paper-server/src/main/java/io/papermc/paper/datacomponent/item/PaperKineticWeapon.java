package io.papermc.paper.datacomponent.item;

import java.util.Optional;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.util.Handleable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

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
    public @Nullable Sound sound() {
        return this.impl.sound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
    }

    @Override
    public @Nullable Sound hitSound() {
        return this.impl.hitSound().map(CraftSound::minecraftHolderToBukkit).orElse(null);
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

        private @Nullable Sound sound = null;
        private @Nullable Sound hitSound = null;

        private float damageMultiplier = 1;
        private float forwardMovement = 0.0F;

        @Override
        public KineticWeapon.Builder contactCooldownTicks(final @NonNegative int ticks) {
            this.contactCooldownTicks = requireNonNegative(ticks, "contactCooldownTicks");
            return this;
        }

        @Override
        public KineticWeapon.Builder delayTicks(final @NonNegative int ticks) {
            this.delayTicks = requireNonNegative(ticks, "delayTicks");
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
        public KineticWeapon.Builder sound(final @Nullable Sound sound) {
            this.sound = sound;
            return this;
        }

        @Override
        public KineticWeapon.Builder hitSound(final @Nullable Sound sound) {
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
                    Optional.ofNullable(this.sound).map(CraftSound::bukkitToMinecraftHolder),
                    Optional.ofNullable(this.hitSound).map(CraftSound::bukkitToMinecraftHolder)
                )
            );
        }
    }
}
