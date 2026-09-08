package org.bukkit.craftbukkit.event.raid;

import net.minecraft.world.level.Level;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftRaid;
import org.bukkit.event.HandlerList;
import org.bukkit.event.raid.RaidStopEvent;

public class CraftRaidStopEvent extends CraftRaidEvent implements RaidStopEvent {

    private final Reason reason;

    public CraftRaidStopEvent(final Raid raid, final World world, final Reason reason) {
        super(raid, world);
        this.reason = reason;
    }

    public CraftRaidStopEvent(final Level level, final net.minecraft.world.entity.raid.Raid raid, final Reason reason) {
        this(new CraftRaid(raid, level), level.getWorld(), reason);
    }

    @Override
    public Reason getReason() {
        return this.reason;
    }

    @Override
    public HandlerList getHandlers() {
        return RaidStopEvent.getHandlerList();
    }
}
