package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EnderDragonShootFireballEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.HandlerList;

public class PaperEnderDragonShootFireballEvent extends CraftEntityEvent implements EnderDragonShootFireballEvent {

    private final DragonFireball fireball;
    private boolean cancelled;

    public PaperEnderDragonShootFireballEvent(final EnderDragon entity, final DragonFireball fireball) {
        super(entity);
        this.fireball = fireball;
    }

    @Override
    public EnderDragon getEntity() {
        return (EnderDragon) this.entity;
    }

    @Override
    public DragonFireball getFireball() {
        return this.fireball;
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
        return EnderDragonShootFireballEvent.getHandlerList();
    }
}
