package org.bukkit.craftbukkit.event.player;

import io.papermc.paper.block.bed.BedEnterAction;
import io.papermc.paper.block.bed.BedRuleResult;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class CraftPlayerBedEnterEvent extends CraftPlayerEvent implements PlayerBedEnterEvent {

    private final Block bed;
    private final BedEnterResult bedEnterResult;
    private final BedEnterAction enterAction;
    private Result useBed = Result.DEFAULT;

    public CraftPlayerBedEnterEvent(final Player player, final Block bed, final BedEnterResult bedEnterResult, final BedEnterAction enterAction) {
        super(player);
        this.bed = bed;
        this.bedEnterResult = bedEnterResult;
        this.enterAction = enterAction;
    }

    @Override
    public Block getBed() {
        return this.bed;
    }

    @Override
    public BedEnterResult getBedEnterResult() {
        return this.bedEnterResult;
    }

    @Override
    public BedEnterAction enterAction() {
        return this.enterAction;
    }

    @Override
    public Result useBed() {
        return this.useBed;
    }

    @Override
    public void setUseBed(final Result useBed) {
        this.useBed = useBed;
    }

    @Override
    public boolean isCancelled() {
        return this.useBed == Result.DENY || (this.useBed == Result.DEFAULT && this.enterAction.canSleep() != BedRuleResult.ALLOWED);
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.setUseBed(cancel ? Result.DENY : (this.useBed == Result.DENY ? Result.DEFAULT : this.useBed));
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerBedEnterEvent.getHandlerList();
    }
}
