package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperBeaconActivatedEvent extends CraftBlockEvent implements BeaconActivatedEvent {

    private @Nullable Beacon beacon;

    public PaperBeaconActivatedEvent(final Block beacon) {
        super(beacon);
    }

    public PaperBeaconActivatedEvent(final Level level, final BlockPos pos) {
        this(CraftBlock.at(level, pos));
    }

    @Override
    public Beacon getBeacon() {
        if (this.beacon == null) {
            this.beacon = (Beacon) this.block.getState();
        }
        return this.beacon;
    }

    @Override
    public HandlerList getHandlers() {
        return BeaconActivatedEvent.getHandlerList();
    }
}
