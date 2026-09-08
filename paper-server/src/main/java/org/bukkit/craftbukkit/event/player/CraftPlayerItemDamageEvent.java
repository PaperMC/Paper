package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerItemDamageEvent extends CraftPlayerEvent implements PlayerItemDamageEvent {

    private final ItemStack item;
    private final int originalDamage;
    private int damage;

    private boolean cancelled;

    public CraftPlayerItemDamageEvent(final Player player, final ItemStack item, final int damage, final int originalDamage) {
        super(player);
        this.item = item;
        this.damage = damage;
        this.originalDamage = originalDamage;
    }

    public CraftPlayerItemDamageEvent(final ServerPlayer player, final net.minecraft.world.item.ItemStack item, final int damage, final int originalDamage) {
        this(player.getBukkitEntity(), CraftItemStack.asCraftMirror(item), damage, originalDamage);
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(final int damage) {
        this.damage = damage;
    }

    @Override
    public int getOriginalDamage() {
        return this.originalDamage;
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
        return PlayerItemDamageEvent.getHandlerList();
    }
}
