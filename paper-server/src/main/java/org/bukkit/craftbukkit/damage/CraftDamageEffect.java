package org.bukkit.craftbukkit.damage;

import net.minecraft.world.damagesource.DamageEffects;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.damage.DamageEffect;

// Paper start
@Deprecated
public class CraftDamageEffect  {
// Paper end

    private final DamageEffects damageEffects;

    public CraftDamageEffect(DamageEffects damageEffects) {
        this.damageEffects = damageEffects;
    }

    public DamageEffects getHandle() {
        return this.damageEffects;
    }

    public Sound getSound() {
        return CraftSound.minecraftToBukkit(this.getHandle().sound()); // Paper
    }

    public static DamageEffect getById(String id) {
        for (DamageEffects damageEffects : DamageEffects.values()) {
            if (damageEffects.getSerializedName().equalsIgnoreCase(id)) {
                return CraftDamageEffect.toBukkit(damageEffects);
            }
        }
        return null;
    }

    public static DamageEffect toBukkit(DamageEffects damageEffects) {
        return CraftDamageType.damageEffectToBukkit(damageEffects); // Paper
    }
}
