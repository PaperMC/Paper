package org.bukkit.craftbukkit.event.raid;

import java.util.Collections;
import java.util.List;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.raid.RaidFinishEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftRaidFinishEvent extends CraftRaidEvent implements RaidFinishEvent {

    private final List<Player> winners;

    public CraftRaidFinishEvent(final Raid raid, final World world, final List<Player> winners) {
        super(raid, world);
        this.winners = winners;
    }

    @Override
    public @Unmodifiable List<Player> getWinners() {
        return Collections.unmodifiableList(this.winners);
    }

    @Override
    public HandlerList getHandlers() {
        return RaidFinishEvent.getHandlerList();
    }
}
