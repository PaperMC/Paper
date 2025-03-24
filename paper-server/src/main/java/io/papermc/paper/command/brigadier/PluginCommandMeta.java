package io.papermc.paper.command.brigadier;

import io.papermc.paper.plugin.configuration.PluginMeta;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record PluginCommandMeta(PluginMeta pluginMeta, @Nullable String description, List<String> aliases) {

    public PluginCommandMeta(final PluginMeta pluginMeta, final @Nullable String description) {
        this(pluginMeta, description, Collections.emptyList());
    }

    public PluginCommandMeta {
        aliases = List.copyOf(aliases);
    }

    public Plugin plugin() {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(this.pluginMeta.getName()));
    }
}
