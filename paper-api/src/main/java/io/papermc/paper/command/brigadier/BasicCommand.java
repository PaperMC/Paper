package io.papermc.paper.command.brigadier;

import java.util.Collection;
import java.util.Collections;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Implementing this interface allows for easily creating "Bukkit-style" {@code String[] args} commands.
 * The implementation handles converting the command to a representation compatible with Brigadier on registration, usually in the form of {@literal /commandlabel <greedy_string>}.
 */
@FunctionalInterface
public interface BasicCommand {

    /**
     * Executes the command with the given {@link CommandSourceStack} and arguments.
     *
     * @param commandSourceStack the commandSourceStack of the command
     * @param args the arguments of the command ignoring repeated spaces
     */
    @ApiStatus.OverrideOnly
    void execute(CommandSourceStack commandSourceStack, String[] args);

    /**
     * Suggests possible completions for the given command {@link CommandSourceStack} and arguments.
     *
     * @param commandSourceStack the commandSourceStack of the command
     * @param args the arguments of the command including repeated spaces
     * @return a collection of suggestions
     */
    @ApiStatus.OverrideOnly
    default Collection<String> suggest(final CommandSourceStack commandSourceStack, final String[] args) {
        return Collections.emptyList();
    }

    /**
     * Checks whether a command sender can receive and run the root command.
     *
     * @param sender the command sender trying to execute the command
     * @return whether the command sender fulfills the root command requirement
     * @see #permission()
     */
    @ApiStatus.OverrideOnly
    default boolean canUse(final CommandSender sender) {
        final String permission = this.permission();
        return permission == null || sender.hasPermission(permission);
    }

    /**
     * Returns the permission for the root command used in {@link #canUse(CommandSender)} by default.
     *
     * @return the permission for the root command used in {@link #canUse(CommandSender)}
     */
    @ApiStatus.OverrideOnly
    default @Nullable String permission() {
        return null;
    }
}
