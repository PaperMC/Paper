package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.entity.Animals;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityEnterLoveModeEvent extends CraftEntityEvent implements EntityEnterLoveModeEvent {

    private final @Nullable HumanEntity humanEntity;
    private int ticksInLove;

    private boolean cancelled;

    public CraftEntityEnterLoveModeEvent(final Animals animal, final @Nullable HumanEntity humanEntity, final int ticksInLove) {
        super(animal);
        this.humanEntity = humanEntity;
        this.ticksInLove = ticksInLove;
    }

    public CraftEntityEnterLoveModeEvent(final Animal animal, final @Nullable Player player, final int ticksInLove) {
        this((Animals) animal.getBukkitEntity(), Optionull.map(player, Player::getBukkitEntity), ticksInLove);
    }

    @Override
    public Animals getEntity() {
        return (Animals) this.entity;
    }

    @Override
    public @Nullable HumanEntity getHumanEntity() {
        return this.humanEntity;
    }

    @Override
    public int getTicksInLove() {
        return this.ticksInLove;
    }

    @Override
    public void setTicksInLove(final int ticksInLove) {
        this.ticksInLove = ticksInLove;
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
        return EntityEnterLoveModeEvent.getHandlerList();
    }
}
