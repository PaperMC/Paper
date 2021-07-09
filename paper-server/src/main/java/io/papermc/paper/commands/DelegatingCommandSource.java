package io.papermc.paper.commands;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class DelegatingCommandSource implements CommandSource {

    private final CommandSource delegate;

    public DelegatingCommandSource(CommandSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendSystemMessage(Component message) {
        delegate.sendSystemMessage(message);
    }

    @Override
    public boolean acceptsSuccess() {
        return delegate.acceptsSuccess();
    }

    @Override
    public boolean acceptsFailure() {
        return delegate.acceptsFailure();
    }

    @Override
    public boolean shouldInformAdmins() {
        return delegate.shouldInformAdmins();
    }

    @Override
    public CommandSender getBukkitSender(CommandSourceStack wrapper) {
        return delegate.getBukkitSender(wrapper);
    }
}
