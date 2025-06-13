package io.papermc.paper.registry.data.sound;

import net.kyori.adventure.key.Key;
import java.util.Optional;

public record SoundEventImpl(Key soundId, Optional<Float> range) implements SoundEvent {
}
