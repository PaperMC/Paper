package org.bukkit.craftbukkit.damage;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageScaling;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DeathMessageType;

public class CraftDamageType implements DamageType, Handleable<net.minecraft.world.damagesource.DamageType> {

    private final NamespacedKey key;
    private final net.minecraft.world.damagesource.DamageType damageType;

    public CraftDamageType(NamespacedKey key, net.minecraft.world.damagesource.DamageType damageType) {
        this.key = key;
        this.damageType = damageType;
    }

    @Override
    public net.minecraft.world.damagesource.DamageType getHandle() {
        return this.damageType;
    }

    @Override
    public String getTranslationKey() {
        return this.getHandle().msgId();
    }

    @Override
    public DamageScaling getDamageScaling() {
        return damageScalingToBukkit(this.getHandle().scaling());
    }

    @Override
    public DamageEffect getDamageEffect() {
        return CraftDamageEffect.toBukkit(this.getHandle().effects());
    }

    @Override
    public DeathMessageType getDeathMessageType() {
        return deathMessageTypeToBukkit(this.getHandle().deathMessageType());
    }

    @Override
    public float getExhaustion() {
        return this.getHandle().exhaustion();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return "CraftDamageType{" + "key=" + this.getKey() + ",damageScaling=" + this.getDamageScaling() + ",damageEffect=" + this.getDamageEffect() + ",deathMessageType=" + this.getDeathMessageType() + ",exhaustion=" + this.getExhaustion() + "}";
    }

    public static DeathMessageType deathMessageTypeToBukkit(net.minecraft.world.damagesource.DeathMessageType deathMessageType) {
        return switch (deathMessageType) {
            case DEFAULT -> DeathMessageType.DEFAULT;
            case FALL_VARIANTS -> DeathMessageType.FALL_VARIANTS;
            case INTENTIONAL_GAME_DESIGN -> DeathMessageType.INTENTIONAL_GAME_DESIGN;
            default -> throw new IllegalArgumentException("NMS DeathMessageType." + deathMessageType + " cannot be converted to a Bukkit DeathMessageType.");
        };
    }

    public static net.minecraft.world.damagesource.DeathMessageType deathMessageTypeToNMS(DeathMessageType deathMessageType) {
        return switch (deathMessageType) {
            case DEFAULT -> net.minecraft.world.damagesource.DeathMessageType.DEFAULT;
            case FALL_VARIANTS -> net.minecraft.world.damagesource.DeathMessageType.FALL_VARIANTS;
            case INTENTIONAL_GAME_DESIGN -> net.minecraft.world.damagesource.DeathMessageType.INTENTIONAL_GAME_DESIGN;
            default -> throw new IllegalArgumentException("Bukkit DeathMessageType." + deathMessageType + " cannot be converted to a NMS DeathMessageType.");
        };
    }

    public static DamageScaling damageScalingToBukkit(net.minecraft.world.damagesource.DamageScaling damageScaling) {
        return switch (damageScaling) {
            case ALWAYS -> DamageScaling.ALWAYS;
            case WHEN_CAUSED_BY_LIVING_NON_PLAYER -> DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
            case NEVER -> DamageScaling.NEVER;
            default -> throw new IllegalArgumentException("NMS DamageScaling." + damageScaling + " cannot be converted to a Bukkit DamageScaling");
        };
    }

    public static net.minecraft.world.damagesource.DamageScaling damageScalingToNMS(DamageScaling damageScaling) {
        return switch (damageScaling) {
            case ALWAYS -> net.minecraft.world.damagesource.DamageScaling.ALWAYS;
            case WHEN_CAUSED_BY_LIVING_NON_PLAYER -> net.minecraft.world.damagesource.DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER;
            case NEVER -> net.minecraft.world.damagesource.DamageScaling.NEVER;
            default -> throw new IllegalArgumentException("Bukkit DamageScaling." + damageScaling + " cannot be converted to a NMS DamageScaling");
        };
    }

    public static DamageType minecraftHolderToBukkit(Holder<net.minecraft.world.damagesource.DamageType> minecraftHolder) {
        return minecraftToBukkit(minecraftHolder.value());
    }

    public static Holder<net.minecraft.world.damagesource.DamageType> bukkitToMinecraftHolder(DamageType bukkitDamageType) {
        Preconditions.checkArgument(bukkitDamageType != null);

        IRegistry<net.minecraft.world.damagesource.DamageType> registry = CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkitDamageType)) instanceof Holder.c<net.minecraft.world.damagesource.DamageType> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkitDamageType
                + ", this can happen if a plugin creates its own damage type with out properly registering it.");
    }

    public static net.minecraft.world.damagesource.DamageType bukkitToMinecraft(DamageType bukkitDamageType) {
        return CraftRegistry.bukkitToMinecraft(bukkitDamageType);
    }

    public static DamageType minecraftToBukkit(net.minecraft.world.damagesource.DamageType minecraftDamageType) {
        return CraftRegistry.minecraftToBukkit(minecraftDamageType, Registries.DAMAGE_TYPE, Registry.DAMAGE_TYPE);
    }
}
