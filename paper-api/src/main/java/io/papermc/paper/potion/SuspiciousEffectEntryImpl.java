package io.papermc.paper.potion;

import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
record SuspiciousEffectEntryImpl(PotionEffectType effect, int duration) implements SuspiciousEffectEntry {
}
