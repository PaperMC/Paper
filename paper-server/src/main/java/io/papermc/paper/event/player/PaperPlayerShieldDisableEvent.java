package io.papermc.paper.event.player;

import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.NonNegative;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public class PaperPlayerShieldDisableEvent extends CraftPlayerEvent implements PlayerShieldDisableEvent {

    private final Entity damager;
    private int cooldown;

    private boolean cancelled;

    public PaperPlayerShieldDisableEvent(final Player player, final Entity damager, final int cooldown) {
        super(player);
        this.damager = damager;
        this.cooldown = cooldown;
    }

    public PaperPlayerShieldDisableEvent(final net.minecraft.world.entity.player.Player player, final net.minecraft.world.entity.Entity attacker, final int cooldown) {
        this((Player) player.getBukkitEntity(), attacker.getBukkitEntity(), cooldown);
    }

    @Override
    public Entity getDamager() {
        return this.damager;
    }

    @Override
    public @NonNegative int getCooldown() {
        return this.cooldown;
    }

    @Override
    public void setCooldown(final @NonNegative int cooldown) {
        this.cooldown = requireNonNegative(cooldown, "cooldown");
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
        return PlayerShieldDisableEvent.getHandlerList();
    }
}
