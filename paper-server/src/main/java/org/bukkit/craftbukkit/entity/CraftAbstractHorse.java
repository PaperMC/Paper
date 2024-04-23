package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventoryAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.AbstractHorseInventory;

public abstract class CraftAbstractHorse extends CraftAnimals implements AbstractHorse {

    public CraftAbstractHorse(CraftServer server, EntityHorseAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityHorseAbstract getHandle() {
        return (EntityHorseAbstract) entity;
    }

    @Override
    public void setVariant(Horse.Variant variant) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getDomestication() {
        return getHandle().getTemper();
    }

    @Override
    public void setDomestication(int value) {
        Preconditions.checkArgument(value >= 0 && value <= this.getMaxDomestication(), "Domestication level (%s) need to be between %s and %s (max domestication)", value, 0, this.getMaxDomestication());
        getHandle().setTemper(value);
    }

    @Override
    public int getMaxDomestication() {
        return getHandle().getMaxTemper();
    }

    @Override
    public void setMaxDomestication(int value) {
        Preconditions.checkArgument(value > 0, "Max domestication (%s) cannot be zero or less", value);
        getHandle().maxDomestication = value;
    }

    @Override
    public double getJumpStrength() {
        return getHandle().getAttributeValue(GenericAttributes.JUMP_STRENGTH);
    }

    @Override
    public void setJumpStrength(double strength) {
        Preconditions.checkArgument(strength >= 0, "Jump strength (%s) cannot be less than zero", strength);
        getHandle().getAttribute(GenericAttributes.JUMP_STRENGTH).setBaseValue(strength);
    }

    @Override
    public boolean isTamed() {
        return getHandle().isTamed();
    }

    @Override
    public void setTamed(boolean tamed) {
        getHandle().setTamed(tamed);
    }

    @Override
    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) return null;
        return getServer().getOfflinePlayer(getOwnerUUID());
    }

    @Override
    public void setOwner(AnimalTamer owner) {
        if (owner != null) {
            setTamed(true);
            getHandle().setTarget(null, null, false);
            setOwnerUUID(owner.getUniqueId());
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    public UUID getOwnerUUID() {
        return getHandle().getOwnerUUID();
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().setOwnerUUID(uuid);
    }

    @Override
    public boolean isEatingHaystack() {
        return getHandle().isEating();
    }

    @Override
    public void setEatingHaystack(boolean eatingHaystack) {
        getHandle().setEating(eatingHaystack);
    }

    @Override
    public AbstractHorseInventory getInventory() {
        return new CraftInventoryAbstractHorse(getHandle().inventory);
    }
}
