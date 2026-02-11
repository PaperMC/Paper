package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperEntityFertilizeEggEvent extends CraftEntityEvent implements EntityFertilizeEggEvent {

    private final LivingEntity father;
    private final @Nullable Player breeder;
    private final @Nullable ItemStack bredWith;
    private int experience;

    private boolean cancelled;

    public PaperEntityFertilizeEggEvent(final LivingEntity mother, final LivingEntity father, final @Nullable Player breeder, final @Nullable ItemStack bredWith, final int experience) {
        super(mother);
        this.father = father;
        this.breeder = breeder;
        this.bredWith = bredWith;
        this.experience = experience;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public LivingEntity getMother() {
        return this.getEntity();
    }

    @Override
    public LivingEntity getFather() {
        return this.father;
    }

    @Override
    public @Nullable Player getBreeder() {
        return this.breeder;
    }

    @Override
    public @Nullable ItemStack getBredWith() {
        return this.bredWith;
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    @Override
    public void setExperience(final int experience) {
        this.experience = experience;
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
        return EntityFertilizeEggEvent.getHandlerList();
    }
}
