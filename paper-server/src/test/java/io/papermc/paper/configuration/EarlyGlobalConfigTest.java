package io.papermc.paper.configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Normal
public class EarlyGlobalConfigTest {

    @TempDir
    Path dir;

    @Test
    public void onlyEarlyFieldsAreLoaded() throws Exception {
        final Path file = this.dir.resolve(PaperConfigurations.GLOBAL_CONFIG_FILE_NAME);
        Files.writeString(file, """
            %s: %d
            misc:
              world-container: worlds
              netty-threads: 13
            advancements:
              disabled:
              - minecraft:story/root
            proxies:
              velocity:
                enabled: true
            """.formatted(Configuration.VERSION_FIELD, GlobalConfiguration.CURRENT_VERSION));

        final GlobalConfiguration config = PaperConfigurationsInitializer.loadEarlyGlobalConfig(file);
        assertEquals("worlds", config.misc.worldContainer);
        assertEquals("update", config.misc.updateFolder);
        assertEquals(Set.of("minecraft:story/root"), config.advancements.disabled);

        assertEquals(4, config.misc.nettyThreads, "non-early field next to early ones must keep its default");
        assertNotEquals("13", System.getProperty("io.netty.eventLoopThreads"), "post-processors must not run");
        assertNull(config.proxies, "sections without early fields must not be created");
        assertNull(config.chunkSystem, "sections without early fields must not be created");
    }

    @Test
    public void oldVersionLoadsWithoutServer() throws Exception {
        final Path file = this.dir.resolve(PaperConfigurations.GLOBAL_CONFIG_FILE_NAME);
        Files.writeString(file, """
            %s: 28
            logging:
              log-player-ip-addresses: false
            misc:
              world-container: worlds
            """.formatted(Configuration.VERSION_FIELD));

        final GlobalConfiguration config = PaperConfigurationsInitializer.loadEarlyGlobalConfig(file);
        assertEquals("worlds", config.misc.worldContainer);
    }

    @Test
    public void defaultsWithoutFile() throws Exception {
        final GlobalConfiguration config = PaperConfigurationsInitializer.loadEarlyGlobalConfig(this.dir.resolve("missing.yml"));
        assertEquals(".", config.misc.worldContainer);
        assertEquals("update", config.misc.updateFolder);
        assertEquals(Set.of("minecraft:story/disabled"), config.advancements.disabled);
        assertNull(config.proxies);
    }
}
