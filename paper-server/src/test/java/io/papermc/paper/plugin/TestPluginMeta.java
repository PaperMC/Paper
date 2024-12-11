package io.papermc.paper.plugin;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestPluginMeta implements PluginMeta {

    private final String identifier;
    private List<String> hardDependencies = List.of();
    private List<String> softDependencies = List.of();
    private List<String> loadBefore = List.of();

    public TestPluginMeta(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public @NotNull String getName() {
        return this.identifier;
    }

    @Override
    public @NotNull String getMainClass() {
        return "null";
    }

    @Override
    public @NotNull PluginLoadOrder getLoadOrder() {
        return PluginLoadOrder.POSTWORLD;
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String getLoggerPrefix() {
        return this.identifier;
    }

    public void setHardDependencies(List<String> hardDependencies) {
        this.hardDependencies = hardDependencies;
    }

    @Override
    public @NotNull List<String> getPluginDependencies() {
        return this.hardDependencies;
    }

    public void setSoftDependencies(List<String> softDependencies) {
        this.softDependencies = softDependencies;
    }

    @Override
    public @NotNull List<String> getPluginSoftDependencies() {
        return this.softDependencies;
    }

    public void setLoadBefore(List<String> loadBefore) {
        this.loadBefore = loadBefore;
    }

    @Override
    public @NotNull List<String> getLoadBeforePlugins() {
        return this.loadBefore;
    }

    @Override
    public @NotNull List<String> getProvidedPlugins() {
        return List.of();
    }

    @Override
    public @NotNull List<String> getAuthors() {
        return List.of();
    }

    @Override
    public @NotNull List<String> getContributors() {
        return List.of();
    }

    @Override
    public @Nullable String getDescription() {
        return "null";
    }

    @Override
    public @Nullable String getWebsite() {
        return "null";
    }

    @Override
    public @NotNull List<Permission> getPermissions() {
        return List.of();
    }

    @Override
    public @NotNull PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }

    @Override
    public @NotNull String getAPIVersion() {
        return "null";
    }
}
