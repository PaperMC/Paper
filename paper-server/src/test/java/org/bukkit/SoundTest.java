package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
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
        for (MinecraftKey effect : BuiltInRegistries.SOUND_EVENT.keySet()) {
            assertNotNull(effect + "", Sound.valueOf(effect.getPath().replace('.', '_').toUpperCase(java.util.Locale.ENGLISH)));
        }
    }

    @Test
    public void testCategory() {
        for (SoundCategory category : SoundCategory.values()) {
            assertNotNull(category + "", net.minecraft.sounds.SoundCategory.valueOf(category.name()));
        }
    }

    @Test
    public void testCategoryReverse() {
        for (net.minecraft.sounds.SoundCategory category : net.minecraft.sounds.SoundCategory.values()) {
            assertNotNull(category + "", SoundCategory.valueOf(category.name()));
        }
    }
}
