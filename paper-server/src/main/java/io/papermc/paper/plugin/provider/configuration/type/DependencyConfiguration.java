package io.papermc.paper.plugin.provider.configuration.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import static java.util.Objects.requireNonNullElse;

@DefaultQualifier(NonNull.class)
@ConfigSerializable
public record DependencyConfiguration(
    LoadOrder load,
    Boolean required,
    Boolean joinClasspath
) {
    @SuppressWarnings("DataFlowIssue") // incorrect intellij inspections
    public DependencyConfiguration {
        required = requireNonNullElse(required, true);
        joinClasspath = requireNonNullElse(joinClasspath, true);
    }

    @ConfigSerializable
    public enum LoadOrder {
        // dependency will now load BEFORE your plugin
        BEFORE,
        // the dependency will now load AFTER your plugin
        AFTER,
        OMIT
    }
}
