package io.papermc.paper.datacomponent.item.consumable;

import net.minecraft.world.item.consume_effects.PlaySoundConsumeEffect;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftSound;

public record PaperPlaySound(
    PlaySoundConsumeEffect internal
) implements ConsumeEffect.PlaySound, PaperConsumableEffect {

    @Override
    public Sound sound() {
        return CraftSound.minecraftHolderToBukkit(this.internal.sound());
    }
}
