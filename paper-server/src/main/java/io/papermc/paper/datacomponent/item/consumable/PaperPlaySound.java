package io.papermc.paper.datacomponent.item.consumable;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.key.Key;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;

public record PaperPlaySound(
    PlaySoundConsumeEffect internal
) implements ConsumeEffect.PlaySound, PaperConsumableEffect {

    @Override
    public Key sound() {
        return PaperAdventure.asAdventure(this.internal.sound().value().location());
    }
}
