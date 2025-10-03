package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;

public record PaperTeleportRandomly(
    TeleportRandomlyConsumeEffect internal
) implements ConsumeEffect.TeleportRandomly, PaperConsumableEffect {

    @Override
    public float diameter() {
        return this.internal.diameter();
    }
}
