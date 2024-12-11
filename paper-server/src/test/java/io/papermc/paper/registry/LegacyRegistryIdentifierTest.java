package io.papermc.paper.registry;

import org.bukkit.GameEvent;
import org.bukkit.MusicInstrument;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

@Deprecated
@VanillaFeature
class LegacyRegistryIdentifierTest {

    @Test
    void testSeveralConversions() {
        assertSame(RegistryKey.GAME_EVENT, PaperRegistryAccess.byType(GameEvent.class));
        assertSame(RegistryKey.TRIM_PATTERN, PaperRegistryAccess.byType(TrimPattern.class));
        assertSame(RegistryKey.INSTRUMENT, PaperRegistryAccess.byType(MusicInstrument.class));
    }
}
