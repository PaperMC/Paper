package io.papermc.paper.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec2;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerFailMoveEvent extends CraftPlayerEvent implements PlayerFailMoveEvent {

    private final FailReason failReason;
    private final Location from;
    private final Location to;
    private boolean allowed = false;
    private boolean logWarning;

    public PaperPlayerFailMoveEvent(
        final Player player, final FailReason failReason, final boolean logWarning, final Location from, final Location to
    ) {
        super(player);
        this.failReason = failReason;
        this.logWarning = logWarning;
        this.from = from;
        this.to = to;
    }

    public PaperPlayerFailMoveEvent(
        final ServerPlayer player, final FailReason failReason, final boolean logWarning, final Location from, final Location to
    ) {
        this(player.getBukkitEntity(), failReason, logWarning, from, to);
    }

    public PaperPlayerFailMoveEvent(
        final ServerGamePacketListenerImpl packetListener,
        final FailReason failReason,
        final boolean logWarning,
        final double targetX,
        final double targetY,
        final double targetZ,
        final float targetYRot,
        final float targetXRot
    ) {
        final Player player = packetListener.getPlayer().getBukkitEntity();
        final Vec2 lastRotation = packetListener.lastRotation();
        this(
            player,
            failReason,
            logWarning,
            CraftLocation.toBukkit(packetListener.lastPosition(), player.getWorld(), lastRotation.x, lastRotation.y),
            new Location(player.getWorld(), targetX, targetY, targetZ, targetYRot, targetXRot)
        );
    }

    @Override
    public FailReason getFailReason() {
        return this.failReason;
    }

    @Override
    public Location getFrom() {
        return this.from.clone();
    }

    @Override
    public Location getTo() {
        return this.to.clone();
    }

    @Override
    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public void setAllowed(final boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public boolean getLogWarning() {
        return this.logWarning;
    }

    @Override
    public void setLogWarning(final boolean logWarning) {
        this.logWarning = logWarning;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerFailMoveEvent.getHandlerList();
    }
}
