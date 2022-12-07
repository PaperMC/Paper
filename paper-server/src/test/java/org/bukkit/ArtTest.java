package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class ArtTest extends AbstractTestingBase {
    private static final int UNIT_MULTIPLIER = 16;

    @Test
    public void verifyMapping() {
        List<Art> arts = Lists.newArrayList(Art.values());

        for (ResourceKey<PaintingVariant> key : BuiltInRegistries.PAINTING_VARIANT.registryKeySet()) {
            Holder<PaintingVariant> enumArt = BuiltInRegistries.PAINTING_VARIANT.getHolderOrThrow(key);
            String name = key.location().getPath();
            int width = enumArt.value().getWidth() / UNIT_MULTIPLIER;
            int height = enumArt.value().getHeight() / UNIT_MULTIPLIER;

            Art subject = CraftArt.NotchToBukkit(enumArt);

            String message = String.format("org.bukkit.Art is missing '%s'", name);
            assertNotNull(message, subject);

            assertThat(Art.getByName(name), is(subject));
            assertThat("Art." + subject + "'s width", subject.getBlockWidth(), is(width));
            assertThat("Art." + subject + "'s height", subject.getBlockHeight(), is(height));

            arts.remove(subject);
        }

        assertThat("org.bukkit.Art has too many arts", arts, is(Collections.EMPTY_LIST));
    }

    @Test
    public void testCraftArtToNotch() {
        Map<Holder<PaintingVariant>, Art> cache = new HashMap<>();
        for (Art art : Art.values()) {
            Holder<PaintingVariant> enumArt = CraftArt.BukkitToNotch(art);
            assertNotNull(art.name(), enumArt);
            assertThat(art.name(), cache.put(enumArt, art), is(nullValue()));
        }
    }

    @Test
    public void testCraftArtToBukkit() {
        Map<Art, Holder<PaintingVariant>> cache = new EnumMap(Art.class);
        for (Holder<PaintingVariant> enumArt : BuiltInRegistries.PAINTING_VARIANT.asHolderIdMap()) {
            Art art = CraftArt.NotchToBukkit(enumArt);
            assertNotNull("Could not CraftArt.NotchToBukkit " + enumArt, art);
            assertThat("Duplicate artwork " + enumArt, cache.put(art, enumArt), is(nullValue()));
        }
    }
}
