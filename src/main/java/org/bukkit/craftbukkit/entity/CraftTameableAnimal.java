package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityTameableAnimal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class CraftTameableAnimal extends CraftAnimals implements Tameable, Creature {
    private AnimalTamer owner;

    public CraftTameableAnimal(CraftServer server, EntityTameableAnimal entity) {
        super(server, entity);
    }

    @Override
    public EntityTameableAnimal getHandle() {
        return (EntityTameableAnimal)super.getHandle();
    }

    public AnimalTamer getOwner() {
        if (owner == null && !("").equals(getOwnerName())) {
            owner = getServer().getPlayer(getOwnerName());

            if (owner == null) {
                owner = getServer().getOfflinePlayer(getOwnerName());
            }
        }

        return owner;
    }

    public String getOwnerName() {
        return getHandle().getOwnerName();
    }

    public boolean isTamed() {
        return getHandle().isTamed();
    }

    public void setOwner(AnimalTamer tamer) {
        owner = tamer;

        if (owner != null) {
            setTamed(true);
            getHandle().setPathEntity(null);

            if (owner instanceof Player) {
                setOwnerName(((Player) owner).getName());
            } else {
                setOwnerName("");
            }
        } else {
            setTamed(false);
            setOwnerName("");
        }
    }

    public void setOwnerName(String ownerName) {
        getHandle().setOwnerName(ownerName);
    }

    public void setTamed(boolean tame) {
        getHandle().setTamed(tame);
    }

    public boolean isSitting() {
        return getHandle().isSitting();
    }

    public void setSitting(boolean sitting) {
        getHandle().setSitting(sitting);
        getHandle().setPathEntity(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner=" + getOwner() + ",tamed=" + isTamed() + "}";
    }
}
