package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Raider;
import org.bukkit.event.HandlerList;

public class PaperBellRevealRaiderEvent extends CraftBlockEvent implements BellRevealRaiderEvent {

    private final Raider raider;
    private boolean cancelled;

    public PaperBellRevealRaiderEvent(final Block bell, final Raider raider) {
        super(bell);
        this.raider = raider;
    }

    public PaperBellRevealRaiderEvent(final BlockPos pos, final LivingEntity raider) {
        this(CraftBlock.at(raider.level(), pos), (Raider) raider.getBukkitEntity());
    }

    @Override
    public Raider getEntity() {
        return this.raider;
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
        return BellRevealRaiderEvent.getHandlerList();
    }
}
