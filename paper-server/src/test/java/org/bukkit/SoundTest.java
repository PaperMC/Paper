package org.bukkit;

import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Normal
public class SoundTest {

    @Test
    public void testCategory() {
        for (SoundCategory category : SoundCategory.values()) {
            assertNotNull(net.minecraft.sounds.SoundSource.valueOf(category.name()), category + "");
        }
    }

    @Test
    public void testCategoryReverse() {
        for (net.minecraft.sounds.SoundSource category : net.minecraft.sounds.SoundSource.values()) {
            assertNotNull(SoundCategory.valueOf(category.name()), category + "");
        }
    }
}
