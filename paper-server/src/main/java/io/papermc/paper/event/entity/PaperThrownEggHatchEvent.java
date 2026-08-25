package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;

public class PaperThrownEggHatchEvent extends CraftEvent implements ThrownEggHatchEvent {

    private final Egg egg;
    private boolean hatching;
    private byte numHatches;
    private EntityType hatchType;

    public PaperThrownEggHatchEvent(final Egg egg, final boolean hatching, final byte numHatches, final EntityType hatchType) {
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchType;
    }

    @Override
    public Egg getEgg() {
        return this.egg;
    }

    @Override
    public boolean isHatching() {
        return this.hatching;
    }

    @Override
    public void setHatching(final boolean hatching) {
        this.hatching = hatching;
    }

    @Override
    public byte getNumHatches() {
        return this.numHatches;
    }

    @Override
    public void setNumHatches(final byte numHatches) {
        this.numHatches = numHatches;
    }

    @Override
    public EntityType getHatchingType() {
        return this.hatchType;
    }

    @Override
    public void setHatchingType(final EntityType hatchType) {
        Preconditions.checkArgument(hatchType.isSpawnable(), "Can't spawn that entity type from an egg!");
        this.hatchType = hatchType;
    }

    @Override
    public HandlerList getHandlers() {
        return ThrownEggHatchEvent.getHandlerList();
    }
}
