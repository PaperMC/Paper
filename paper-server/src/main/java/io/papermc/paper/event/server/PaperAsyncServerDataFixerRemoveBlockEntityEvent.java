package io.papermc.paper.event.server;

import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperAsyncServerDataFixerRemoveBlockEntityEvent extends CraftEvent implements AsyncServerDataFixerRemoveBlockEntityEvent { // that name

    private final Key worldKey;
    private final Key blockEntityType;
    private final BlockPosition blockPosition;
    private final PersistentDataContainerView persistentDataContainerView;

    public PaperAsyncServerDataFixerRemoveBlockEntityEvent(
        final Key worldKey,
        final Key blockEntityType,
        final BlockPosition blockPosition,
        final PersistentDataContainerView persistentDataContainerView
    ) {
        super(!Bukkit.isPrimaryThread());
        this.worldKey = worldKey;
        this.blockEntityType = blockEntityType;
        this.blockPosition = blockPosition;
        this.persistentDataContainerView = persistentDataContainerView;
    }

    @Override
    public Key getWorldKey() {
        return this.worldKey;
    }

    @Override
    public Key getBlockEntityType() {
        return this.blockEntityType;
    }

    @Override
    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    @Override
    public PersistentDataContainerView getPersistentDataContainerView() {
        return this.persistentDataContainerView;
    }

    @Override
    public HandlerList getHandlers() {
        return AsyncServerDataFixerRemoveBlockEntityEvent.getHandlerList();
    }
}
