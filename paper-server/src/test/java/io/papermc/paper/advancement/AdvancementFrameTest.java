package io.papermc.paper.advancement;

import net.kyori.adventure.text.format.NamedTextColor;
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
            final NamedTextColor expectedColor = NamedTextColor.NAMES.value(net.minecraft.network.chat.TextColor.fromLegacyFormat(advancementType.getChatColor()).toString());
            final String expectedTranslationKey = ((TranslatableContents) advancementType.getDisplayName().getContents()).getKey();
            final var frame = PaperAdvancementDisplay.asPaperFrame(advancementType);
            assertEquals(expectedTranslationKey, frame.translationKey(), "The translation keys should be the same");
            assertEquals(expectedColor, frame.color(), "The frame colors should be the same");
            assertEquals(advancementType.getSerializedName(), AdvancementDisplay.Frame.NAMES.key(frame));
        }
    }
}
