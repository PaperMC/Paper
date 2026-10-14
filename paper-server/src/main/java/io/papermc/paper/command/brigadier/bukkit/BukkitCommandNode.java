package io.papermc.paper.command.brigadier.bukkit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.CommandSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.event.server.TabCompleteEvent;

public class BukkitCommandNode extends LiteralCommandNode<CommandSourceStack> {

    private final Command command;

    private BukkitCommandNode(String literal, Command command, BukkitBrigCommand bukkitBrigCommand) {
        super(
            literal, bukkitBrigCommand, source -> {
                // If the source is null, assume it's true.
                // As bukkit doesn't really map the command sender well in all cases
                if (source instanceof net.minecraft.commands.CommandSourceStack commandSourceStack && commandSourceStack.source == CommandSource.NULL) {
                    return true;
                } else {
                    return command.testPermissionSilent(source.getSender());
                }
            },
            null, null, false
        );
        this.command = command;
    }

    public static BukkitCommandNode of(String name, Command command) {
        BukkitBrigCommand bukkitBrigCommand = new BukkitBrigCommand(command, name);
        BukkitCommandNode commandNode = new BukkitCommandNode(name, command, bukkitBrigCommand);
        commandNode.addChild(
            RequiredArgumentBuilder.<CommandSourceStack, String>argument("args", StringArgumentType.greedyString())
                .suggests(new BukkitBrigSuggestionProvider(command, name))
                .executes(bukkitBrigCommand).build()
        );

        return commandNode;
    }

    public Command getBukkitCommand() {
        return this.command;
    }

    public static class BukkitBrigCommand implements com.mojang.brigadier.Command<CommandSourceStack> {

        private final org.bukkit.command.Command command;
        private final String literal;

        BukkitBrigCommand(org.bukkit.command.Command command, String literal) {
            this.command = command;
            this.literal = literal;
        }

        @Override
        public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            CommandSender sender = context.getSource().getSender();

            String content = context.getRange().get(context.getInput());
            String[] args = org.apache.commons.lang3.StringUtils.split(content, ' '); // fix adjacent spaces (from console/plugins) causing empty array elements

            // Note: we don't return the result of target.execute as thats success / failure, we return handled (true) or not handled (false)
            this.command.execute(sender, this.literal, Arrays.copyOfRange(args, 1, args.length));

            // return true as command was handled
            return 1;
        }
    }

    static class BukkitBrigSuggestionProvider implements SuggestionProvider<CommandSourceStack> {

        private final org.bukkit.command.Command command;
        private final String literal;

        BukkitBrigSuggestionProvider(org.bukkit.command.Command command, String literal) {
            this.command = command;
            this.literal = literal;
        }

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            // Paper start
            org.bukkit.command.CommandSender sender = context.getSource().getSender();
            String[] args = builder.getRemaining().split(" ", -1); // We need the command included -- Set limit to -1, allow for trailing spaces

            List<String> results = null;
            Location pos = context.getSource().getLocation();
            try {
                results = this.command.tabComplete(sender, this.literal, args, pos.clone());
            } catch (CommandException ex) {
                sender.sendMessage(Component.text("An internal error occurred while attempting to tab-complete this command", NamedTextColor.RED));
                Bukkit.getServer().getLogger().log(Level.SEVERE, "Exception when " + sender.getName() + " attempted to tab complete " + builder.getRemaining(), ex);
            }

            if (sender instanceof final Player player) {
                TabCompleteEvent tabEvent = new org.bukkit.event.server.TabCompleteEvent(player, builder.getInput(), results != null ? results : new ArrayList<>(), true, pos); // Paper - AsyncTabCompleteEvent
                if (!tabEvent.callEvent()) {
                    results = null;
                } else {
                    results = tabEvent.getCompletions();
                }
            }
            // Paper end
            if (results == null) {
                return builder.buildFuture();
            }

            // Defaults to sub nodes, but we have just one giant args node, so offset accordingly
            builder = builder.createOffset(builder.getInput().lastIndexOf(' ') + 1);

            for (String s : results) {
                builder.suggest(s);
            }

            return builder.buildFuture();
        }
    }

}
