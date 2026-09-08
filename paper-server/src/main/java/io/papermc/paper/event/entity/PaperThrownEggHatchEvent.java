package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import org.bukkit.craftbukkit.event.data.TransientEggInfo;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.index.qual.NonNegative;

public class PaperThrownEggHatchEvent extends CraftEntityEvent implements ThrownEggHatchEvent {

    private final TransientEggInfo eggInfo;

    public PaperThrownEggHatchEvent(final Egg egg, final TransientEggInfo eggInfo) {
        super(egg);
        this.eggInfo = eggInfo;
    }

    public PaperThrownEggHatchEvent(final ThrownEgg egg, final TransientEggInfo eggInfo) {
        this((Egg) egg.getBukkitEntity(), eggInfo);
    }

    @Override
    public Egg getEntity() {
        return (Egg) this.entity;
    }

    @Override
    public boolean isHatching() {
        return this.eggInfo.isHatching();
    }

    @Override
    public void setHatching(final boolean hatching) {
        this.eggInfo.setHatching(hatching);
    }

    @Override
    public @NonNegative byte getNumHatches() {
        return (byte) this.eggInfo.getNumHatches();
    }

    @Override
    public void setNumHatches(final @NonNegative byte numHatches) {
        this.eggInfo.setNumHatches(numHatches);
    }

    @Override
    public EntityType getHatchingType() {
        return this.eggInfo.getType();
    }

    @Override
    public void setHatchingType(final EntityType hatchType) {
        this.eggInfo.setType(hatchType);
    }

    @Override
    public HandlerList getHandlers() {
        return ThrownEggHatchEvent.getHandlerList();
    }
}
