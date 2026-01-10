package io.papermc.paper.datacomponent.item.consumable;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import org.bukkit.potion.PotionEffectType;

public record PaperRemoveStatusEffects(
    RemoveStatusEffectsConsumeEffect internal
) implements ConsumeEffect.RemoveStatusEffects, PaperConsumableEffect {

    @Override
    public RegistryKeySet<PotionEffectType> removeEffects() {
        return PaperRegistrySets.convertToApi(RegistryKey.MOB_EFFECT, this.internal.effects());
    }
}
