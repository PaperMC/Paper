package org.bukkit.craftbukkit.potion;

import net.minecraft.server.MobEffectList;

import org.bukkit.potion.PotionEffectType;

public class CraftPotionEffectType extends PotionEffectType {
    private final MobEffectList handle;

    public CraftPotionEffectType(MobEffectList handle) {
        super(handle.id);
        this.handle = handle;
    }

    @Override
    public double getDurationModifier() {
        return handle.getDurationModifier();
    }

    public MobEffectList getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        switch (handle.id) {
        case 1:
            return "SPEED";
        case 2:
            return "SLOW";
        case 3:
            return "FAST_DIGGING";
        case 4:
            return "SLOW_DIGGING";
        case 5:
            return "INCREASE_DAMAGE";
        case 6:
            return "HEAL";
        case 7:
            return "HARM";
        case 8:
            return "JUMP";
        case 9:
            return "CONFUSION";
        case 10:
            return "REGENERATION";
        case 11:
            return "DAMAGE_RESISTANCE";
        case 12:
            return "FIRE_RESISTANCE";
        case 13:
            return "WATER_BREATHING";
        case 14:
            return "INVISIBILITY";
        case 15:
            return "BLINDNESS";
        case 16:
            return "NIGHT_VISION";
        case 17:
            return "HUNGER";
        case 18:
            return "WEAKNESS";
        case 19:
            return "POISON";
        case 20:
            return "WITHER";
        case 21:
            return "HEALTH_BOOST";
        case 22:
            return "ABSORPTION";
        case 23:
            return "SATURATION";
        default:
            return "UNKNOWN_EFFECT_TYPE_" + handle.id;
        }
    }

    @Override
    public boolean isInstant() {
        return handle.isInstant();
    }
}
