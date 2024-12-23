package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.damagesource.*;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperDamageTypeRegistryEntry implements DamageTypeRegistryEntry {
    protected @Nullable String messageId;
    protected @Nullable Float exhaustion;
    protected @Nullable DamageScaling damageScaling;
    protected @Nullable DamageEffects damageEffects;
    protected @Nullable DeathMessageType deathMessageType;

    protected final Conversions conversions;

    public PaperDamageTypeRegistryEntry(
            final Conversions conversions,
            final @Nullable DamageType internal
    ) {
        this.conversions = conversions;
        if (internal == null) return;

        this.messageId = internal.msgId();
        this.exhaustion = internal.exhaustion();
        this.damageScaling = internal.scaling();
        this.damageEffects = internal.effects();
        this.deathMessageType = internal.deathMessageType();
    }

    @Override
    public String messageId() {
        return asConfigured(messageId, "messsageId");
    }

    @Override
    public Float exhaustion() {
        return asConfigured(exhaustion, "exhaustion");
    }

    @Override
    public org.bukkit.damage.DamageScaling damageScaling() {
        return CraftDamageType.damageScalingToBukkit(asConfigured(this.damageScaling, "damageScaling"));
    }

    @Override
    public @Nullable DamageEffect damageEffect() {
        return damageEffects == null ? null : CraftDamageType.damageEffectToBukkit(damageEffects);
    }

    @Override
    public org.bukkit.damage.@Nullable DeathMessageType deathMessageType() {
        return deathMessageType == null ? null : CraftDamageType.deathMessageTypeToBukkit(deathMessageType);
    }

    public static final class PaperBuilder extends PaperDamageTypeRegistryEntry implements DamageTypeRegistryEntry.Builder, PaperRegistryBuilder<DamageType, org.bukkit.damage.DamageType> {

        public PaperBuilder(final Conversions conversions, final @Nullable DamageType internal) {
            super(conversions, internal);
        }

        @Override
        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public Builder exhaustion(Float exhaustion) {
            this.exhaustion = exhaustion;
            return this;
        }

        @Override
        public Builder damageScaling(org.bukkit.damage.DamageScaling scaling) {
            this.damageScaling = CraftDamageType.damageScalingToNMS(scaling);
            return this;
        }

        @Override
        public Builder damageEffect(@Nullable DamageEffect effect) {
            this.damageEffects = effect == null ? null : CraftDamageType.damageEffectToNMS(effect);
            return this;
        }

        @Override
        public Builder deathMessageType(org.bukkit.damage.@Nullable DeathMessageType deathMessageType) {
            this.deathMessageType = deathMessageType == null ? null : CraftDamageType.deathMessageTypeToNMS(deathMessageType);
            return this;
        }

        @Override
        public DamageType build() {
            if (this.damageEffects != null && this.deathMessageType != null) {
                return new DamageType(
                        asConfigured(this.messageId, "messsageId"),
                        asConfigured(this.damageScaling, "scaling"),
                        asConfigured(this.exhaustion, "exhaustion"),
                        this.damageEffects,
                        this.deathMessageType);
            } else if (this.damageEffects != null) {
                return new DamageType(
                        asConfigured(this.messageId, "messsageId"),
                        asConfigured(this.damageScaling, "scaling"),
                        asConfigured(this.exhaustion, "exhaustion"),
                        this.damageEffects);
            } else {
                return new DamageType(
                        asConfigured(this.messageId, "messsageId"),
                        asConfigured(this.damageScaling, "scaling"),
                        asConfigured(this.exhaustion, "exhaustion")
                );
            }
        }
    }
}
