package io.papermc.paper.event.block;

import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperTNTPrimeEvent extends CraftBlockEvent implements TNTPrimeEvent {

    private final PrimeReason reason;
    private final @Nullable Entity primerEntity;

    private boolean cancelled;

    public PaperTNTPrimeEvent(final Block block, final PrimeReason reason, final @Nullable Entity primerEntity) {
        super(block);
        this.reason = reason;
        this.primerEntity = primerEntity;
    }

    public PaperTNTPrimeEvent(final Level level, final BlockPos pos, final PrimeReason reason, final net.minecraft.world.entity.@Nullable Entity primerEntity) {
        this(CraftBlock.at(level, pos), reason, Optionull.map(primerEntity, net.minecraft.world.entity.Entity::getBukkitEntity));
    }

    @Override
    public PrimeReason getReason() {
        return this.reason;
    }

    @Override
    public @Nullable Entity getPrimerEntity() {
        return this.primerEntity;
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
        return TNTPrimeEvent.getHandlerList();
    }
}
