package io.papermc.paper.plugin;

import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Normal
public class PluginNamingTest {
    private static final String TEST_NAME = "Test_Plugin";
    private static final String TEST_VERSION = "1.0";

    private final PaperPluginMeta pluginMeta;

    public PluginNamingTest() {
        this.pluginMeta = new PaperPluginMeta();
        this.pluginMeta.setName(TEST_NAME);
        this.pluginMeta.setVersion(TEST_VERSION);
    }

    @Test
    public void testName() {
        Assertions.assertEquals(TEST_NAME, this.pluginMeta.getName());
    }

    @Test
    public void testDisplayName() {
        Assertions.assertEquals(TEST_NAME + " v" + TEST_VERSION, this.pluginMeta.getDisplayName());
    }
}
