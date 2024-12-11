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
        assertEquals("Done", PlainTextComponentSerializer.plainText().serialize(Component.translatable("narrator.loading.done")));
    }

    @Test
    public void testLegacyComponentSerializerProvider() {
        assertEquals("§cDone", LegacyComponentSerializer.legacySection().serialize(Component.translatable("narrator.loading.done", NamedTextColor.RED)));
        assertEquals("&cDone", LegacyComponentSerializer.legacyAmpersand().serialize(Component.translatable("narrator.loading.done", NamedTextColor.RED)));
    }
}
