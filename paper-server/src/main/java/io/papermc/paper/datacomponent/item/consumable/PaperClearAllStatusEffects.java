package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;

public record PaperClearAllStatusEffects(
    net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect impl
) implements ConsumeEffect.ClearAllStatusEffects, PaperConsumableEffect<ClearAllStatusEffectsConsumeEffect> {

    @Override
    public net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect getHandle() {
        return this.impl;
    }
}
