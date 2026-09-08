package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requireNonNegative;

public class CraftEntityBreedEvent extends CraftEntityEvent implements EntityBreedEvent {

    private final LivingEntity mother;
    private final LivingEntity father;
    private final @Nullable LivingEntity breeder;
    private final @Nullable ItemStack bredWith;
    private int experience;

    private boolean cancelled;

    public CraftEntityBreedEvent(
        final LivingEntity child,
        final LivingEntity mother,
        final LivingEntity father,
        final @Nullable LivingEntity breeder,
        final @Nullable ItemStack bredWith,
        final int experience
    ) {
        super(child);

        this.mother = mother;
        this.father = father;
        this.breeder = breeder; // Breeder can be null in the case of spontaneous conception
        this.bredWith = bredWith;
        this.experience = experience;
    }

    public CraftEntityBreedEvent(
        final net.minecraft.world.entity.LivingEntity child,
        final net.minecraft.world.entity.LivingEntity mother,
        final net.minecraft.world.entity.LivingEntity father,
        final net.minecraft.world.entity.@Nullable LivingEntity breeder,
        final net.minecraft.world.item.@Nullable ItemStack bredWith,
        final int experience
    ) {
        this(
            child.getBukkitEntity(),
            mother.getBukkitEntity(),
            father.getBukkitEntity(),
            Optionull.map(breeder, net.minecraft.world.entity.LivingEntity::getBukkitEntity),
            Optionull.map(bredWith, CraftItemStack::asBukkitCopy),
            experience
        );
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public LivingEntity getMother() {
        return this.mother;
    }

    @Override
    public LivingEntity getFather() {
        return this.father;
    }

    @Override
    public @Nullable LivingEntity getBreeder() {
        return this.breeder;
    }

    @Override
    public @Nullable ItemStack getBredWith() {
        return this.bredWith;
    }

    @Override
    public @NonNegative int getExperience() {
        return this.experience;
    }

    @Override
    public void setExperience(final @NonNegative int experience) {
        this.experience = requireNonNegative(experience, "experience");
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
        return EntityBreedEvent.getHandlerList();
    }
}
