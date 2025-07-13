package org.bukkit.craftbukkit.damage;

import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageScaling;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DeathMessageType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftDamageType extends HolderableBase<net.minecraft.world.damagesource.DamageType> implements DamageType {

    public CraftDamageType(final Holder<net.minecraft.world.damagesource.DamageType> holder) {
        super(holder);
    }

    @Override
    public String getTranslationKey() {
        return this.getHandle().msgId();
    }

    @Override
    public DamageScaling getDamageScaling() {
        return CraftDamageType.damageScalingToBukkit(this.getHandle().scaling());
    }

    @Override
    public DamageEffect getDamageEffect() {
        return CraftDamageEffect.toBukkit(this.getHandle().effects());
    }

    @Override
    public DeathMessageType getDeathMessageType() {
        return CraftDamageType.deathMessageTypeToBukkit(this.getHandle().deathMessageType());
    }

    @Override
    public float getExhaustion() {
        return this.getHandle().exhaustion();
    }

    public static DeathMessageType deathMessageTypeToBukkit(net.minecraft.world.damagesource.DeathMessageType deathMessageType) {
        return switch (deathMessageType) {
            case DEFAULT -> DeathMessageType.DEFAULT;
            case FALL_VARIANTS -> DeathMessageType.FALL_VARIANTS;
            case INTENTIONAL_GAME_DESIGN -> DeathMessageType.INTENTIONAL_GAME_DESIGN;
        };
    }

    public static net.minecraft.world.damagesource.DeathMessageType deathMessageTypeToNMS(DeathMessageType deathMessageType) {
        return switch (deathMessageType) {
            case DEFAULT -> net.minecraft.world.damagesource.DeathMessageType.DEFAULT;
            case FALL_VARIANTS -> net.minecraft.world.damagesource.DeathMessageType.FALL_VARIANTS;
            case INTENTIONAL_GAME_DESIGN -> net.minecraft.world.damagesource.DeathMessageType.INTENTIONAL_GAME_DESIGN;
        };
    }

    public static DamageScaling damageScalingToBukkit(net.minecraft.world.damagesource.DamageScaling damageScaling) {
        return switch (damageScaling) {
            case ALWAYS -> DamageScaling.ALWAYS;
            case WHEN_CAUSED_BY_LIVING_NON_PLAYER -> DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
            case NEVER -> DamageScaling.NEVER;
        };
    }

    public static net.minecraft.world.damagesource.DamageScaling damageScalingToNMS(DamageScaling damageScaling) {
        return switch (damageScaling) {
            case ALWAYS -> net.minecraft.world.damagesource.DamageScaling.ALWAYS;
            case WHEN_CAUSED_BY_LIVING_NON_PLAYER -> net.minecraft.world.damagesource.DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
            case NEVER -> net.minecraft.world.damagesource.DamageScaling.NEVER;
        };
    }

    public static DamageType minecraftHolderToBukkit(Holder<net.minecraft.world.damagesource.DamageType> minecraftHolder) {
        return CraftRegistry.minecraftHolderToBukkit(minecraftHolder, Registries.DAMAGE_TYPE);
    }

    public static Holder<net.minecraft.world.damagesource.DamageType> bukkitToMinecraftHolder(DamageType bukkitDamageType) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkitDamageType);
    }

    public static net.minecraft.world.damagesource.DamageType bukkitToMinecraft(DamageType bukkitDamageType) {
        return CraftRegistry.bukkitToMinecraft(bukkitDamageType);
    }

    public static DamageType minecraftToBukkit(net.minecraft.world.damagesource.DamageType minecraftDamageType) {
        return CraftRegistry.minecraftToBukkit(minecraftDamageType, Registries.DAMAGE_TYPE);
    }
}
