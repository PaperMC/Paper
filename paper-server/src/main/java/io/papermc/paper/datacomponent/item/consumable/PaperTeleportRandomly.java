package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;

public record PaperTeleportRandomly(
    net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect impl
) implements ConsumeEffect.TeleportRandomly, PaperConsumableEffect<TeleportRandomlyConsumeEffect> {
    @Override
    public float diameter() {
        return this.impl.diameter();
    }

    @Override
    public net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect getHandle() {
        return this.impl;
    }
}
