package org.bukkit.craftbukkit.event.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class CraftPlayerBedLeaveEvent extends CraftPlayerEvent implements PlayerBedLeaveEvent {

    private final Block bed;
    private boolean setBedSpawn = true;
    private boolean cancelled;

    public CraftPlayerBedLeaveEvent(final Player player, final Block bed) {
        super(player);
        this.bed = bed;
    }

    public CraftPlayerBedLeaveEvent(final ServerPlayer player, final BlockPos pos) {
        this(player.getBukkitEntity(), CraftBlock.at(player.level(), pos));
    }

    @Override
    public Block getBlock() {
        return this.bed;
    }

    @Override
    public boolean shouldSetSpawnLocation() {
        return this.setBedSpawn;
    }

    @Override
    public void setSpawnLocation(final boolean setBedSpawn) {
        this.setBedSpawn = setBedSpawn;
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
        return PlayerBedLeaveEvent.getHandlerList();
    }
}
