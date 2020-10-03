package net.minecraft.server;

import java.util.function.UnaryOperator;

public class DedicatedServerSettings {

    private final java.nio.file.Path path;
    private DedicatedServerProperties properties;

    public DedicatedServerSettings(IRegistryCustom iregistrycustom, java.nio.file.Path java_nio_file_path) {
        this.path = java_nio_file_path;
        this.properties = DedicatedServerProperties.load(iregistrycustom, java_nio_file_path);
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
