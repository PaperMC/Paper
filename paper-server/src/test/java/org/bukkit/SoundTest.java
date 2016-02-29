package org.bukkit;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.SoundEffect;

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

    @Test
    public void testReverse() {
        for (MinecraftKey effect : SoundEffect.a.keySet()) {
            assertNotNull(effect + "", Sound.valueOf(effect.a().replace('.', '_').toUpperCase()));
        }
    }
}
