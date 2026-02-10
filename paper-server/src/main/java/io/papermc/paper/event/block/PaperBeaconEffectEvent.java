package io.papermc.paper.event.block;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

public class PaperBeaconEffectEvent extends CraftBlockEvent implements BeaconEffectEvent {

    private final Player player;
    private final boolean primary;
    private PotionEffect effect;

    private boolean cancelled;

    public PaperBeaconEffectEvent(final Block beacon, final PotionEffect effect, final Player player, final boolean primary) {
        super(beacon);
        this.effect = effect;
        this.player = player;
        this.primary = primary;
    }

    @Override
    public PotionEffect getEffect() {
        return this.effect;
    }

    @Override
    public void setEffect(final PotionEffect effect) {
        this.effect = effect;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isPrimary() {
        return this.primary;
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
        return BeaconEffectEvent.getHandlerList();
    }
}
