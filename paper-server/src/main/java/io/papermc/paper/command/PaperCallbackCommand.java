package io.papermc.paper.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.adventure.providers.ClickCallbackProviderImpl;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperCallbackCommand {
    public static final String DESCRIPTION = "ClickEvent callback";

    public static LiteralCommandNode<CommandSourceStack> create() {
        final PaperCallbackCommand command = new PaperCallbackCommand();
        return Commands.literal("callback")
            .then(Commands.argument("uuid", ArgumentTypes.uuid())
                .executes(command::execute))
            .build();
    }

    private int execute(final CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        final UUID id = context.getArgument("uuid", UUID.class);
        ClickCallbackProviderImpl.CALLBACK_MANAGER.runCallback(sender, id);
        return Command.SINGLE_SUCCESS;
    }
}
