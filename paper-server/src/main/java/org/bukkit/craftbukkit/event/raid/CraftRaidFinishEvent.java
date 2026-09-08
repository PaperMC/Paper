package org.bukkit.craftbukkit.event.raid;

import java.util.Collections;
import java.util.List;
import net.minecraft.world.level.Level;
import org.bukkit.Raid;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftRaid;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.raid.RaidFinishEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftRaidFinishEvent extends CraftRaidEvent implements RaidFinishEvent {

    private final List<Player> winners;

    public CraftRaidFinishEvent(final Raid raid, final World world, final List<Player> winners) {
        super(raid, world);
        this.winners = Collections.unmodifiableList(winners);
    }

    public CraftRaidFinishEvent(final Level level, final net.minecraft.world.entity.raid.Raid raid, final List<Player> winners) {
        this(new CraftRaid(raid, level), level.getWorld(), winners);
    }

    @Override
    public @Unmodifiable List<Player> getWinners() {
        return this.winners;
    }

    @Override
    public HandlerList getHandlers() {
        return RaidFinishEvent.getHandlerList();
    }
}
