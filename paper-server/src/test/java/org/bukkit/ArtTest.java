package org.bukkit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.minecraft.server.EnumArt;

import org.junit.Test;

import com.google.common.collect.Lists;

public class ArtTest {
    private static final int UNIT_MULTIPLIER = 16;

    @Test
    public void verifyMapping() {
        List<Art> arts = Lists.newArrayList(Art.values());

        for (EnumArt enumArt : EnumArt.values()) {
            int id = enumArt.ordinal();
            String name = enumArt.B;
            int width = enumArt.C / UNIT_MULTIPLIER;
            int height = enumArt.D / UNIT_MULTIPLIER;

            Art subject = Art.getById(id);

            String message = String.format("org.bukkit.Art is missing id: %d named: '%s'", id - Achievement.STATISTIC_OFFSET, name);
            assertNotNull(message, subject);

            assertThat(Art.getByName(name), is(subject));
            assertThat("Art." + subject + "'s width", subject.getBlockWidth(), is(width));
            assertThat("Art." + subject + "'s height", subject.getBlockHeight(), is(height));

            arts.remove(subject);
        }

        assertThat("org.bukkit.Art has too many arts", arts, hasSize(0));
    }
}
