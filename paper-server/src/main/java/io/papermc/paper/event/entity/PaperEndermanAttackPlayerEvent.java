package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import net.minecraft.world.entity.monster.EnderMan;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperEndermanAttackPlayerEvent extends CraftEntityEvent implements EndermanAttackPlayerEvent {

    private final Player player;
    private boolean cancelled;

    public PaperEndermanAttackPlayerEvent(final Enderman entity, final Player player) {
        super(entity);
        this.player = player;
    }

    public PaperEndermanAttackPlayerEvent(final EnderMan entity, final net.minecraft.world.entity.player.Player player) {
        this((Enderman) entity.getBukkitEntity(), (Player) player.getBukkitEntity());
    }

    @Override
    public Enderman getEntity() {
        return (Enderman) this.entity;
    }

    @Override
    public Player getPlayer() {
        return this.player;
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
        return EndermanAttackPlayerEvent.getHandlerList();
    }
}
