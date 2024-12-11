package org.bukkit.craftbukkit.entity;

import java.util.UUID;
import net.minecraft.world.entity.TamableAnimal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {
    public CraftTameableAnimal(CraftServer server, TamableAnimal entity) {
        super(server, entity);
    }

    @Override
    public TamableAnimal getHandle() {
        return (TamableAnimal) super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return this.getHandle().getOwnerUUID();
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        this.getHandle().setOwnerUUID(uuid);
    }

    @Override
    public AnimalTamer getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }

        AnimalTamer owner = this.getServer().getPlayer(this.getOwnerUUID());
        if (owner == null) {
            owner = this.getServer().getOfflinePlayer(this.getOwnerUUID());
        }

        return owner;
    }

    @Override
    public boolean isTamed() {
        return this.getHandle().isTame();
    }

    @Override
    public void setOwner(AnimalTamer tamer) {
        if (tamer != null) {
            this.setTamed(true);
            this.getHandle().setTarget(null, null, false);
            this.setOwnerUUID(tamer.getUniqueId());
        } else {
            this.setTamed(false);
            this.setOwnerUUID(null);
        }
    }

    @Override
    public void setTamed(boolean tame) {
        this.getHandle().setTame(tame, true);
        if (!tame) {
            this.setOwnerUUID(null);
        }
    }

    public boolean isSitting() {
        return this.getHandle().isInSittingPose();
    }

    public void setSitting(boolean sitting) {
        this.getHandle().setInSittingPose(sitting);
        this.getHandle().setOrderedToSit(sitting);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{owner=" + this.getOwner() + ",tamed=" + this.isTamed() + "}";
    }
}
