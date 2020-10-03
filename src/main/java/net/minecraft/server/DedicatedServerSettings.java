package net.minecraft.server;

import java.util.function.UnaryOperator;
// CraftBukkit start
import java.io.File;
import joptsimple.OptionSet;
// CraftBukkit end

public class DedicatedServerSettings {

    private final java.nio.file.Path path;
    private DedicatedServerProperties properties;

    // CraftBukkit start
    public DedicatedServerSettings(IRegistryCustom iregistrycustom, OptionSet optionset) {
        this.path = ((File) optionset.valueOf("config")).toPath();
        this.properties = DedicatedServerProperties.load(iregistrycustom, path, optionset);
        // CraftBukkit end
    }

    public DedicatedServerProperties getProperties() {
        return this.properties;
    }

    public void save() {
        this.properties.savePropertiesFile(this.path);
    }

    public DedicatedServerSettings setProperty(UnaryOperator<DedicatedServerProperties> unaryoperator) {
        (this.properties = (DedicatedServerProperties) unaryoperator.apply(this.properties)).savePropertiesFile(this.path);
        return this;
    }
}
