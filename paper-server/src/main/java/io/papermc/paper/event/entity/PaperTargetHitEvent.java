package io.papermc.paper.event.entity;

import io.papermc.paper.event.block.TargetHitEvent;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.phys.BlockHitResult;
import org.bukkit.craftbukkit.event.entity.CraftProjectileHitEvent;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;

import static io.papermc.paper.util.BoundChecker.requireRange;

public class PaperTargetHitEvent extends CraftProjectileHitEvent implements TargetHitEvent {

    private int signalStrength;

    public PaperTargetHitEvent(final Projectile projectile, final BlockHitResult hitResult, final int signalStrength) {
        super(projectile, hitResult);
        this.signalStrength = signalStrength;
    }

    @Override
    public @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int getSignalStrength() {
        return this.signalStrength;
    }

    @Override
    public void setSignalStrength(final @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int signalStrength) {
        this.signalStrength = requireRange(signalStrength, "signalStrength", Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX);
    }

    @Override
    public HandlerList getHandlers() {
        return TargetHitEvent.getHandlerList();
    }
}
