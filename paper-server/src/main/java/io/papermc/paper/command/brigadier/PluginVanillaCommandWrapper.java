package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// Exists to that /help can show the plugin
public class PluginVanillaCommandWrapper extends VanillaCommandWrapper implements PluginIdentifiableCommand {

    private final Plugin plugin;
    private final List<String> aliases;

    public PluginVanillaCommandWrapper(String name, String description, String usageMessage, List<String> aliases, CommandNode<CommandSourceStack> vanillaCommand, Plugin plugin) {
        super(name, description, usageMessage, aliases, vanillaCommand, null);
        this.plugin = plugin;
        this.aliases = aliases;
    }

    @Override
    public @NotNull List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public @NotNull Command setAliases(@NotNull List<String> aliases) {
        return this;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
