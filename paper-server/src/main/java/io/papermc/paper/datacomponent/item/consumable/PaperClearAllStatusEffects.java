package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;

public record PaperClearAllStatusEffects(
    ClearAllStatusEffectsConsumeEffect internal
) implements ConsumeEffect.ClearAllStatusEffects, PaperConsumableEffect {

    public static final ConsumeEffect.ClearAllStatusEffects INSTANCE = new PaperClearAllStatusEffects(ClearAllStatusEffectsConsumeEffect.INSTANCE);
}
