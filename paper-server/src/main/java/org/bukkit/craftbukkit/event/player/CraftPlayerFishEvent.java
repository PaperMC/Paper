package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class CraftPlayerFishEvent extends CraftPlayerEvent implements PlayerFishEvent {

    private final @Nullable Entity entity;
    private final FishHook hookEntity;
    private final @Nullable EquipmentSlot hand;
    private final State state;
    private int exp;

    private boolean cancelled;

    public CraftPlayerFishEvent(final Player player, final @Nullable Entity entity, final FishHook hookEntity, final @Nullable EquipmentSlot hand, final State state) {
        super(player);
        this.entity = entity;
        this.hookEntity = hookEntity;
        this.hand = hand;
        this.state = state;
    }

    public CraftPlayerFishEvent(final Player player, final @Nullable Entity entity, final FishHook hookEntity, final State state) {
        this(player, entity, hookEntity, null, state);
    }

    public @Nullable Entity getCaught() {
        return this.entity;
    }

    @Override
    public FishHook getHook() {
        return this.hookEntity;
    }

    @Override
    public @Nullable EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public int getExpToDrop() {
        return this.exp;
    }

    @Override
    public void setExpToDrop(final int amount) {
        this.exp = amount;
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
        return PlayerFishEvent.getHandlerList();
    }
}
