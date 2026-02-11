package io.papermc.paper.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperEntityDyeEvent extends CraftEntityEvent implements EntityDyeEvent {

    private final @Nullable Player player;
    private DyeColor dyeColor;

    private boolean cancelled;

    public PaperEntityDyeEvent(final Entity entity, final DyeColor dyeColor, final @Nullable Player player) {
        super(entity);
        this.dyeColor = dyeColor;
        this.player = player;
    }

    @Override
    public DyeColor getColor() {
        return this.dyeColor;
    }

    @Override
    public void setColor(final DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityDyeEvent.getHandlerList();
    }
}
