package io.papermc.paper.datacomponent.item.consumable;

public record PaperClearAllStatusEffects(
    net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect impl
) implements ConsumeEffect.ClearAllStatusEffects, PaperConsumableEffectImpl<net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect> {

    @Override
    public net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect getHandle() {
        return this.impl;
    }
}
