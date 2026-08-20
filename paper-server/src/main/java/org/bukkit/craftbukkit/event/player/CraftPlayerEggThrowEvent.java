package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class CraftPlayerEggThrowEvent extends CraftPlayerEvent implements PlayerEggThrowEvent {

    private final Egg egg;
    private boolean hatching;
    private EntityType hatchType;
    private byte numHatches;

    public CraftPlayerEggThrowEvent(final Player player, final Egg egg, final boolean hatching, final byte numHatches, final EntityType hatchingType) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchingType;
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
    public EntityType getHatchingType() {
        return this.hatchType;
    }

    @Override
    public void setHatchingType(final EntityType hatchType) {
        Preconditions.checkArgument(hatchType.isSpawnable(), "Can't spawn that entity type from an egg!");
        this.hatchType = hatchType;
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
    public HandlerList getHandlers() {
        return PlayerEggThrowEvent.getHandlerList();
    }
}
