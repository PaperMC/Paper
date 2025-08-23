package io.papermc.paper.datacomponent.item.consumable;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.key.Key;
import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;

public record PaperPlaySound(
    PlaySoundConsumeEffect impl
) implements ConsumeEffect.PlaySound, PaperConsumableEffect<PlaySoundConsumeEffect> {

    @Override
    public Key sound() {
        return PaperAdventure.asAdventure(this.impl.sound().value().location());
    }

    @Override
    public PlaySoundConsumeEffect getHandle() {
        return this.impl;
    }
}
