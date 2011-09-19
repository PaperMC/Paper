package org.bukkit.configuration;

public class MemorySectionTest extends ConfigurationSectionTest {
    @Override
    public ConfigurationSection getConfigurationSection() {
        return new MemoryConfiguration().createSection("section");
    }
}
