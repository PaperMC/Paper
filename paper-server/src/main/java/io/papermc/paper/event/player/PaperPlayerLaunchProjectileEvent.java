package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerLaunchProjectileEvent extends CraftPlayerEvent implements PlayerLaunchProjectileEvent {

    private final Projectile projectile;
    private final ItemStack itemStack;
    private boolean consumeItem = true;

    private boolean cancelled;

    public PaperPlayerLaunchProjectileEvent(final Player shooter, final ItemStack itemStack, final Projectile projectile) {
        super(shooter);
        this.itemStack = itemStack;
        this.projectile = projectile;
    }

    @Override
    public Projectile getProjectile() {
        return this.projectile;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public boolean shouldConsume() {
        return this.consumeItem;
    }

    @Override
    public void setShouldConsume(final boolean consumeItem) {
        this.consumeItem = consumeItem;
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
        return PlayerLaunchProjectileEvent.getHandlerList();
    }
}
