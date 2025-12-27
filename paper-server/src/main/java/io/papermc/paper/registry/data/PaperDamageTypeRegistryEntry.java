package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperDamageTypeRegistryEntry implements DamageTypeRegistryEntry {

    protected @Nullable String messageId;
    protected @Nullable DamageScaling scaling;
    protected @Nullable Float exhaustion;
    protected DamageEffects effects = DamageEffects.HURT;
    protected DeathMessageType deathMessageType = DeathMessageType.DEFAULT;

    protected final Conversions conversions;

    public PaperDamageTypeRegistryEntry(
        final Conversions conversions,
        final @Nullable DamageType internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        this.messageId = internal.msgId();
        this.scaling = internal.scaling();
        this.exhaustion = internal.exhaustion();
        this.effects = internal.effects();
        this.deathMessageType = internal.deathMessageType();
    }

    @Override
    public String messageId() {
        return asConfigured(this.messageId, "messageId");
    }

    @Override
    public org.bukkit.damage.DamageScaling damageScaling() {
        return CraftDamageType.damageScalingToBukkit(asConfigured(this.scaling, "scaling"));
    }

    @Override
    public float exhaustion() {
        return asConfigured(this.exhaustion, "exhaustion");
    }

    @Override
    public DamageEffect damageEffect() {
        return CraftDamageEffect.toBukkit(this.effects);
    }

    @Override
    public org.bukkit.damage.DeathMessageType deathMessageType() {
        return CraftDamageType.deathMessageTypeToBukkit(this.deathMessageType);
    }

    public static final class PaperBuilder extends PaperDamageTypeRegistryEntry implements DamageTypeRegistryEntry.Builder, PaperRegistryBuilder<DamageType, org.bukkit.damage.DamageType> {

        public PaperBuilder(final Conversions conversions, final @Nullable DamageType internal) {
            super(conversions, internal);
        }

        @Override
        public Builder messageId(final String messageId) {
            this.messageId = asArgument(messageId, "messageId");
            return this;
        }

        @Override
        public Builder damageScaling(final org.bukkit.damage.DamageScaling scaling) {
            this.scaling = CraftDamageType.damageScalingToNMS(asArgument(scaling, "scaling"));
            return this;
        }

        @Override
        public Builder exhaustion(final float exhaustion) {
            this.exhaustion = exhaustion;
            return this;
        }

        @Override
        public Builder damageEffect(final DamageEffect effect) {
            this.effects = ((CraftDamageEffect) asArgument(effect, "effects")).getHandle();
            return this;
        }

        @Override
        public Builder deathMessageType(final org.bukkit.damage.DeathMessageType deathMessageType) {
            this.deathMessageType = CraftDamageType.deathMessageTypeToNMS(asArgument(deathMessageType, "deathMessageType"));
            return this;
        }

        @Override
        public DamageType build() {
            return new DamageType(
                asConfigured(this.messageId, "messageId"),
                asConfigured(this.scaling, "scaling"),
                asConfigured(this.exhaustion, "exhaustion"),
                this.effects,
                this.deathMessageType
            );
        }
    }
}
