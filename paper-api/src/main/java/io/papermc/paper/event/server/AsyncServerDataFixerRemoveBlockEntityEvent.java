package io.papermc.paper.event.server;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

// TODO javadocs
@NullMarked
public class AsyncServerDataFixerRemoveBlockEntityEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Key worldKey;
    private final Key blockEntityId;
    private final BlockPosition blockPosition;
    private final PersistentDataContainerView persistentDataContainerView;

    public AsyncServerDataFixerRemoveBlockEntityEvent(
        final Key worldKey,
        final Key blockEntityId,
        final BlockPosition blockPosition,
        final PersistentDataContainerView persistentDataContainerView
    ) {
        super(!Bukkit.isPrimaryThread());
        this.worldKey = worldKey;
        this.blockEntityId = blockEntityId;
        this.blockPosition = blockPosition;
        this.persistentDataContainerView = persistentDataContainerView;
    }

    public Key getBlockEntityId() {
        return blockEntityId;
    }

    public Key getWorldKey() {
        return worldKey;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public PersistentDataContainerView getPersistentDataContainerView() {
        return persistentDataContainerView;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
