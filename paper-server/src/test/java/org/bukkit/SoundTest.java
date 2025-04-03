package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;

import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
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
