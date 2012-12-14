package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.bukkit.craftbukkit.CraftSound;
import org.junit.Test;


public class SoundTest {

    @Test
    public void testGetSound() {
        for (Sound sound : Sound.values()) {
            assertThat(sound.name(), CraftSound.getSound(sound), is(not(nullValue())));
        }
    }
}
