package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import net.minecraft.server.IRegistry;
import net.minecraft.server.MinecraftKey;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class SoundTest extends AbstractTestingBase {

    @Test
    public void testGetSound() {
        for (Sound sound : Sound.values()) {
            assertThat(sound.name(), CraftSound.getSoundEffect(sound), is(not(nullValue())));
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
