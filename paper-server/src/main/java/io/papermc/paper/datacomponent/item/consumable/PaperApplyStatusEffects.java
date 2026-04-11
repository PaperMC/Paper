package io.papermc.paper.datacomponent.item.consumable;

import java.util.List;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.potion.PotionEffect;

import static io.papermc.paper.util.MCUtil.transformUnmodifiable;

public record PaperApplyStatusEffects(
    ApplyStatusEffectsConsumeEffect internal
) implements ConsumeEffect.ApplyStatusEffects, PaperConsumableEffect {

    @Override
    public List<PotionEffect> effects() {
        return transformUnmodifiable(this.internal().effects(), CraftPotionUtil::toBukkit);
    }

    @Override
    public float probability() {
        return this.internal.probability();
    }
}
