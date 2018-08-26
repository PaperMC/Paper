package org.bukkit;

import net.minecraft.server.IRegistry;
import net.minecraft.server.MinecraftKey;

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
        for (MinecraftKey effect : IRegistry.SOUND_EVENT.keySet()) {
            assertNotNull(effect + "", Sound.valueOf(effect.getKey().replace('.', '_').toUpperCase(java.util.Locale.ENGLISH)));
        }
    }

    @Test
    public void testCategory() {
        for (SoundCategory category : SoundCategory.values()) {
            assertNotNull(category + "", net.minecraft.server.SoundCategory.valueOf(category.name()));
        }
    }

    @Test
    public void testCategoryReverse() {
        for (net.minecraft.server.SoundCategory category : net.minecraft.server.SoundCategory.values()) {
            assertNotNull(category + "", SoundCategory.valueOf(category.name()));
        }
    }
}
