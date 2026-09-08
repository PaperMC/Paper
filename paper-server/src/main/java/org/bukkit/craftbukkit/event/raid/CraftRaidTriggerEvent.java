package org.bukkit.craftbukkit.event.raid;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftRaid;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.raid.RaidTriggerEvent;

public class CraftRaidTriggerEvent extends CraftRaidEvent implements RaidTriggerEvent {

    private final Player player;
    private boolean cancelled;

    public CraftRaidTriggerEvent(final Raid raid, final World world, final Player player) {
        super(raid, world);
        this.player = player;
    }

    public CraftRaidTriggerEvent(final Level level, final net.minecraft.world.entity.raid.Raid raid, final ServerPlayer player) {
        this(new CraftRaid(raid, level), level.getWorld(), player.getBukkitEntity());
    }

    @Override
    public Player getPlayer() {
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
        return RaidTriggerEvent.getHandlerList();
    }
}
