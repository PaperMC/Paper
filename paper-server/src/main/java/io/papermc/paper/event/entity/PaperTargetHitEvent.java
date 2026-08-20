package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.event.block.TargetHitEvent;
import net.minecraft.world.level.redstone.Redstone;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.event.entity.CraftProjectileHitEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

public class PaperTargetHitEvent extends CraftProjectileHitEvent implements TargetHitEvent {

    private int signalStrength;

    public PaperTargetHitEvent(final Projectile projectile, final Block block, final BlockFace blockFace, final int signalStrength) {
        super(projectile, null, block, blockFace);
        this.signalStrength = signalStrength;
    }

    @Override
    public @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int getSignalStrength() {
        return this.signalStrength;
    }

    @Override
    public void setSignalStrength(final @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int signalStrength) {
        Preconditions.checkArgument(signalStrength >= Redstone.SIGNAL_MIN && signalStrength <= Redstone.SIGNAL_MAX, "Signal strength out of range (%s), must be in range [%s, %s]", signalStrength, Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX);
        this.signalStrength = signalStrength;
    }

    @Override
    public HandlerList getHandlers() {
        return TargetHitEvent.getHandlerList();
    }
}
