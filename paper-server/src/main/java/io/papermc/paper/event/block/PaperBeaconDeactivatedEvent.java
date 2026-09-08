package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperBeaconDeactivatedEvent extends CraftBlockEvent implements BeaconDeactivatedEvent {

    private @Nullable Beacon beacon;

    public PaperBeaconDeactivatedEvent(final Block beacon) {
        super(beacon);
    }

    public PaperBeaconDeactivatedEvent(final Level level, final BlockPos pos) {
        this(CraftBlock.at(level, pos));
    }

    @Override
    public @Nullable Beacon getBeacon() {
        if (this.beacon == null && this.block.getType() == Material.BEACON) {
            this.beacon = (Beacon) this.block.getState();
        }
        return this.beacon;
    }

    @Override
    public HandlerList getHandlers() {
        return BeaconDeactivatedEvent.getHandlerList();
    }
}
