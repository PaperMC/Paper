package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CraftPotionBrewer implements PotionBrewer {

    @Override
    public Collection<PotionEffect> getEffects(PotionType type, boolean upgraded, boolean extended) {
        Preconditions.checkArgument(!type.getKey().getKey().startsWith("strong_"), "Strong potion type cannot be used directly, got %s", type.getKey());
        Preconditions.checkArgument(!type.getKey().getKey().startsWith("long_"), "Extended potion type cannot be used directly, got %s", type.getKey());

        return CraftPotionUtil.fromBukkit(new PotionData(type, upgraded, extended)).getPotionEffects();
    }

    @Override
    public Collection<PotionEffect> getEffectsFromDamage(int damage) {
        return new ArrayList<PotionEffect>();
    }

    @Override
    public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier) {
        return new PotionEffect(potion, potion.isInstant() ? 1 : (int) (duration * potion.getDurationModifier()), amplifier);
    }
}
