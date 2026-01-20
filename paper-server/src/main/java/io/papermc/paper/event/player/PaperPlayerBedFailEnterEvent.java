package io.papermc.paper.event.player;

import io.papermc.paper.block.bed.BedEnterAction;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerBedFailEnterEvent extends CraftPlayerEvent implements PlayerBedFailEnterEvent {

    private final FailReason failReason;
    private final Block bed;
    private final BedEnterAction enterAction;
    private boolean willExplode;
    private @Nullable Component message;

    private boolean cancelled;

    public PaperPlayerBedFailEnterEvent(final Player player, final FailReason failReason, final Block bed, final boolean willExplode, final BedEnterAction enterAction) {
        super(player);
        this.failReason = failReason;
        this.bed = bed;
        this.enterAction = enterAction;
        this.willExplode = willExplode;
        this.message = enterAction.errorMessage();
    }

    @Override
    public FailReason getFailReason() {
        return this.failReason;
    }

    @Override
    public BedEnterAction enterAction() {
        return this.enterAction;
    }

    @Override
    public Block getBed() {
        return this.bed;
    }

    @Override
    public boolean getWillExplode() {
        return this.willExplode;
    }

    @Override
    public void setWillExplode(final boolean willExplode) {
        this.willExplode = willExplode;
    }

    @Override
    public @Nullable Component getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(final @Nullable Component message) {
        this.message = message;
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
        return PlayerBedFailEnterEvent.getHandlerList();
    }
}
