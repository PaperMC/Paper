package io.papermc.paper.event.player;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.NonNegative;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public class PaperPlayerItemGroupCooldownEvent extends CraftPlayerEvent implements PlayerItemGroupCooldownEvent {

    private final NamespacedKey cooldownGroup;
    private int cooldown;

    private boolean cancelled;

    public PaperPlayerItemGroupCooldownEvent(final Player player, final NamespacedKey cooldownGroup, final int cooldown) {
        super(player);
        this.cooldownGroup = cooldownGroup;
        this.cooldown = cooldown;
    }

    public PaperPlayerItemGroupCooldownEvent(final ServerPlayer player, final Identifier cooldownGroup, final int cooldown) {
        this(
            player.getBukkitEntity(), CraftNamespacedKey.fromMinecraft(cooldownGroup), cooldown
        );
    }

    @Override
    public NamespacedKey getCooldownGroup() {
        return this.cooldownGroup;
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
        return PlayerItemGroupCooldownEvent.getHandlerList();
    }
}
