package io.papermc.paper.command;

import io.papermc.paper.adventure.providers.ClickCallbackProviderImpl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import java.util.UUID;

@DefaultQualifier(NonNull.class)
public class CallbackCommand extends Command {

    protected CallbackCommand(final String name) {
        super(name);
        this.description = "ClickEvent callback";
        this.usageMessage = "/callback <uuid>";
    }

    @Override
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        if (args.length != 1) {
            return false;
        }

        final UUID id;
        try {
            id = UUID.fromString(args[0]);
        } catch (final IllegalArgumentException ignored) {
            return false;
        }

        ClickCallbackProviderImpl.CALLBACK_MANAGER.runCallback(sender, id);
        return true;
    }
}
