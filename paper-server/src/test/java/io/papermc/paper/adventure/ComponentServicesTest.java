package io.papermc.paper.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AllFeatures
public class ComponentServicesTest {

    @Test
    public void testPlainTextComponentSerializerProvider() {
        assertEquals("Paper", PlainTextComponentSerializer.plainText().serialize(Component.translatable("item.minecraft.paper")));
    }

    @Test
    public void testLegacyComponentSerializerProvider() {
        assertEquals("§cPaper", LegacyComponentSerializer.legacySection().serialize(Component.translatable("item.minecraft.paper", NamedTextColor.RED)));
        assertEquals("&cPaper", LegacyComponentSerializer.legacyAmpersand().serialize(Component.translatable("item.minecraft.paper", NamedTextColor.RED)));
    }
}
