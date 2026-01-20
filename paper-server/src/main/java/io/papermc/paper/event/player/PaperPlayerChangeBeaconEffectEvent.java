package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.Nullable;

public class PaperPlayerChangeBeaconEffectEvent extends CraftPlayerEvent implements PlayerChangeBeaconEffectEvent {

    private final Block beacon;
    private @Nullable PotionEffectType primary;
    private @Nullable PotionEffectType secondary;
    private boolean consumeItem = true;

    private boolean cancelled;

    public PaperPlayerChangeBeaconEffectEvent(final Player player, final @Nullable PotionEffectType primary, final @Nullable PotionEffectType secondary, final Block beacon) {
        super(player);
        this.primary = primary;
        this.secondary = secondary;
        this.beacon = beacon;
    }

    @Override
    public @Nullable PotionEffectType getPrimary() {
        return this.primary;
    }

    @Override
    public void setPrimary(final @Nullable PotionEffectType primary) {
        this.primary = primary;
    }

    @Override
    public @Nullable PotionEffectType getSecondary() {
        return this.secondary;
    }

    @Override
    public void setSecondary(final @Nullable PotionEffectType secondary) {
        this.secondary = secondary;
    }

    @Override
    public Block getBeacon() {
        return this.beacon;
    }

    @Override
    public boolean willConsumeItem() {
        return this.consumeItem;
    }

    @Override
    public void setConsumeItem(final boolean consumeItem) {
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
        return PlayerChangeBeaconEffectEvent.getHandlerList();
    }
}
