package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerAttackEntityCooldownResetEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerAttackEntityCooldownResetEvent extends CraftPlayerEvent implements PlayerAttackEntityCooldownResetEvent {

    private final Entity attackedEntity;
    private final float cooledAttackStrength;

    private boolean cancelled;

    public PaperPlayerAttackEntityCooldownResetEvent(final Player player, final Entity attackedEntity, final float cooledAttackStrength) {
        super(player);
        this.attackedEntity = attackedEntity;
        this.cooledAttackStrength = cooledAttackStrength;
    }

    public PaperPlayerAttackEntityCooldownResetEvent(
        final net.minecraft.world.entity.player.Player player,
        final net.minecraft.world.entity.Entity attackEntity,
        final float cooledAttackStrength
     ) {
        this((Player) player.getBukkitEntity(), attackEntity.getBukkitEntity(), cooledAttackStrength);
    }

    @Override
    public Entity getAttackedEntity() {
        return this.attackedEntity;
    }

    @Override
    public float getCooledAttackStrength() {
        return this.cooledAttackStrength;
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
        return PlayerAttackEntityCooldownResetEvent.getHandlerList();
    }
}
