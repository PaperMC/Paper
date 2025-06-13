package io.papermc.paper.registry.data.sound;

import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;

public interface SoundEvent {

    static SoundEvent soundEvent(Sound sound) {
        return new SoundEventReferenceImpl(sound);
    }

    static SoundEvent soundEvent(Key soundId) {
        return new SoundEventImpl(soundId, Optional.empty());
    }

    static SoundEvent soundEvent(Key soundId, float range) {
        return new SoundEventImpl(soundId, Optional.of(range));
    }
}
