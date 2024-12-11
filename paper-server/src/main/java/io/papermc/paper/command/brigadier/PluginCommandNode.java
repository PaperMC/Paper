package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PluginCommandNode extends LiteralCommandNode<CommandSourceStack> {

    private final PluginMeta plugin;
    private final String description;
    private List<String> aliases = Collections.emptyList();

    public PluginCommandNode(final @NotNull String literal, final @NotNull PluginMeta plugin, final @NotNull LiteralCommandNode<CommandSourceStack> rootLiteral, final @Nullable String description) {
        super(
            literal, rootLiteral.getCommand(), rootLiteral.getRequirement(),
            rootLiteral.getRedirect(), rootLiteral.getRedirectModifier(), rootLiteral.isFork()
        );
        this.plugin = plugin;
        this.description = description;

        for (CommandNode<CommandSourceStack> argument : rootLiteral.getChildren()) {
            this.addChild(argument);
        }
    }

    @NotNull
    public Plugin getPlugin() {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(this.plugin.getName()));
    }

    @NotNull
    public String getDescription() {
        return this.description;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}
