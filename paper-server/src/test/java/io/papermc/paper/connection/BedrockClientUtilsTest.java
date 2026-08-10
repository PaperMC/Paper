package io.papermc.paper.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import net.minecraft.world.inventory.ContainerInput;
import org.junit.jupiter.api.Test;

public class BedrockClientUtilsTest {

    @Test
    public void testRemapBedrockContainerButtonForPickup() {
        assertEquals(1, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.PICKUP, 0, true));
        assertEquals(0, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.PICKUP, 1, true));
        assertEquals(2, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.PICKUP, 2, true));
    }

    @Test
    public void testRemapBedrockContainerButtonForQuickMoveAndThrow() {
        assertEquals(1, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.QUICK_MOVE, 0, true));
        assertEquals(0, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.THROW, 1, true));
        assertEquals(2, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.QUICK_MOVE, 2, true));
    }

    @Test
    public void testRemapNoBedrockKeepsButton() {
        assertEquals(0, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.PICKUP, 0, false));
        assertEquals(1, BedrockClientUtils.remapBedrockContainerButton(ContainerInput.QUICK_MOVE, 1, false));
    }

    @Test
    public void testIsJapaneseLocale() {
        assertTrue(BedrockClientUtils.isJapaneseLocale("ja_jp"));
        assertTrue(BedrockClientUtils.isJapaneseLocale("ja"));
        assertFalse(BedrockClientUtils.isJapaneseLocale("en_us"));
    }

    @Test
    public void testParsePlayerLocaleFallsBackToJapanese() {
        assertEquals("ja", BedrockClientUtils.parsePlayerLocale("ja_JP").getLanguage());
        assertEquals("ja", BedrockClientUtils.parsePlayerLocale("ja").getLanguage());
        assertEquals(Locale.US, BedrockClientUtils.parsePlayerLocale("not-a-locale"));
    }
}
