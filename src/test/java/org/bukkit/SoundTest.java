package org.bukkit;

import static org.junit.Assert.assertNotNull;

import org.bukkit.craftbukkit.CraftSound;
import org.junit.Test;


public class SoundTest {

    @Test
    public void testGetSound() {
        for (Sound sound : Sound.values()) {
            assertNotNull(sound.name(), CraftSound.getSound(sound));
        }
    }
}
