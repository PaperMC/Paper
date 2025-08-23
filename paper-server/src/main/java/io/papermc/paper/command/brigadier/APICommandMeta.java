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
public record APICommandMeta(@Nullable PluginMeta pluginMeta, @Nullable String description, List<String> aliases, @Nullable String helpCommandNamespace) {

    public APICommandMeta(final @Nullable PluginMeta pluginMeta, final @Nullable String description) {
        this(pluginMeta, description, Collections.emptyList(), null);
    }

    public APICommandMeta {
        aliases = List.copyOf(aliases);
    }

    @Nullable
    public Plugin plugin() {
        return this.pluginMeta == null ? null : Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(this.pluginMeta.getName()));
    }

    public APICommandMeta withAliases(List<String> registeredAliases) {
        return new APICommandMeta(this.pluginMeta, this.description, List.copyOf(registeredAliases), this.helpCommandNamespace);
    }
}
