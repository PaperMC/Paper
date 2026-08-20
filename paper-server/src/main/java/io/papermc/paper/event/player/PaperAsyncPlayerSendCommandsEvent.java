package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.tree.RootCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperAsyncPlayerSendCommandsEvent<S extends CommandSourceStack> extends CraftPlayerEvent implements AsyncPlayerSendCommandsEvent<S> {

    private final RootCommandNode<S> node;
    private final boolean hasFiredAsync;

    public PaperAsyncPlayerSendCommandsEvent(final Player player, final RootCommandNode<S> node, final boolean hasFiredAsync) {
        super(!Bukkit.isPrimaryThread(), player);
        this.node = node;
        this.hasFiredAsync = hasFiredAsync;
    }

    @Override
    public RootCommandNode<S> getCommandNode() {
        return this.node;
    }

    @Override
    public boolean hasFiredAsync() {
        return this.hasFiredAsync;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncPlayerSendCommandsEvent.getHandlerList();
    }
}
