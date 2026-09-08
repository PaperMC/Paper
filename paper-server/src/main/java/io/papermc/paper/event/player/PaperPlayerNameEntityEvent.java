package io.papermc.paper.event.player;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerNameEntityEvent extends CraftPlayerEvent implements PlayerNameEntityEvent {

    private LivingEntity entity;
    private @Nullable Component name;
    private boolean persistent;

    private boolean cancelled;

    public PaperPlayerNameEntityEvent(final Player player, final LivingEntity entity, final Component name, final boolean persistent) {
        super(player);
        this.entity = entity;
        this.name = name;
        this.persistent = persistent;
    }

    public PaperPlayerNameEntityEvent(
        final net.minecraft.world.entity.player.Player player,
        final net.minecraft.world.entity.LivingEntity entity,
        final net.minecraft.network.chat.Component name,
        final boolean persistent
    ) {
        this((Player) player.getBukkitEntity(), entity.getBukkitEntity(), PaperAdventure.asAdventure(name), persistent);
    }

    @Override
    public @Nullable Component getName() {
        return this.name;
    }

    @Override
    public void setName(final @Nullable Component name) {
        this.name = name;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(final LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean isPersistent() {
        return this.persistent;
    }

    @Override
    public void setPersistent(final boolean persistent) {
        this.persistent = persistent;
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
        return PlayerNameEntityEvent.getHandlerList();
    }
}
