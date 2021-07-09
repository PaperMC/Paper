package io.papermc.paper.advancement;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
public class AdvancementFrameTest {

    @Test
    public void test() {
        for (final AdvancementType advancementType : AdvancementType.values()) {
            final TextColor expectedColor = PaperAdventure.asAdventure(advancementType.getChatColor());
            final String expectedTranslationKey = ((TranslatableContents) advancementType.getDisplayName().getContents()).getKey();
            final var frame = PaperAdvancementDisplay.asPaperFrame(advancementType);
            assertEquals(expectedTranslationKey, frame.translationKey(), "The translation keys should be the same");
            assertEquals(expectedColor, frame.color(), "The frame colors should be the same");
            assertEquals(advancementType.getSerializedName(), AdvancementDisplay.Frame.NAMES.key(frame));
        }
    }
}
